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

import classes.ParcDisney;
import classes.Ville;
import repositories.ParcDisneyRepository;
import repositories.VilleRepository;

import java.util.List;

public class GestionParc {

	private VilleRepository villeRepository;
    private ParcDisneyRepository parcDisneyRepository;
    private ListView<String> listView;
    private SessionFactory sessionFactory;

    public void startGestion(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Parcs");

        configureHibernate();

        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 800, 600);

            listView = new ListView<>();
            displayParcs();

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
        parcDisneyRepository = new ParcDisneyRepository(sessionFactory);
        villeRepository = new VilleRepository(sessionFactory);
    }

    private void closeHibernate() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private VBox createButtonsBox() {
        VBox vbox = new VBox(10);

        Button addButton = new Button("Ajouter un parc");
        Button editButton = new Button("Modifier un parc");
        Button deleteButton = new Button("Supprimer un parc");

        addButton.setOnAction(event -> showAddParcDialog());
        editButton.setOnAction(event -> showEditParcDialog());
        deleteButton.setOnAction(event -> showDeleteParcDialog());

        vbox.getChildren().addAll(addButton, editButton, deleteButton);
        return vbox;
    }

    private void displayParcs() {
        List<ParcDisney> parcs = parcDisneyRepository.get();

        listView.getItems().clear();
        for (ParcDisney parc : parcs) {
            listView.getItems().add("ID : " + parc.getId() + ", Nom : " + parc.getNom());
        }
    }

    private void showAddParcDialog() {
    	
        List<Ville> villes = villeRepository.get();
        ChoiceDialog<Ville> dialogVille = new ChoiceDialog<>(null, villes);
        dialogVille.setTitle("Choisir une ville");
        dialogVille.setHeaderText(null);
        dialogVille.setContentText("Choisir une ville pour le parc :");


        dialogVille.showAndWait().ifPresent(selectedVille -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ajouter un parc");
            dialog.setHeaderText(null);
            dialog.setContentText("Nom du parc :");

            dialog.showAndWait().ifPresent(nom -> {
                ParcDisney nouveauParc = new ParcDisney();
                nouveauParc.setNom(nom);
                nouveauParc.setVille(selectedVille);
                parcDisneyRepository.upsert(nouveauParc);
                displayParcs();
            });
        });
    }
    private void showEditParcDialog() {
        List<ParcDisney> parcs = parcDisneyRepository.get();
        ChoiceDialog<ParcDisney> dialog = new ChoiceDialog<>(null, parcs);
        dialog.setTitle("Modifier un parc");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir un parc à modifier :");

        dialog.showAndWait().ifPresent(selectedParc -> {
            TextInputDialog editDialog = new TextInputDialog(selectedParc.getNom());
            editDialog.setTitle("Modifier un parc");
            editDialog.setHeaderText(null);
            editDialog.setContentText("Nouveau nom du parc :");

            editDialog.showAndWait().ifPresent(nouveauNom -> {
                selectedParc.setNom(nouveauNom);
                boolean updateSuccess = parcDisneyRepository.update(selectedParc);
                if (updateSuccess) {
                    displayParcs();
                } else {
                    showErrorAlert("Une erreur s'est produite lors de la mise à jour.");
                }
            });
        });
    }

    private void showDeleteParcDialog() {
        List<ParcDisney> parcs = parcDisneyRepository.get();
        ChoiceDialog<ParcDisney> dialog = new ChoiceDialog<>(null, parcs);
        dialog.setTitle("Supprimer un parc");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir un parc à supprimer :");

        dialog.showAndWait().ifPresent(selectedParc -> {
            boolean deleteSuccess = parcDisneyRepository.remove(selectedParc.getId());
            if (deleteSuccess) {
                displayParcs();
            } else {
                showErrorAlert("Le parc n'existe pas ou une erreur s'est produite.");
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
