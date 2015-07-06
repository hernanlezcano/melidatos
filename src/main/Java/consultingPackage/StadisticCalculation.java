package consultingPackage;

//In this class we are going to obtain the median and MAD to eliminate outliers with z-score.
//Z-score FORMULA: (abs(vecout-med)/MAD)>4
//MAD=median(abs(vecout-med))

import java.util.Arrays;

import javax.swing.*;

import wrapperPackage.WrapperItems;

public class StadisticCalculation {

	public double obtainMedian(WrapperItems wrapperItems){
		
		int length = wrapperItems.getResults().length;
		//Arrays.sort(wrapperItems.getResults());
		double [] prices = new double[length];
		
		double median = 0;
		
		for(int i=0;i<wrapperItems.getResults().length;i++){
			prices[i] = (double) wrapperItems.getResults()[i].getPrice();
		}
		
		Arrays.sort(prices);
		
		if(prices.length/2 % 1 == 0 && prices.length != 0){
			median = prices[prices.length/2];
			//System.out.println("Mediana = Cantidad impar: " + median);
		}else if(prices.length != 0){
			median = (prices[(int)prices.length/2] + prices[(int)prices.length/2])/2;
			//System.out.println("Mediana = Cantidad par: " + median);
		}
		
		return median;
	}
	
	public double obtainMAD(WrapperItems wrapperItems){
		
		
		double median = obtainMedian(wrapperItems);
		double [] absoluteValues = new double[wrapperItems.getResults().length];
		double MAD = 0;
		
		for(int i=0;i<wrapperItems.getResults().length;i++){
			absoluteValues[i] = Math.abs((wrapperItems.getResults()[i].getPrice() - median));
		}
		
		Arrays.sort(absoluteValues);
		
		if(absoluteValues.length/2 % 1 == 0 && absoluteValues.length != 0){
			MAD = absoluteValues[absoluteValues.length/2];
			//System.out.println("MAD = Cantidad impar: " + MAD);
		}else if(absoluteValues.length != 0){
			MAD = (absoluteValues[(int)absoluteValues.length/2] + absoluteValues[(int)absoluteValues.length/2])/2;
			//System.out.println("MAD = Cantidad par: " + MAD);
		}
		
		return MAD;
	}
	
}