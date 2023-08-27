package repositories;

import org.hibernate.SessionFactory;

import classes.Ville;
import interfaces.IVilleRepository;

public class VilleRepository extends MutatorRepository<Ville> implements IVilleRepository {
	public VilleRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Ville.class);
	}

}
