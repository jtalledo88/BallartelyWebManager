package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.ProveedorDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.jpa.repository.ProveedorRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Service
public class ProveedorService {
	
	private EntityManager em;
	
	@Autowired
	private ProveedorRepository proveedorRepository;
	
	@Autowired
	private ProveedorDao proveedorDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	public List<Provider> buscarProveedores(Provider provider) throws BallartelyException {		
		return proveedorDao.getProvidersDataBase(em, provider);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Provider agregarProveedor(Provider provider) throws BallartelyException {
		return proveedorRepository.save(provider);
	}
	
	public Provider obtenerProveedor(int itemProvider) throws BallartelyException {
		return proveedorRepository.findOne(itemProvider);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Provider editarProveedor(Provider provider) throws BallartelyException {
		return proveedorRepository.save(provider);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void eliminarProveedor(Provider provider) throws BallartelyException {
		proveedorRepository.delete(provider);
	}
	
	public List<Provider> getListaProveedores() throws BallartelyException {
		return proveedorRepository.findAll();
	}

	public ProveedorRepository getProveedorRepository() {
		return proveedorRepository;
	}

	public void setProveedorRepository(ProveedorRepository proveedorRepository) {
		this.proveedorRepository = proveedorRepository;
	}

	public ProveedorDao getProveedorDao() {
		return proveedorDao;
	}

	public void setProveedorDao(ProveedorDao proveedorDao) {
		this.proveedorDao = proveedorDao;
	}

}
