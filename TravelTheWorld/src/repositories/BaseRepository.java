package repositories;

import org.hibernate.SessionFactory;

import classes.BaseEntity;
import interfaces.IBaseRepository;

public abstract class BaseRepository<T extends BaseEntity> implements IBaseRepository<T> {

	private final Class<T> type;
	private SessionFactory sessionFactory = null;

	public BaseRepository(SessionFactory sessionFactory, Class<T> type) {
		this.sessionFactory = sessionFactory;
		this.type = type;
	}

	public Class<T> getType(){
		return type;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
