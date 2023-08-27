package main.java;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import repositories.AvionRepository;
import repositories.TrainRepository;
import classes.Avion;
import classes.Train;

import java.util.List;

public class GestionMoyenTransport {

    private AvionRepository avionRepository;
    private TrainRepository trainRepository;
    private ListView<String> listView;
    private SessionFactory sessionFactory;

    public void startGestion(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Moyens de Transport");

        configureHibernate();

        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 800, 600);

            listView = new ListView<>();
            displayMoyensTransport();

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
        avionRepository = new AvionRepository(sessionFactory);
        trainRepository = new TrainRepository(sessionFactory);
    }

    private void closeHibernate() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private VBox createButtonsBox() {
        VBox vbox = new VBox(10);

        Button addAvionButton = new Button("Ajouter un avion");
        Button addTrainButton = new Button("Ajouter un train");
        Button editButton = new Button("Modifier un moyen de transport");
        Button deleteButton = new Button("Supprimer un moyen de transport");

        addAvionButton.setOnAction(event -> showAddAvionDialog());
        addTrainButton.setOnAction(event -> showAddTrainDialog());
        editButton.setOnAction(event -> showEditMoyenTransportDialog());
        deleteButton.setOnAction(event -> showDeleteMoyenTransportDialog());

        vbox.getChildren().addAll(addAvionButton, addTrainButton, editButton, deleteButton);
        return vbox;
    }

    private void displayMoyensTransport() {
        List<Avion> avions = avionRepository.get();
        List<Train> trains = trainRepository.get();

        listView.getItems().clear();
        for (Avion avion : avions) {
            listView.getItems().add("Avion - ID : " + avion.getId() + ", Nom : " + avion.getNom());
        }
        for (Train train : trains) {
            listView.getItems().add("Train - ID : " + train.getId() + ", Nom : " + train.getNom());
        }
    }
    private void showAddAvionDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un avion");
        dialog.setHeaderText(null);
        dialog.setContentText("Nom de l'avion :");

        dialog.showAndWait().ifPresent(nom -> {
            Dialog<String> avionDialog = new TextInputDialog();
            avionDialog.setTitle("Ajouter un avion");
            avionDialog.setHeaderText(null);
            avionDialog.setContentText("Nombre de places de l'avion :");

            avionDialog.showAndWait().ifPresent(nbPlaces -> {
                try {
                    Avion nouvelAvion = new Avion();
                    nouvelAvion.setNom(nom);
                    nouvelAvion.setNbPlaces(Integer.parseInt(nbPlaces));
                    avionRepository.upsert(nouvelAvion);
                    displayMoyensTransport();
                } catch (NumberFormatException e) {
                    showErrorAlert("Veuillez entrer un nombre valide de places.");
                }
            });
        });
    }

    private void showAddTrainDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un train");
        dialog.setHeaderText(null);
        dialog.setContentText("Nom du train :");

        dialog.showAndWait().ifPresent(nom -> {
            Dialog<String> trainDialog = new TextInputDialog();
            trainDialog.setTitle("Ajouter un train");
            trainDialog.setHeaderText(null);
            trainDialog.setContentText("Nombre de wagons du train :");

            trainDialog.showAndWait().ifPresent(nbWagons -> {
                try {
                    Train nouveauTrain = new Train();
                    nouveauTrain.setNom(nom);
                    nouveauTrain.setNbWagons(Integer.parseInt(nbWagons));
                    trainRepository.upsert(nouveauTrain);
                    displayMoyensTransport();
                } catch (NumberFormatException e) {
                    showErrorAlert("Veuillez entrer un nombre valide de wagons.");
                }
            });
        });
    }
    private void showEditMoyenTransportDialog() {
        List<Avion> avions = avionRepository.get();
        List<Train> trains = trainRepository.get();

        // Créer un choix entre avions et trains
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Avion", "Avion", "Train");
        dialog.setTitle("Modifier un moyen de transport");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir un moyen de transport à modifier :");

        dialog.showAndWait().ifPresent(selectedType -> {
            if (selectedType.equals("Avion")) {
                ChoiceDialog<Avion> avionDialog = new ChoiceDialog<>(null, avions);
                avionDialog.setTitle("Choisir un avion à modifier");
                avionDialog.setHeaderText(null);
                avionDialog.setContentText("Choisir un avion :");

                avionDialog.showAndWait().ifPresent(selectedAvion -> {
                    TextInputDialog editDialog = new TextInputDialog(selectedAvion.getNom());
                    editDialog.setTitle("Modifier un avion");
                    editDialog.setHeaderText(null);
                    editDialog.setContentText("Nouveau nom de l'avion :");

                    editDialog.showAndWait().ifPresent(nouveauNom -> {
                        selectedAvion.setNom(nouveauNom);
                        Dialog<String> nbPlacesDialog = new TextInputDialog(String.valueOf(selectedAvion.getNbPlaces()));
                        nbPlacesDialog.setTitle("Modifier le nombre de places");
                        nbPlacesDialog.setHeaderText(null);
                        nbPlacesDialog.setContentText("Nouveau nombre de places :");

                        nbPlacesDialog.showAndWait().ifPresent(nbPlaces -> {
                            try {
                                selectedAvion.setNbPlaces(Integer.parseInt(nbPlaces));
                                avionRepository.update(selectedAvion);
                                displayMoyensTransport();
                            } catch (NumberFormatException e) {
                                showErrorAlert("Veuillez entrer un nombre valide de places.");
                            }
                        });
                    });
                });
            } else if (selectedType.equals("Train")) {
                ChoiceDialog<Train> trainDialog = new ChoiceDialog<>(null, trains);
                trainDialog.setTitle("Choisir un train à modifier");
                trainDialog.setHeaderText(null);
                trainDialog.setContentText("Choisir un train :");

                trainDialog.showAndWait().ifPresent(selectedTrain -> {
                    TextInputDialog editDialog = new TextInputDialog(selectedTrain.getNom());
                    editDialog.setTitle("Modifier un train");
                    editDialog.setHeaderText(null);
                    editDialog.setContentText("Nouveau nom du train :");

                    editDialog.showAndWait().ifPresent(nouveauNom -> {
                        selectedTrain.setNom(nouveauNom);
                        Dialog<String> nbWagonsDialog = new TextInputDialog(String.valueOf(selectedTrain.getNbWagons()));
                        nbWagonsDialog.setTitle("Modifier le nombre de wagons");
                        nbWagonsDialog.setHeaderText(null);
                        nbWagonsDialog.setContentText("Nouveau nombre de wagons :");

                        nbWagonsDialog.showAndWait().ifPresent(nbWagons -> {
                            try {
                                selectedTrain.setNbWagons(Integer.parseInt(nbWagons));
                                trainRepository.update(selectedTrain);
                                displayMoyensTransport();
                            } catch (NumberFormatException e) {
                                showErrorAlert("Veuillez entrer un nombre valide de wagons.");
                            }
                        });
                    });
                });
            }
        });
    }



    private void showDeleteMoyenTransportDialog() {
        List<Avion> avions = avionRepository.get();
        List<Train> trains = trainRepository.get();

        // Créer un choix entre avions et trains
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Avion", "Avion", "Train");
        dialog.setTitle("Supprimer un moyen de transport");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir un moyen de transport à supprimer :");

        dialog.showAndWait().ifPresent(selectedType -> {
            if (selectedType.equals("Avion")) {
                ChoiceDialog<Avion> avionDialog = new ChoiceDialog<>(null, avions);
                avionDialog.setTitle("Choisir un avion à supprimer");
                avionDialog.setHeaderText(null);
                avionDialog.setContentText("Choisir un avion :");

                avionDialog.showAndWait().ifPresent(selectedAvion -> {
                    avionRepository.remove(selectedAvion.getId());
                    displayMoyensTransport();
                });
            } else if (selectedType.equals("Train")) {
                ChoiceDialog<Train> trainDialog = new ChoiceDialog<>(null, trains);
                trainDialog.setTitle("Choisir un train à supprimer");
                trainDialog.setHeaderText(null);
                trainDialog.setContentText("Choisir un train :");

                trainDialog.showAndWait().ifPresent(selectedTrain -> {
                    trainRepository.remove(selectedTrain.getId());
                    displayMoyensTransport();
                });
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
