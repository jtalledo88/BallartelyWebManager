package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.Invoice;
import pe.com.foxsoft.ballartelyweb.jpa.repository.FacturaRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Service
public class FacturaService {
	
	private EntityManager em;
	
	@Autowired
	private FacturaRepository facturaRepository;
	
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Transactional(readOnly=false, rollbackFor=BallartelyException.class)
	public String insertarFactura(Invoice invoice) throws BallartelyException {
		/** Grabamos la cabecera a BD **/
		try {
			Invoice objFactura = facturaRepository.save(invoice);
			return Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_SUCCESS, objFactura.getId());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public List<Invoice> obtenerFacturasPorGuia(GuideHead guideHead) {
		return facturaRepository.findByGuideHead(guideHead);
	}

	public FacturaRepository getFacturaRepository() {
		return facturaRepository;
	}

	public void setFacturaRepository(FacturaRepository facturaRepository) {
		this.facturaRepository = facturaRepository;
	}
}
