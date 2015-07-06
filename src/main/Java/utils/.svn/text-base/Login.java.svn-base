package utils;

import persistanceClasses.Users;
import daoClasses.DAOUsers_MySQL;


public class Login {

	private String mail;
	private String pass;
	
	
	public Users verifyUser(String mail, String pass){
		
		this.mail = mail;
		this.pass = pass;

		//Search the mail and pass exists in the database.
		Users user = DAOUsers_MySQL.getInstance().get(this.mail, this.pass);
		
		if( user != null){
			
			return user;
			
		}
		else{
			
			return null;
		}
		
	}
	
}
