package repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import classes.ParcVisite;
import interfaces.IParcVisiteRepository;

public class ParcVisiteRepository extends MutatorRepository<ParcVisite> implements IParcVisiteRepository {
	public ParcVisiteRepository(SessionFactory sessionFactory) {
		super(sessionFactory, ParcVisite.class);
	}
	
	public List<ParcVisite> getByUsername(String username) {
	    try (Session session = this.getSessionFactory().openSession()) {
	        session.beginTransaction();

	        List<ParcVisite> visites = session.createQuery(
	            "SELECT pv " +
	            "FROM ParcVisite pv " +
	            "JOIN pv.user u " +
	            "JOIN pv.parcDisney pd " +
	            "WHERE u.nom = :username", ParcVisite.class)
	            .setParameter("username", username)
	            .list();

	        System.out.println(visites);
	        session.getTransaction().commit();
	        return visites;
	    } catch (Exception e) {
	        System.err.println(e);
	        return null;
	    }
	}



}
