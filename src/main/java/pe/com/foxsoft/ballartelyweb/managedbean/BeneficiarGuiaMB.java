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
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.EtiquetaProductoService;
import pe.com.foxsoft.ballartelyweb.spring.service.GuiaService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class BeneficiarGuiaMB {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GuiaService guiaService;
	
	@Autowired
	private EtiquetaProductoService etiquetaProductoService;
	
	
	private List<GuideHead> lstGuideHeadMain;
	private List<GuideDetail> lstGuideDetailMain;
	private List<ProductStock> lstEtiquetasStockMain;
	
	private int cantidadVivosDetalle;
	private int cantidadMuertosDetalle;
	private int cantTotalBeneficiados;
	
	private GuideHead objGuideHeadMain;

	public BeneficiarGuiaMB() {
		lstGuideHeadMain = new ArrayList<>();
		lstGuideDetailMain = new ArrayList<>();
		lstEtiquetasStockMain = new ArrayList<>();
	}
	
	@PostConstruct
    public void init() {
		//Logica despues de construir pagina
	}
	
	public void beneficiarGuia() {
		String sMensaje = null;
		try {
			if(lstEtiquetasStockMain == null || lstEtiquetasStockMain.isEmpty()) {
				Utilitarios.mensajeInfo("Error de validación", "Listado de etiquetas no disponible.");
				return;
			}
			if((this.cantidadVivosDetalle + this.cantidadMuertosDetalle) != this.cantTotalBeneficiados) {
				Utilitarios.mensajeInfo("Error de validación", "Cantidad beneficiada debe ser igual a la cantidad .");
				return;
			}
			objGuideHeadMain.setGuideBenefied(Constantes.BENEFIED_YES);
			sMensaje = guiaService.beneficiarGuia(objGuideHeadMain, lstEtiquetasStockMain);
			Utilitarios.mensaje("", sMensaje);
			reiniciarVista();
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en beneficiarGuia, intente nuevamente.";
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
	
	public void cargaTablaEtiquetas() {
		String sMensaje = null;
		try {
			if(objGuideHeadMain != null) {
				List<ProductLabel> lstEtiquetas = this.etiquetaProductoService.getListaEtiquetaProductos();
				for(ProductLabel productLabel: lstEtiquetas) {
					ProductStock productStock = new ProductStock();
					productStock.setProductLabel(productLabel);
					productStock.setGuideHead(objGuideHeadMain);
					if(Constantes.PRODUCT_CODE_MUERTOS.equals(productLabel.getProductLabelCode())) {
						productStock.setProductStockCant(cantidadMuertosDetalle);
						cantTotalBeneficiados += cantidadMuertosDetalle;
					}
					if(!existeItemListaEtiqueta(productStock)) {
						this.lstEtiquetasStockMain.add(productStock);
					}
				}
			}
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en cargaTablaEtiquetas, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}
	
	private boolean existeItemListaEtiqueta(ProductStock productStockNew) {
		for(ProductStock productStock: lstEtiquetasStockMain) {
			if(productStock.getProductLabel().getId() == productStockNew.getProductLabel().getId()) {
				return true;
			}
		}
		return false;
	}

	public void onEtiquetaCellEdit(CellEditEvent cellEditEvent) {
		int oldCant = (Integer)cellEditEvent.getOldValue();
		int newCant = (Integer)cellEditEvent.getNewValue();
		cantTotalBeneficiados += newCant;
		cantTotalBeneficiados -= oldCant;
	}

	private void obtenerGuiasHead() {
		if(objGuideHeadMain != null) {
			this.lstGuideHeadMain = new ArrayList<>();
			this.lstGuideHeadMain.add(objGuideHeadMain);
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
		cargaTablaEtiquetas();
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

	public int getCantTotalBeneficiados() {
		return cantTotalBeneficiados;
	}

	public void setCantTotalBeneficiados(int cantTotalBeneficiados) {
		this.cantTotalBeneficiados = cantTotalBeneficiados;
	}	
	
}
