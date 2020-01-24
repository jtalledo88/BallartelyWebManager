package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.GuiaDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideCotization;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetailSales;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaCabeceraRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaCotizacionRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaDetalleRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaDetalleVentaRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.MovimientoRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.StockProductoRepository;
import pe.com.foxsoft.ballartelyweb.spring.domain.ProductGuide;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Service
public class GuiaService {
	
	private EntityManager em;
	
	@Autowired
	private GuiaCabeceraRepository guiaCabeceraRepository;
	
	@Autowired
	private GuiaDetalleRepository guiaDetalleRepository;
	
	@Autowired
	private GuiaDetalleVentaRepository guiaDetalleVentaRepository;
	
	@Autowired 
	private MovimientoRepository movimientoRepository;
	
	@Autowired
	private StockProductoRepository stockProductoRepository;
	
	@Autowired
	private GuiaCotizacionRepository guiaCotizacionRepository;
	
	@Autowired
	private GuiaDao guiaDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Transactional(readOnly=false, rollbackFor=BallartelyException.class)
	public String insertarGuia(GuideHead guideHead, List<GuideDetail> lstGuideDetails, Movement movement) throws BallartelyException {
		/** Grabamos la cabecera a BD **/
		GuideHead objGuiaCabecera = guiaCabeceraRepository.save(guideHead);
		
		/** Registramos el detalle de la compra en BD y actualizamos el STOCK del producto registrado **/
		for(GuideDetail detail: lstGuideDetails) {
			detail.setGuideHead(objGuiaCabecera);
		}
		guiaDetalleRepository.save(lstGuideDetails);
		/** Registramos el movimiento en BD**/
		movimientoRepository.save(movement);
		return Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_SUCCESS, objGuiaCabecera.getId());
	}
	
	@Transactional(readOnly=false, rollbackFor=BallartelyException.class)
	public String insertarGuiaVenta(GuideHead guideHead, List<GuideDetailSales> lstGuideDetails, List<ProductGuide> lstProductGuideStock, Movement movement) throws BallartelyException {
		/** Grabamos la cabecera a BD **/
		GuideHead objGuiaCabecera = guiaCabeceraRepository.save(guideHead);
		
		/** Registramos el detalle de la compra en BD y actualizamos el STOCK del producto registrado **/
		for(GuideDetailSales detail: lstGuideDetails) {
			detail.setId(null);
			detail.setGuideHead(objGuiaCabecera);
		}
		/** Actualizamos el stock**/
		guiaDetalleVentaRepository.save(lstGuideDetails);
		for(ProductGuide productGuide: lstProductGuideStock) {
			guiaDao.actualizarStockGuia(em, productGuide);
		}
		/** Registramos el movimiento en BD**/
		movimientoRepository.save(movement);
		return Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_SUCCESS, objGuiaCabecera.getId());
	}
	
	@Transactional(readOnly=false, rollbackFor=BallartelyException.class)
	public String beneficiarGuia(GuideHead guideHead, List<ProductStock> lstLabelsStockGuide) throws BallartelyException{
		if(guideHead != null) {
			guiaCabeceraRepository.save(guideHead);
		}
		List<ProductStock> lstResult = stockProductoRepository.save(lstLabelsStockGuide);
		return Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_LIST_SUCCESS, lstResult.size(), lstLabelsStockGuide.size());
	}
	
	@Transactional(readOnly=false, rollbackFor=BallartelyException.class)
	public String cotizarGuia(GuideHead guideHead, GuideCotization objGuideCotization) throws BallartelyException{
		guiaCabeceraRepository.save(guideHead);
		GuideCotization guideCotization = guiaCotizacionRepository.save(objGuideCotization);
		return Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_SUCCESS, guideCotization.getId());
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public List<GuideHead> getListaGuiasCabecera() throws BallartelyException {
		return guiaCabeceraRepository.findAll(new Sort(Sort.Direction.DESC, "guideCreationDate"));
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public List<GuideCotization> getListaGuiaCotizacion(int guideHeadId) throws BallartelyException {
		return guiaDao.getListaGuiaCotizacion(em, guideHeadId);
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public List<GuideHead> getListaGuiasCabecera(GuideHead objGuideSearch, Date emissionDateInit, Date emissionDateEnd, 
			Date creationDateInit, Date creationDateEnd) throws BallartelyException {
		return guiaDao.getGuideHeadsDataBase(em, objGuideSearch, emissionDateInit, 
				emissionDateEnd, creationDateInit, creationDateEnd);
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public List<GuideDetail> getListaGuiasDetalle(int guideHeadId) throws BallartelyException {
		return guiaDao.getGuideDetailsDataBase(em, guideHeadId);
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public List<GuideDetailSales> getListaGuiasVentaDetalle(int guideHeadId) throws BallartelyException {
		return guiaDao.getGuideSaleDetailsDataBase(em, guideHeadId);
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public List<GuideDetailSales> getListaGuiasDetalleVenta(int guideHeadId) throws BallartelyException {
		return guiaDao.getGuideDetailSalesDataBase(em, guideHeadId);
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public GuideCotization getCotization(GuideHead guideHead) throws BallartelyException {
		GuideCotization guideCotization = new GuideCotization();
		guideCotization.setGuideHead(guideHead);
		Example<GuideCotization> eGuideCotization = Example.of(guideCotization);
		return guiaCotizacionRepository.findOne(eGuideCotization);
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public List<ProductGuide> getListaProductoGuia() throws BallartelyException {
		return guiaDao.getListaProductoGuia(em);
	}
	
	@Transactional(readOnly=true, rollbackFor=BallartelyException.class)
	public boolean validarGuiaRetorno(Integer guideHeadId) throws BallartelyException {
		Integer guideQuantity = guiaDao.getGuideQuantityDataBase(em, guideHeadId);
		Integer guideStockQuantity = guiaDao.getStockGuideQuantityDataBase(em, guideHeadId);
		return !(guideQuantity.compareTo(guideStockQuantity) == 0);
	}

	public GuiaCabeceraRepository getGuiaCabeceraRepository() {
		return guiaCabeceraRepository;
	}

	public void setGuiaCabeceraRepository(GuiaCabeceraRepository guiaCabeceraRepository) {
		this.guiaCabeceraRepository = guiaCabeceraRepository;
	}

	public GuiaDetalleRepository getGuiaDetalleRepository() {
		return guiaDetalleRepository;
	}

	public void setGuiaDetalleRepository(GuiaDetalleRepository guiaDetalleRepository) {
		this.guiaDetalleRepository = guiaDetalleRepository;
	}

	public GuiaDetalleVentaRepository getGuiaDetalleVentaRepository() {
		return guiaDetalleVentaRepository;
	}

	public void setGuiaDetalleVentaRepository(GuiaDetalleVentaRepository guiaDetalleVentaRepository) {
		this.guiaDetalleVentaRepository = guiaDetalleVentaRepository;
	}

	public MovimientoRepository getMovimientoRepository() {
		return movimientoRepository;
	}

	public void setMovimientoRepository(MovimientoRepository movimientoRepository) {
		this.movimientoRepository = movimientoRepository;
	}
	
	public GuiaDao getGuiaDao() {
		return guiaDao;
	}

	public void setGuiaDao(GuiaDao guiaDao) {
		this.guiaDao = guiaDao;
	}

	public StockProductoRepository getStockProductoRepository() {
		return stockProductoRepository;
	}

	public void setStockProductoRepository(StockProductoRepository stockProductoRepository) {
		this.stockProductoRepository = stockProductoRepository;
	}

	public GuiaCotizacionRepository getGuiaCotizacionRepository() {
		return guiaCotizacionRepository;
	}

	public void setGuiaCotizacionRepository(GuiaCotizacionRepository guiaCotizacionRepository) {
		this.guiaCotizacionRepository = guiaCotizacionRepository;
	}

}
