package consultingPackage;

import org.json.simple.JSONArray;

public class Configurations {
	
	private static Configurations instance;
	private String frequency;
	private int itemsQuantity;
	private String hourSchedule;
	private JSONArray categoriesAllowed;
	
	private Configurations(){
		
	}

	public static Configurations getInstance() {

		if (instance == null) {
			instance = new Configurations();
		}

		return instance;
		
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public int getItemsQuantity() {
		return itemsQuantity;
	}

	public void setItemsQuantity(int itemsQuantity) {
		this.itemsQuantity = itemsQuantity;
	}

	public String getHourSchedule() {
		return hourSchedule;
	}

	public void setHourSchedule(String hourSchedule) {
		this.hourSchedule = hourSchedule;
	}
	
	public  JSONArray getCategoriesAllowed() {
		return categoriesAllowed;
	}

	public void setCategoriesAllowed(JSONArray categories) {
		this.categoriesAllowed = categories;
	}
	
	
}
