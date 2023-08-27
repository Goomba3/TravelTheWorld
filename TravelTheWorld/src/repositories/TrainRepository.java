package repositories;

import org.hibernate.SessionFactory;

import classes.Train;
import interfaces.ITrainRepository;

public class TrainRepository extends MutatorRepository<Train> implements ITrainRepository {
	public TrainRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Train.class);
	}
}
