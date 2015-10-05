package daoClasses;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import persistanceClasses.Datestopquantitys;
import persistanceClasses.Itemprices;
import persistanceClasses.Itemsquantitys;
import persistanceClasses.Mercadopago;
import persistanceClasses.Producttree;
import persistanceClasses.Publications;
import persistanceClasses.Records;
import persistanceClasses.State;
import persistanceClasses.Statesdata;
import persistanceFactory.HibernateUtils;

public class DAORecords_MySQL{
	
	private static DAORecords_MySQL instance;

	private DAORecords_MySQL() {
	}

	public static DAORecords_MySQL getInstance() {

		if (instance == null) {
			instance = new DAORecords_MySQL();
		}

		return instance;

	}
	
	//With this function we insert the records of the prices.
	public void insertRecord(String productId, String descProduct ,double soldQuantity,double offerQuantity, 
			double avgPrice, double maxPrice, double minPrice, int acceptsMercadoPago, int noMercadoPago, 
			String[][] statesQuantitys, LinkedList<String[]> stopQuantitys, int sampleSize, 
			LinkedList<String[]> publicationsData){ //esta funcion guarda todo los productos q trae una consulta a la API
		// Obtain a session of MySQL factory
		Session session = HibernateUtils.getSessionFactory().openSession();
				
		// Start a transaction
		session.beginTransaction();
		
		Date date = new Date();
		
		Records records = new Records(productId, descProduct , date, sampleSize);
		session.save(records);
		Itemprices itemPrices = new Itemprices(records, avgPrice, maxPrice, minPrice);
		session.save(itemPrices);
		Itemsquantitys itemsQuantity = new Itemsquantitys(records, (long) offerQuantity, (long) soldQuantity);
		session.save(itemsQuantity);
		Mercadopago mercadoPago = new Mercadopago(records, acceptsMercadoPago, noMercadoPago);
		session.save(mercadoPago);
		for(int i = 0;i<statesQuantitys.length;i++){ //recorre toda la matriz preguntando cada uno de los estados primero bdd para relacionar y guarda
			Query query = session.createQuery("from State where idState = :id ");
			query.setParameter("id", statesQuantitys[i][0]);
			List list = query.list();
			System.out.println(statesQuantitys[i][0]);
			State state = (State) list.get(0);
			Statesdata statedata;
			if(Integer.valueOf(statesQuantitys[i][4]) == 0){
				statedata = new Statesdata(state,records, Double.valueOf(statesQuantitys[i][1]), Double.valueOf(statesQuantitys[i][2]),0);
			}else{
				statedata = new Statesdata(state,records, Double.valueOf(statesQuantitys[i][1]), Double.valueOf(statesQuantitys[i][2]),
						Double.valueOf(statesQuantitys[i][3])/Integer.valueOf(statesQuantitys[i][4]));
			}
			session.save(statedata);
		}
		for(int i = 0;i<stopQuantitys.size();i++){
			Datestopquantitys dateStopQuantitys = new Datestopquantitys(records,stopQuantitys.get(i)[0],Long.valueOf(stopQuantitys.get(i)[1]));
			session.save(dateStopQuantitys);
		}
		for(int i = 0;i<publicationsData.size();i++){
			if(publicationsData.get(i)[0].length() > 200){
				publicationsData.get(i)[0] = publicationsData.get(i)[0].substring(0, 199);
			}
			if(publicationsData.get(i)[1].length() > 200){
				publicationsData.get(i)[1] = publicationsData.get(i)[1].substring(0, 199);
			}
			Publications publications = new Publications(records, publicationsData.get(i)[0], publicationsData.get(i)[1], Double.valueOf(publicationsData.get(i)[2]));
			System.out.println(publicationsData.get(i)[0] +" , "+ publicationsData.get(i)[1]+" , "+Double.valueOf(publicationsData.get(i)[2]));
			session.save(publications);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	public void insertRelationship(String idSon, String idFather, String descFather, String descSon, boolean isLeaf){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		// Start a transaction
		session.beginTransaction();
		
		Date date = new Date();
		
		Producttree producttree = new Producttree(idSon, idFather, descFather, descSon, isLeaf, date);
		session.save(producttree);
		session.getTransaction().commit();
		session.close();
	}
	
}
