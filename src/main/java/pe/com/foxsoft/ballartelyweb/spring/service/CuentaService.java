package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.CuentaDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.repository.CuentaRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Service
public class CuentaService {
	
	private EntityManager em;
	
	@Autowired
	private CuentaRepository cuentaRepository;
	
	private CuentaDao cuentaDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public CuentaRepository getCuentaJPA() {
		return cuentaRepository;
	}

	public void setCuentaJPA(CuentaRepository cuentaJPA) {
		this.cuentaRepository = cuentaJPA;
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
	
	public List<Account> obtenerCuentas(Account account) throws BallartelyException {
		return cuentaDao.getAccountsByOwnerDataBase(em, account);
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

}
