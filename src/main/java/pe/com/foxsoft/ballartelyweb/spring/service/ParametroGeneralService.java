package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.ParametroGeneralDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.jpa.repository.ParametroGeneralRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Service
public class ParametroGeneralService {
	
	private EntityManager em;
	
	@Autowired
	private ParametroGeneralRepository parametroGeneralRepository;
	
	@Autowired
	private ParametroGeneralDao parametroGeneralDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public ParametroGeneralRepository getParametroGeneralJPA() {
		return parametroGeneralRepository;
	}

	public void setParametroGeneralJPA(ParametroGeneralRepository parametroGeneralJPA) {
		this.parametroGeneralRepository = parametroGeneralJPA;
	}
	
	public List<GeneralParameter> obtenerListaParametros(String tblCode) throws BallartelyException {
		return parametroGeneralDao.getGeneralParametersDataBase(em, tblCode);
	}
	
	public List<GeneralParameter> buscarParametrosGenerales(GeneralParameter generalParameter) throws BallartelyException {
		return parametroGeneralDao.getGeneralParametersDataBase(em, generalParameter);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public GeneralParameter agregarParametroGeneral(GeneralParameter generalParameter) throws BallartelyException {
		return parametroGeneralRepository.save(generalParameter);
	}
	
	public GeneralParameter obtenerParametroGeneral(int itemGeneralParameter) throws BallartelyException {
		return parametroGeneralRepository.findOne(itemGeneralParameter);
	}
	
	public GeneralParameter obtenerParametroGeneral(String codeGeneralParameter) throws BallartelyException {
		return parametroGeneralDao.getGeneralParameterDataBase(em, codeGeneralParameter);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public GeneralParameter editarParametroGeneral(GeneralParameter generalParameter) throws BallartelyException {
		return parametroGeneralRepository.save(generalParameter);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void eliminarParametroGeneral(GeneralParameter generalParameter) throws BallartelyException {
		parametroGeneralRepository.delete(generalParameter);
	}
	
	public List<GeneralParameter> getListaParametrosGenerales() throws BallartelyException {
		return parametroGeneralRepository.findAll();
	}

	public ParametroGeneralRepository getParametroGeneralRepository() {
		return parametroGeneralRepository;
	}

	public void setParametroGeneralRepository(ParametroGeneralRepository parametroGeneralRepository) {
		this.parametroGeneralRepository = parametroGeneralRepository;
	}

	public ParametroGeneralDao getParametroGeneralDao() {
		return parametroGeneralDao;
	}

	public void setParametroGeneralDao(ParametroGeneralDao parametroGeneralDao) {
		this.parametroGeneralDao = parametroGeneralDao;
	}

}
