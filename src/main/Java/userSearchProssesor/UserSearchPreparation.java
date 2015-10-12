package userSearchProssesor;

import java.util.ArrayList;
import java.util.List;

import utils.CategoryCache;

import daoClasses.DAOData_MySQL;

public class UserSearchPreparation {
	private String productId;
	
	private static UserSearchPreparation instance;

	private UserSearchPreparation() {
		
	}

	public static UserSearchPreparation getInstance() {

		if (instance == null) {
			instance = new UserSearchPreparation();
		}

		return instance;

	}
	
	public List <String[]> getSuggestions(String word, String pais){
		List<Object[]> list = DAOData_MySQL.getInstance().getWordsSuggested(word, pais);
		ArrayList <String[]> suggestions = new ArrayList<String[]>();
		
		for(int i=0; i<list.size();i++){
			suggestions.add(new String[]{(String) list.get(i)[0], (String) list.get(i)[1], (String) list.get(i)[2]});
		}
		
		return suggestions;
	}
	
	public List <String[]> getSuggestionsById(String test){
		List<Object[]> list = DAOData_MySQL.getInstance().getElementById(test);
		ArrayList <String[]> suggestions = new ArrayList<String[]>();
		
		for(int i=0; i<list.size();i++){
			suggestions.add(new String[]{(String) list.get(i)[0], (String) list.get(i)[1], (String) list.get(i)[2]});
		}
		
		return suggestions;
	}
	
	public String searchProductIdByDescription(String prodDesc){ 
		
		try{
			this.productId = (String) DAOData_MySQL.getInstance().getLastSavedProductsById(prodDesc).get(0);
		}catch(Exception e){
			System.out.println("ERROR "+e.getMessage());
			this.productId = null;
		}
		
		return productId;
	}
	
	
	
}
