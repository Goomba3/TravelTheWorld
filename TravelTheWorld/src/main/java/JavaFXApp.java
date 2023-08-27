package main.java;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import classes.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import repositories.UserRepository;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class JavaFXApp extends Application {

    // Instances pour gérer différentes fonctionnalités
    private GestionVille gestionVille = new GestionVille();
    private GestionParc gestionParc = new GestionParc();
    private GestionStation gestionStation = new GestionStation();
    private GestionMoyenTransport gestionMoyenTransport = new GestionMoyenTransport();
    private GestionTrajet gestionTrajet = new GestionTrajet();
    
    private UserRepository userRepository;
    private SessionFactory sessionFactory;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	configureHibernate();
        primaryStage.setTitle("Travel The World");

        // Crée le conteneur principal
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1200, 1000);
        
        // Définit la couleur de fond
        root.setStyle("-fx-background-color: #f06292;");

        // Crée la boîte de menu
        VBox menuBox = createMenuBox(primaryStage);
        root.setCenter(menuBox);

        primaryStage.setOnCloseRequest(event -> closeHibernate());
        
        // Configure la scène et affiche la fenêtre
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMenuBox(Stage primaryStage) {
        VBox vbox = new VBox(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(100));

        Label usersLabel = new Label("Utilisateurs :");
        usersLabel.setFont(Font.font("Verdana", 14));
        
        // Créer un ListView pour afficher la liste des utilisateurs
        ListView<String> userListView = new ListView<>();
        userListView.setPrefHeight(200);
        userListView.setStyle("-fx-background-color: #ff6f00; -fx-background-radius: 20; -fx-text-fill: white;");
        
        
        // Obtenir la liste des utilisateurs
        List<User> users = userRepository.get();

        // Créer un modèle pour le ListView
        ObservableList<String> userNames = FXCollections.observableArrayList();
        for (User user : users) {
            userNames.add(user.getNom());
        }
        
        userListView.setItems(userNames); // Ajouter les noms d'utilisateurs au ListView

        // Styles pour les boutons
        Font buttonFont = Font.font("Verdana", 20);
        Color buttonTextColor = Color.WHITE;
        String buttonStyle = "-fx-background-color: #ff6f00; -fx-background-radius: 20; -fx-text-fill: white;";

        // Crée les boutons pour les différentes fonctionnalités
        Button gestionVilleButton = new Button("Gestion des Villes");
        Button gestionParcButton = new Button("Gestion des Parcs");
        Button gestionStationButton = new Button("Gestion des Stations");
        Button gestionMTButton = new Button("Gestion des Moyens de Transport");
        Button gestionTrajetButton = new Button("Gestion des Trajets");
        
        // Applique les styles aux boutons
        gestionVilleButton.setFont(buttonFont);
        gestionVilleButton.setTextFill(buttonTextColor);
        gestionVilleButton.setStyle(buttonStyle);
        
        gestionParcButton.setFont(buttonFont);
        gestionParcButton.setTextFill(buttonTextColor);
        gestionParcButton.setStyle(buttonStyle);
        
        gestionStationButton.setFont(buttonFont);
        gestionStationButton.setTextFill(buttonTextColor);
        gestionStationButton.setStyle(buttonStyle);
        
        gestionMTButton.setFont(buttonFont);
        gestionMTButton.setTextFill(buttonTextColor);
        gestionMTButton.setStyle(buttonStyle);

        gestionTrajetButton.setFont(buttonFont);
        gestionTrajetButton.setTextFill(buttonTextColor);
        gestionTrajetButton.setStyle(buttonStyle);
        
        // Associe les actions aux boutons pour lancer les fonctionnalités correspondantes
        gestionVilleButton.setOnAction(event -> gestionVille.startGestion(primaryStage));
        gestionParcButton.setOnAction(event -> gestionParc.startGestion(primaryStage));
        gestionStationButton.setOnAction(event -> gestionStation.startGestion(primaryStage));
        gestionMTButton.setOnAction(event -> gestionMoyenTransport.startGestion(primaryStage));
        gestionTrajetButton.setOnAction(event -> gestionTrajet.startGestion(primaryStage));

        // Ajoute d'abord la liste des utilisateurs, puis les boutons à la boîte de menu
        vbox.getChildren().addAll(usersLabel, userListView, gestionVilleButton, gestionParcButton, gestionStationButton, gestionMTButton, gestionTrajetButton);

        return vbox;
    }
    

    private void configureHibernate() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("/resources/hibernate.cfg.xml")
                .build();

        Metadata meta = new MetadataSources(ssr)
                .getMetadataBuilder()
                .build();

        sessionFactory = meta.getSessionFactoryBuilder().build();
        userRepository = new UserRepository(sessionFactory);
    }

    private void closeHibernate() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

}
