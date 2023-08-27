package repositories;

import org.hibernate.SessionFactory;

import classes.MoyenTransport;
import interfaces.IMoyenTransportRepository;

public class MoyenTransportRepository extends AccessorRepository<MoyenTransport> implements IMoyenTransportRepository {
    public MoyenTransportRepository(SessionFactory sessionFactory) {
        super(sessionFactory, MoyenTransport.class);
    }
}
