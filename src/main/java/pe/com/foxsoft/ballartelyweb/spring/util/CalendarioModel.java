package pe.com.foxsoft.ballartelyweb.spring.util;

import java.util.Date;
import java.util.List;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.MovimientoService;

public class CalendarioModel extends LazyScheduleModel{
	
	private static final long serialVersionUID = 8544292752468037798L;
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private MovimientoService movimientoService;
	
	public CalendarioModel(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}

	@Override
	public void loadEvents(Date start, Date end) {
		List<Movement> lstMovements = getListMovements(start, end);
		if(lstMovements != null) {
			for(Movement mov: lstMovements) {
				if(Constantes.MOVEMENT_TYPE_SALES.equals(mov.getMovementType())) {
					addEvent(new DefaultScheduleEvent(Constantes.MOVEMENT_OBSERVATION_GUIDE_SALES
							+ "Cliente: " + mov.getCustomer().getCustomerNames(), mov.getMovementDate(), mov.getMovementDate()));
				}else if(Constantes.MOVEMENT_TYPE_PAY.equals(mov.getMovementType()) && mov.getCustomer() != null) {
					addEvent(new DefaultScheduleEvent(Constantes.MOVEMENT_OBSERVATION_PAY
							+ "Cliente: " + mov.getCustomer().getCustomerNames(), mov.getMovementDate(), mov.getMovementDate()));
				}
			}
		}
	}
	
	public List<Movement> getListMovements(Date start, Date end) {
        try {
			List<Movement> lstMovements = this.movimientoService.getListaMovimientosCuenta(null, start, end);
			return lstMovements;
		} catch (BallartelyException e) {
			logger.error(e.getMessage(), e);
		}
        return null; 
    }
}	
