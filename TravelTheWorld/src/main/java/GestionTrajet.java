package main.java;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repositories.TrajetRepository;
import repositories.TrainRepository;
import repositories.AvionRepository;
import repositories.GareRepository;
import repositories.AeroportRepository;
import classes.Trajet;
import classes.MoyenTransport;
import classes.Train;
import classes.Avion;
import classes.Gare;
import classes.Station;
import classes.Aeroport;
import java.util.ArrayList;
import java.util.List;

public class GestionTrajet {

    private TrainRepository trainRepository;
    private AvionRepository avionRepository;
    private GareRepository gareRepository;
    private AeroportRepository aeroportRepository;
    private TrajetRepository trajetRepository;
    private ListView<String> listView;
    private SessionFactory sessionFactory;

    private ComboBox<String> stationTypeComboBox;

    public void startGestion(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Trajets");

        configureHibernate();

        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 800, 600);

            listView = new ListView<>();
            displayTrajets();

            Button retourMenuButton = new Button("Retour au menu");
            retourMenuButton.setOnAction(event -> {
                primaryStage.close();
                new JavaFXApp().start(new Stage());
            });

            VBox buttonsBox = createButtonsBox();
            buttonsBox.getChildren().add(retourMenuButton);

            root.setLeft(listView);
            root.setRight(buttonsBox);

            primaryStage.setScene(scene);
        } catch (Exception e) {
            showErrorAlert("Une erreur s'est produite : " + e.getMessage());
        }

        primaryStage.setOnCloseRequest(event -> closeHibernate());

        primaryStage.show();
    }

    private void configureHibernate() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("/resources/hibernate.cfg.xml")
                .build();

        Metadata meta = new MetadataSources(ssr)
                .getMetadataBuilder()
                .build();

        sessionFactory = meta.getSessionFactoryBuilder().build();
        trainRepository = new TrainRepository(sessionFactory);
        avionRepository = new AvionRepository(sessionFactory);
        gareRepository = new GareRepository(sessionFactory);
        aeroportRepository = new AeroportRepository(sessionFactory);
        trajetRepository = new TrajetRepository(sessionFactory);
    }

    private void closeHibernate() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private VBox createButtonsBox() {
        VBox vbox = new VBox(10);

        stationTypeComboBox = new ComboBox<>();
        stationTypeComboBox.getItems().addAll("Aeroport", "Gare");
        stationTypeComboBox.setPromptText("Sélectionner un type de station");

        Button addTrajetButton = new Button("Ajouter un trajet");
        Button editButton = new Button("Modifier un trajet");
        Button deleteButton = new Button("Supprimer un trajet");

        addTrajetButton.setOnAction(event -> showAddTrajetDialog());
        editButton.setOnAction(event -> showEditTrajetDialog());
        deleteButton.setOnAction(event -> showDeleteTrajetDialog());

        vbox.getChildren().addAll(stationTypeComboBox, addTrajetButton, editButton, deleteButton);
        return vbox;
    }

    private void displayTrajets() {
        List<Trajet> trajets = trajetRepository.get();

        listView.getItems().clear();
        for (Trajet trajet : trajets) {
            listView.getItems().add(formatTrajet(trajet));
        }
    }

    private String formatTrajet(Trajet trajet) {
        String startStationName = trajet.getStartStation().getNom();
        String endStationName = trajet.getEndStation().getNom();
        return trajet.getMoyenTransport().getNom() + " - Départ : " + startStationName + ", Arrivée : " + endStationName;
    }
    
    private boolean checkStationsAndTransportCompatibility(MoyenTransport moyenTransport, Station startStation, Station endStation) {
        // Vérifie la compatibilité entre les stations et le moyen de transport
        if (moyenTransport instanceof Train) {
            // Si le moyen de transport est un train, les stations doivent être des gares
            return startStation instanceof Gare && endStation instanceof Gare;
        } else if (moyenTransport instanceof Avion) {
            // Si le moyen de transport est un avion, les stations doivent être des aéroports
            return startStation instanceof Aeroport && endStation instanceof Aeroport;
        }
        return false; // Retourne false si le moyen de transport n'est ni un train ni un avion
    }
    
    private void showAddTrajetDialog() {
        String selectedStationType = stationTypeComboBox.getValue();
        if (selectedStationType == null) {
            showErrorAlert("Veuillez sélectionner une station.");
            return;
        }

        List<Station> stations = new ArrayList<Station>();
        List<MoyenTransport> moyenTransports = new ArrayList<MoyenTransport>();

        try {
            if (selectedStationType.equals("Aeroport")) {
                stations.addAll(aeroportRepository.get());
                moyenTransports.addAll(avionRepository.get());
            } else if (selectedStationType.equals("Gare")) {
                stations.addAll(gareRepository.get());
                moyenTransports.addAll(trainRepository.get());
            } else {
                showErrorAlert("Type de station non reconnu.");
                return;
            }

            ChoiceDialog<Station> startStationDialog = new ChoiceDialog<>(null, stations);
            startStationDialog.setTitle("Choisir une station de départ");
            startStationDialog.setHeaderText(null);
            startStationDialog.setContentText("Choisir une station de départ pour le trajet :");

            startStationDialog.showAndWait().ifPresent(selectedStartStation -> {
                List<Station> endStations = new ArrayList<>(stations);
                endStations.remove(selectedStartStation);

                ChoiceDialog<Station> endStationDialog = new ChoiceDialog<>(null, endStations);
                endStationDialog.setTitle("Choisir une station d'arrivée");
                endStationDialog.setHeaderText(null);
                endStationDialog.setContentText("Choisir une station d'arrivée pour le trajet :");

                endStationDialog.showAndWait().ifPresent(selectedEndStation -> {
                    ChoiceDialog<MoyenTransport> moyenTransportDialog = new ChoiceDialog<>(null, moyenTransports);
                    moyenTransportDialog.setTitle("Choisir un moyen de transport");
                    moyenTransportDialog.setHeaderText(null);
                    moyenTransportDialog.setContentText("Choisir un moyen de transport pour le trajet :");

                    moyenTransportDialog.showAndWait().ifPresent(selectedMoyenTransport -> {
						if (checkStationsAndTransportCompatibility(selectedMoyenTransport, selectedStartStation, selectedEndStation)) {
						    Trajet nouveauTrajet = new Trajet();
						    nouveauTrajet.setMoyenTransport(selectedMoyenTransport);
						    nouveauTrajet.setStartStation(selectedStartStation);
						    nouveauTrajet.setEndStation(selectedEndStation);
						    trajetRepository.upsert(nouveauTrajet);
						    displayTrajets();
						} else {
						    showErrorAlert("Les stations et le moyen de transport ne sont pas compatibles.");
						}

                    });

                });
            });
        } catch (Exception e) {
            showErrorAlert("Une erreur s'est produite : " + e.getMessage());
        }
    }

    private void showEditTrajetDialog() {
        List<Trajet> trajets = trajetRepository.get();

        ChoiceDialog<Trajet> dialog = new ChoiceDialog<>(null, trajets);
        dialog.setTitle("Modifier un trajet");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir un trajet à modifier :");

        dialog.showAndWait().ifPresent(selectedTrajet -> {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();

                List<Station> stations = new ArrayList<>();
                if (selectedTrajet.getMoyenTransport() instanceof Train) {
                    stations.addAll(gareRepository.get());
                } else if (selectedTrajet.getMoyenTransport() instanceof Avion) {
                    stations.addAll(aeroportRepository.get());
                }

                ChoiceDialog<Station> startStationDialog = new ChoiceDialog<>(selectedTrajet.getStartStation(), stations);
                startStationDialog.setTitle("Modifier la station de départ");
                startStationDialog.setHeaderText(null);
                startStationDialog.setContentText("Choisir une nouvelle station de départ pour le trajet :");

                startStationDialog.showAndWait().ifPresent(newStartStation -> {
                    List<Station> endStations = new ArrayList<>(stations);
                    endStations.remove(newStartStation);

                    ChoiceDialog<Station> endStationDialog = new ChoiceDialog<>(selectedTrajet.getEndStation(), endStations);
                    endStationDialog.setTitle("Modifier la station d'arrivée");
                    endStationDialog.setHeaderText(null);
                    endStationDialog.setContentText("Choisir une nouvelle station d'arrivée pour le trajet :");

                    endStationDialog.showAndWait().ifPresent(newEndStation -> {
                        if (checkStationsAndTransportCompatibility(selectedTrajet.getMoyenTransport(), newStartStation, newEndStation)) {
                            selectedTrajet.setStartStation(newStartStation);
                            selectedTrajet.setEndStation(newEndStation);
                            trajetRepository.update(selectedTrajet); // Use the session for update
                            transaction.commit();
                            displayTrajets();
                        } else {
                            transaction.rollback();
                            showErrorAlert("Les stations et le moyen de transport ne sont pas compatibles.");
                        }
                    });
                });
            } catch (Exception e) {
                showErrorAlert("Une erreur s'est produite : " + e.getMessage());
            }
        });
    }

    private void showDeleteTrajetDialog() {
        List<Trajet> trajets = trajetRepository.get();

        ChoiceDialog<Trajet> dialog = new ChoiceDialog<>(null, trajets);
        dialog.setTitle("Supprimer un trajet");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir un trajet à supprimer :");

        dialog.showAndWait().ifPresent(selectedTrajet -> {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();

                boolean deleteSuccess = trajetRepository.remove(selectedTrajet.getId()); // Use the session for deletion
                if (deleteSuccess) {
                    transaction.commit();
                    displayTrajets();
                } else {
                    transaction.rollback();
                    showErrorAlert("Le trajet n'existe pas ou une erreur s'est produite.");
                }
            } catch (Exception e) {
                showErrorAlert("Une erreur s'est produite : " + e.getMessage());
            }
        });
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
