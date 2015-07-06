package controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import daoClasses.DAOUsers_MySQL;


@Controller
public class UserRegisterController extends HttpServlet{

	@RequestMapping (value="userRegister", method = RequestMethod.GET)
	public String register(HttpServletRequest request, HttpServletResponse response) {
		
		return "userRegister";
	}
	
	@RequestMapping (value="onUserRegistration", method = RequestMethod.POST)
	public void onUserRegistration(HttpServletRequest request, HttpServletResponse response) {
		
		//Try to save the user
		try {
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			String idMail = request.getParameter("email");
			String name = request.getParameter("first");
			String lastName = request.getParameter("second");
			String password =request.getParameter("retrypassword");
		    
		    DAOUsers_MySQL.getInstance().insert(idMail, name, lastName, password);
			
			response.sendRedirect("index");
			
		} catch (ConstraintViolationException e) {
			// TODO Auto-generated catch block
			try {
				response.sendRedirect("index");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			
	}
	
}
