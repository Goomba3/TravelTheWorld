package repositories;

import org.hibernate.SessionFactory;

import classes.Gare;
import interfaces.IGareRepository;

public class GareRepository extends MutatorRepository<Gare> implements IGareRepository {
	public GareRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Gare.class);
	}

}
