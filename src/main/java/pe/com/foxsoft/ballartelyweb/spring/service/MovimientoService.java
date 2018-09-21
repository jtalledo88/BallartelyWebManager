package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.MovimientoDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.repository.MovimientoRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Service
public class MovimientoService {
	
	private EntityManager em;
	
	@Autowired
	private MovimientoRepository movimientoRepository;

	@Autowired
	private MovimientoDao movimientoDao;
	
	public EntityManager getEm() {
		return em;
	}

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	@Transactional(readOnly=false, rollbackFor=BallartelyException.class)
	public Movement agregarMovimiento(Movement movement) throws BallartelyException {
		return movimientoRepository.save(movement);
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public List<Movement> getListaMovimientosCuenta(Integer accountId, Date startDate, Date endDate) throws BallartelyException {
		return movimientoDao.getMovementsByAccountDataBase(em, accountId, startDate, endDate);
	}

	public MovimientoRepository getMovimientoRepository() {
		return movimientoRepository;
	}

	public void setMovimientoRepository(MovimientoRepository movimientoRepository) {
		this.movimientoRepository = movimientoRepository;
	}

	public MovimientoDao getMovimientoDao() {
		return movimientoDao;
	}

	public void setMovimientoDao(MovimientoDao movimientoDao) {
		this.movimientoDao = movimientoDao;
	}
	
	
}
