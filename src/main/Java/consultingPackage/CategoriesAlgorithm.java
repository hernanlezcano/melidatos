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
				String [][] statesQuantitys = new String[25][5];
				
				//Load the states
				statesQuantitys[0][0] = "Buenos Aires"; 		statesQuantitys[0][1] = "0";  	statesQuantitys[0][2] = "0";	statesQuantitys[0][3] = "0";	statesQuantitys[0][4] = "0";
				statesQuantitys[1][0] = "Cordoba"; 				statesQuantitys[1][1] = "0";  	statesQuantitys[1][2] = "0";	statesQuantitys[1][3] = "0";	statesQuantitys[1][4] = "0";
				statesQuantitys[2][0] = "Salta"; 				statesQuantitys[2][1] = "0";  	statesQuantitys[2][2] = "0";	statesQuantitys[2][3] = "0";	statesQuantitys[2][4] = "0";
				statesQuantitys[3][0] = "Neuquen";				statesQuantitys[3][1] = "0";	statesQuantitys[3][2] = "0";	statesQuantitys[3][3] = "0";	statesQuantitys[3][4] = "0";
				statesQuantitys[4][0] = "Rio Negro";			statesQuantitys[4][1] = "0";	statesQuantitys[4][2] = "0";	statesQuantitys[4][3] = "0";	statesQuantitys[4][4] = "0";
				statesQuantitys[5][0] = "Jujuy";				statesQuantitys[5][1] = "0";	statesQuantitys[5][2] = "0";	statesQuantitys[5][3] = "0";	statesQuantitys[5][4] = "0";
				statesQuantitys[6][0] = "La rioja";				statesQuantitys[6][1] = "0";	statesQuantitys[6][2] = "0";	statesQuantitys[6][3] = "0";	statesQuantitys[6][4] = "0";
				statesQuantitys[7][0] = "Tucuman";				statesQuantitys[7][1] = "0";	statesQuantitys[7][2] = "0";	statesQuantitys[7][3] = "0";	statesQuantitys[7][4] = "0";
				statesQuantitys[8][0] = "Santiago del Estero";	statesQuantitys[8][1] = "0";	statesQuantitys[8][2] = "0";	statesQuantitys[8][3] = "0";	statesQuantitys[8][4] = "0";
				statesQuantitys[9][0] = "Formosa";				statesQuantitys[9][1] = "0";	statesQuantitys[9][2] = "0";	statesQuantitys[9][3] = "0";	statesQuantitys[9][4] = "0";
				statesQuantitys[10][0] = "Santa Fe";			statesQuantitys[10][1] = "0";	statesQuantitys[10][2] = "0";	statesQuantitys[10][3] = "0";	statesQuantitys[10][4] = "0";
				statesQuantitys[11][0] = "Catamarca";			statesQuantitys[11][1] = "0";	statesQuantitys[11][2] = "0";	statesQuantitys[11][3] = "0";	statesQuantitys[11][4] = "0";
				statesQuantitys[12][0] = "La Pampa";			statesQuantitys[12][1] = "0";	statesQuantitys[12][2] = "0";	statesQuantitys[12][3] = "0";	statesQuantitys[12][4] = "0";
				statesQuantitys[13][0] = "Tierra del Fuego";	statesQuantitys[13][1] = "0";	statesQuantitys[13][2] = "0";	statesQuantitys[13][3] = "0";	statesQuantitys[13][4] = "0";
				statesQuantitys[14][0] = "Chubut";				statesQuantitys[14][1] = "0";	statesQuantitys[14][2] = "0";	statesQuantitys[14][3] = "0";	statesQuantitys[14][4] = "0";
				statesQuantitys[15][0] = "Chaco";				statesQuantitys[15][1] = "0";	statesQuantitys[15][2] = "0";	statesQuantitys[15][3] = "0";	statesQuantitys[15][4] = "0";
				statesQuantitys[16][0] = "Corrientes";			statesQuantitys[16][1] = "0";	statesQuantitys[16][2] = "0";	statesQuantitys[16][3] = "0";	statesQuantitys[16][4] = "0";
				statesQuantitys[17][0] = "Entre Rios";			statesQuantitys[17][1] = "0";	statesQuantitys[17][2] = "0";	statesQuantitys[17][3] = "0";	statesQuantitys[17][4] = "0";
				statesQuantitys[18][0] = "Mendoza";				statesQuantitys[18][1] = "0";	statesQuantitys[18][2] = "0";	statesQuantitys[18][3] = "0";	statesQuantitys[18][4] = "0";
				statesQuantitys[19][0] = "Misiones";			statesQuantitys[19][1] = "0";	statesQuantitys[19][2] = "0";	statesQuantitys[19][3] = "0";	statesQuantitys[19][4] = "0";
				statesQuantitys[20][0] = "San juan";			statesQuantitys[20][1] = "0";	statesQuantitys[20][2] = "0";	statesQuantitys[20][3] = "0";	statesQuantitys[20][4] = "0";
				statesQuantitys[21][0] = "San Luis";			statesQuantitys[21][1] = "0";	statesQuantitys[21][2] = "0";	statesQuantitys[21][3] = "0";	statesQuantitys[21][4] = "0";
				statesQuantitys[22][0] = "Santa cruz";			statesQuantitys[22][1] = "0";	statesQuantitys[22][2] = "0";	statesQuantitys[22][3] = "0";	statesQuantitys[22][4] = "0";
				statesQuantitys[23][0] = "Capital Federal";		statesQuantitys[23][1] = "0";	statesQuantitys[23][2] = "0";	statesQuantitys[23][3] = "0";	statesQuantitys[23][4] = "0";
				
				//Brasil
				statesQuantitys[24][0] = "Acre";				statesQuantitys[24][1] = "0";	statesQuantitys[24][2] = "0";	statesQuantitys[24][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[25][0] = "Alagoas"; 			statesQuantitys[25][1] = "0";	statesQuantitys[25][2] = "0";	statesQuantitys[25][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[26][0] = "Amapá";				statesQuantitys[26][1] = "0";	statesQuantitys[26][2] = "0";	statesQuantitys[26][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[27][0] = "Amazonas";			statesQuantitys[27][1] = "0";	statesQuantitys[27][2] = "0";	statesQuantitys[27][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[28][0] = "Bahia";				statesQuantitys[28][1] = "0";	statesQuantitys[28][2] = "0";	statesQuantitys[28][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[29][0] = "Ceará";				statesQuantitys[29][1] = "0";	statesQuantitys[29][2] = "0";	statesQuantitys[29][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[30][0] = "Distrito Federal";	statesQuantitys[30][1] = "0";	statesQuantitys[30][2] = "0";	statesQuantitys[30][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[31][0] = "Espirito Santo";		statesQuantitys[31][1] = "0";	statesQuantitys[31][2] = "0";	statesQuantitys[31][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[32][0] = "Goiás";				statesQuantitys[32][1] = "0";	statesQuantitys[32][2] = "0";	statesQuantitys[32][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[33][0] = "Maranhão";			statesQuantitys[33][1] = "0";	statesQuantitys[33][2] = "0";	statesQuantitys[33][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[34][0] = "Mato Grosso do Sul";	statesQuantitys[34][1] = "0";	statesQuantitys[34][2] = "0";	statesQuantitys[34][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[35][0] = "Minas Gerais";		statesQuantitys[35][1] = "0";	statesQuantitys[35][2] = "0";	statesQuantitys[35][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[36][0] = "Paraná";				statesQuantitys[36][1] = "0";	statesQuantitys[36][2] = "0";	statesQuantitys[36][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[37][0] = "Paraiba";				statesQuantitys[37][1] = "0";	statesQuantitys[37][2] = "0";	statesQuantitys[37][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[38][0] = "Pará";				statesQuantitys[38][1] = "0";	statesQuantitys[38][2] = "0";	statesQuantitys[38][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[39][0] = "Pernambuco";			statesQuantitys[39][1] = "0";	statesQuantitys[39][2] = "0";	statesQuantitys[39][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[40][0] = "Piauí";				statesQuantitys[40][1] = "0";	statesQuantitys[40][2] = "0";	statesQuantitys[40][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[41][0] = "Rio Grande do Norte";	statesQuantitys[41][1] = "0";	statesQuantitys[41][2] = "0";	statesQuantitys[41][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[42][0] = "Rio Grande do Sul";	statesQuantitys[42][1] = "0";	statesQuantitys[42][2] = "0";	statesQuantitys[42][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[43][0] = "Rio de Janeiro";		statesQuantitys[43][1] = "0";	statesQuantitys[43][2] = "0";	statesQuantitys[43][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[44][0] = "Rondônia";			statesQuantitys[44][1] = "0";	statesQuantitys[44][2] = "0";	statesQuantitys[44][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[45][0] = "Rorarima";			statesQuantitys[45][1] = "0";	statesQuantitys[45][2] = "0";	statesQuantitys[45][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[46][0] = "Santa Catarina";		statesQuantitys[46][1] = "0";	statesQuantitys[46][2] = "0";	statesQuantitys[46][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[47][0] = "Sergipe";				statesQuantitys[47][1] = "0";	statesQuantitys[47][2] = "0";	statesQuantitys[47][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[48][0] = "São Paulo";			statesQuantitys[48][1] = "0";	statesQuantitys[48][2] = "0";	statesQuantitys[48][3] = "0";	statesQuantitys[23][4] = "0";
				statesQuantitys[49][0] = "Tocantins";			statesQuantitys[49][1] = "0";	statesQuantitys[49][2] = "0";	statesQuantitys[49][3] = "0";	statesQuantitys[23][4] = "0";
				
				
				statesQuantitys[24][0] = "Otro";		        statesQuantitys[24][1] = "0";	statesQuantitys[24][2] = "0";	statesQuantitys[24][3] = "0";	statesQuantitys[24][4] = "0";
				
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
								default:
									statesQuantitys[24][1] = String.valueOf(Double.valueOf(statesQuantitys[23][1]) + items.getResults()[i].getAvailable_quantity());
									statesQuantitys[24][2] = String.valueOf(Double.valueOf(statesQuantitys[23][2]) + items.getResults()[i].getSold_quantity());
									statesQuantitys[24][3] = String.valueOf(Double.valueOf(statesQuantitys[23][3]) + items.getResults()[i].getPrice());
									statesQuantitys[24][4] = String.valueOf(Integer.valueOf(statesQuantitys[23][4]) + 1);
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
