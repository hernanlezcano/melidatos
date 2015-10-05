package consultingPackage;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimerTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import wrapperPackage.WrapperItems;

import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.Response;

import daoClasses.DAORecords_MySQL;

public class CategoriesAlgorithm extends TimerTask{
	
	Category [] category;
	private Meli m = new Meli(1446274906831857L, "rnT2BpdRLJALy2E2cahxbYe2wW4ZylBu");
	static DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	static Date date;
	//This array will contain all the threads running
	Thread [] threads;
	int attempts;

	public CategoriesAlgorithm(){
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		getRootCategories();
	}
	
	public void getRootCategories(){
		Response response;
		date = new Date();
		String [] countries = {"MLA","MLB","MLC"};
		
		JSONArray categoriesAllowed = Configurations.getInstance().getCategoriesAllowed();
		
		ArrayList categoriesJsonArray = ConsultAPI.getInstance().getRootCategories(countries);
		
		//System.out.println(categoriesJsonArray);
		
		Iterator<String> iterator = categoriesJsonArray.iterator();
		int i = 0;
		
		if(((JSONObject)categoriesAllowed.get(0)).get("id").equals("todos")){
			threads = new Thread[categoriesJsonArray.size()];
			while(iterator.hasNext()){
				iterator.next();
				Thread thread = new Thread(new Category((JSONObject) categoriesJsonArray.get(i)));
				//System.out.println("Categorie: " + ((JSONObject) categoriesJsonArray.get(i)).get("id"));
				threads[i] = thread;
				i++;
			}
		}else{
			int k = 0;
			threads = new Thread[categoriesAllowed.toArray().length];
			while(iterator.hasNext()){
				iterator.next();
				for(int j=0;j<categoriesAllowed.toArray().length;j++){
					if(((JSONObject) categoriesJsonArray.get(i)).get("id").equals(((JSONObject) categoriesAllowed.get(j)).get("id"))){
						Thread thread = new Thread(new Category((JSONObject) categoriesJsonArray.get(i)));
						//System.out.println("Categorie: " + ((JSONObject) categoriesJsonArray.get(i)).get("id"));
						threads[k] = thread;
						k++;
					}
				}
				i++;
			}
		}
		//carga todas las hebras que le ponga
		for(int j=0;j<threads.length;j++){
			threads[j].start();
		}
		
		//para q espere que termine todas las hebras antes de cerrar, si termina una no se cierra todo
		for(int j=0;j<threads.length;j++){
			try {
				threads[j].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		date = new Date();
		System.out.println("Termino de proceso: " + hourFormat.format(date));
		
	}
	
	public class Category extends Thread{
		
		//Stack to contain fathers.
		private LinkedList<String> fifoFathers = new LinkedList<String>();
		//This stack will contain a column to indicate the father and a column to indicate the son, to register the relationship
		private LinkedList<String[]> fifoRelationship = new LinkedList<String[]>();
		//Stack to contain the childrens
		private LinkedList<String[]> fifoChildrens = new LinkedList<String[]>();
		//Stack to store the categories leafs
		private LinkedList<String> fifoLeafs = new LinkedList<String>();
		
		int attempts = 0;
		private JSONObject jsonObject;
		
		public Category(JSONObject category){
			
			jsonObject = category;
		}
		
		public void run(){
			//System.out.println("CategoriaId: " + jsonObject.get("id") + " ,Nombre: " + jsonObject.get("name"));
			
			fifoFathers.push((String) jsonObject.get("id"));
			
			while(fifoFathers.size() > 0){
				getChildrens(fifoFathers.pop());
			}
			
			storeRelationship();
			
			Thread thread = new Thread(new Items());
			thread.setName("2da hebra " + jsonObject.get("name"));
			thread.start();
			
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    //System.out.println(Thread.currentThread().getId() + " -> closed");
			Thread.currentThread().interrupt();
			return;
		}
		
		public void getChildrens(String categoryId){
			
				JSONObject obj = ConsultAPI.getInstance().getCategoryJSON(categoryId);
				//childrenJsonObject will contain the fathers at first level
				if(obj == null){
					return;
				}
				JSONObject categoryJsonObject = (JSONObject) obj;
				JSONArray childrens = (JSONArray) categoryJsonObject.get("children_categories");
				
				//por cada uno de los children le concateno antes el pat from rut, si lo hago dentro del if dejo afuera todas las hojas
				fifoChildrens.push(new String[]{(String) categoryJsonObject.get("id"),(String) categoryJsonObject.get("name")});
				
				if(childrens.size() > 0){
					Iterator<String> iterator = childrens.iterator();
					int i = 0;
					while(iterator.hasNext()){
						iterator.next();
						
						//Instance a children for each children in the category.
						JSONObject children = (JSONObject) childrens.get(i);
						fifoFathers.push((String) children.get("id"));
						fifoRelationship.push(new String[]{(String) children.get("id"), (String) categoryJsonObject.get("id") ,
													(String) categoryJsonObject.get("name"), (String) children.get("name")});
						i++;
					}
				}else{
					fifoLeafs.push((String) categoryJsonObject.get("id"));
					return;
				}
				
			return;
		}
		
		public void storeRelationship(){
			DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			boolean isLeaf;
			
			for(int j = 0;j<fifoRelationship.size();j++){
				isLeaf = false;
				//Indicate if the children in this relationship is a leaf
				if(fifoLeafs.contains(fifoRelationship.get(j)[0])){
					isLeaf = true;
				}
				//Save the relationships
				DAORecords_MySQL daoRecords = DAORecords_MySQL.getInstance();
				daoRecords.insertRelationship(fifoRelationship.get(j)[0], fifoRelationship.get(j)[1], fifoRelationship.get(j)[2], fifoRelationship.get(j)[3], isLeaf);
			}
		}
		
		public class Items extends Thread{
			
			WrapperItems itemsPerCategory;
			
			public void run(){
				
				DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date();
			
				for(int i=0;i<fifoChildrens.size();i++){
					this.obtainChildrensItems(fifoChildrens.get(i)[0],fifoChildrens.get(i)[1]);
				}
				
				Thread.currentThread().interrupt();
				return;
			}
			
			public void obtainChildrensItems(String childrenId, String childrenName){
				System.out.println("corriendo obtainchildrenItems");
				ConsultAPI consult = ConsultAPI.getInstance();
				itemsPerCategory = consult.getItemsPerCategory(childrenId);
				if(itemsPerCategory==null){
					//Do nothing because the JSON is wrong
				}else{
					calculateData(itemsPerCategory, childrenId, childrenName);
				}
				
			}
			
			//In the future the 4 value can be seted from the admin interfaz.
			//Z-score FORMULA: (abs(vecout-med)/MAD)>4
			public void calculateData(WrapperItems items, String childrenId, String childrenName){
				
				double maxPrice = 0, minPrice = 0, averagePrice = 0, sumPrices = 0, soldQuantity = 0, offerQuantity = 0;
				int count = 0;
				double median = new StadisticCalculation().obtainMedian(items);
				double MAD = new StadisticCalculation().obtainMAD(items);
				
				int acceptsMercadoPago = 0, noMercadoPago = 0;
				
				//Create a matrix to store the states quantitys of each item. 
				String [][] statesQuantitys = new String[67][5];
				
				//Load the states
				//Argentina
				statesQuantitys[0][0] = "AR-B"; 		statesQuantitys[0][1] = "0";  	statesQuantitys[0][2] = "0";	statesQuantitys[0][3] = "0";	statesQuantitys[0][4] = "0";
				statesQuantitys[1][0] = "AR-C"; 		statesQuantitys[1][1] = "0";  	statesQuantitys[1][2] = "0";	statesQuantitys[1][3] = "0";	statesQuantitys[1][4] = "0";
				statesQuantitys[2][0] = "AR-K"; 		statesQuantitys[2][1] = "0";  	statesQuantitys[2][2] = "0";	statesQuantitys[2][3] = "0";	statesQuantitys[2][4] = "0";
				statesQuantitys[3][0] = "AR-Q";			statesQuantitys[3][1] = "0";	statesQuantitys[3][2] = "0";	statesQuantitys[3][3] = "0";	statesQuantitys[3][4] = "0";
				statesQuantitys[4][0] = "AR-R";			statesQuantitys[4][1] = "0";	statesQuantitys[4][2] = "0";	statesQuantitys[4][3] = "0";	statesQuantitys[4][4] = "0";
				statesQuantitys[5][0] = "AR-Y";			statesQuantitys[5][1] = "0";	statesQuantitys[5][2] = "0";	statesQuantitys[5][3] = "0";	statesQuantitys[5][4] = "0";
				statesQuantitys[6][0] = "AR-F";			statesQuantitys[6][1] = "0";	statesQuantitys[6][2] = "0";	statesQuantitys[6][3] = "0";	statesQuantitys[6][4] = "0";
				statesQuantitys[7][0] = "AR-T";			statesQuantitys[7][1] = "0";	statesQuantitys[7][2] = "0";	statesQuantitys[7][3] = "0";	statesQuantitys[7][4] = "0";
				statesQuantitys[8][0] = "AR-G";			statesQuantitys[8][1] = "0";	statesQuantitys[8][2] = "0";	statesQuantitys[8][3] = "0";	statesQuantitys[8][4] = "0";
				statesQuantitys[9][0] = "AR-P";			statesQuantitys[9][1] = "0";	statesQuantitys[9][2] = "0";	statesQuantitys[9][3] = "0";	statesQuantitys[9][4] = "0";
				statesQuantitys[10][0] = "AR-S";		statesQuantitys[10][1] = "0";	statesQuantitys[10][2] = "0";	statesQuantitys[10][3] = "0";	statesQuantitys[10][4] = "0";
				statesQuantitys[11][0] = "AR-K";		statesQuantitys[11][1] = "0";	statesQuantitys[11][2] = "0";	statesQuantitys[11][3] = "0";	statesQuantitys[11][4] = "0";
				statesQuantitys[12][0] = "AR-L";		statesQuantitys[12][1] = "0";	statesQuantitys[12][2] = "0";	statesQuantitys[12][3] = "0";	statesQuantitys[12][4] = "0";
				statesQuantitys[13][0] = "AR-V";		statesQuantitys[13][1] = "0";	statesQuantitys[13][2] = "0";	statesQuantitys[13][3] = "0";	statesQuantitys[13][4] = "0";
				statesQuantitys[14][0] = "AR-U";		statesQuantitys[14][1] = "0";	statesQuantitys[14][2] = "0";	statesQuantitys[14][3] = "0";	statesQuantitys[14][4] = "0";
				statesQuantitys[15][0] = "AR-H";		statesQuantitys[15][1] = "0";	statesQuantitys[15][2] = "0";	statesQuantitys[15][3] = "0";	statesQuantitys[15][4] = "0";
				statesQuantitys[16][0] = "AR-W";		statesQuantitys[16][1] = "0";	statesQuantitys[16][2] = "0";	statesQuantitys[16][3] = "0";	statesQuantitys[16][4] = "0";
				statesQuantitys[17][0] = "AR-E";		statesQuantitys[17][1] = "0";	statesQuantitys[17][2] = "0";	statesQuantitys[17][3] = "0";	statesQuantitys[17][4] = "0";
				statesQuantitys[18][0] = "AR-M";		statesQuantitys[18][1] = "0";	statesQuantitys[18][2] = "0";	statesQuantitys[18][3] = "0";	statesQuantitys[18][4] = "0";
				statesQuantitys[19][0] = "AR-N";		statesQuantitys[19][1] = "0";	statesQuantitys[19][2] = "0";	statesQuantitys[19][3] = "0";	statesQuantitys[19][4] = "0";
				statesQuantitys[20][0] = "AR-J";		statesQuantitys[20][1] = "0";	statesQuantitys[20][2] = "0";	statesQuantitys[20][3] = "0";	statesQuantitys[20][4] = "0";
				statesQuantitys[21][0] = "AR-D";		statesQuantitys[21][1] = "0";	statesQuantitys[21][2] = "0";	statesQuantitys[21][3] = "0";	statesQuantitys[21][4] = "0";
				statesQuantitys[22][0] = "AR-Z";		statesQuantitys[22][1] = "0";	statesQuantitys[22][2] = "0";	statesQuantitys[22][3] = "0";	statesQuantitys[22][4] = "0";
				statesQuantitys[23][0] = "AR-C";		statesQuantitys[23][1] = "0";	statesQuantitys[23][2] = "0";	statesQuantitys[23][3] = "0";	statesQuantitys[23][4] = "0";
				
				//Brasil
				statesQuantitys[24][0] = "BR-AC";		statesQuantitys[24][1] = "0";	statesQuantitys[24][2] = "0";	statesQuantitys[24][3] = "0";	statesQuantitys[24][4] = "0";
				statesQuantitys[25][0] = "BR-AL"; 		statesQuantitys[25][1] = "0";	statesQuantitys[25][2] = "0";	statesQuantitys[25][3] = "0";	statesQuantitys[25][4] = "0";
				statesQuantitys[26][0] = "BR-AP";		statesQuantitys[26][1] = "0";	statesQuantitys[26][2] = "0";	statesQuantitys[26][3] = "0";	statesQuantitys[26][4] = "0";
				statesQuantitys[27][0] = "BR-AM";		statesQuantitys[27][1] = "0";	statesQuantitys[27][2] = "0";	statesQuantitys[27][3] = "0";	statesQuantitys[27][4] = "0";
				statesQuantitys[28][0] = "BR-BA";		statesQuantitys[28][1] = "0";	statesQuantitys[28][2] = "0";	statesQuantitys[28][3] = "0";	statesQuantitys[28][4] = "0";
				statesQuantitys[29][0] = "BR-CE";		statesQuantitys[29][1] = "0";	statesQuantitys[29][2] = "0";	statesQuantitys[29][3] = "0";	statesQuantitys[29][4] = "0";
				statesQuantitys[30][0] = "BR-DF";		statesQuantitys[30][1] = "0";	statesQuantitys[30][2] = "0";	statesQuantitys[30][3] = "0";	statesQuantitys[30][4] = "0";
				statesQuantitys[31][0] = "BR-ES";		statesQuantitys[31][1] = "0";	statesQuantitys[31][2] = "0";	statesQuantitys[31][3] = "0";	statesQuantitys[31][4] = "0";
				statesQuantitys[32][0] = "BR-GO";		statesQuantitys[32][1] = "0";	statesQuantitys[32][2] = "0";	statesQuantitys[32][3] = "0";	statesQuantitys[32][4] = "0";
				statesQuantitys[33][0] = "BR-MA";		statesQuantitys[33][1] = "0";	statesQuantitys[33][2] = "0";	statesQuantitys[33][3] = "0";	statesQuantitys[33][4] = "0";
				statesQuantitys[34][0] = "BR-MT";		statesQuantitys[34][1] = "0";	statesQuantitys[34][2] = "0";	statesQuantitys[34][3] = "0";	statesQuantitys[34][4] = "0";
				statesQuantitys[35][0] = "BR-MS";		statesQuantitys[35][1] = "0";	statesQuantitys[35][2] = "0";	statesQuantitys[35][3] = "0";	statesQuantitys[35][4] = "0";
				statesQuantitys[36][0] = "BR-MG";		statesQuantitys[36][1] = "0";	statesQuantitys[36][2] = "0";	statesQuantitys[36][3] = "0";	statesQuantitys[36][4] = "0";
				statesQuantitys[37][0] = "BR-PR";		statesQuantitys[37][1] = "0";	statesQuantitys[37][2] = "0";	statesQuantitys[37][3] = "0";	statesQuantitys[37][4] = "0";
				statesQuantitys[38][0] = "BR-PB";		statesQuantitys[38][1] = "0";	statesQuantitys[38][2] = "0";	statesQuantitys[38][3] = "0";	statesQuantitys[38][4] = "0";
				statesQuantitys[39][0] = "BR-PA";		statesQuantitys[39][1] = "0";	statesQuantitys[39][2] = "0";	statesQuantitys[39][3] = "0";	statesQuantitys[39][4] = "0";
				statesQuantitys[40][0] = "BR-PE";		statesQuantitys[40][1] = "0";	statesQuantitys[40][2] = "0";	statesQuantitys[40][3] = "0";	statesQuantitys[40][4] = "0";
				statesQuantitys[41][0] = "BR-PI";		statesQuantitys[41][1] = "0";	statesQuantitys[41][2] = "0";	statesQuantitys[41][3] = "0";	statesQuantitys[41][4] = "0";
				statesQuantitys[42][0] = "BR-RN";		statesQuantitys[42][1] = "0";	statesQuantitys[42][2] = "0";	statesQuantitys[42][3] = "0";	statesQuantitys[42][4] = "0";
				statesQuantitys[43][0] = "BR-RS";		statesQuantitys[43][1] = "0";	statesQuantitys[43][2] = "0";	statesQuantitys[43][3] = "0";	statesQuantitys[43][4] = "0";
				statesQuantitys[44][0] = "BR-RJ";		statesQuantitys[44][1] = "0";	statesQuantitys[44][2] = "0";	statesQuantitys[44][3] = "0";	statesQuantitys[44][4] = "0";
				statesQuantitys[45][0] = "BR-RO";		statesQuantitys[45][1] = "0";	statesQuantitys[45][2] = "0";	statesQuantitys[45][3] = "0";	statesQuantitys[45][4] = "0";
				statesQuantitys[46][0] = "BR-RR";		statesQuantitys[46][1] = "0";	statesQuantitys[46][2] = "0";	statesQuantitys[46][3] = "0";	statesQuantitys[46][4] = "0";
				statesQuantitys[47][0] = "BR-SC";		statesQuantitys[47][1] = "0";	statesQuantitys[47][2] = "0";	statesQuantitys[47][3] = "0";	statesQuantitys[47][4] = "0";
				statesQuantitys[48][0] = "BR-SE";		statesQuantitys[48][1] = "0";	statesQuantitys[48][2] = "0";	statesQuantitys[48][3] = "0";	statesQuantitys[48][4] = "0";
				statesQuantitys[49][0] = "BR-SP";		statesQuantitys[49][1] = "0";	statesQuantitys[49][2] = "0";	statesQuantitys[49][3] = "0";	statesQuantitys[49][4] = "0";
				statesQuantitys[50][0] = "BR-TO";		statesQuantitys[50][1] = "0";	statesQuantitys[50][2] = "0";	statesQuantitys[50][3] = "0";	statesQuantitys[50][4] = "0";
				
				//Chile
				statesQuantitys[51][0] = "CL-AI";		statesQuantitys[51][1] = "0";	statesQuantitys[51][2] = "0";	statesQuantitys[51][3] = "0";	statesQuantitys[51][4] = "0";
				statesQuantitys[52][0] = "CL-AN";		statesQuantitys[52][1] = "0";	statesQuantitys[52][2] = "0";	statesQuantitys[52][3] = "0";	statesQuantitys[52][4] = "0";
				statesQuantitys[53][0] = "CL-AP";		statesQuantitys[53][1] = "0";	statesQuantitys[53][2] = "0";	statesQuantitys[53][3] = "0";	statesQuantitys[53][4] = "0";
				statesQuantitys[54][0] = "CL-AT";		statesQuantitys[54][1] = "0";	statesQuantitys[54][2] = "0";	statesQuantitys[54][3] = "0";	statesQuantitys[54][4] = "0";
				statesQuantitys[55][0] = "CL-BI";		statesQuantitys[55][1] = "0";	statesQuantitys[55][2] = "0";	statesQuantitys[55][3] = "0";	statesQuantitys[55][4] = "0";
				statesQuantitys[56][0] = "CL-CO";		statesQuantitys[56][1] = "0";	statesQuantitys[56][2] = "0";	statesQuantitys[56][3] = "0";	statesQuantitys[56][4] = "0";
				statesQuantitys[57][0] = "CL-AR";		statesQuantitys[57][1] = "0";	statesQuantitys[57][2] = "0";	statesQuantitys[57][3] = "0";	statesQuantitys[57][4] = "0";
				statesQuantitys[58][0] = "CL-LI";		statesQuantitys[58][1] = "0";	statesQuantitys[58][2] = "0";	statesQuantitys[58][3] = "0";	statesQuantitys[58][4] = "0";
				statesQuantitys[59][0] = "CL-LL";		statesQuantitys[59][1] = "0";	statesQuantitys[59][2] = "0";	statesQuantitys[59][3] = "0";	statesQuantitys[59][4] = "0";
				statesQuantitys[60][0] = "CL-LR";		statesQuantitys[60][1] = "0";	statesQuantitys[60][2] = "0";	statesQuantitys[60][3] = "0";	statesQuantitys[60][4] = "0";
				statesQuantitys[61][0] = "CL-MA";		statesQuantitys[61][1] = "0";	statesQuantitys[61][2] = "0";	statesQuantitys[61][3] = "0";	statesQuantitys[61][4] = "0";
				statesQuantitys[62][0] = "CL-ML";		statesQuantitys[62][1] = "0";	statesQuantitys[62][2] = "0";	statesQuantitys[62][3] = "0";	statesQuantitys[62][4] = "0";
				statesQuantitys[63][0] = "CL-RM";		statesQuantitys[63][1] = "0";	statesQuantitys[63][2] = "0";	statesQuantitys[63][3] = "0";	statesQuantitys[63][4] = "0";
				statesQuantitys[64][0] = "CL-TA";		statesQuantitys[64][1] = "0";	statesQuantitys[64][2] = "0";	statesQuantitys[64][3] = "0";	statesQuantitys[64][4] = "0";
				statesQuantitys[65][0] = "CL-VS";		statesQuantitys[65][1] = "0";	statesQuantitys[65][2] = "0";	statesQuantitys[65][3] = "0";	statesQuantitys[65][4] = "0";
				
				statesQuantitys[66][0] = "Otro";		       		statesQuantitys[66][1] = "0";	statesQuantitys[66][2] = "0";	statesQuantitys[66][3] = "0";	statesQuantitys[66][4] = "0";
				
				//Create a stack to store the stopdates and quantitys of items.
				//stopQuantitys[Date, StopQuantity]
				LinkedList<String[]> stopQuantitys = new LinkedList<String[]>();
				
				//Obtain publications titles and permalink
				//publicationsData[title, permalink]
				LinkedList<String[]> publicationsData = new LinkedList<String[]>();
				
				for(int i=0;i<items.getResults().length;i++){
					
					//Eliminate outliers
					if((Math.abs(items.getResults()[i].getPrice()-median)/MAD)<=4){
						//The condition can be null, in for example "cosmetologia"
						if(items.getResults()[i].getCondition() == null || items.getResults()[i].getCondition().contains("new")){
							sumPrices = sumPrices + items.getResults()[i].getPrice();
							
							if(maxPrice < items.getResults()[i].getPrice()){
								maxPrice = items.getResults()[i].getPrice();
							}
							if(minPrice > items.getResults()[i].getPrice() || count==0){
								minPrice = items.getResults()[i].getPrice();
							}
							
							if(items.getResults()[i].isAccepts_mercadopago()){
								acceptsMercadoPago++;
							}else{
								noMercadoPago++;
							}
							soldQuantity = soldQuantity + items.getResults()[i].getSold_quantity();
							offerQuantity = offerQuantity + items.getResults()[i].getAvailable_quantity();
							count++;
							
							switch(items.getResults()[i].getAddress().getState_id()){
								//Argentina
								case "AR-B":
									//Buenos aires
									statesQuantitys[0][1] = String.valueOf(Double.valueOf(statesQuantitys[0][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[0][2] = String.valueOf(Double.valueOf(statesQuantitys[0][2]) + items.getResults()[i].getSold_quantity());	
									statesQuantitys[0][3] = String.valueOf(Double.valueOf(statesQuantitys[0][3]) + items.getResults()[i].getPrice());
									statesQuantitys[0][4] = String.valueOf(Integer.valueOf(statesQuantitys[0][4]) + 1);
									break;
								case "AR-X":
									//Cordoba
									statesQuantitys[1][1] = String.valueOf(Double.valueOf(statesQuantitys[1][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[1][2] = String.valueOf(Double.valueOf(statesQuantitys[1][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[1][3] = String.valueOf(Double.valueOf(statesQuantitys[1][3]) + items.getResults()[i].getPrice());
									statesQuantitys[1][4] = String.valueOf(Integer.valueOf(statesQuantitys[1][4]) + 1);
									break;
								case "AR-A":
									//Salta
									statesQuantitys[2][1] = String.valueOf(Double.valueOf(statesQuantitys[2][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[2][2] = String.valueOf(Double.valueOf(statesQuantitys[2][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[2][3] = String.valueOf(Double.valueOf(statesQuantitys[2][3]) + items.getResults()[i].getPrice());
									statesQuantitys[2][4] = String.valueOf(Integer.valueOf(statesQuantitys[2][4]) + 1);
									break;
								case "AR-Q":
									//Neuquen
									statesQuantitys[3][1] = String.valueOf(Double.valueOf(statesQuantitys[3][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[3][2] = String.valueOf(Double.valueOf(statesQuantitys[3][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[3][3] = String.valueOf(Double.valueOf(statesQuantitys[3][3]) + items.getResults()[i].getPrice());
									statesQuantitys[3][4] = String.valueOf(Integer.valueOf(statesQuantitys[3][4]) + 1);
									break;
								case "AR-R":
									//Rio negro
									statesQuantitys[4][1] = String.valueOf(Double.valueOf(statesQuantitys[4][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[4][2] = String.valueOf(Double.valueOf(statesQuantitys[4][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[4][3] = String.valueOf(Double.valueOf(statesQuantitys[4][3]) + items.getResults()[i].getPrice());
									statesQuantitys[4][4] = String.valueOf(Integer.valueOf(statesQuantitys[4][4]) + 1);
									break;
								case "AR-Y":
									//Jujuy
									statesQuantitys[5][1] = String.valueOf(Double.valueOf(statesQuantitys[5][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[5][2] = String.valueOf(Double.valueOf(statesQuantitys[5][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[5][3] = String.valueOf(Double.valueOf(statesQuantitys[5][3]) + items.getResults()[i].getPrice());
									statesQuantitys[5][4] = String.valueOf(Integer.valueOf(statesQuantitys[5][4]) + 1);
									
									break;
								case "AR-F":
									//La rioja
									statesQuantitys[6][1] = String.valueOf(Double.valueOf(statesQuantitys[6][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[6][2] = String.valueOf(Double.valueOf(statesQuantitys[6][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[6][3] = String.valueOf(Double.valueOf(statesQuantitys[6][3]) + items.getResults()[i].getPrice());
									statesQuantitys[6][4] = String.valueOf(Integer.valueOf(statesQuantitys[6][4]) + 1);
									break;
								case "AR-T":
									//Tucuman
									statesQuantitys[7][1] = String.valueOf(Double.valueOf(statesQuantitys[7][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[7][2] = String.valueOf(Double.valueOf(statesQuantitys[7][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[7][3] = String.valueOf(Double.valueOf(statesQuantitys[7][3]) + items.getResults()[i].getPrice());
									statesQuantitys[7][4] = String.valueOf(Integer.valueOf(statesQuantitys[7][4]) + 1);
									break;
								case "AR-G":
									//Santiago del estero
									statesQuantitys[8][1] = String.valueOf(Double.valueOf(statesQuantitys[8][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[8][2] = String.valueOf(Double.valueOf(statesQuantitys[8][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[8][3] = String.valueOf(Double.valueOf(statesQuantitys[8][3]) + items.getResults()[i].getPrice());
									statesQuantitys[8][4] = String.valueOf(Integer.valueOf(statesQuantitys[8][4]) + 1);
									break;
								case "AR-P":
									//Formosa
									statesQuantitys[9][1] = String.valueOf(Double.valueOf(statesQuantitys[9][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[9][2] = String.valueOf(Double.valueOf(statesQuantitys[9][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[9][3] = String.valueOf(Double.valueOf(statesQuantitys[9][3]) + items.getResults()[i].getPrice());
									statesQuantitys[9][4] = String.valueOf(Integer.valueOf(statesQuantitys[9][4]) + 1);
									break;
								case "AR-S":
									//Santa fe
									statesQuantitys[10][1] = String.valueOf(Double.valueOf(statesQuantitys[10][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[10][2] = String.valueOf(Double.valueOf(statesQuantitys[10][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[10][3] = String.valueOf(Double.valueOf(statesQuantitys[10][3]) + items.getResults()[i].getPrice());
									statesQuantitys[10][4] = String.valueOf(Integer.valueOf(statesQuantitys[10][4]) + 1);
									break;
								case "AR-K":
									//Catamarca
									statesQuantitys[11][1] = String.valueOf(Double.valueOf(statesQuantitys[11][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[11][2] = String.valueOf(Double.valueOf(statesQuantitys[11][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[11][3] = String.valueOf(Double.valueOf(statesQuantitys[11][3]) + items.getResults()[i].getPrice());
									statesQuantitys[11][4] = String.valueOf(Integer.valueOf(statesQuantitys[11][4]) + 1);
									break;
								case "AR-L":
									//La pampa
									statesQuantitys[12][1] = String.valueOf(Double.valueOf(statesQuantitys[12][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[12][2] = String.valueOf(Double.valueOf(statesQuantitys[12][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[12][3] = String.valueOf(Double.valueOf(statesQuantitys[12][3]) + items.getResults()[i].getPrice());
									statesQuantitys[12][4] = String.valueOf(Integer.valueOf(statesQuantitys[12][4]) + 1);
									break;
								case "AR-V":
									//Tierra del fuego
									statesQuantitys[13][1] = String.valueOf(Double.valueOf(statesQuantitys[13][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[13][2] = String.valueOf(Double.valueOf(statesQuantitys[13][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[13][3] = String.valueOf(Double.valueOf(statesQuantitys[13][3]) + items.getResults()[i].getPrice());
									statesQuantitys[13][4] = String.valueOf(Integer.valueOf(statesQuantitys[13][4]) + 1);
									break;
								case "AR-U":
									//Chubut
									statesQuantitys[14][1] = String.valueOf(Double.valueOf(statesQuantitys[14][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[14][2] = String.valueOf(Double.valueOf(statesQuantitys[14][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[14][3] = String.valueOf(Double.valueOf(statesQuantitys[14][3]) + items.getResults()[i].getPrice());
									statesQuantitys[14][4] = String.valueOf(Integer.valueOf(statesQuantitys[14][4]) + 1);
									break;
								case "AR-H":
									//Chaco
									statesQuantitys[15][1] = String.valueOf(Double.valueOf(statesQuantitys[15][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[15][2] = String.valueOf(Double.valueOf(statesQuantitys[15][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[15][3] = String.valueOf(Double.valueOf(statesQuantitys[15][3]) + items.getResults()[i].getPrice());
									statesQuantitys[15][4] = String.valueOf(Integer.valueOf(statesQuantitys[15][4]) + 1);
									break;
								case "AR-W":
									//Corrientes
									statesQuantitys[16][1] = String.valueOf(Double.valueOf(statesQuantitys[16][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[16][2] = String.valueOf(Double.valueOf(statesQuantitys[16][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[16][3] = String.valueOf(Double.valueOf(statesQuantitys[16][3]) + items.getResults()[i].getPrice());
									statesQuantitys[16][4] = String.valueOf(Integer.valueOf(statesQuantitys[16][4]) + 1);
									break;
								case "AR-E":
									//Entre rios
									statesQuantitys[17][1] = String.valueOf(Double.valueOf(statesQuantitys[17][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[17][2] = String.valueOf(Double.valueOf(statesQuantitys[17][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[17][3] = String.valueOf(Double.valueOf(statesQuantitys[17][3]) + items.getResults()[i].getPrice());
									statesQuantitys[17][4] = String.valueOf(Integer.valueOf(statesQuantitys[17][4]) + 1);
									break;
								case "AR-M":
									//Mendoza
									statesQuantitys[18][1] = String.valueOf(Double.valueOf(statesQuantitys[18][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[18][2] = String.valueOf(Double.valueOf(statesQuantitys[18][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[18][3] = String.valueOf(Double.valueOf(statesQuantitys[18][3]) + items.getResults()[i].getPrice());
									statesQuantitys[18][4] = String.valueOf(Integer.valueOf(statesQuantitys[18][4]) + 1);
									break;
								case "AR-N":
									//Misiones
									statesQuantitys[19][1] = String.valueOf(Double.valueOf(statesQuantitys[19][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[19][2] = String.valueOf(Double.valueOf(statesQuantitys[19][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[19][3] = String.valueOf(Double.valueOf(statesQuantitys[19][3]) + items.getResults()[i].getPrice());
									statesQuantitys[19][4] = String.valueOf(Integer.valueOf(statesQuantitys[19][4]) + 1);
									break;
								case "AR-J":
									//San juan
									statesQuantitys[20][1] = String.valueOf(Double.valueOf(statesQuantitys[20][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[20][2] = String.valueOf(Double.valueOf(statesQuantitys[20][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[20][3] = String.valueOf(Double.valueOf(statesQuantitys[20][3]) + items.getResults()[i].getPrice());
									statesQuantitys[20][4] = String.valueOf(Integer.valueOf(statesQuantitys[20][4]) + 1);
									break;
								case "AR-D":
									//San luis
									statesQuantitys[21][1] = String.valueOf(Double.valueOf(statesQuantitys[21][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[21][2] = String.valueOf(Double.valueOf(statesQuantitys[21][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[21][3] = String.valueOf(Double.valueOf(statesQuantitys[21][3]) + items.getResults()[i].getPrice());
									statesQuantitys[21][4] = String.valueOf(Integer.valueOf(statesQuantitys[21][4]) + 1);
									break;
								case "AR-Z":
									//Santa cruz
									statesQuantitys[22][1] = String.valueOf(Double.valueOf(statesQuantitys[22][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[22][2] = String.valueOf(Double.valueOf(statesQuantitys[22][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[22][3] = String.valueOf(Double.valueOf(statesQuantitys[22][3]) + items.getResults()[i].getPrice());
									statesQuantitys[22][4] = String.valueOf(Integer.valueOf(statesQuantitys[22][4]) + 1);
									break;
								case "AR-C":
									//Capital federal
									statesQuantitys[23][1] = String.valueOf(Double.valueOf(statesQuantitys[23][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[23][2] = String.valueOf(Double.valueOf(statesQuantitys[23][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[23][3] = String.valueOf(Double.valueOf(statesQuantitys[23][3]) + items.getResults()[i].getPrice());
									statesQuantitys[23][4] = String.valueOf(Integer.valueOf(statesQuantitys[23][4]) + 1);
									break;
								case "BR-AC":
									//Acre
									statesQuantitys[24][1] = String.valueOf(Double.valueOf(statesQuantitys[24][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[24][2] = String.valueOf(Double.valueOf(statesQuantitys[24][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[24][3] = String.valueOf(Double.valueOf(statesQuantitys[24][3]) + items.getResults()[i].getPrice());
									statesQuantitys[24][4] = String.valueOf(Integer.valueOf(statesQuantitys[24][4]) + 1);
									break;
								case "BR-AL":
									//Alagoas
									statesQuantitys[25][1] = String.valueOf(Double.valueOf(statesQuantitys[25][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[25][2] = String.valueOf(Double.valueOf(statesQuantitys[25][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[25][3] = String.valueOf(Double.valueOf(statesQuantitys[25][3]) + items.getResults()[i].getPrice());
									statesQuantitys[25][4] = String.valueOf(Integer.valueOf(statesQuantitys[25][4]) + 1);
									break;
								case "BR-AP":
									//Amapá
									statesQuantitys[26][1] = String.valueOf(Double.valueOf(statesQuantitys[26][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[26][2] = String.valueOf(Double.valueOf(statesQuantitys[26][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[26][3] = String.valueOf(Double.valueOf(statesQuantitys[26][3]) + items.getResults()[i].getPrice());
									statesQuantitys[26][4] = String.valueOf(Integer.valueOf(statesQuantitys[26][4]) + 1);
									break;
								case "BR-AM":
									//Amazonas
									statesQuantitys[27][1] = String.valueOf(Double.valueOf(statesQuantitys[27][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[27][2] = String.valueOf(Double.valueOf(statesQuantitys[27][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[27][3] = String.valueOf(Double.valueOf(statesQuantitys[27][3]) + items.getResults()[i].getPrice());
									statesQuantitys[27][4] = String.valueOf(Integer.valueOf(statesQuantitys[27][4]) + 1);
									break;
								case "BR-BA":
									//Bahia
									statesQuantitys[28][1] = String.valueOf(Double.valueOf(statesQuantitys[28][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[28][2] = String.valueOf(Double.valueOf(statesQuantitys[28][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[28][3] = String.valueOf(Double.valueOf(statesQuantitys[28][3]) + items.getResults()[i].getPrice());
									statesQuantitys[28][4] = String.valueOf(Integer.valueOf(statesQuantitys[28][4]) + 1);
									break;
								case "BR-CE":
									//Ceará
									statesQuantitys[29][1] = String.valueOf(Double.valueOf(statesQuantitys[29][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[29][2] = String.valueOf(Double.valueOf(statesQuantitys[29][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[29][3] = String.valueOf(Double.valueOf(statesQuantitys[29][3]) + items.getResults()[i].getPrice());
									statesQuantitys[29][4] = String.valueOf(Integer.valueOf(statesQuantitys[29][4]) + 1);
									break;
								case "BR-DF":
									//Distrito Federal
									statesQuantitys[30][1] = String.valueOf(Double.valueOf(statesQuantitys[30][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[30][2] = String.valueOf(Double.valueOf(statesQuantitys[30][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[30][3] = String.valueOf(Double.valueOf(statesQuantitys[30][3]) + items.getResults()[i].getPrice());
									statesQuantitys[30][4] = String.valueOf(Integer.valueOf(statesQuantitys[30][4]) + 1);
									break;
								case "BR-ES":
									//Espirito Santo
									statesQuantitys[31][1] = String.valueOf(Double.valueOf(statesQuantitys[31][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[31][2] = String.valueOf(Double.valueOf(statesQuantitys[31][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[31][3] = String.valueOf(Double.valueOf(statesQuantitys[31][3]) + items.getResults()[i].getPrice());
									statesQuantitys[31][4] = String.valueOf(Integer.valueOf(statesQuantitys[31][4]) + 1);
									break;	
								case "BR-GO":
									//Goiás
									statesQuantitys[32][1] = String.valueOf(Double.valueOf(statesQuantitys[32][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[32][2] = String.valueOf(Double.valueOf(statesQuantitys[32][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[32][3] = String.valueOf(Double.valueOf(statesQuantitys[32][3]) + items.getResults()[i].getPrice());
									statesQuantitys[32][4] = String.valueOf(Integer.valueOf(statesQuantitys[32][4]) + 1);
									break;	
								case "BR-MA":
									//Maranhao
									statesQuantitys[33][1] = String.valueOf(Double.valueOf(statesQuantitys[33][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[33][2] = String.valueOf(Double.valueOf(statesQuantitys[33][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[33][3] = String.valueOf(Double.valueOf(statesQuantitys[33][3]) + items.getResults()[i].getPrice());
									statesQuantitys[33][4] = String.valueOf(Integer.valueOf(statesQuantitys[33][4]) + 1);
									break;
								case "BR-MT":
									//Mato Grosso
									statesQuantitys[34][1] = String.valueOf(Double.valueOf(statesQuantitys[34][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[34][2] = String.valueOf(Double.valueOf(statesQuantitys[34][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[34][3] = String.valueOf(Double.valueOf(statesQuantitys[34][3]) + items.getResults()[i].getPrice());
									statesQuantitys[34][4] = String.valueOf(Integer.valueOf(statesQuantitys[34][4]) + 1);
									break;
								case "BR-MS":
									//Mato Grosso do Sul
									statesQuantitys[35][1] = String.valueOf(Double.valueOf(statesQuantitys[35][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[35][2] = String.valueOf(Double.valueOf(statesQuantitys[35][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[35][3] = String.valueOf(Double.valueOf(statesQuantitys[35][3]) + items.getResults()[i].getPrice());
									statesQuantitys[35][4] = String.valueOf(Integer.valueOf(statesQuantitys[35][4]) + 1);
									break;
								case "BR-MG":
									//Minas Gerais
									statesQuantitys[36][1] = String.valueOf(Double.valueOf(statesQuantitys[36][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[36][2] = String.valueOf(Double.valueOf(statesQuantitys[36][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[36][3] = String.valueOf(Double.valueOf(statesQuantitys[36][3]) + items.getResults()[i].getPrice());
									statesQuantitys[36][4] = String.valueOf(Integer.valueOf(statesQuantitys[36][4]) + 1);
									break;
								case "BR-PR":
									//Paraná
									statesQuantitys[37][1] = String.valueOf(Double.valueOf(statesQuantitys[37][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[37][2] = String.valueOf(Double.valueOf(statesQuantitys[37][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[37][3] = String.valueOf(Double.valueOf(statesQuantitys[37][3]) + items.getResults()[i].getPrice());
									statesQuantitys[37][4] = String.valueOf(Integer.valueOf(statesQuantitys[37][4]) + 1);
									break;
								case "BR-PB":
									//Paraíba
									statesQuantitys[38][1] = String.valueOf(Double.valueOf(statesQuantitys[38][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[38][2] = String.valueOf(Double.valueOf(statesQuantitys[38][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[38][3] = String.valueOf(Double.valueOf(statesQuantitys[38][3]) + items.getResults()[i].getPrice());
									statesQuantitys[38][4] = String.valueOf(Integer.valueOf(statesQuantitys[38][4]) + 1);
									break;
								case "BR-PA":
									//Pará
									statesQuantitys[39][1] = String.valueOf(Double.valueOf(statesQuantitys[39][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[39][2] = String.valueOf(Double.valueOf(statesQuantitys[39][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[39][3] = String.valueOf(Double.valueOf(statesQuantitys[39][3]) + items.getResults()[i].getPrice());
									statesQuantitys[39][4] = String.valueOf(Integer.valueOf(statesQuantitys[39][4]) + 1);
									break;
								case "BR-PE":
									//Pernambuco
									statesQuantitys[40][1] = String.valueOf(Double.valueOf(statesQuantitys[40][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[40][2] = String.valueOf(Double.valueOf(statesQuantitys[40][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[40][3] = String.valueOf(Double.valueOf(statesQuantitys[40][3]) + items.getResults()[i].getPrice());
									statesQuantitys[40][4] = String.valueOf(Integer.valueOf(statesQuantitys[40][4]) + 1);
									break;
								case "BR-PI":
									//Piauí
									statesQuantitys[41][1] = String.valueOf(Double.valueOf(statesQuantitys[41][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[41][2] = String.valueOf(Double.valueOf(statesQuantitys[41][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[41][3] = String.valueOf(Double.valueOf(statesQuantitys[41][3]) + items.getResults()[i].getPrice());
									statesQuantitys[41][4] = String.valueOf(Integer.valueOf(statesQuantitys[41][4]) + 1);
									break;
								case "BR-RN":
									//Rio Grande do Norte
									statesQuantitys[42][1] = String.valueOf(Double.valueOf(statesQuantitys[42][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[42][2] = String.valueOf(Double.valueOf(statesQuantitys[42][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[42][3] = String.valueOf(Double.valueOf(statesQuantitys[42][3]) + items.getResults()[i].getPrice());
									statesQuantitys[42][4] = String.valueOf(Integer.valueOf(statesQuantitys[42][4]) + 1);
									break;
								case "BR-RS":
									//Rio Grande do Sul
									statesQuantitys[43][1] = String.valueOf(Double.valueOf(statesQuantitys[43][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[43][2] = String.valueOf(Double.valueOf(statesQuantitys[43][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[43][3] = String.valueOf(Double.valueOf(statesQuantitys[43][3]) + items.getResults()[i].getPrice());
									statesQuantitys[43][4] = String.valueOf(Integer.valueOf(statesQuantitys[43][4]) + 1);
									break;
								case "BR-RJ":
									//Rio de Janeiro
									statesQuantitys[44][1] = String.valueOf(Double.valueOf(statesQuantitys[44][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[44][2] = String.valueOf(Double.valueOf(statesQuantitys[44][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[44][3] = String.valueOf(Double.valueOf(statesQuantitys[44][3]) + items.getResults()[i].getPrice());
									statesQuantitys[44][4] = String.valueOf(Integer.valueOf(statesQuantitys[44][4]) + 1);
									break;
								case "BR-RO":
									//Rindônia
									statesQuantitys[45][1] = String.valueOf(Double.valueOf(statesQuantitys[45][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[45][2] = String.valueOf(Double.valueOf(statesQuantitys[45][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[45][3] = String.valueOf(Double.valueOf(statesQuantitys[45][3]) + items.getResults()[i].getPrice());
									statesQuantitys[45][4] = String.valueOf(Integer.valueOf(statesQuantitys[45][4]) + 1);
									break;
								case "BR-RR":
									//Roraima
									statesQuantitys[46][1] = String.valueOf(Double.valueOf(statesQuantitys[46][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[46][2] = String.valueOf(Double.valueOf(statesQuantitys[46][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[46][3] = String.valueOf(Double.valueOf(statesQuantitys[46][3]) + items.getResults()[i].getPrice());
									statesQuantitys[46][4] = String.valueOf(Integer.valueOf(statesQuantitys[46][4]) + 1);
									break;
								case "BR-SC":
									//Santa Catarina
									statesQuantitys[47][1] = String.valueOf(Double.valueOf(statesQuantitys[47][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[47][2] = String.valueOf(Double.valueOf(statesQuantitys[47][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[47][3] = String.valueOf(Double.valueOf(statesQuantitys[47][3]) + items.getResults()[i].getPrice());
									statesQuantitys[47][4] = String.valueOf(Integer.valueOf(statesQuantitys[47][4]) + 1);
									break;
								case "BR-SE":
									//Sergipe
									statesQuantitys[48][1] = String.valueOf(Double.valueOf(statesQuantitys[48][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[48][2] = String.valueOf(Double.valueOf(statesQuantitys[48][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[48][3] = String.valueOf(Double.valueOf(statesQuantitys[48][3]) + items.getResults()[i].getPrice());
									statesQuantitys[48][4] = String.valueOf(Integer.valueOf(statesQuantitys[48][4]) + 1);
									break;
								case "BR-SP":
									//Sao Paulo
									statesQuantitys[49][1] = String.valueOf(Double.valueOf(statesQuantitys[49][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[49][2] = String.valueOf(Double.valueOf(statesQuantitys[49][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[49][3] = String.valueOf(Double.valueOf(statesQuantitys[49][3]) + items.getResults()[i].getPrice());
									statesQuantitys[49][4] = String.valueOf(Integer.valueOf(statesQuantitys[49][4]) + 1);
									break;
								case "BR-TO":
									//Tocantins
									statesQuantitys[50][1] = String.valueOf(Double.valueOf(statesQuantitys[50][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[50][2] = String.valueOf(Double.valueOf(statesQuantitys[50][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[50][3] = String.valueOf(Double.valueOf(statesQuantitys[50][3]) + items.getResults()[i].getPrice());
									statesQuantitys[50][4] = String.valueOf(Integer.valueOf(statesQuantitys[50][4]) + 1);
									break;
								
								//Chile
								case "CL-AI":
									//Aisén
									statesQuantitys[51][1] = String.valueOf(Double.valueOf(statesQuantitys[51][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[51][2] = String.valueOf(Double.valueOf(statesQuantitys[51][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[51][3] = String.valueOf(Double.valueOf(statesQuantitys[51][3]) + items.getResults()[i].getPrice());
									statesQuantitys[51][4] = String.valueOf(Integer.valueOf(statesQuantitys[51][4]) + 1);
									break;
								case "CL-AN":
									//Antofagasto
									statesQuantitys[52][1] = String.valueOf(Double.valueOf(statesQuantitys[52][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[52][2] = String.valueOf(Double.valueOf(statesQuantitys[52][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[52][3] = String.valueOf(Double.valueOf(statesQuantitys[52][3]) + items.getResults()[i].getPrice());
									statesQuantitys[52][4] = String.valueOf(Integer.valueOf(statesQuantitys[52][4]) + 1);
									break;
								case "CL-AP":
									//Arica y Parinanota
									statesQuantitys[53][1] = String.valueOf(Double.valueOf(statesQuantitys[53][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[53][2] = String.valueOf(Double.valueOf(statesQuantitys[53][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[53][3] = String.valueOf(Double.valueOf(statesQuantitys[53][3]) + items.getResults()[i].getPrice());
									statesQuantitys[53][4] = String.valueOf(Integer.valueOf(statesQuantitys[53][4]) + 1);
									break;
								case "CL-AT":
									//Atacama
									statesQuantitys[54][1] = String.valueOf(Double.valueOf(statesQuantitys[54][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[54][2] = String.valueOf(Double.valueOf(statesQuantitys[54][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[54][3] = String.valueOf(Double.valueOf(statesQuantitys[54][3]) + items.getResults()[i].getPrice());
									statesQuantitys[54][4] = String.valueOf(Integer.valueOf(statesQuantitys[54][4]) + 1);
									break;
								case "CL-BI":
									//Biobío
									statesQuantitys[55][1] = String.valueOf(Double.valueOf(statesQuantitys[55][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[55][2] = String.valueOf(Double.valueOf(statesQuantitys[55][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[55][3] = String.valueOf(Double.valueOf(statesQuantitys[55][3]) + items.getResults()[i].getPrice());
									statesQuantitys[55][4] = String.valueOf(Integer.valueOf(statesQuantitys[55][4]) + 1);
									break;
								case "CL-CO":
									//Coquimbo
									statesQuantitys[56][1] = String.valueOf(Double.valueOf(statesQuantitys[56][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[56][2] = String.valueOf(Double.valueOf(statesQuantitys[56][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[56][3] = String.valueOf(Double.valueOf(statesQuantitys[56][3]) + items.getResults()[i].getPrice());
									statesQuantitys[56][4] = String.valueOf(Integer.valueOf(statesQuantitys[56][4]) + 1);
									break;	
								case "CL-AR":
									//La Araucanía
									statesQuantitys[57][1] = String.valueOf(Double.valueOf(statesQuantitys[57][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[57][2] = String.valueOf(Double.valueOf(statesQuantitys[57][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[57][3] = String.valueOf(Double.valueOf(statesQuantitys[57][3]) + items.getResults()[i].getPrice());
									statesQuantitys[57][4] = String.valueOf(Integer.valueOf(statesQuantitys[57][4]) + 1);
									break;	
								case "CL-LI":
									//Libertador B. O'Higgins
									statesQuantitys[58][1] = String.valueOf(Double.valueOf(statesQuantitys[58][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[58][2] = String.valueOf(Double.valueOf(statesQuantitys[58][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[58][3] = String.valueOf(Double.valueOf(statesQuantitys[58][3]) + items.getResults()[i].getPrice());
									statesQuantitys[58][4] = String.valueOf(Integer.valueOf(statesQuantitys[58][4]) + 1);
									break;
								case "CL-LL":
									//Los Lagos
									statesQuantitys[59][1] = String.valueOf(Double.valueOf(statesQuantitys[59][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[59][2] = String.valueOf(Double.valueOf(statesQuantitys[59][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[59][3] = String.valueOf(Double.valueOf(statesQuantitys[59][3]) + items.getResults()[i].getPrice());
									statesQuantitys[59][4] = String.valueOf(Integer.valueOf(statesQuantitys[59][4]) + 1);
									break;
								case "CL-LR":
									//Los Rios
									statesQuantitys[60][1] = String.valueOf(Double.valueOf(statesQuantitys[60][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[60][2] = String.valueOf(Double.valueOf(statesQuantitys[60][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[60][3] = String.valueOf(Double.valueOf(statesQuantitys[60][3]) + items.getResults()[i].getPrice());
									statesQuantitys[60][4] = String.valueOf(Integer.valueOf(statesQuantitys[60][4]) + 1);
									break;
								case "CL-MA":
									//Magallanes
									statesQuantitys[61][1] = String.valueOf(Double.valueOf(statesQuantitys[61][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[61][2] = String.valueOf(Double.valueOf(statesQuantitys[61][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[61][3] = String.valueOf(Double.valueOf(statesQuantitys[61][3]) + items.getResults()[i].getPrice());
									statesQuantitys[61][4] = String.valueOf(Integer.valueOf(statesQuantitys[61][4]) + 1);
									break;
								case "CL-ML":
									//Maule
									statesQuantitys[62][1] = String.valueOf(Double.valueOf(statesQuantitys[62][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[62][2] = String.valueOf(Double.valueOf(statesQuantitys[62][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[62][3] = String.valueOf(Double.valueOf(statesQuantitys[62][3]) + items.getResults()[i].getPrice());
									statesQuantitys[62][4] = String.valueOf(Integer.valueOf(statesQuantitys[62][4]) + 1);
									break;
								case "CL-RM":
									//RM (Metropolitana)
									statesQuantitys[63][1] = String.valueOf(Double.valueOf(statesQuantitys[63][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[63][2] = String.valueOf(Double.valueOf(statesQuantitys[63][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[63][3] = String.valueOf(Double.valueOf(statesQuantitys[63][3]) + items.getResults()[i].getPrice());
									statesQuantitys[63][4] = String.valueOf(Integer.valueOf(statesQuantitys[63][4]) + 1);
									break;
								case "CL-TA":
									//Tarapacá
									statesQuantitys[64][1] = String.valueOf(Double.valueOf(statesQuantitys[64][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[64][2] = String.valueOf(Double.valueOf(statesQuantitys[64][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[64][3] = String.valueOf(Double.valueOf(statesQuantitys[64][3]) + items.getResults()[i].getPrice());
									statesQuantitys[64][4] = String.valueOf(Integer.valueOf(statesQuantitys[64][4]) + 1);
									break;
								case "CL-VS":
									//Valparaíso
									statesQuantitys[65][1] = String.valueOf(Double.valueOf(statesQuantitys[65][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[65][2] = String.valueOf(Double.valueOf(statesQuantitys[65][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[65][3] = String.valueOf(Double.valueOf(statesQuantitys[65][3]) + items.getResults()[i].getPrice());
									statesQuantitys[65][4] = String.valueOf(Integer.valueOf(statesQuantitys[65][4]) + 1);
									break;
								default:
									statesQuantitys[66][1] = String.valueOf(Double.valueOf(statesQuantitys[66][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[66][2] = String.valueOf(Double.valueOf(statesQuantitys[66][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[66][3] = String.valueOf(Double.valueOf(statesQuantitys[66][3]) + items.getResults()[i].getPrice());
									statesQuantitys[66][4] = String.valueOf(Integer.valueOf(statesQuantitys[66][4]) + 1);
									break;
							}
										
							//If the date is not stored, create a new one and push into the stack
							boolean isDateAlreadyStored = false;
							for(int j = 0;j<stopQuantitys.size();j++){
								if(stopQuantitys.get(j)[0].equals(items.getResults()[i].getStop_time().substring(0, 10))){
									isDateAlreadyStored = true;
								}
							}
							
							if(!isDateAlreadyStored){
								stopQuantitys.push(new String[]{(String) items.getResults()[i].getStop_time().substring(0, 10),
																	String.valueOf(items.getResults()[i].getAvailable_quantity())});
							}else{
								int j=0;
								do{
									if(stopQuantitys.get(j)[0].equals(items.getResults()[i].getStop_time().substring(0, 10))){
										stopQuantitys.get(j)[1] = String.valueOf(Integer.valueOf(stopQuantitys.get(j)[1]) + items.getResults()[i].getAvailable_quantity());
									}
									j++;
								}while(j<stopQuantitys.size());
							}
						}
						
						publicationsData.push(new String[]{items.getResults()[i].getTitle(),items.getResults()[i].getPermalink(), String.valueOf(items.getResults()[i].getPrice())});
					}
					
				}
			
				//Obtain average
				if(sumPrices == 0){
					averagePrice = 0;
				}else{
					averagePrice = sumPrices/count;
				}
				storeCalculation(childrenId, childrenName, soldQuantity, offerQuantity ,maxPrice, 
						minPrice, averagePrice, acceptsMercadoPago, noMercadoPago, statesQuantitys, stopQuantitys, 
						items.getResults().length, publicationsData);
			}
			
			public void storeCalculation(String childrenId, String childrenName, double soldQuantity, 
					double offerQuantity ,double maxPrice, double minPrice, double averagePrice,
					int acceptsMercadoPago, int noMercadoPago, String[][] statesQuantitys, LinkedList<String[]> stopQuantitys, 
					int sampleSize, LinkedList<String[]> publicationsData){
				
				//Save the calculated data
				DAORecords_MySQL daoRecords = DAORecords_MySQL.getInstance();
				daoRecords.insertRecord(childrenId, childrenName, soldQuantity, offerQuantity, averagePrice, 
						maxPrice, minPrice, acceptsMercadoPago, noMercadoPago, statesQuantitys, stopQuantitys, 
						sampleSize, publicationsData);
			}
		}
		
	}

}
