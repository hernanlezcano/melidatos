package userSearchProssesor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import daoClasses.DAOData_MySQL;

public class InformationKitTwo {
	
	ArrayList <String[]> statesOffers = new ArrayList();
	ArrayList <String[]> historiesOffers = new ArrayList();
	ArrayList <String[]> dateStopQuantitys = new ArrayList();
	ArrayList <String[]> aceptsMercadoPago = new ArrayList();
	
	public InformationKitTwo(String productId, int paisId){
		obtainActualStatesOffer(productId, paisId);
		obtainHistoriesOffers(productId);
		obtainStopDateQuantitys(productId);
		obtainAcceptsMercado(productId);
	}
	
	public ArrayList getStatesOffer(){
		return this.statesOffers;
	}
	public ArrayList getHistoriesOffer(){
		return this.historiesOffers;
	}
	public ArrayList getDateStopQuantitys(){
		return this.dateStopQuantitys;
	}
	public ArrayList getAceptsMercadoPago(){
		return this.aceptsMercadoPago;
	}
	
	private void obtainActualStatesOffer(String productId, int paisId){
		List<Object[]> list = DAOData_MySQL.getInstance().getActualStatesOfferByProductId(productId, paisId);
		
		for(int i=0;i<list.size();i++){
			
			Object [] statesOffer = list.get(i);
			System.out.println(statesOffer[0] + " - " + String.valueOf(statesOffer[1]) + productId);
			statesOffers.add(new String[] { "'" + statesOffer[0] + "'", String.valueOf(statesOffer[1])});
			
		}
	}
	private void obtainHistoriesOffers(String productId){
		List<Object[]> list = DAOData_MySQL.getInstance().getOfferQuantitysHistoriesByProductId(productId);
		
		historiesOffers.add(new String[] {"'Day'","'Oferta'"});
		
		for(int i=0;i<list.size();i++){
			
			Object [] historiesOffer = list.get(i);
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
//			System.out.println(formatter.format(historiesOffer[0]) + " - " + String.valueOf(historiesOffer[1]));
			historiesOffers.add(new String[] { "'" + formatter.format(historiesOffer[0]) + "'", String.valueOf(historiesOffer[1])});
			
		}
	}
	
	private void obtainStopDateQuantitys(String productId){
		List<Object[]> list = DAOData_MySQL.getInstance().getDateStopQuantitysByProductId(productId);
		
		dateStopQuantitys.add(new String[] {"'Day'","'Cantidad de bajas'"});
		
		for(int i=0;i<list.size();i++){
			
			Object [] stopQuantitys = list.get(i);
//			System.out.println(stopQuantitys[0] + " - " + String.valueOf(stopQuantitys[1]));
//			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
//			System.out.println(formatter.format(stopQuantitys[0]) + " - " + String.valueOf(stopQuantitys[1]));
			dateStopQuantitys.add(new String[] { "'" + stopQuantitys[0] + "'", String.valueOf(stopQuantitys[1])});
			
		}
		
	}
	
	private void obtainAcceptsMercado(String productId){
		List<Object[]> list = DAOData_MySQL.getInstance().getAceptsMercadoPagoByProductId(productId);
		
		Object [] aceptsMercadoPago = list.get(0);
		//System.out.println(aceptsMercadoPago[0] + " - " + String.valueOf(aceptsMercadoPago[1]));
//		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println(formatter.format(stopQuantitys[0]) + " - " + String.valueOf(stopQuantitys[1]));
		this.aceptsMercadoPago.add(new String[] { "'Que aceptan mercado pago'", String.valueOf(aceptsMercadoPago[0])});
		this.aceptsMercadoPago.add(new String[] { "'Que NO acepta mercado pago'", String.valueOf(aceptsMercadoPago[1])});	
		
	}
	
}
