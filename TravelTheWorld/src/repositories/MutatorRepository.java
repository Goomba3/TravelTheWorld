package repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import classes.BaseEntity;
import interfaces.IMutatorRepository;

public abstract class MutatorRepository<T extends BaseEntity> extends AccessorRepository<T> implements IMutatorRepository<T> {

    public MutatorRepository(SessionFactory sessionFactory, Class<T> type){
        super(sessionFactory, type);
    }

    @Override
    public int add(T entity) {
        try {
            Session session = this.getSessionFactory().openSession();
            session.beginTransaction();

            session.persist(entity);

            session.getTransaction().commit();
            session.close();

            return entity.getId();
        } catch (Exception e) {
            System.err.println(e);
            return -1;
        }
    }

    @Override
    public boolean remove(int id) {
        try {
            T existing = this.get(id);

            Session session = this.getSessionFactory().openSession();
            session.beginTransaction();

            if (existing != null)
                session.remove(existing);

            session.getTransaction().commit();
            session.close();

            return existing != null;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean update(T entity) {
        try {
            Session session = this.getSessionFactory().openSession();
            session.beginTransaction();

            session.merge(entity);

            session.getTransaction().commit();
            session.close();

            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public int upsert(T entity){
        try {
            T savedInstance = this.get(entity.getId());

            Session session = this.getSessionFactory().openSession();
            session.beginTransaction();

            if(savedInstance != null)
                session.merge(entity);
            else{
                entity.setId(0);
                session.persist(entity);
            }

            session.getTransaction().commit();
            session.close();

            return entity.getId();
        } catch (Exception e) {
            System.err.println(e);
            return -1;
        }
    }
}