package userSearchProssesor;

import java.util.ArrayList;
import java.util.List;
import java.text.Format;
import java.text.SimpleDateFormat;


import daoClasses.DAOData_MySQL;

public class InformationKitThree {
	ArrayList <String []> statesSold = new ArrayList();
	ArrayList <String[]> historiesSolds = new ArrayList();
	
	public InformationKitThree(String productId){
		obtainActualStatesSold(productId);
		obtainHistoriesOffers(productId);
	}
	
	public ArrayList getStatesSold(){
		return this.statesSold;
	}
	public ArrayList getHistoriesSold(){
		return this.historiesSolds;
	}
	
	private void obtainActualStatesSold(String productId){
		List<Object[]> list = DAOData_MySQL.getInstance().getActualStatesSoldByProductId(productId);
		
		for(int i=0;i<list.size();i++){
			
			Object [] stateSold = list.get(i);
//			System.out.println(stateSold[0] + " - " + String.valueOf(stateSold[1]));
			statesSold.add(new String[] { "'" + stateSold[0] + "'", String.valueOf(stateSold[1])});
			
		}
	}
	
	private void obtainHistoriesOffers(String productId){
		List<Object[]> list = DAOData_MySQL.getInstance().getSoldQuantitysHistoriesByProductId(productId);
		
		historiesSolds.add(new String[] {"'Day'","'Demanda'"});
		
		for(int i=0;i<list.size();i++){
			
			Object [] historiesSold = list.get(i);
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
//			System.out.println(formatter.format(historiesOffer[0]) + " - " + String.valueOf(historiesOffer[1]));
			historiesSolds.add(new String[] { "'" + formatter.format(historiesSold[0]) + "'", String.valueOf(historiesSold[1])});
			
		}
	}

	
}
