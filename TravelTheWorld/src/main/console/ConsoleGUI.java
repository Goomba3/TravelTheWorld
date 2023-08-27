package main.console;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import classes.Ville;
import interfaces.IVilleRepository;
import repositories.VilleRepository;

public class ConsoleGUI {

    public static void main(String[] args) {

        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("/ressources/hibernate.cfg.xml")
                .build();

        Metadata meta = new MetadataSources(ssr)
                .getMetadataBuilder()
                .build();

        try (SessionFactory sessionFactory = meta.getSessionFactoryBuilder().build();
				IVilleRepository villeRepository = new VilleRepository(sessionFactory)) {
            try (Scanner scanner = new Scanner(System.in)) {
				int choice = -1;

				while (choice != 0) {
				    System.out.println("========= Gestion des Villes =========");
				    System.out.println("1. Lire les villes");
				    System.out.println("2. Ajouter une ville");
				    System.out.println("3. Supprimer une ville");
				    System.out.println("4. Modifier une ville");
				    System.out.println("0. Quitter");
				    System.out.print("Choix : ");
				    choice = scanner.nextInt();
				    scanner.nextLine();

				    System.out.println();

				    switch (choice) {
				        case 1:
				            List<Ville> villes = villeRepository.get();
				            System.out.println("===== Liste des Villes =====");
				            for (Ville ville : villes) {
				                System.out.println("ID : " + ville.getId() + ", Nom : " + ville.getNom());
				            }
				            break;
				        case 2:
				            System.out.print("Nom de la ville : ");
				            String villeNom = scanner.nextLine();
				            Ville nouvelleVille = new Ville();
				            nouvelleVille.setNom(villeNom);
				            int newId = villeRepository.upsert(nouvelleVille);
				            System.out.println("Ville ajoutée avec l'ID : " + newId);
				            break;
				        case 3:
				            System.out.print("ID de la ville à supprimer : ");
				            int villeIdToDelete = scanner.nextInt();
				            boolean deleteSuccess = villeRepository.remove(villeIdToDelete);
				            if (deleteSuccess) {
				                System.out.println("Ville supprimée avec succès.");
				            } else {
				                System.out.println("La ville n'existe pas ou une erreur s'est produite.");
				            }
				            break;
				        case 4:
				            int choiceInt = -1;
				            while (choiceInt < 0) {
				            	try {
				            		System.out.print("ID de la ville à modifier : ");
				                	choiceInt = scanner.nextInt();
				                	scanner.nextLine();
				            	} catch (InputMismatchException e) {
				                	System.out.print("Veuillez entrer un ID valide.");
				                    System.out.println();
				                	scanner.nextLine();
				                	choiceInt = -1;
				            	}
				            }
				            int villeIdToUpdate = choiceInt;
				            Ville villeToUpdate = villeRepository.get(villeIdToUpdate);
				            if (villeToUpdate != null) {
				                System.out.print("Nouveau nom de la ville : ");
				                String newVilleNom = scanner.nextLine();
				                villeToUpdate.setNom(newVilleNom);
				                boolean updateSuccess = villeRepository.update(villeToUpdate);
				                if (updateSuccess) {
				                    System.out.println("Ville mise à jour avec succès.");
				                } else {
				                    System.out.println("Une erreur s'est produite lors de la mise à jour.");
				                }
				            } else {
				                System.out.println("La ville n'existe pas.");
				            }
				            break;
				        case 0:
				            System.out.println("Au revoir !");
				            break;
				        default:
				            System.out.println("Choix invalide. Veuillez réessayer.");
				            break;
				    }

				    System.out.println();
				}
			}
        } catch (Exception e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
    }
}
