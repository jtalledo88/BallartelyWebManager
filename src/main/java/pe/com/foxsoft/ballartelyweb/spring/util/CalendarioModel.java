package pe.com.foxsoft.ballartelyweb.spring.util;

import java.util.Calendar;
import java.util.Date;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;

public class CalendarioModel extends LazyScheduleModel{
	
	private static final long serialVersionUID = 8544292752468037798L;

	
	@Override
	public void loadEvents(Date start, Date end) {
		Date random = getRandomDate(start);
        addEvent(new DefaultScheduleEvent("Entregar Pedido Cliente A", random, random));
         
        random = getRandomDate(start);
        addEvent(new DefaultScheduleEvent("Comprar Cliente B", random, random, "classRed"));
	}
	
	public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
    }
}	
