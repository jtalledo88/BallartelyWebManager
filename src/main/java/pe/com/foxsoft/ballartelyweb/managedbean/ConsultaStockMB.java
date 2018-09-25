package pe.com.foxsoft.ballartelyweb.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.EtiquetaProductoService;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class ConsultaStockMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EtiquetaProductoService etiquetaProductoService;
	
	@Autowired
	private Propiedades propiedades;
	
	private List<ProductStock> lstStock;
	
	private Integer totalStock;
	private Integer canRegTablaPrincipal;

	public ConsultaStockMB() {
		lstStock = new ArrayList<>();
	}

	private void cargarStock() {
		try {
			this.lstStock = this.etiquetaProductoService.getListaStockProductos();
			this.canRegTablaPrincipal = 0;
			for(ProductStock p : lstStock) {
				this.canRegTablaPrincipal += p.getProductStockCant();
	        }
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en cargarStock, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}
	
	public void calcularTotal(Object o) {
	    this.totalStock = 0;
	    if(o instanceof Integer) {
	    	Integer guideId = (Integer)o;
	    	for(ProductStock p : lstStock) {
	    		if(p.getGuideHead().getId() == guideId) {
	    			totalStock += p.getProductStockCant();
	    		}
	        }
	    }
        
	}
	
	public List<ProductStock> getLstStock() {
		cargarStock();
		return lstStock;
	}

	public void setLstStock(List<ProductStock> lstStock) {
		this.lstStock = lstStock;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public Integer getCanRegTablaPrincipal() {
		return canRegTablaPrincipal;
	}

	public void setCanRegTablaPrincipal(Integer canRegTablaPrincipal) {
		this.canRegTablaPrincipal = canRegTablaPrincipal;
	}

	public EtiquetaProductoService getEtiquetaProductoService() {
		return etiquetaProductoService;
	}

	public void setEtiquetaProductoService(EtiquetaProductoService etiquetaProductoService) {
		this.etiquetaProductoService = etiquetaProductoService;
	}

	public Propiedades getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Propiedades propiedades) {
		this.propiedades = propiedades;
	}
	
}
