package repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import classes.User;
import interfaces.IUserRepository;

public class UserRepository extends MutatorRepository<User> implements IUserRepository {
	public UserRepository(SessionFactory sessionFactory) {
		super(sessionFactory, User.class);
	}

    public User getByUsername(String username) {
        try {
            Session session = this.getSessionFactory().openSession();
            session.beginTransaction();

            User user = (User) session.createQuery("FROM User WHERE nom = :username", User.class)
                                     .setParameter("username", username)
                                     .uniqueResult();

            session.getTransaction().commit();
            session.close();
            return user;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
}
