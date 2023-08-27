package repositories;

import org.hibernate.SessionFactory;

import classes.Avion;
import interfaces.IAvionRepository;

public class AvionRepository extends MutatorRepository<Avion> implements IAvionRepository {
	public AvionRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Avion.class);
	}
}
