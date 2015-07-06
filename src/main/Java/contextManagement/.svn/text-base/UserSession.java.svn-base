package contextManagement;

import java.util.ArrayList;


//This object will store all the relationships between the users logged and their sessionId in a stack called "userSession"
public class UserSession {

	private ArrayList<String[]> userSession = new ArrayList<String[]>();
	
	private static UserSession instance;

	private UserSession() {
	}

	public static UserSession getInstance() {

		if (instance == null) {
			instance = new UserSession();
		}

		return instance;
	}
	
	public void addUserSession(String userMail, String sessionId){
		userSession.add(new String[]{userMail,sessionId});
		System.out.println("Adding session: " + userSession.get(userSession.size()-1)[0] + userSession.get(userSession.size()-1)[1]);
	}
	
	public void deleteUserSession(String sessionId){
		
		for(int i=0;i<userSession.size();i++){
			
			//Delete the session.
			if(userSession.get(i)[1] == sessionId){
				userSession.remove(i);
			}
		}
	}
	
	public boolean checkSession(String sessionId){
		for(int i = 0;i<userSession.size();i++){
			if(userSession.get(i)[1] == sessionId){
				System.out.println("Active session");
				return true;
			}
		}
		System.out.println("Deprecated session,sessionId: " + sessionId);
		return false;
	}
	
}
