package controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import userSearchProssesor.InformationKitOne;
import userSearchProssesor.InformationKitThree;
import userSearchProssesor.InformationKitTwo;
import userSearchProssesor.UserSearchPreparation;

@Controller
public class InformationController {
	
	@RequestMapping (value="index", method = RequestMethod.GET)
	public String mainContent(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("data"," ");
		
		return "IndexInterface";
	}
	@RequestMapping (value="mainpublications", method = RequestMethod.GET)
	public String mainpublications(HttpServletRequest request, HttpServletResponse response) {
		return "publications";
		
	}
	
	@RequestMapping (value="autosuggest", method = RequestMethod.POST)
	public @ResponseBody String autosuggest(HttpServletRequest request, HttpServletResponse response){
		//System.out.println("called w-> " + request.getParameter("word"));
		//System.out.println("called w2-> " + request.getParameter("test"));
		
		JSONObject obj;
		JSONArray list = new JSONArray();
		//este es el que te hace la consulta, se va a userserprepareition
		if (request.getParameter("test") == ""){
			System.out.println("entro a test");
		UserSearchPreparation prepare = UserSearchPreparation.getInstance();
		List<String[]> suggestions = prepare.getSuggestions(request.getParameter("word"), request.getParameter("pais"));
		//System.out.println(request.getParameter("test"));
				
		for (int i=0; i<suggestions.size();i++){
			obj = new JSONObject();
			obj.put("father", String.valueOf(suggestions.get(i)[0]));
			obj.put("product", String.valueOf(suggestions.get(i)[1]));
			obj.put("key", String.valueOf(suggestions.get(i)[2]));
			list.add(obj);
			
		}		
		}
		else {
			System.out.println("entro a test");
			UserSearchPreparation prepare = UserSearchPreparation.getInstance();
			List<String[]> suggestions = prepare.getSuggestionsById(request.getParameter("test"));
				
			for (int i=0; i<suggestions.size();i++){
				obj = new JSONObject();
				obj.put("product", String.valueOf(suggestions.get(i)[0]));
				obj.put("father", String.valueOf(suggestions.get(i)[1]));
				obj.put("key", String.valueOf(suggestions.get(i)[2]));
				list.add(obj);
				
			}		
			
		}
			System.out.println(list.toJSONString());
			return list.toJSONString();
	}

	@RequestMapping (value="publications", method = RequestMethod.POST)
	public @ResponseBody String publications(HttpServletRequest request, HttpServletResponse response){
		
		UserSearchPreparation prepare = UserSearchPreparation.getInstance();
		String productId = prepare.searchProductIdByDescription(request.getParameter("search"));
		System.out.println(request.getParameter("search"));
		InformationKitOne informationKitOne = new InformationKitOne(productId);
		
		List<String[]> publications = informationKitOne.getPublications();
		JSONObject obj;
		JSONArray list = new JSONArray();
		
		for (int i=0; i<publications.size();i++){
			obj = new JSONObject();
			obj.put("title", String.valueOf(publications.get(i)[0]));
			obj.put("permalink", String.valueOf(publications.get(i)[1]));
			obj.put("price", String.valueOf(publications.get(i)[2]));
			list.add(obj);
			
		}
		
		//System.out.println(list.toJSONString());
		
//		request.setAttribute("sampleSize", informationKitOne.getSampleSize());
		return list.toJSONString();
	}
	
	@RequestMapping (value="InformationRequest", method = RequestMethod.GET)
	public String informationRequest(HttpServletRequest request, HttpServletResponse response) {
		
		
		request.setAttribute("project",request.getParameter("project"));
		request.getAttribute("project-id");
		System.out.println("informationRequest"  + request.getParameter("project-id"));


		String productId = request.getParameter("project-id");
		//sSystem.out.println("aca prineto la variable productId"  + productId);
	
		if(productId == ""){
			request.setAttribute("data",request.getParameter("project") +  "- No se ha encontrado informacion");
			return "IndexInterface";
		}
		//Obtain the data of the prices kit
		InformationKitOne informationKitOne = new InformationKitOne(productId);
		//System.out.println("acaprineto infokitone" + informationKitOne);
		request.setAttribute("sampleSize", informationKitOne.getSampleSize());
		
		DecimalFormat df = new DecimalFormat("#.##");
		request.setAttribute("avg","$ " + df.format(informationKitOne.getAvgPrice()));
		request.setAttribute("min","$ " + df.format(informationKitOne.getMinPrice()));
		request.setAttribute("max","$ " + df.format(informationKitOne.getMaxPrice()));
		String historiesString = "";
		ArrayList  <String[]> histories = informationKitOne.getHistories();
		for(int i = 0;i<histories.size();i++){
			historiesString+="["+ histories.get(i)[0] + " , " + histories.get(i)[1] + " , " + histories.get(i)[2] + " , " + histories.get(i)[3]+"],";
		}
		request.setAttribute("historyPrices", historiesString);
		System.out.println("historyPrices" + historiesString);
		
		String avgStatesString = "";
		ArrayList  <String[]> averages = informationKitOne.getStatesAvgPrices();
		for(int i = 0;i<averages.size();i++){
			avgStatesString+="["+ averages.get(i)[0] + " , " + averages.get(i)[1] + "],";
		}
		request.setAttribute("avgStates", avgStatesString);
		System.out.println("avgStates " + avgStatesString);
		
		
		//Obtain the data of the offer kit
		InformationKitTwo informationKitTwo = new InformationKitTwo(productId);
		String statesOfferString = "";
		ArrayList  <String[]> statesOffer = informationKitTwo.getStatesOffer();
		
		//change this, and doit in the model
		statesOfferString+="['State','Offer quantity'],";
		for(int i = 0;i<statesOffer.size();i++){
			statesOfferString+="["+ statesOffer.get(i)[0] + " , " + statesOffer.get(i)[1] + "],";
			
		}
		
		String historyOfferString = "";
		ArrayList  <String[]> historyOffer =informationKitTwo.getHistoriesOffer();
		
		for(int i = 0;i<historyOffer.size();i++){
			historyOfferString+="["+ historyOffer.get(i)[0] + " , " + historyOffer.get(i)[1] + "],";
		}
		
		String stopQuantitysString = "";
		ArrayList  <String[]> stopQuantitys =informationKitTwo.getDateStopQuantitys();
		
		for(int i = 0;i<stopQuantitys.size();i++){
			stopQuantitysString+="["+ stopQuantitys.get(i)[0] + " , " + stopQuantitys.get(i)[1] + "],";
		}
		
		String aceptsMercadoPagoString = "";
		//ArrayList  <String[]> aceptsMercadoPago = informationKitTwo.getAceptsMercadoPago();
		
//		for(int i = 0;i<aceptsMercadoPago.size();i++){
//			aceptsMercadoPagoString+="["+ aceptsMercadoPago.get(i)[0] + " , " + aceptsMercadoPago.get(i)[1] + "],";
//		}
		
		aceptsMercadoPagoString+="['Que acepta Mercadopago:' , " + informationKitOne.getSampleSize() + "],";
		aceptsMercadoPagoString+="['Que NO acepta Mercadopago: ', 0],";
		
		
		request.setAttribute("aceptsMercadoPago", aceptsMercadoPagoString);
		request.setAttribute("historyOffers", historyOfferString);
		request.setAttribute("statesOffers", statesOfferString);
		request.setAttribute("stopQuantitys", stopQuantitysString);
		//Obtain the data of the market kit
//		InformationKitTwo informationKitTwo = new InformationKitTwo("MLA73674");
		
//		String statesOfferString = "";
		statesOfferString = "";
//		ArrayList  <String[]> statesOffer = informationKitTwo.getStatesOffer();
		String SoldAvailableString = "";
		SoldAvailableString+="['Oferta','Demanda'],";
		
		InformationKitThree informationKitThree = new InformationKitThree(productId);
		String statesSoldString = "";
		ArrayList  <String[]> statesSold = informationKitThree.getStatesSold();
		statesSoldString+="['State','Sold quantity'],";
		for(int i = 0;i<statesSold.size();i++){
			statesSoldString+="["+ statesSold.get(i)[0] + " , " + statesSold.get(i)[1] + "],";
		}
		
		String historySoldsString = "";
		ArrayList  <String[]> historySold =informationKitThree.getHistoriesSold();
		
		for(int i = 0;i<historySold.size();i++){
			historySoldsString+="["+ historySold.get(i)[0] + " , " + historySold.get(i)[1] + "],";
		}
		
		String historyOfferSoldsString = "";
		for(int i = 0;i<historySold.size();i++){
			historyOfferSoldsString+="["+ historySold.get(i)[0] + " , " + historyOffer.get(i)[1] + " , " + historySold.get(i)[1] + "],";
		}
		
		request.setAttribute("statesSold", statesSoldString);
		request.setAttribute("historySolds", historySoldsString);
		request.setAttribute("offerSolds", historyOfferSoldsString);
		
		return "PrincipalInterface";
	}
}
