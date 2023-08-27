package repositories;

import org.hibernate.SessionFactory;

import classes.Aeroport;
import interfaces.IAeroportRepository;

public class AeroportRepository extends MutatorRepository<Aeroport> implements IAeroportRepository {
	public AeroportRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Aeroport.class);
	}

}
