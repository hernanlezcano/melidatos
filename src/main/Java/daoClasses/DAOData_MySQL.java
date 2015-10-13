package daoClasses;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import persistanceClasses.Itemprices;
import persistanceClasses.Records;
import persistanceFactory.HibernateUtils;


public class DAOData_MySQL{
	
	private static DAOData_MySQL instance;

	private DAOData_MySQL() {
	}

	public static DAOData_MySQL getInstance() {

		if (instance == null) {
			instance = new DAOData_MySQL();
		}

		return instance;

	}
	
	//For the moment this method returns a list of all the matches of the word seted by the user, later its gonna be refined.
	public List getLastSavedProductsById(String descProd){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		System.out.println("DAO: "+descProd);
		// Start a transaction
		session.beginTransaction();
		//Este tiene q ser un solo id sino busca mal
		Query query = session.createQuery("Select idSon from Producttree where descSon = :name AND date in (select max(date) from Producttree WHERE descSon = :name)");
		query.setParameter("name", descProd);
		List list = query.list();
		session.close();
		return list;
	}
	
	
	public List<Object[]> getElementById(String test){
		//cuando haces el autocomplete le hace la consulta a producttree y usa este
		Session session = HibernateUtils.getSessionFactory().openSession();
		// Start a transaction	
		session.beginTransaction();
		//busqueda original que trae todo los item en la busqueda
		//Query query = session.createQuery("Select descFather, descSon, idFather from Producttree GROUP BY descSon");
		
		Query query = session.createQuery("Select descFather, descSon, idSon from Producttree where idFather = :search" );
		query.setParameter("search", test);
		
		List <Object[]> list = query.list();
		session.close();
		//System.out.println("Aca imprimos list" + list);
		return list;
	}
	
	public List<Object[]> getWordsSuggested(String word, String pais){
		//cuando haces el autocomplete le hace la consulta a producttree y usa este
		Session session = HibernateUtils.getSessionFactory().openSession();
		// Start a transaction	
		session.beginTransaction();
		//busqueda original que trae todo los item en la busqueda
		//Query query = session.createQuery("Select descFather, descSon, idFather from Producttree GROUP BY descSon");
		
		Query query = session.createQuery("Select descFather, descSon, idFather from Producttree where descFather like :search and idFather like :search2 GROUP BY descFather" );
		query.setParameter("search", word + "%");
		query.setParameter("search2", pais + "%");
		
		List <Object[]> list = query.list();
		session.close();
		//System.out.println("Aca imprimos list" + list);
		return list;
	}
	
	//This function will search in database by product description in Records table and Itemprices, and return the data of avg, max and min of each date
	public List<Object[]> getItemPricesHistoriesByProductId(String productId){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT R.dateRecord, avg(IT.avgPrices), max(IT.maxPrices), min(IT.minPrices) " +
										  "FROM Itemprices as IT INNER JOIN IT.records as R " +
										  "where R.productId = :id group by dateRecord");
		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
	}

	public List <Object[]> getLastSavedItemPriceByProductId(String productId){
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		//Start a transaction
		session.beginTransaction();
		System.out.println(productId + "-> id");
		Query query = session.createQuery("SELECT avg(IT.avgPrices), max(IT.maxPrices), min(IT.minPrices) " +
										  "FROM Itemprices as IT JOIN IT.records as R " +
										  "WHERE R.productId = :id AND R.dateRecord IN (SELECT max(R.dateRecord) FROM Records as R WHERE R.productId = :id)");
		
//		Query query = session.createQuery("SELECT avg(P.price), max(P.price), min(P.price) " +
//										  "FROM Publications as P INNER JOIN P.records as R " +
//										  "WHERE R.productId = :id AND R.dateRecord IN (SELECT max(R.dateRecord) FROM Records as R WHERE R.productId = :id)");
//		SELECT  avg(P.price),max(P.price), min(P.price)
//		FROM publications as P, records as R 
//		WHERE P.idRecord = R.idRecord AND
//		      R.productId= 'MLA116622' AND 
//		      R.dateRecord IN (SELECT max(R.dateRecord) FROM records as R WHERE R.productId = 'MLA116622')
//				
		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
	}
	
	public List <Object[]> getStatesAvgPriceByProductId(String productId){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT SD.state.descState, SD.avgPrice " +
				  							"FROM Statesdata as SD INNER JOIN SD.records as R " +
				  							"WHERE R.productId = :id AND R.dateRecord IN " +
				  							"(SELECT max(R.dateRecord) FROM Records as R WHERE R.productId = :id) " +
				  							"GROUP BY SD.state.descState " +
				  							"HAVING SD.avgPrice > 0");

		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
	}
	
	public List <Object[]> getActualStatesOfferByProductId(String productId, int paisId){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT SD.state.descState, sum(SD.offerQuantity) " +
										  "FROM Statesdata as SD INNER JOIN SD.records as R " +
										  "WHERE R.productId = :id AND idCountry = :pais AND R.dateRecord IN " +
										  "(SELECT max(R.dateRecord) FROM Records as R WHERE R.productId = :id) "+
										   "GROUP BY SD.state.descState");
		query.setParameter("id", productId);
		query.setParameter("pais", paisId ); 
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
	}
	
	public List <Object[]> getActualStatesSoldByProductId(String productId){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT SD.state.descState, sum(SD.soldQuantity) " +
				  							"FROM Statesdata as SD INNER JOIN SD.records as R " +
				  							"WHERE R.productId = :id AND R.dateRecord IN " +
				  							"(SELECT max(R.dateRecord) FROM Records as R WHERE R.productId = :id) " +
				  							"GROUP BY SD.state.descState");
		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
	}
	
	public List <Object[]> getOfferQuantitysHistoriesByProductId(String productId){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT R.dateRecord, sum(IQ.offerQuantitys) " +
				  							"FROM Itemsquantitys as IQ INNER JOIN IQ.records as R " +
				  							"WHERE R.productId= :id " +
				  							"GROUP BY R.dateRecord");
		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
		
	}
	
	public List <Object[]> getDateStopQuantitysByProductId(String productId){
	
		Session session = HibernateUtils.getSessionFactory().openSession();
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT SQ.date, SQ.stopQuantity " +
				  							"FROM Datestopquantitys as SQ INNER JOIN SQ.records as R " +
				  							"WHERE R.productId= :id AND R.dateRecord IN " +
				  							"(SELECT max(R.dateRecord) FROM Records as R WHERE R.productId = :id) " +
				  							"ORDER BY SQ.date");
		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
		
	}
	
	public List <Object[]> getAceptsMercadoPagoByProductId(String productId){
	
		Session session = HibernateUtils.getSessionFactory().openSession();
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT MP.acceptsMercadoPago, MP.noMercadoPago " +
				  							"FROM Mercadopago as MP INNER JOIN MP.records as R " +
				  							"WHERE R.productId= :id AND R.dateRecord IN " +
				  							"(SELECT max(R.dateRecord) FROM Records as R WHERE R.productId = :id) "
				  							);
		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
		
	}
	public List <Object[]> getSoldQuantitysHistoriesByProductId(String productId){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT R.dateRecord, sum(IQ.soldQuantitys) " +
				  							"FROM Itemsquantitys as IQ JOIN IQ.records as R " +
				  							"WHERE R.productId= :id " +
				  							"GROUP BY R.dateRecord");
		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
		session.close();
		
		return list;
		
	}
	
	public List<Object[]> getPublications(String productId){
		Session session = HibernateUtils.getSessionFactory().openSession();
		//Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT P.description, P.permalink, P.price " +
				  							"FROM Publications as P INNER JOIN P.records as R " +
				  							"WHERE R.productId= :id AND " +
				  								  "R.dateRecord IN " +
				  								  "(SELECT max(R.dateRecord) FROM Records as R WHERE R.productId = :id) " +
				  						    "Order By P.price");
		query.setParameter("id", productId);
		List <Object[]> list = query.list();
		
//		SELECT p.description, p.permalink, p.price
//		FROM records as r, publications as p
//		WHERE r.idRecord = p.idRecord AND
//		      r.productId = 'MLA6414' AND
//		      r.dateRecord IN (SELECT max(R.dateRecord) FROM records as R WHERE R.productId = 'MLA6414')
		
		session.close();
		
		return list;
	}
	public List getSampleSize(String productId){
		
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		// Start a transaction
		session.beginTransaction();
		Query query = session.createQuery("SELECT sampleSize " +
										  "FROM Records " +
										  "WHERE productId = :id AND " +
										  		 "dateRecord in (SELECT max(dateRecord) FROM Records WHERE productId = :id)");
		query.setParameter("id", productId);
		List list = query.list();
		session.close();
		
		return list;
	}
	
	
}
