package repositories;

import org.hibernate.SessionFactory;

import classes.ParcDisney;
import interfaces.IParcDisneyRepository;

public class ParcDisneyRepository extends MutatorRepository<ParcDisney> implements IParcDisneyRepository {
	public ParcDisneyRepository(SessionFactory sessionFactory) {
		super(sessionFactory, ParcDisney.class);
	}

}
