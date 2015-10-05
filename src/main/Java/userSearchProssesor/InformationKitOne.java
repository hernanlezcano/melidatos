package userSearchProssesor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import persistanceClasses.Producttree;
import persistanceClasses.Records;
import sun.text.normalizer.UBiDiProps;

import daoClasses.DAOData_MySQL;


public class InformationKitOne {
	
	double avgPrice = 0, minPrice = 0, maxPrice = 0; 
	double sumAvgPrice = 0;
	long sampleSize;
	ArrayList <String[]> histories = new ArrayList();
	ArrayList <String[]> statesAvgPrices = new ArrayList();
	ArrayList <String[]> publications = new ArrayList();
	
	public InformationKitOne(String productId){
		getBasicInformation(productId);
		obtainHistoriesPrices(productId);
		obtainStatesAvgPrices(productId);
		obtainPublications(productId);
		searchProductIdByDescription(productId);
	}
	
	public double getAvgPrice(){
		return avgPrice;
	}
	
	public double getMinPrice(){
		return minPrice;
	}
	
	public double getMaxPrice(){
		return maxPrice;
	}
	public ArrayList getHistories(){
		return histories;
	}
	public ArrayList getStatesAvgPrices(){
		return statesAvgPrices;
	}
	public ArrayList getPublications(){
		return publications;
	}
	public long getSampleSize(){
		return sampleSize;
	}
	//This function will obtain the newest basic information like average, minimum and maximum prices values
	private void getBasicInformation(String productId){
		
		List<Object[]> list = DAOData_MySQL.getInstance().getLastSavedItemPriceByProductId(productId);
		if(list.size() != 0){
			Object [] data = list.get(0);
			avgPrice = Double.valueOf(String.valueOf(data[0]));
			maxPrice = Double.valueOf(String.valueOf(data[1]));
			minPrice = Double.valueOf(String.valueOf(data[2]));
		}
	}
	
	private void obtainHistoriesPrices(String productId){
		
		List<Object[]> list = DAOData_MySQL.getInstance().getItemPricesHistoriesByProductId(productId);
		
		histories.add(new String[] {"'Day'","'Precio minimo'","'Promedio'","'Precio maximo'"});
		
		for(int i=0;i<list.size();i++){
			
			Object [] history = list.get(i);
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			histories.add(new String[] { "'" + formatter.format(history[0]) + "'",String.valueOf(history[3]),String.valueOf(history[1]),String.valueOf(history[2])});
		}
	}
	private void obtainStatesAvgPrices(String productId){
		List<Object[]> list = DAOData_MySQL.getInstance().getStatesAvgPriceByProductId(productId);
		statesAvgPrices.add(new String[] {"'Provincia'","'Precio'"});
		
		for(int i=0;i<list.size();i++){
			
			Object [] AvgPrices = list.get(i);
			statesAvgPrices.add(new String[] { "'" +  String.valueOf(AvgPrices[0]) + "'",  String.valueOf(AvgPrices[1])});
			
		}
	}
	private void obtainPublications(String productId){
		List<Object[]> list = DAOData_MySQL.getInstance().getPublications(productId);
		
		for(int i=0;i<list.size();i++){
			
			Object [] publication = list.get(i);
			publications.add(new String[] {String.valueOf(publication[0]),  String.valueOf(publication[1]), String.valueOf(publication[2])});
			
		}
	}
	private void searchProductIdByDescription(String productId){ 
		
		sampleSize = (long) ((Double) DAOData_MySQL.getInstance().getSampleSize(productId).get(0)).intValue();

	}
	
}
