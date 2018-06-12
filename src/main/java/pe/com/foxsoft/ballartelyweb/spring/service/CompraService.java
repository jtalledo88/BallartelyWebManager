package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.CompraDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetailLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingHead;
import pe.com.foxsoft.ballartelyweb.jpa.repository.CompraCabeceraRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.CompraDetalleLabelRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.CompraDetalleRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Service
public class CompraService {
	
	private EntityManager em;
	
	@Autowired
	private CompraCabeceraRepository compraCabeceraRepository;
	
	@Autowired
	private CompraDetalleRepository compraDetalleRepository;
	
	@Autowired
	private CompraDetalleLabelRepository compraDetalleLabelRepository;
	
	@Autowired
	private CompraDao compraDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public CompraCabeceraRepository getCompraJPA() {
		return compraCabeceraRepository;
	}

	public void setCompraJPA(CompraCabeceraRepository compraJPA) {
		this.compraCabeceraRepository = compraJPA;
	}

	@Transactional(readOnly=false, rollbackFor=BallartelyException.class)
	public String insertarCompra(ShippingHead shippinghead, List<ShippingDetail> lstShippingDetails, Movement movement) throws BallartelyException {
		return compraDao.insertShippingDataBase(em, shippinghead, lstShippingDetails, movement);
	}
	
	public List<ShippingHead> getListaComprasCabecera() throws BallartelyException {
		return compraCabeceraRepository.findAll(new Sort(Sort.Direction.DESC, "shippingCreationDate"));
	}
	
	public List<ShippingDetail> getListaComprasDetalle(int ShippingHeadId) throws BallartelyException {
		return compraDao.getShippingsDetailsDataBase(em, ShippingHeadId);
	}
		
	public List<ShippingDetailLabel> getListaComprasDetalleLabel(int ShippingDetailId) throws BallartelyException {
		return compraDao.getShippingsDetailsLabelDataBase(em, ShippingDetailId);
	}
	
	public ShippingDetailLabel getComprasDetalleLabel(int shippingDetailLabelId) throws BallartelyException {
		return compraDetalleLabelRepository.findOne(shippingDetailLabelId);
	}
	
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void eliminarCompraDetalleLabel(ShippingDetailLabel shippingDetailLabel) throws BallartelyException {
		compraDetalleLabelRepository.delete(shippingDetailLabel);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public String grabarCompraDetalleLabel(List<ShippingDetailLabel> lstEtiquetasMain) throws BallartelyException{
		return compraDao.grabarCompraDetalleLabel(em, lstEtiquetasMain);
	}

	public CompraCabeceraRepository getCompraCabeceraRepository() {
		return compraCabeceraRepository;
	}

	public void setCompraCabeceraRepository(CompraCabeceraRepository compraCabeceraRepository) {
		this.compraCabeceraRepository = compraCabeceraRepository;
	}

	public CompraDetalleRepository getCompraDetalleRepository() {
		return compraDetalleRepository;
	}

	public void setCompraDetalleRepository(CompraDetalleRepository compraDetalleRepository) {
		this.compraDetalleRepository = compraDetalleRepository;
	}

	public CompraDetalleLabelRepository getCompraDetalleLabelRepository() {
		return compraDetalleLabelRepository;
	}

	public void setCompraDetalleLabelRepository(CompraDetalleLabelRepository compraDetalleLabelRepository) {
		this.compraDetalleLabelRepository = compraDetalleLabelRepository;
	}

	public CompraDao getCompraDao() {
		return compraDao;
	}

	public void setCompraDao(CompraDao compraDao) {
		this.compraDao = compraDao;
	}

	
	
}
