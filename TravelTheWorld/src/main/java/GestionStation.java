package main.java;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import repositories.AeroportRepository;
import repositories.GareRepository;
import repositories.VilleRepository;
import classes.Aeroport;
import classes.Gare;
import classes.Ville;

import java.util.List;

public class GestionStation {

	private VilleRepository villeRepository;
    private AeroportRepository aeroportRepository;
    private GareRepository gareRepository;
    private ListView<String> listView;
    private SessionFactory sessionFactory;

    public void startGestion(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Stations");

        configureHibernate();

        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 800, 600);

            listView = new ListView<>();
            displayStations();

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
        villeRepository = new VilleRepository(sessionFactory);
        aeroportRepository = new AeroportRepository(sessionFactory);
        gareRepository = new GareRepository(sessionFactory);
    }

    private void closeHibernate() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private VBox createButtonsBox() {
        VBox vbox = new VBox(10);

        Button addAeroportButton = new Button("Ajouter un aéroport");
        Button addGareButton = new Button("Ajouter une gare");
        Button editButton = new Button("Modifier une station");
        Button deleteButton = new Button("Supprimer une station");

        addAeroportButton.setOnAction(event -> showAddAeroportDialog());
        addGareButton.setOnAction(event -> showAddGareDialog());
        editButton.setOnAction(event -> showEditStationDialog());
        deleteButton.setOnAction(event -> showDeleteStationDialog());

        vbox.getChildren().addAll(addAeroportButton, addGareButton, editButton, deleteButton);
        return vbox;
    }

    private void displayStations() {
        List<Aeroport> aeroports = aeroportRepository.get();
        List<Gare> gares = gareRepository.get();

        listView.getItems().clear();
        for (Aeroport aeroport : aeroports) {
            listView.getItems().add("Aéroport - ID : " + aeroport.getId() + ", Nom : " + aeroport.getNom());
        }
        for (Gare gare : gares) {
            listView.getItems().add("Gare - ID : " + gare.getId() + ", Nom : " + gare.getNom());
        }
    }

	private void showAddAeroportDialog() {
	    List<Ville> villes = villeRepository.get();
	    ChoiceDialog<Ville> dialogVille = new ChoiceDialog<>(null, villes);
	    dialogVille.setTitle("Choisir une ville");
	    dialogVille.setHeaderText(null);
	    dialogVille.setContentText("Choisir une ville pour l'aéroport :");
	
	    dialogVille.showAndWait().ifPresent(selectedVille -> {
	        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
	        dialog.setTitle("Ajouter un aéroport");
	        dialog.setHeaderText(null);
	
	        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
	
	        GridPane grid = new GridPane();
	        grid.setHgap(10);
	        grid.setVgap(10);
	
	        TextField nomField = new TextField();
	        nomField.setPromptText("Nom de l'aéroport");
	        TextField maxAvionsField = new TextField();
	        maxAvionsField.setPromptText("Nombre maximal d'avions");
	
	        grid.add(new Label("Nom de l'aéroport:"), 0, 0);
	        grid.add(nomField, 1, 0);
	        grid.add(new Label("Nombre maximal d'avions:"), 0, 1);
	        grid.add(maxAvionsField, 1, 1);
	
	        dialog.getDialogPane().setContent(grid);
	
	        dialog.setResultConverter(dialogButton -> {
	            if (dialogButton == ButtonType.OK) {
	                String nom = nomField.getText();
	                try {
	                    int maxAvions = Integer.parseInt(maxAvionsField.getText());
	                    return new Pair<>(nom, maxAvions);
	                } catch (NumberFormatException e) {
	                    Alert alert = new Alert(AlertType.ERROR);
	                    alert.setTitle("Error");
	                    alert.setHeaderText("Invalid Input");
	                    alert.setContentText("Please enter a valid integer for max avions.");
	                    alert.showAndWait();
	                    return null;
	                }
	            }
	            return null;
	        });
	
	        dialog.showAndWait().ifPresent(result -> {
	            String nom = result.getKey();
	            int maxAvions = result.getValue();
	            
	            Aeroport nouvelAeroport = new Aeroport();
	            nouvelAeroport.setNom(nom);
	            nouvelAeroport.setMaxAvions(maxAvions);
	            nouvelAeroport.setVille(selectedVille);
	            aeroportRepository.upsert(nouvelAeroport);
	            displayStations();
	        });
	    });
	}

    private void showAddGareDialog() {
        List<Ville> villes = villeRepository.get();
        ChoiceDialog<Ville> dialogVille = new ChoiceDialog<>(null, villes);
        dialogVille.setTitle("Choisir une ville");
        dialogVille.setHeaderText(null);
        dialogVille.setContentText("Choisir une ville pour la gare :");

        dialogVille.showAndWait().ifPresent(selectedVille -> {
            Dialog<Pair<String, Integer>> dialog = new Dialog<>();
            dialog.setTitle("Ajouter une gare");
            dialog.setHeaderText(null);

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nomField = new TextField();
            nomField.setPromptText("Nom de la gare");
            TextField maxTrainsField = new TextField();
            maxTrainsField.setPromptText("Nombre maximal de trains");

            grid.add(new Label("Nom de la gare:"), 0, 0);
            grid.add(nomField, 1, 0);
            grid.add(new Label("Nombre maximal de trains:"), 0, 1);
            grid.add(maxTrainsField, 1, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    String nom = nomField.getText();
                    try {
                        int maxTrains = Integer.parseInt(maxTrainsField.getText());
                        return new Pair<>(nom, maxTrains);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Input");
                        alert.setContentText("Please enter a valid integer for max trains.");
                        alert.showAndWait();
                        return null;
                    }
                }
                return null;
            });

            // Show the dialog and retrieve the entered values
            dialog.showAndWait().ifPresent(result -> {
                String nom = result.getKey();
                int maxTrains = result.getValue();
                
                Gare nouvelleGare = new Gare();
                nouvelleGare.setNom(nom);
                nouvelleGare.setMaxTrains(maxTrains);
                nouvelleGare.setVille(selectedVille);
                gareRepository.upsert(nouvelleGare);
                displayStations();
            });
        });
    }
    
    private void showEditStationDialog() {
        List<Aeroport> aeroports = aeroportRepository.get();
        List<Gare> gares = gareRepository.get();

        // Créer un choix entre aéroports et gares
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Aéroport", "Aéroport", "Gare");
        dialog.setTitle("Modifier une station");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir une station à modifier :");

        dialog.showAndWait().ifPresent(selectedType -> {
            if (selectedType.equals("Aéroport")) {
                ChoiceDialog<Aeroport> aeroportDialog = new ChoiceDialog<>(null, aeroports);
                aeroportDialog.setTitle("Choisir un aéroport à modifier");
                aeroportDialog.setHeaderText(null);
                aeroportDialog.setContentText("Choisir un aéroport :");

                aeroportDialog.showAndWait().ifPresent(selectedAeroport -> {
                    Dialog<Pair<String, Integer>> editDialog = new Dialog<>();
                    editDialog.setTitle("Modifier un aéroport");
                    editDialog.setHeaderText(null);

                    editDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);

                    TextField nomField = new TextField(selectedAeroport.getNom());
                    TextField maxAvionsField = new TextField(Integer.toString(selectedAeroport.getMaxAvions()));

                    grid.add(new Label("Nouveau nom de l'aéroport:"), 0, 0);
                    grid.add(nomField, 1, 0);
                    grid.add(new Label("Nouveau nombre maximal d'avions:"), 0, 1);
                    grid.add(maxAvionsField, 1, 1);

                    editDialog.getDialogPane().setContent(grid);

                    editDialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            String nouveauNom = nomField.getText();
                            try {
                                int nouveauMaxAvions = Integer.parseInt(maxAvionsField.getText());
                                return new Pair<>(nouveauNom, nouveauMaxAvions);
                            } catch (NumberFormatException e) {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Invalid Input");
                                alert.setContentText("Please enter a valid integer for max avions.");
                                alert.showAndWait();
                                return null;
                            }
                        }
                        return null;
                    });

                    editDialog.showAndWait().ifPresent(result -> {
                        String nouveauNom = result.getKey();
                        int nouveauMaxAvions = result.getValue();

                        selectedAeroport.setNom(nouveauNom);
                        selectedAeroport.setMaxAvions(nouveauMaxAvions);
                        boolean updateSuccess = aeroportRepository.update(selectedAeroport);
                        if (updateSuccess) {
                            displayStations();
                        } else {
                            showErrorAlert("Une erreur s'est produite lors de la mise à jour.");
                        }
                    });
                });
            } else if (selectedType.equals("Gare")) {
                ChoiceDialog<Gare> gareDialog = new ChoiceDialog<>(null, gares);
                gareDialog.setTitle("Choisir une gare à modifier");
                gareDialog.setHeaderText(null);
                gareDialog.setContentText("Choisir une gare :");

                gareDialog.showAndWait().ifPresent(selectedGare -> {
                    // Create a custom dialog for editing a gare
                    Dialog<Pair<String, Integer>> editDialog = new Dialog<>();
                    editDialog.setTitle("Modifier une gare");
                    editDialog.setHeaderText(null);

                    // Set the button types
                    editDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    // Create a GridPane layout to hold the input fields
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);

                    TextField nomField = new TextField(selectedGare.getNom());
                    TextField maxTrainsField = new TextField(Integer.toString(selectedGare.getMaxTrains()));

                    grid.add(new Label("Nouveau nom de la gare:"), 0, 0);
                    grid.add(nomField, 1, 0);
                    grid.add(new Label("Nouveau nombre maximal de trains:"), 0, 1);
                    grid.add(maxTrainsField, 1, 1);

                    editDialog.getDialogPane().setContent(grid);

                    // Convert the result to a Pair<String, Integer> when the OK button is clicked
                    editDialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            String nouveauNom = nomField.getText();
                            try {
                                int nouveauMaxTrains = Integer.parseInt(maxTrainsField.getText());
                                return new Pair<>(nouveauNom, nouveauMaxTrains);
                            } catch (NumberFormatException e) {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Invalid Input");
                                alert.setContentText("Please enter a valid integer for max trains.");
                                alert.showAndWait();
                                return null;
                            }
                        }
                        return null;
                    });

                    // Show the dialog and retrieve the entered values
                    editDialog.showAndWait().ifPresent(result -> {
                        String nouveauNom = result.getKey();
                        int nouveauMaxTrains = result.getValue();

                        selectedGare.setNom(nouveauNom);
                        selectedGare.setMaxTrains(nouveauMaxTrains);
                        boolean updateSuccess = gareRepository.update(selectedGare);
                        if (updateSuccess) {
                            displayStations();
                        } else {
                            showErrorAlert("Une erreur s'est produite lors de la mise à jour.");
                        }
                    });
                });
            }
        });
    }


    private void showDeleteStationDialog() {
        List<Aeroport> aeroports = aeroportRepository.get();
        List<Gare> gares = gareRepository.get();

        // Créer un choix entre aéroports et gares
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Aéroport", "Aéroport", "Gare");
        dialog.setTitle("Supprimer une station");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir une station à supprimer :");

        dialog.showAndWait().ifPresent(selectedType -> {
            if (selectedType.equals("Aéroport")) {
                ChoiceDialog<Aeroport> aeroportDialog = new ChoiceDialog<>(null, aeroports);
                aeroportDialog.setTitle("Choisir un aéroport à supprimer");
                aeroportDialog.setHeaderText(null);
                aeroportDialog.setContentText("Choisir un aéroport :");

                aeroportDialog.showAndWait().ifPresent(selectedAeroport -> {
                    boolean deleteSuccess = aeroportRepository.remove(selectedAeroport.getId());
                    if (deleteSuccess) {
                        displayStations();
                    } else {
                        showErrorAlert("L'aéroport n'existe pas ou une erreur s'est produite.");
                    }
                });
            } else if (selectedType.equals("Gare")) {
                ChoiceDialog<Gare> gareDialog = new ChoiceDialog<>(null, gares);
                gareDialog.setTitle("Choisir une gare à supprimer");
                gareDialog.setHeaderText(null);
                gareDialog.setContentText("Choisir une gare :");

                gareDialog.showAndWait().ifPresent(selectedGare -> {
                    boolean deleteSuccess = gareRepository.remove(selectedGare.getId());
                    if (deleteSuccess) {
                        displayStations();
                    } else {
                        showErrorAlert("La gare n'existe pas ou une erreur s'est produite.");
                    }
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
