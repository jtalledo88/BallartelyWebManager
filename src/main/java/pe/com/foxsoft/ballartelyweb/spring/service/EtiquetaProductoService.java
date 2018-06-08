package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.EtiquetaProductoDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.repository.EtiquetaProductoRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Service
public class EtiquetaProductoService {
	
	private EntityManager em;
	
	@Autowired
	private EtiquetaProductoRepository etiquetaProductoRepository;
	
	@Autowired
	private EtiquetaProductoDao etiquetaProductoDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public EtiquetaProductoRepository getEtiquetaProductoJPA() {
		return etiquetaProductoRepository;
	}

	public void setEtiquetaProductoJPA(EtiquetaProductoRepository etiquetaProductoJPA) {
		this.etiquetaProductoRepository = etiquetaProductoJPA;
	}

	public List<ProductLabel> buscarEtiquetaProductos(ProductLabel productLabel) throws BallartelyException {		
		return etiquetaProductoDao.getProductLabelsDataBase(em, productLabel);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public String agregarEtiquetaProducto(ProductLabel productLabel) throws BallartelyException {
		return etiquetaProductoDao.persistProductLabelDataBase(em, productLabel);
	}
	
	public ProductLabel obtenerEtiquetaProducto(int itemProductLabel) throws BallartelyException {
		return etiquetaProductoRepository.findOne(itemProductLabel);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public ProductLabel editarEtiquetaProducto(ProductLabel productLabel) throws BallartelyException {
		return etiquetaProductoRepository.save(productLabel);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void eliminarEtiquetaProducto(ProductLabel productLabel) throws BallartelyException {
		etiquetaProductoRepository.delete(productLabel);
	}
	
	public List<ProductLabel> getListaEtiquetaProductos() throws BallartelyException {
		return etiquetaProductoRepository.findAll();
	}

	public EtiquetaProductoRepository getEtiquetaProductoRepository() {
		return etiquetaProductoRepository;
	}

	public void setEtiquetaProductoRepository(EtiquetaProductoRepository etiquetaProductoRepository) {
		this.etiquetaProductoRepository = etiquetaProductoRepository;
	}

	public EtiquetaProductoDao getEtiquetaProductoDao() {
		return etiquetaProductoDao;
	}

	public void setEtiquetaProductoDao(EtiquetaProductoDao etiquetaProductoDao) {
		this.etiquetaProductoDao = etiquetaProductoDao;
	}

}
