package daoClasses;

import org.hibernate.Query;
import org.hibernate.Session;

import persistanceClasses.Users;
import persistanceFactory.HibernateUtils;

public class DAOUsers_MySQL extends DAO{

	private static DAOUsers_MySQL instance;

	private DAOUsers_MySQL() {
	}

	public static DAOUsers_MySQL getInstance() {

		if (instance == null) {
			instance = new DAOUsers_MySQL();
		}

		return instance;

	}

	@Override
	public Users insert(String idMail, String name, String lastName, String pass) {

		// Obtain a session of MySQL factory
		Session session = HibernateUtils.getSessionFactory().openSession();

		// Start a transaction
		session.beginTransaction();
		Users user = new Users(idMail, name, lastName, pass, false);
		session.save(user);
		session.getTransaction().commit();
		session.close();
		
		return user;
	}

	@Override
	public Users delete(String idMail) {

		// Obtain a session of MySQL factory
		Session session = HibernateUtils.getSessionFactory().openSession();

		// Start a transaction
		session.beginTransaction();

		// Obtain an user with the mail that is passed as argument.
		Users user = (Users) session.createQuery("SELECT u FROM Users u WHERE idMail= '" + idMail + "'").uniqueResult();

		session.delete(user);
		session.getTransaction().commit();
		session.close();
		
		return user;
	}

	@Override
	public Users update(String idOlderMail, String idMail, String name, String lastName, String pass) {

		// Obtain a session of MySQL factory
		Session session = HibernateUtils.getSessionFactory().openSession();

		Query query = session.createQuery("update Users set idMail = :idMail, name= :name, lastName= :lastName, password= :pass " +
				" where idMail = '"+ idOlderMail +"'");
		query.setParameter("idMail", idMail);
		query.setParameter("name", name);
		query.setParameter("lastName", lastName);
		query.setParameter("pass", pass);

		session.beginTransaction();
		query.executeUpdate();
		session.getTransaction().commit();
		session.close();
		
		return null;

	}

	@Override
	public Users get(String idMail, String pass) {
		
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		session.beginTransaction();
		Users user = (Users) session.createQuery("SELECT u FROM Users u WHERE idMail= '" + idMail + "' AND password= '"+ pass +"'").uniqueResult();
		session.getTransaction().commit();
		session.close();
		
		return user;
	}

}
