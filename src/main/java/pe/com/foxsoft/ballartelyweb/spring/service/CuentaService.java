package pe.com.foxsoft.ballartelyweb.spring.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.CuentaDao;
import pe.com.foxsoft.ballartelyweb.jpa.dao.MovimientoDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.repository.CuentaRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Service
public class CuentaService {
	
	private EntityManager em;
	
	@Autowired
	private CuentaRepository cuentaRepository;
	
	@Autowired
	private CuentaDao cuentaDao;
	
	@Autowired
	private MovimientoDao movimientoDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Account agregarCuenta(Account account) throws BallartelyException {
		return cuentaRepository.save(account);
	}
	
	public Account obtenerCuenta(int itemAccount) throws BallartelyException {
		return cuentaRepository.findOne(itemAccount);
	}
	
	public Account obtenerCuentaPrincipal() throws BallartelyException {
		return cuentaDao.getAccountPrincipalDataBase(em);
	}
	
	public List<Account> obtenerCuentas(Integer customerId) throws BallartelyException {
		return cuentaDao.getAccountsByOwnerDataBase(em, customerId);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Account editarCuenta(Account account) throws BallartelyException {
		return cuentaRepository.save(account);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void eliminarCuenta(Account account) throws BallartelyException {
		cuentaRepository.delete(account);
	}

	@Transactional(readOnly=true, noRollbackFor=BallartelyException.class)
	public List<Account> getListaCuentas() throws BallartelyException {
		return cuentaRepository.findAll();
	}
	
	@Transactional(readOnly=true, noRollbackFor=BallartelyException.class)
	public BigDecimal getAmountAccountDataBase(Integer itemAccount) throws BallartelyException {
		BigDecimal amountSales = movimientoDao.getAmountAccountDataBase(em, itemAccount, Constantes.MOVEMENT_TYPE_SALES);
		BigDecimal amountPay = movimientoDao.getAmountAccountDataBase(em, itemAccount, Constantes.MOVEMENT_TYPE_PAY);
		return amountSales.subtract(amountPay);
	}

	public CuentaRepository getCuentaRepository() {
		return cuentaRepository;
	}

	public void setCuentaRepository(CuentaRepository cuentaRepository) {
		this.cuentaRepository = cuentaRepository;
	}

	public CuentaDao getCuentaDao() {
		return cuentaDao;
	}

	public void setCuentaDao(CuentaDao cuentaDao) {
		this.cuentaDao = cuentaDao;
	}

	public MovimientoDao getMovimientoDao() {
		return movimientoDao;
	}

	public void setMovimientoDao(MovimientoDao movimientoDao) {
		this.movimientoDao = movimientoDao;
	}

}
