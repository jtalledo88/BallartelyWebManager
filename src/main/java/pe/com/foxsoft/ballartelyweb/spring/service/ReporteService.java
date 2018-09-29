package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.ReporteDao;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Service
public class ReporteService {
	
	private EntityManager em;

	@Autowired
	private ReporteDao reporteDao;
	
	public EntityManager getEm() {
		return em;
	}

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public Map<String, Object> getReporteVentasPorDia() throws BallartelyException {
		Map<String, Object> mapDate = new LinkedHashMap<>();
		mapDate.put("lunes", 0L);
		mapDate.put("martes", 0L);
		mapDate.put("miércoles", 0L);
		mapDate.put("jueves", 0L);
		mapDate.put("viernes", 0L);
		mapDate.put("sábado", 0L);
		mapDate.put("domingo", 0L);
		
		List<Object[]> lstReport = reporteDao.getSalesByDateDataBase(em);
		for(Object[] arr: lstReport) {
			String dName = Utilitarios.getDayName(arr[0]);
			mapDate.replace(dName, (Long)mapDate.get(dName) + (Long)arr[1]);
		}
		
		
		return mapDate;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public Map<String, Object> getReporteVentasPorCliente() throws BallartelyException {
		Map<String, Object> mapCustomer = new LinkedHashMap<>();
		
		List<Object[]> lstReport = reporteDao.getSalesByCustomerDataBase(em);
		for(Object[] arr: lstReport) {
			if(!mapCustomer.containsKey(arr[0])) {
				List<Object[]> lstEntry = new ArrayList<>();
				lstEntry.add(arr);
				mapCustomer.put(arr[0].toString(), lstEntry);
			}else {
				List<Object[]> lstEntry = (List<Object[]>)mapCustomer.get(arr[0]);
				lstEntry.add(arr);
				mapCustomer.replace(arr[0].toString(), lstEntry);
			}
			
		}
		
		return mapCustomer;
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public Map<String, Object> getReporteVentasPorPeriodo() throws BallartelyException {
		Map<String, Object> mapDate = new LinkedHashMap<>();
		mapDate.put("enero", 0L);
		mapDate.put("febrero", 0L);
		mapDate.put("marzo", 0L);
		mapDate.put("abril", 0L);
		mapDate.put("mayo", 0L);
		mapDate.put("junio", 0L);
		mapDate.put("julio", 0L);
		mapDate.put("agosto", 0L);
		mapDate.put("septiembre", 0L);
		mapDate.put("octubre", 0L);
		mapDate.put("noviembre", 0L);
		mapDate.put("diciembre", 0L);
		
		List<Object[]> lstReport = reporteDao.getSalesByPeriodDataBase(em);
		for(Object[] arr: lstReport) {
			String dName = Utilitarios.getMonthName((Integer)arr[0]);
			mapDate.replace(dName, (Long)arr[1]);
		}
		
		
		return mapDate;
	}

	public ReporteDao getReporteDao() {
		return reporteDao;
	}

	public void setReporteDao(ReporteDao reporteDao) {
		this.reporteDao = reporteDao;
	}
	
	
}
