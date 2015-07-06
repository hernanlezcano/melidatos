package wrapperPackage;

import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//La clase se llama asi para identificar que esta es un contenedor de los datos de items 
//buscados, por ej: ipod, tv, etc..

public class WrapperItems {
	String site_id;
	String query;
	Result [] result;
	
	//El constructor va a parsear toda la data y delegar el parseo a las clases cuando sea 
	//necesario
	public WrapperItems(JSONObject itemsJsonObject){
				
		this.setSite_id(String.valueOf(itemsJsonObject.get("site_id")));
		this.setQuery((String) itemsJsonObject.get("query"));
		this.setResults((JSONArray) itemsJsonObject.get("results"));
	}
	
	public void setSite_id(String site_id){
		this.site_id = site_id;
	}
	public void setQuery(String query){
		this.query = query;
	}
	
	public void setResults(JSONArray results){
		Iterator<String> iterator = results.iterator();
		int i = 0;
		//Seteamos el tama�o del arreglo con la cantidad de resultados que tenemos.
		this.result = new Result[results.size()];
		while(iterator.hasNext()){
			
			iterator.next();
			//Instanciar un objecto results por cada result
			this.result[i] = new Result((JSONObject) results.get(i));
			i++;
		}
	}
	
	public String getSite_id(){
		return this.site_id;
	}
	public String getQuery(){
		return this.query;
	}
	
	public Result [] getResults(){
		return this.result;
	}
	
	public class Result{
		String id;
		String site_id;
		String title;
		String subtitle;
		Seller seller;
		double price;
		String currency_id;
		long available_quantity;
		long sold_quantity;
		String buying_mode;
		String listing_type_id;
		String stop_time;
		String condition;
		String permalink;
		String thumbnail;
		boolean accepts_mercadopago;
		Installments installments;
		Address address;
		Shipping shipping;
		Seller_address seller_address;
		//See attributes
		
		public Result(JSONObject results){
			
			this.setID(String.valueOf(results.get("id")));
			this.setSite_id(String.valueOf(results.get("site_id")));
			this.setTitle((String) results.get("title"));
			this.setSubTitle((String) results.get("subtitle"));
			this.setSeller((JSONObject) results.get("seller"));
			//Ver que getPrice esta sobrecargado
			try{
				this.setPrice(Double.valueOf(String.valueOf(results.get("price"))));
			} catch(java.lang.ClassCastException e){
				this.setPrice((long) Long.valueOf(String.valueOf(results.get("price"))));
			} catch(java.lang.NullPointerException e){
				//Precio a convenir, lo hacemos igual a 0
				this.setPrice((long) 0);
			} catch(java.lang.NumberFormatException e){
				//Precio a convenir, lo hacemos igual a 0
				this.setPrice((long) 0);
			}
			this.setCurrency_id((String) results.get("currency_id"));
			try{
				this.setAvailable_Quantity(Long.valueOf(String.valueOf(results.get("available_quantity"))));
			} catch(java.lang.NullPointerException e){
				this.setAvailable_Quantity((long) 0);
			} catch(java.lang.NumberFormatException e){
				this.setAvailable_Quantity((long) 0);
			}
			this.setSold_Quantity(Long.valueOf(String.valueOf(results.get("sold_quantity"))));
			this.setBuying_Mode((String) results.get("buying_mode"));
			this.setListing_type_id((String) results.get("listing_type_id"));
			this.setStop_Time((String) results.get("stop_time"));
			this.setCondition((String) results.get("condition"));
			this.setPermalink((String) results.get("permalink"));
			this.setThumbnail((String) results.get("thumbnail"));
			this.setAccepts_MercadoPago(Boolean.valueOf(String.valueOf(results.get("accepts_mercadopago"))));
			try{
				this.setInstallments((JSONObject) results.get("installments"));
			} catch(java.lang.NullPointerException e){
				this.setInstallments((JSONObject) null);
			}
			this.setAddress((JSONObject) results.get("address"));
			this.setShipping((JSONObject) results.get("shipping"));
			this.setSeller_Address((JSONObject) results.get("seller_address"));
		}
		
		private void setID(String id){
			this.id = id;
		}
		private void setSite_id(String site_id){
			this.site_id = site_id;
		}
		private void setTitle(String title){
			this.title = title;
		}
		private void setSubTitle(String subtitle){
			this.subtitle = subtitle;
		}
		private void setSeller(JSONObject seller){
			this.seller = new Seller(seller);
		}
		//getPrice sobrecargado porque el precio puede ser long o double
		private void setPrice(double price){
			this.price = price;
		}
		private void setPrice(long price){
			this.price = (double) price;
		}
		private void setCurrency_id(String currency_id){
			this.currency_id = currency_id;
		}
		private void setAvailable_Quantity(long available_quantity){
			this.available_quantity = available_quantity;
		}
		private void setSold_Quantity(long sold_quantity){
			this.sold_quantity = sold_quantity;
			
		}
		private void setBuying_Mode(String buying_mode){
			this.buying_mode = buying_mode;
		}
		private void setListing_type_id(String listing_type_id){
			this.listing_type_id = listing_type_id;
		}
		private void setStop_Time(String stop_time){
			this.stop_time = stop_time;
		}
		private void setCondition(String condition){
			this.condition = condition;
		}
		private void setPermalink(String permalink){
			this.permalink = permalink;
		}
		private void setThumbnail(String thumbnail){
			this.thumbnail = thumbnail;
		}
		private void setAccepts_MercadoPago(boolean accepts_mercadopago){
			this.accepts_mercadopago = accepts_mercadopago;
		}
		private void setInstallments(JSONObject installments){
			this.installments = new Installments(installments);
		}
		private void setAddress(JSONObject address){
			this.address = new Address(address);
		}
		private void setShipping(JSONObject shipping){
			this.shipping = new Shipping(shipping);
		}
		private void setSeller_Address(JSONObject seller_address){
			this.seller_address = new Seller_address(seller_address);
		}
		
  		public String getId() {
			return id;
		}

		public String getSite_id() {
			return site_id;
		}

		public String getTitle() {
			return title;
		}

		public String getSubtitle() {
			return subtitle;
		}

		public Seller getSeller() {
			return seller;
		}

		public double getPrice() {
			return price;
		}

		public String getCurrency_id() {
			return currency_id;
		}

		public long getAvailable_quantity() {
			return available_quantity;
		}

		public long getSold_quantity() {
			return sold_quantity;
		}

		public String getBuying_mode() {
			return buying_mode;
		}

		public String getListing_type_id() {
			return listing_type_id;
		}

		public String getStop_time() {
			return stop_time;
		}

		public String getCondition() {
			return condition;
		}

		public String getPermalink() {
			return permalink;
		}

		public String getThumbnail() {
			return thumbnail;
		}

		public boolean isAccepts_mercadopago() {
			return accepts_mercadopago;
		}

		public Installments getInstallments() {
			return installments;
		}

		public Address getAddress() {
			return address;
		}

		public Shipping getShipping() {
			return shipping;
		}

		public Seller_address getSeller_address() {
			return this.seller_address;
		}

		public class Seller{
			String id;
			String power_seller_status;
			boolean car_dealer;
			boolean real_estate_agency;
			
			public Seller(JSONObject seller){
				
				this.setID(String.valueOf(seller.get("id")));	
				this.setPower_Seller_Status((String) seller.get("power_seller_status"));
				this.setCar_Dealer(Boolean.valueOf(String.valueOf(seller.get("car_dealer"))));
				this.setReal_State_Agency(Boolean.valueOf(String.valueOf(seller.get("real_estate_agency"))));
			}
			private void setID(String id){
				this.id = id;
			}
			private void setPower_Seller_Status(String power_seller_status){
				this.power_seller_status = power_seller_status;
			}
			private void setCar_Dealer(boolean car_dealer){
				this.car_dealer = car_dealer;
			}
			private void setReal_State_Agency(boolean real_estate_agency){
				this.real_estate_agency = real_estate_agency;
			}
			public String getId() {
				return id;
			}
			public String getPower_seller_status() {
				return power_seller_status;
			}
			public boolean isCar_dealer() {
				return car_dealer;
			}
			public boolean isReal_estate_agency() {
				return real_estate_agency;
			}
			
		}
		public class Installments{
			long quantity;
			double amount;
			String currency_id;
			public Installments(JSONObject installments){
				if(installments != null){
					this.setQuantity(Long.valueOf(String.valueOf(installments.get("quantity"))));
					try{
						this.setAmount(Double.valueOf(String.valueOf(installments.get("amount"))));
					}catch(java.lang.ClassCastException e){
						this.setAmount(Long.valueOf(String.valueOf(installments.get("amount"))));
					}
					this.setCurrency_id((String) installments.get("currency_id"));
				}
				else{
					this.quantity = 0;
					this.amount = 0;
					this.currency_id = null;
				}
			}
			private void setQuantity(long quantity){
				this.quantity = quantity;
			}
			private void setAmount(double amount){
				this.amount = amount;
			}
			private void setAmount(long amount){
				this.amount = amount;
			}
			private void setCurrency_id(String currency_id){
				this.currency_id = currency_id;
			}
			
			public long getQuantity() {
				return quantity;
			}
			public double getAmount() {
				return amount;
			}
			public String getCurrency_id() {
				return currency_id;
			}

		}
		public class Address{
			String state_id;
			String state_name;
			String city_id;
			String city_name;
			public Address(JSONObject address){
				
				this.setState_id(String.valueOf(address.get("state_id")));
				this.setState_Name((String) address.get("state_name"));
				this.setCity_id(String.valueOf(address.get("city_id")));
				this.setCity_Name((String) address.get("city_name"));
				
			}
			private void setState_id(String state_id){
				this.state_id = state_id;
			}
			private void setState_Name(String state_name){
				this.state_name = state_name;
			}
			private void setCity_id(String city_id){
				this.city_id = city_id;
			}
			private void setCity_Name(String city_name){
				this.city_name = city_name;
			}
			public String getState_id() {
				return state_id;
			}
			public String getState_name() {
				return state_name;
			}
			public String getCity_id() {
				return city_id;
			}
			public String getCity_name() {
				return city_name;
			}
			
		}
		public class Shipping{
			boolean free_shipping;
			String mode;
			public Shipping(JSONObject shipping){
				this.setFree_Shipping(Boolean.valueOf(String.valueOf(shipping.get("free_shipping"))));
				this.setMode((String) shipping.get("mode"));
				
			}
			private void setFree_Shipping(boolean free_shiping){
				this.free_shipping = free_shiping;
			}
			private void setMode(String mode){
				this.mode = mode;
			}
			public boolean isFree_shipping() {
				return free_shipping;
			}
			public String getMode() {
				return mode;
			}

		}
		public class Seller_address{
			String id;
			String comment;
			String address_line;
			String zip_code;
			Country country;
			State state;
			City city;
			String latitude;
			String longitude;
			public Seller_address(JSONObject seller_address){
				
				this.setId(String.valueOf(seller_address.get("id")));
				this.setComment((String) seller_address.get("seller_address"));
				this.setAddress_line((String) seller_address.get("address_line"));
				this.setZip_Code((String) seller_address.get("zip_code"));
				this.setCountry((JSONObject) seller_address.get("country"));
				this.setState((JSONObject) seller_address.get("state"));
				this.setCity((JSONObject) seller_address.get("city"));
				try{
					this.setLatitude((String) seller_address.get("latitude"));
				} catch(java.lang.ClassCastException e){
					this.setLatitude((String) String.valueOf(seller_address.get("latitude")));
				} 
				try{
					this.setLongitude((String) seller_address.get("longitude"));
				} catch(java.lang.ClassCastException e){
					this.setLongitude((String) String.valueOf(seller_address.get("longitude")));
				} 
				
				
			}
			private void setId(String id){
				this.id = id;
			}
			private void setComment(String comment){
				this.comment = comment;
			}
			private void setAddress_line(String address_line){
				this.address_line = address_line;
			}
			private void setZip_Code(String zip_code){
				this.zip_code = zip_code;
			}
			private void setCountry(JSONObject country){
				this.country = new Country(country);
			}
			private void setState(JSONObject state){
				this.state = new State(state);
			}
			private void setCity(JSONObject city){
				this.city = new City(city);
			}
			private void setLatitude(String latitude){
				this.latitude = latitude;
			}
			private void setLongitude(String longitude){
				this.longitude = longitude;
			}
			public String getId() {
				return id;
			}
			public String getComment() {
				return comment;
			}
			public String getAddress_line() {
				return address_line;
			}
			public String getZip_code() {
				return zip_code;
			}
			public Country getCountry() {
				return country;
			}
			public State getState() {
				return state;
			}
			public City getCity() {
				return city;
			}
			public String getLatitude() {
				return latitude;
			}
			public String getLongitude() {
				return longitude;
			}

			public class Country{
				String id;
				String name;
				public Country(JSONObject country){
					this.setId(String.valueOf(country.get("id")));
					this.setName((String) country.get("name"));
				}
				private void setId(String id){
					this.id = id;
				}
				private void setName(String name){
					this.name = name;
				}
				
				public String getId() {
					return id;
				}
				public String getName() {
					return name;
				}
				
				
			}
			public class State{
				String id;
				String name;
				public State(JSONObject state){
					this.setId(String.valueOf(state.get("id")));
					this.setName((String) state.get("name"));
				}
				private void setId(String id){
					this.id = id;
				}
				private void setName(String name){
					this.name = name;
				}
				
				public String getId() {
					return id;
				}
				public String getName() {
					return name;
				}
				
				
			}
			public class City{
				String id;
				String name;
				public City(JSONObject city){
					this.setId(String.valueOf(city.get("id")));
					this.setName((String) city.get("name"));
				}
				private void setId(String id){
					this.id = id;
				}
				private void setName(String name){
					this.name = name;
				}
				
				public String getId() {
					return id;
				}
				public String getName() {
					return name;
				}
				
				
			}
		}
		
	}
}

	

