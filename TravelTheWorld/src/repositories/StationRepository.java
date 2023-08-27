package repositories;

import org.hibernate.SessionFactory;

import classes.Station;
import interfaces.IStationRepository;

public class StationRepository extends AccessorRepository<Station> implements IStationRepository {
    public StationRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Station.class);
    }
}
