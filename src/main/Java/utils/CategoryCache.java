package utils;

import java.util.LinkedList;
import java.util.List;

import daoClasses.DAOData_MySQL;

public class CategoryCache {
	
	private List<Object[]> relationship = new LinkedList <Object[]>();
	
	private static CategoryCache instance;
	private int suggestLimit = 0;
	
		private CategoryCache() {
		
	}

	public static CategoryCache getInstance() {

		if (instance == null) {
			instance = new CategoryCache();
		}

		return instance;

	}
	
	public void sotreCategory(){
		//el name2 lo agruegue yo pero no se que hace capaz si se rompio algo es eso
		relationship = DAOData_MySQL.getInstance().getWordsSuggested("name","name2");
		
//		if(relationship.contains(new String[]{(String) nameSon, (String) nameFather, (String) idSon })){
//			//Do nothing because the element is already in the stack
//		}else{
//			relationship.push(new String[]{(String) nameSon, (String) nameFather, (String) idSon });
//		}
		
	}
	
	public LinkedList <String[]> getCategories(String nameSon){
		LinkedList <String[]> categoriesSuggest = new LinkedList <String[]>();
		
		//The number of suggestions are gona be 10 for the moment
//		for(int i=0;i<relationship.size();i++){
//			if(String.valueOf(relationship.get(i)[0]).indexOf(nameSon) != -1){
//				categoriesSuggest.push(relationship.get(i));
//				suggestLimit++;
//				if(suggestLimit>=10){
//					suggestLimit=0;
//					System.out.println("Categories suggested: " + categoriesSuggest.toString());
//					break;
//				}
//			}
//		}
		return categoriesSuggest;
	}
	
}
