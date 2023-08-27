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

import interfaces.IVilleRepository;
import classes.Ville;
import repositories.VilleRepository;

import java.util.List;

public class GestionVille {

    private IVilleRepository villeRepository;
    private ListView<String> listView;
    private SessionFactory sessionFactory;

    // Méthode pour démarrer l'interface de gestion des villes
    public void startGestion(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Villes");

        // Configuration de la connexion à la base de données via Hibernate
        configureHibernate();

        try {
            // Création de l'interface utilisateur
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 800, 600);

            // Création de la liste pour afficher les villes
            listView = new ListView<>();
            displayVilles();

            // Création du bouton "Retour au menu"
            Button retourMenuButton = new Button("Retour au menu");
            retourMenuButton.setOnAction(event -> {
                // Fermer la fenêtre de gestion des villes
                primaryStage.close();

                // Ouvrir à nouveau la fenêtre du menu principal
                new JavaFXApp().start(new Stage());
            });

            // Création d'une boîte pour les boutons d'action
            VBox buttonsBox = createButtonsBox();
            buttonsBox.getChildren().add(retourMenuButton);

            // Placement des composants dans le BorderPane
            root.setLeft(listView);
            root.setRight(buttonsBox);

            // Configuration de la scène
            primaryStage.setScene(scene);
        } catch (Exception e) {
            showErrorAlert("Une erreur s'est produite : " + e.getMessage());
        }

        // Gestion de la fermeture de la fenêtre
        primaryStage.setOnCloseRequest(event -> closeHibernate());

        // Affichage de la fenêtre
        primaryStage.show();
    }

    // Configuration de Hibernate pour la gestion de la base de données
    private void configureHibernate() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("/resources/hibernate.cfg.xml")
                .build();

        Metadata meta = new MetadataSources(ssr)
                .getMetadataBuilder()
                .build();

        sessionFactory = meta.getSessionFactoryBuilder().build();
        villeRepository = new VilleRepository(sessionFactory);
    }

    // Fermeture de la connexion Hibernate
    private void closeHibernate() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    // Création de la boîte de boutons d'action
    private VBox createButtonsBox() {
        VBox vbox = new VBox(10);

        // Création des boutons pour ajouter, modifier et supprimer
        Button addButton = new Button("Ajouter une ville");
        Button editButton = new Button("Modifier une ville");
        Button deleteButton = new Button("Supprimer une ville");

        // Association des actions aux boutons
        addButton.setOnAction(event -> showAddVilleDialog());
        editButton.setOnAction(event -> showEditVilleDialog());
        deleteButton.setOnAction(event -> showDeleteVilleDialog());

        // Ajout des boutons à la boîte
        vbox.getChildren().addAll(addButton, editButton, deleteButton);
        return vbox;
    }

    // Affichage de la liste des villes
    private void displayVilles() {
        // Récupération des villes depuis la base de données
        List<Ville> villes = villeRepository.get();

        // Effacement de la liste précédente
        listView.getItems().clear();
        
        // Ajout des noms des villes à la ListView
        for (Ville ville : villes) {
            listView.getItems().add("ID : " + ville.getId() + ", Nom : " + ville.getNom());
        }
    }

    // Affichage du dialogue pour ajouter une nouvelle ville
    private void showAddVilleDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter une ville");
        dialog.setHeaderText(null);
        dialog.setContentText("Nom de la ville :");

        dialog.showAndWait().ifPresent(nom -> {
            Ville nouvelleVille = new Ville();
            nouvelleVille.setNom(nom);
            villeRepository.upsert(nouvelleVille);
            displayVilles();
        });
    }

    // Affichage du dialogue pour supprimer une ville
    private void showDeleteVilleDialog() {
        List<Ville> villes = villeRepository.get();
        ChoiceDialog<Ville> dialog = new ChoiceDialog<>(null, villes);
        dialog.setTitle("Supprimer une ville");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir une ville à supprimer :");

        dialog.showAndWait().ifPresent(selectedVille -> {
            boolean deleteSuccess = villeRepository.remove(selectedVille.getId());
            if (deleteSuccess) {
                displayVilles();
            } else {
                showErrorAlert("La ville n'existe pas ou une erreur s'est produite.");
            }
        });
    }

    // Affichage du dialogue pour modifier une ville
    private void showEditVilleDialog() {
        List<Ville> villes = villeRepository.get();
        ChoiceDialog<Ville> dialog = new ChoiceDialog<>(null, villes);
        dialog.setTitle("Modifier une ville");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir une ville à modifier :");

        dialog.showAndWait().ifPresent(selectedVille -> {
            TextInputDialog editDialog = new TextInputDialog(selectedVille.getNom());
            editDialog.setTitle("Modifier une ville");
            editDialog.setHeaderText(null);
            editDialog.setContentText("Nouveau nom de la ville :");

            editDialog.showAndWait().ifPresent(nouveauNom -> {
                selectedVille.setNom(nouveauNom);
                boolean updateSuccess = villeRepository.update(selectedVille);
                if (updateSuccess) {
                    displayVilles();
                } else {
                    showErrorAlert("Une erreur s'est produite lors de la mise à jour.");
                }
            });
        });
    }

    // Affichage d'une alerte en cas d'erreur
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
