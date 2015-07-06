package contextManagement;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SessionCounterListener implements HttpSessionListener{

	private static int totalActiveSessions;
	
	public static int getTotalActiveSessions(){
		return totalActiveSessions;
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		totalActiveSessions++;
		System.out.println("Session created - add one session to the counter");
		printCounter(arg0);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		totalActiveSessions--;
		System.out.println("Session destroyed - deduct one session to the counter");
		printCounter(arg0);
	}
	
	private void printCounter(HttpSessionEvent sessionEvent){
		
		HttpSession session = sessionEvent.getSession();
		
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		
		CounterService counterService = (CounterService) ctx.getBean("counterService");
		counterService.printCounter(totalActiveSessions);
		
	}
	

}
