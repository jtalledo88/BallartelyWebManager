package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.EtiquetaProductoDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;
import pe.com.foxsoft.ballartelyweb.jpa.repository.EtiquetaProductoRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.StockProductoRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Service
public class EtiquetaProductoService {
	
	private EntityManager em;
	
	@Autowired
	private EtiquetaProductoRepository etiquetaProductoRepository;
	
	@Autowired 
	private StockProductoRepository stockProductoRepository;
	
	@Autowired
	private EtiquetaProductoDao etiquetaProductoDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public List<ProductLabel> buscarEtiquetaProductos(ProductLabel productLabel) throws BallartelyException {		
		return etiquetaProductoDao.getProductLabelsDataBase(em, productLabel);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public ProductLabel agregarEtiquetaProducto(ProductLabel productLabel) throws BallartelyException {
		return etiquetaProductoRepository.save(productLabel);
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
		ProductLabel productLabel = new ProductLabel();
		productLabel.setProductLabelStatus(Constantes.STATUS_ACTIVE);
		Example<ProductLabel> eProductLabel = Example.of(productLabel);
		return etiquetaProductoRepository.findAll(eProductLabel);
	}
	
	public List<ProductStock> getListaStockProductos(GuideHead guideHead) throws BallartelyException {
		ProductStock productStock = new ProductStock();
		productStock.setGuideHead(guideHead);
		Example<ProductStock> eProductStock = Example.of(productStock);
		return stockProductoRepository.findAll(eProductStock);
	}
	
	public List<ProductStock> getListaStockProductos() throws BallartelyException {
		return stockProductoRepository.findAll();
	}
	
	public List<ProductLabel> getListaEtiquetaProductosVenta() throws BallartelyException {
		return etiquetaProductoDao.getProductLabelSalesDataBase(em);
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

	public StockProductoRepository getStockProductoRepository() {
		return stockProductoRepository;
	}

	public void setStockProductoRepository(StockProductoRepository stockProductoRepository) {
		this.stockProductoRepository = stockProductoRepository;
	}

}
