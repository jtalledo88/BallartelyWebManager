package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.GuiaDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideCotization;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaCabeceraRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaCotizacionRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaDetalleRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.MovimientoRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.StockProductoRepository;
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
	public String beneficiarGuia(GuideHead guideHead, List<ProductStock> lstLabelsStockGuide) throws BallartelyException{
		guiaCabeceraRepository.save(guideHead);
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
	
	public List<GuideHead> getListaGuiasCabecera(GuideHead objGuideSearch, Date emissionDateInit, Date emissionDateEnd, 
			Date creationDateInit, Date creationDateEnd) throws BallartelyException {
		return guiaDao.getGuideHeadsDataBase(em, objGuideSearch, emissionDateInit, 
				emissionDateEnd, creationDateInit, creationDateEnd);
	}
	
	public List<GuideDetail> getListaGuiasDetalle(int guideHeadId) throws BallartelyException {
		return guiaDao.getGuideDetailsDataBase(em, guideHeadId);
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
