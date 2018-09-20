package pe.com.foxsoft.ballartelyweb.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

import org.primefaces.event.CellEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.EtiquetaProductoService;
import pe.com.foxsoft.ballartelyweb.spring.service.GuiaService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class RegistroDevolucionMB {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GuiaService guiaService;
	
	@Autowired
	private EtiquetaProductoService etiquetaProductoService;
	
	@Autowired
	private Propiedades propiedades;
	
	private List<GuideHead> lstGuideHeadMain;
	private List<GuideDetail> lstGuideDetailMain;
	private List<ProductStock> lstEtiquetasStockMain;
	
	private int cantidadVivosDetalle;
	private int cantidadMuertosDetalle;
	private int cantTotalBeneficiados;
	
	private GuideHead objGuideHeadMain;

	private boolean flagReturn;
	private boolean flagEdit;

	public RegistroDevolucionMB() {
		lstGuideHeadMain = new ArrayList<>();
		lstGuideDetailMain = new ArrayList<>();
		lstEtiquetasStockMain = new ArrayList<>();
	}
	
	@PostConstruct
    public void init() {
		
	}
	
	public void registrarDevolucion() {
		String sMensaje = null;
		try {
			if(lstEtiquetasStockMain == null || lstEtiquetasStockMain.isEmpty()) {
				Utilitarios.mensajeInfo("Error de validación", "Listado de etiquetas no disponible.");
				return;
			}
			
			if((this.cantidadVivosDetalle + this.cantidadMuertosDetalle) < this.cantTotalBeneficiados) {
				Utilitarios.mensajeInfo("Error de validación", "Cantidad de stock debe ser menor a la cantidad total.");
				return;
			}
			sMensaje = guiaService.beneficiarGuia(null, lstEtiquetasStockMain);
			Utilitarios.mensaje("", sMensaje);
			reiniciarVista();
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en registrarDevolucion, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}
	


	private void reiniciarVista() {
		this.cantidadMuertosDetalle = 0;
		this.cantidadVivosDetalle = 0;
		this.lstEtiquetasStockMain = new ArrayList<>();
		this.lstGuideDetailMain = new ArrayList<>();
		this.lstGuideHeadMain = new ArrayList<>();
		this.objGuideHeadMain = null;
		flagEdit = false;
		flagReturn = false;
	}

	private void cargaTablaDetalle() {
		String sMensaje = null;
		try {
			int cantVivos = 0;
			int cantMuertos = 0;
			if(objGuideHeadMain != null) {
				this.lstGuideDetailMain = this.guiaService.getListaGuiasDetalle(objGuideHeadMain.getId());
				if(lstGuideDetailMain != null) {
					for(GuideDetail gDetail: lstGuideDetailMain) {
						cantVivos += gDetail.getProductQuantity();
						cantMuertos += gDetail.getDeadQuantity();
					}
				}
				this.cantidadVivosDetalle = cantVivos - cantMuertos;
				this.cantidadMuertosDetalle = cantMuertos;
			}
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en cargaTablaDetalle, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	private void cargaTablaEtiquetas() {
		String sMensaje = null;
		try {
			if(objGuideHeadMain != null) {
				lstEtiquetasStockMain = this.etiquetaProductoService.getListaStockProductos(objGuideHeadMain);
				for(ProductStock productStock: lstEtiquetasStockMain) {
					if(Constantes.PRODUCT_CODE_MUERTOS.equals(productStock.getProductLabel().getProductLabelCode())) {
						productStock.setProductStockCant(cantidadMuertosDetalle);
						cantTotalBeneficiados += cantidadMuertosDetalle;
					}
				}
			}
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en cargaTablaEtiquetas, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	public void onEtiquetaCellEdit(CellEditEvent cellEditEvent) {
		int oldCant = (Integer)cellEditEvent.getOldValue();
		int newCant = (Integer)cellEditEvent.getNewValue();
		cantTotalBeneficiados += newCant;
		cantTotalBeneficiados -= oldCant;
		flagEdit = true;
	}

	private void obtenerGuiasHead() {
		if(objGuideHeadMain != null) {
			this.lstGuideHeadMain = new ArrayList<>();
			this.lstGuideHeadMain.add(objGuideHeadMain);
		}
	}
	
	private void generaFlagReturn() {
		try {
			this.flagReturn = this.guiaService.validarGuiaRetorno(this.objGuideHeadMain.getId());
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrio un error en generaFlagReturn, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
		
	}

	public List<GuideHead> getLstGuideHeadMain() {
		obtenerGuiasHead();
		return lstGuideHeadMain;
	}

	public void setLstGuideHeadMain(List<GuideHead> lstGuideHeadMain) {
		this.lstGuideHeadMain = lstGuideHeadMain;
	}

	public List<GuideDetail> getLstGuideDetailMain() {
		cargaTablaDetalle();
		return lstGuideDetailMain;
	}

	public void setLstGuideDetailMain(List<GuideDetail> lstGuideDetailMain) {
		this.lstGuideDetailMain = lstGuideDetailMain;
	}

	public List<ProductStock> getLstEtiquetasStockMain() {
		if(!flagEdit) {
			cargaTablaEtiquetas();
		}
		return lstEtiquetasStockMain;
	}

	public void setLstEtiquetasStockMain(List<ProductStock> lstEtiquetasStockMain) {
		this.lstEtiquetasStockMain = lstEtiquetasStockMain;
	}

	public GuiaService getGuiaService() {
		return guiaService;
	}

	public void setGuiaService(GuiaService guiaService) {
		this.guiaService = guiaService;
	}
	
	public EtiquetaProductoService getEtiquetaProductoService() {
		return etiquetaProductoService;
	}

	public void setEtiquetaProductoService(EtiquetaProductoService etiquetaProductoService) {
		this.etiquetaProductoService = etiquetaProductoService;
	}

	public GuideHead getObjGuideHeadMain() {
		return objGuideHeadMain;
	}

	public void setObjGuideHeadMain(GuideHead objGuideHeadMain) {
		this.objGuideHeadMain = objGuideHeadMain;
	}

	public int getCantidadVivosDetalle() {
		return cantidadVivosDetalle;
	}

	public void setCantidadVivosDetalle(int cantidadVivosDetalle) {
		this.cantidadVivosDetalle = cantidadVivosDetalle;
	}

	public boolean isFlagReturn() {
		if(this.objGuideHeadMain != null) {
			generaFlagReturn();
		}
		return flagReturn;
	}

	public void setFlagCotized(boolean flagReturn) {
		this.flagReturn = flagReturn;
	}

	public Propiedades getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Propiedades propiedades) {
		this.propiedades = propiedades;
	}
	
	public int getCantTotalBeneficiados() {
		return cantTotalBeneficiados;
	}

	public void setCantTotalBeneficiados(int cantTotalBeneficiados) {
		this.cantTotalBeneficiados = cantTotalBeneficiados;
	}	
	
}
