package daoClasses;

import persistanceClasses.Users;

public abstract class DAO {
	
	public abstract Users insert(String idMail, String name, String lastName, String pass);
	
	public abstract Users delete(String idMail);
	
	public abstract Users update(String idOlderMail, String idMail, String name, String lastName, String pass) ;
	
	public abstract Users get(String idMail, String pass);
	
}