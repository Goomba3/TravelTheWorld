package main.web;

import javax.swing.*;
import javax.swing.SwingWorker;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import classes.Ville;
import classes.ParcDisney;
import classes.ParcVisite;
import classes.User;
import repositories.ParcDisneyRepository;
import repositories.ParcVisiteRepository;
import repositories.TrajetRepository;
import repositories.UserRepository;
import repositories.VilleRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TravelTheWorldWebApp {
	
    private UserRepository userRepository;
    private ParcVisiteRepository parcVisiteRepository;
    private ParcDisneyRepository parcDisneyRepository;
    private VilleRepository villeRepository;
    private TrajetRepository trajetRepository;
    private SessionFactory sessionFactory;

    JFrame frame = new JFrame();
    User loggedUser;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TravelTheWorldWebApp();
        });
    }

    public TravelTheWorldWebApp() {
        // Configuration de la connexion Hibernate
        configureHibernate();
        
        // Interface de connexion et de création d'utilisateur
        setupLoginUI();

        // Gestionnaire d'événements pour la fermeture de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeHibernate();
            }
        });
    }

    // Méthode pour configurer l'interface de connexion et de création d'utilisateur
    private void setupLoginUI() {
        JButton createButton = new JButton("Créer utilisateur");
        JButton loginButton = new JButton("Connexion");
        JTextField usernameField = new JTextField(20);
        JLabel messageLabel = new JLabel("");

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        panel.setLayout(new GridLayout(0, 1));

        panel.add(new JLabel("Nom :"));
        panel.add(usernameField);
        panel.add(loginButton);
        panel.add(createButton);
        panel.add(messageLabel);

        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("Travel The World");
        frame.pack();
        frame.setVisible(true);

        // Gestionnaire d'événements pour le bouton "Créer utilisateur"
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                
                // Vérifie si le nom d'utilisateur est vide et disponible
                if (username.trim().isEmpty()) {
                    messageLabel.setText("Le nom d'utilisateur ne peut pas être vide.");
                    return;
                }
                if (userRepository.getByUsername(username) != null) {
                    messageLabel.setText("L'utilisateur existe déjà.");
                    return;
                }
                
                User newUser = new User();
                newUser.setNom(username);
                int userId = userRepository.add(newUser);
                if (userId != -1) {
                    messageLabel.setText("Utilisateur créé : " + username);
                } else {
                    messageLabel.setText("Error.");
                }
                messageLabel.setText("Utilisateur créé : " + username);
            }
        });

        // Gestionnaire d'événements pour le bouton "Connexion"
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();

                // Exécution asynchrone pour la recherche de l'utilisateur dans la base de données
                SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
                    @Override
                    protected User doInBackground() throws Exception {
                        return userRepository.getByUsername(username);
                    }

                    @Override
                    protected void done() {
                        try {
                            User existingUser = get();
                            if (existingUser != null) {
                                messageLabel.setText("Connecté : " + username);
                                loggedUser = existingUser;
                                setupParcVisiteGUI();
                            } else {
                                messageLabel.setText("Utilisateur introuvable.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }lo
                };

                worker.execute(); // Exécution du SwingWorker
            }
        });
    }
    
    // Méthode pour configurer l'interface de gestion des visites de parcs
    private void setupParcVisiteGUI() {
        // Nettoie le contenu de la fenêtre
        frame.getContentPane().removeAll();

        // Crée les composants de l'interface de gestion des visites de parcs
        JLabel titleLabel = new JLabel("Gestion des Visites de Parcs");
        JButton addVisitButton = new JButton("Ajouter Visite");
        JButton editVisitButton = new JButton("Modifier Visite");
        JButton deleteVisitButton = new JButton("Supprimer Visite");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel();
        upperPanel.add(titleLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        DefaultListModel<ParcVisite> parcVisiteListModel = new DefaultListModel<>();
        JList<ParcVisite> parcVisiteList = new JList<>(parcVisiteListModel);
        centerPanel.add(new JScrollPane(parcVisiteList), BorderLayout.CENTER);

        Thread thread = new Thread(() -> {
            // Obtient les visites de parcs pour l'utilisateur connecté
            List<ParcVisite> parcVisites = parcVisiteRepository.getByUsername(loggedUser.getNom());
            SwingUtilities.invokeLater(() -> {
                for (ParcVisite parcVisite : parcVisites) {
                    parcVisiteListModel.addElement(parcVisite);
                }
                frame.revalidate();
                frame.repaint();
            });
        });

        thread.start();

        JPanel lowerPanel = new JPanel();
        lowerPanel.add(addVisitButton);
        lowerPanel.add(editVisitButton);
        lowerPanel.add(deleteVisitButton);

        panel.add(upperPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(lowerPanel, BorderLayout.SOUTH);

        frame.add(panel);

        frame.revalidate();
        frame.repaint();
    }
    
    // Méthode pour configurer la connexion Hibernate
    private void configureHibernate() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("/resources/hibernate.cfg.xml")
                .build();

        Metadata meta = new MetadataSources(ssr)
                .getMetadataBuilder()
                .build();

        sessionFactory = meta.getSessionFactoryBuilder().build();
        userRepository = new UserRepository(sessionFactory);
        villeRepository = new VilleRepository(sessionFactory);
        parcDisneyRepository = new ParcDisneyRepository(sessionFactory);
        trajetRepository = new TrajetRepository(sessionFactory);
        parcVisiteRepository = new ParcVisiteRepository(sessionFactory);
    }

    // Méthode pour fermer la connexion Hibernate
    private void closeHibernate() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
