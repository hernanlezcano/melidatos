package consultingPackage;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleAlgorithm {
	
	private final static int fONE_DAY = 0;
	TimerTask categoriesAlgorithm;
	Timer timer;
	
	public ScheduleAlgorithm(){
		categoriesAlgorithm = new CategoriesAlgorithm();
	    //perform the task once a day at 4 a.m., starting tomorrow morning
	    //(other styles are possible as well)
	    timer = new Timer();
	}
	
	public void startAlgorithm(){
		System.out.println("Inicio");
	    timer.scheduleAtFixedRate(categoriesAlgorithm, getSchedule(), getFrequency());
	}
	
	public void stopAlgorithm(){
		System.out.println("Se paro");
		categoriesAlgorithm.cancel();
	}
	
	public Date getSchedule(){
		Calendar tomorrow = new GregorianCalendar();
	    tomorrow.add(Calendar.DATE, fONE_DAY);
	    Calendar result = new GregorianCalendar(
	      tomorrow.get(Calendar.YEAR),
	      tomorrow.get(Calendar.MONTH),
	      tomorrow.get(Calendar.DATE),
	      getHour(),
	      getMinutes()
	    );
	    return result.getTime();
	}
	
	public int getHour(){
		
		return Integer.valueOf(Configurations.getInstance().getHourSchedule().substring(0, 2));
		
	}
	public int getMinutes(){
		
		return Integer.valueOf(Configurations.getInstance().getHourSchedule().substring(3, 5));
		
	}
	public long getFrequency(){
		if(Configurations.getInstance().getFrequency().contains("day")){
			return 1000*60*60*24;
		}else if(Configurations.getInstance().getFrequency().contains("semanal")){
			return 1000*60*60*24*7;
		}else if(Configurations.getInstance().getFrequency().contains("month")){
			return 1000*60*60*24*30;
		}
		return 0;
	}
	
}
