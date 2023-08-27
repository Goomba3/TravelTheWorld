package repositories;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import classes.BaseEntity;
import interfaces.IAccessorRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public abstract class AccessorRepository<T extends BaseEntity> extends BaseRepository<T>
		implements IAccessorRepository<T> {

	public AccessorRepository(SessionFactory sessionFactory, Class<T> type) {
		super(sessionFactory, type);
	}

	@Override
	public List<T> get(){
		try {
			Session session = this.getSessionFactory().openSession();
			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(this.getType());
			criteria.from(this.getType());
			List<T> data = session.createQuery(criteria).getResultList();

			session.getTransaction().commit();
			session.close();

			return data;
		} catch (Exception e) {
			System.err.println(e);
			return new ArrayList<>();
		}
	}

	@Override
	public T get(int id) {
		try {
			Session session = this.getSessionFactory().openSession();
			session.beginTransaction();

			T t = session.get(this.getType(), id);

			session.getTransaction().commit();
			session.close();

			return t;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@Override
	public void close() {
		try {
			this.getSessionFactory().close();
		} catch (HibernateException e) {
			System.err.println(e);
		}
	}
}
