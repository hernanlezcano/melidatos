package consultingPackage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import wrapperPackage.WrapperItems;


import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;

public class ConsultAPI {
	private Meli m = new Meli(1446274906831857L, "rnT2BpdRLJALy2E2cahxbYe2wW4ZylBu");
	WrapperItems wrapperItems;
	CategoriesAlgorithm categories;
	LinkedList<String> childrens = new LinkedList<String>();
	int attempts;
	
	private static ConsultAPI instance;

	private ConsultAPI(){
		attempts = 0;
	}

	public static ConsultAPI getInstance() {

		if (instance == null) {
			instance = new ConsultAPI();
		}

		return instance;
	}
	
	public JSONObject getCategoryJSON(String categoryId){
		
		JSONObject category = null;
		
		try {
			Response response = m.get("/categories/"+ categoryId);
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(response.getResponseBody());
			//JSONObject categoryJsonObject = (JSONObject) obj;
			category = (JSONObject) obj;
		} catch (MeliException e) {
			// TODO Auto-generated catch block
			if(attempts <= 10000){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					getItemsPerCategory(categoryId);
					e1.printStackTrace();
				}
				System.out.println("new attempts: " + categoryId);
				attempts++;
				getItemsPerCategory(categoryId);
			}
			//e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("ParseException in category: " + categoryId);
			//e.printStackTrace();
			category = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException in category: " + categoryId);
			//e.printStackTrace();
			category = null;
		}
		//System.out.println("Corriendo getCategoryJSON");
		return category;
	}
	
	public ArrayList getRootCategories(){
		Response response;
		ArrayList categoriesJsonArray = null;
		try {
			response = m.get("/sites/MLA/categories");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(response.getResponseBody());
			categoriesJsonArray = (ArrayList) obj;
		} catch (MeliException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("ParseException in root categories");
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException in root categories ");
			//e.printStackTrace();
		}
		
		//System.out.println("Corriendo getRootCategoruies");
		return categoriesJsonArray;
	}
	
	public WrapperItems getItemsPerCategory(String categoryId){
		
		Response response;
		int limit = Configurations.getInstance().getItemsQuantity();
		try {
			FluentStringsMap params = new FluentStringsMap();
			params.add("category",categoryId);
			params.add("limit", String.valueOf(limit));
			params.add("offset", "0");
			params.add("sort", "price_desc");
			response = m.get("/sites/MLA/search", params);//parametro de pais
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(response.getResponseBody());
			JSONObject itemsJsonObject = (JSONObject) obj;
			System.out.println(obj.toString());
			wrapperItems = new WrapperItems(itemsJsonObject);
			
			
		} catch (MeliException e) {
			// TODO Auto-generated catch block
			if(attempts <= 10000){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					
					getItemsPerCategory(categoryId);
					e1.printStackTrace();
				}
				System.out.println("new attempts: " + categoryId);
				attempts++;
				getItemsPerCategory(categoryId);
			} else{
				return null;
			}
			//e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("ParseException in category: " + categoryId);
			//e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException in category: " + categoryId);
			//e.printStackTrace();
			return null;
		} 
		System.out.println("Corriendo WrappeItems");
		return wrapperItems;
		
	}
	
	public WrapperItems getWrapperItem(){
		return this.wrapperItems;
	}
}