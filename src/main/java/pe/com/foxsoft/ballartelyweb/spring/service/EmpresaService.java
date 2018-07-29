package pe.com.foxsoft.ballartelyweb.spring.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.EmpresaDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.Enterprise;
import pe.com.foxsoft.ballartelyweb.jpa.repository.EmpresaRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Service
public class EmpresaService {
	
	
	private EntityManager em;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private EmpresaDao empresaDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public EmpresaRepository getEmpresaRepository() {
		return empresaRepository;
	}
	
	public void setEmpresaRepository(EmpresaRepository empresaRepository) {
		this.empresaRepository = empresaRepository;
	}
	
	public Enterprise getEnterprise(Enterprise user) throws BallartelyException {
		return empresaDao.getEnterpriseDataBase(em, user);
	}
	
	public Enterprise getEnterprise(String login) throws BallartelyException {
		return empresaDao.getEnterpriseDataBase(em, login);
	}
	
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Enterprise editarEmpresa(Enterprise enterprise) throws BallartelyException {
		return empresaRepository.save(enterprise);
	}

	public EmpresaDao getEmpresaDao() {
		return empresaDao;
	}

	public void setEmpresaDao(EmpresaDao empresaDao) {
		this.empresaDao = empresaDao;
	}

}
