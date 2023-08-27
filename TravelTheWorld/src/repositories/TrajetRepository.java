package repositories;

import interfaces.ITrajetRepository;

import org.hibernate.SessionFactory;

import classes.Trajet;

public class TrajetRepository extends MutatorRepository<Trajet> implements ITrajetRepository {
	public TrajetRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Trajet.class);
	}


}
