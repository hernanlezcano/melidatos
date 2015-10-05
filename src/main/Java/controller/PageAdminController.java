package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import consultingPackage.CategoriesAlgorithm;
import consultingPackage.CategoriesAlgorithm.Category;
import consultingPackage.Configurations;
import consultingPackage.ConsultAPI;
import consultingPackage.ScheduleAlgorithm;
import contextManagement.UserSession;
import daoClasses.DAOUsers_MySQL;
import utils.Login;

@Controller
public class PageAdminController extends HttpServlet{
	
	
	@RequestMapping (value="pageAdminLogin", method = RequestMethod.GET)
	public String pageAdminLogin(HttpServletRequest request, HttpServletResponse response) {
		
		
		return "pageAdminLogin";
		
	}
	
	@RequestMapping (value="adminLogin", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Login login = new Login();
		//Receive the mail and the password
		if(login.verifyUser(request.getParameter("mail"), request.getParameter("pass")) != null &&
				login.verifyUser(request.getParameter("mail"), request.getParameter("pass")).isIsAdmin()){
				UserSession.getInstance().addUserSession(request.getParameter("mail"), request.getSession().getId());
				response.sendRedirect("pageAdminHistory");
		}
		else{
			//Pass and user not exists
			//return "pageAdminLogin";
			response.sendRedirect("pageAdminLogin");
		}

	}
	
	@RequestMapping (value="pageAdminHistory", method = RequestMethod.GET)
	public String pageAdminHistory(HttpServletRequest request, HttpServletResponse response){
		
		if(!checkUser(request.getSession().getId())){
			return "pageAdminLogin";
		}
		String [] countries = {"MLA","MLB","MLC"};
		JSONArray categoriesJsonArray = ConsultAPI.getInstance().getRootCategories(countries);
		System.out.println(categoriesJsonArray);
		
		JSONArray arreglo = categoriesJsonArray;
		
		request.setAttribute("categories",arreglo.toJSONString());
		
		return "pageAdminHistory";
		
	}
	
	@RequestMapping (value="startRecordsAlgorithm", method = RequestMethod.POST)
	public void startRecordsAlgorithm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if(!checkUser(request.getSession().getId())){
			response.sendRedirect("pageAdminLogin");
		}
		
		JSONParser parser = new JSONParser();
		JSONArray categories = null;		
		try {
			categories = null;
			//System.out.println("categories imprime esto: " + request.getParameter("categories"));
			Object obj = parser.parse(request.getParameter("categories"));
			categories = (JSONArray) obj;
			
		} catch (ParseException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Configurations.getInstance().setFrequency(request.getParameter("frequency"));
		Configurations.getInstance().setItemsQuantity(Integer.valueOf(request.getParameter("itemQuantity")));
		Configurations.getInstance().setHourSchedule(request.getParameter("scheduleHour"));
		Configurations.getInstance().setCategoriesAllowed(categories);
		
		ScheduleAlgorithm schedule = new ScheduleAlgorithm();
		schedule.startAlgorithm();
		
	}
	@RequestMapping (value="stopRecordsAlgorithm", method = RequestMethod.POST)
	public void stopRecordsAlgorithm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if(!checkUser(request.getSession().getId())){
			response.sendRedirect("pageAdminLogin");
		}
		ScheduleAlgorithm schedule = new ScheduleAlgorithm();
		schedule.stopAlgorithm();
		
	}
	
	@RequestMapping (value="saveTheUser", method = RequestMethod.POST)
	public void saveTheUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//Try to save the user
				try {
					response.setStatus(HttpServletResponse.SC_ACCEPTED);
					String idMail = request.getParameter("idMail");
					String name = request.getParameter("name");
					String lastName = request.getParameter("lastName");
					String password =request.getParameter("password");
				    
				    DAOUsers_MySQL.getInstance().insert(idMail, name, lastName, password);
					
					
				} catch (ConstraintViolationException e) {
					// TODO Auto-generated catch block
					try {
						response.sendRedirect("index");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
					
				} 
	}
	
	private boolean checkUser(String sessionId){
		if(UserSession.getInstance().checkSession(sessionId)){
			return true;
		}
		return false;
	}
	
}