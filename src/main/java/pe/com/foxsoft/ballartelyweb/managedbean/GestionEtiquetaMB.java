package pe.com.foxsoft.ballartelyweb.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetailLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingHead;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.CompraService;
import pe.com.foxsoft.ballartelyweb.spring.service.EtiquetaProductoService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class GestionEtiquetaMB {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CompraService compraService;
	
	@Autowired
	private EtiquetaProductoService etiquetaProductoService;
	
	
	private List<ShippingHead> lstComprasHeadMain;
	private List<ShippingDetail> lstComprasDetailMain;
	private List<ShippingDetailLabel> lstEtiquetasMain;
	private DualListModel<ShippingDetailLabel> lstEtiquetasSeleccionMain;
	private List<ShippingDetailLabel> lstEtiquetasAgregar;
	private List<ShippingDetailLabel> lstEtiquetasEliminar;
	
	private ShippingHead objCompraSeleccionada;
	private ShippingDetail objCompraDetalleSeleccionada;
	private ShippingDetailLabel objCompraDetalleLabelSeleccionada;
	private ShippingDetailLabel objCompraDetalleLabelEditar;

	private boolean flagInhabilitaGestEtiq;

	public GestionEtiquetaMB() {
		lstComprasHeadMain = new ArrayList<>();
		lstComprasDetailMain = new ArrayList<>();
		lstEtiquetasMain = new ArrayList<>();
		lstEtiquetasSeleccionMain = new DualListModel<>();
		objCompraDetalleLabelEditar = new ShippingDetailLabel();
		flagInhabilitaGestEtiq = true;
		objCompraSeleccionada = null;
		objCompraDetalleSeleccionada = null;
		objCompraDetalleLabelSeleccionada = null;
	}
	
	@PostConstruct
    public void init() {
		openGestionarEtiquetas();
	}
	
	public void cargaTablaDetalle() {
		String sMensaje = null;
		try {
			if(objCompraSeleccionada != null) {
				this.lstComprasDetailMain = this.compraService.getListaComprasDetalle(objCompraSeleccionada.getShippingId()); 
			}
		} catch (BallartelyException e) {
			sMensaje = "Error en cargaTablaDetalle";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void cargaTablaEtiquetas() {
		String sMensaje = null;
		try {
			if(objCompraDetalleSeleccionada != null) {
				this.lstEtiquetasMain = this.compraService.getListaComprasDetalleLabel(objCompraDetalleSeleccionada.getShippingDetailId());
				this.flagInhabilitaGestEtiq = false;
			}
		} catch (BallartelyException e) {
			sMensaje = "Error en cargaTablaEtiquetas";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void openGestionarEtiquetas() {
		String sMensaje = null;
		try {
			if(objCompraDetalleSeleccionada != null) {
				List<ShippingDetailLabel> lstEtiquetasInicio = new ArrayList<>();
				List<ShippingDetailLabel> lstEtiquetasFinal = new ArrayList<>();
				for(ShippingDetailLabel detailLabel: lstEtiquetasMain) {
					if(!Constantes.DETAIL_LABEL_TYPE_ORIGIN.equals(detailLabel.getShippingDetailLabelType())) {
						lstEtiquetasFinal.add(detailLabel);
					}
				}
				List<ProductLabel> lstEtiquetasProducto  = this.etiquetaProductoService.getListaEtiquetaProductos();
				for(ProductLabel productLabel: lstEtiquetasProducto) {
					ShippingDetailLabel detailLabel = new ShippingDetailLabel();
					detailLabel.setProductLabel(productLabel);
					detailLabel.setShippingDetail(objCompraDetalleSeleccionada);
					detailLabel.setShippingDetailLabelType(Constantes.DETAIL_LABEL_TYPE_ADDITIONAL);
					lstEtiquetasInicio.add(detailLabel);
				}
				for(ShippingDetailLabel sdl: lstEtiquetasMain) {
					for(int i=0; i<lstEtiquetasInicio.size(); i++) {
						ShippingDetailLabel ps = lstEtiquetasInicio.get(i);
						if(ps.getProductLabel().getProductLabelId() == sdl.getProductLabel().getProductLabelId()) {
							lstEtiquetasInicio.remove(ps);
							break;
						}
					}
				}
				lstEtiquetasSeleccionMain = new DualListModel<>(lstEtiquetasInicio, lstEtiquetasFinal);
			}
			
		} catch (BallartelyException e) {
			sMensaje = "Error en openGestionarEtiquetas";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void onTransfer(TransferEvent event) {
		if(event.isRemove()) {
			lstEtiquetasEliminar = new ArrayList<>();
			for(Object o: event.getItems()) {
				lstEtiquetasEliminar.add((ShippingDetailLabel)o);
			}
		}else if(event.isAdd()) {
			lstEtiquetasAgregar = new ArrayList<>();
			int i = 0;
			for(Object o: event.getItems()) {
				ShippingDetailLabel detailLabelAdd = (ShippingDetailLabel)o;
				detailLabelAdd.setShippingDetailLabelId(i--);
				detailLabelAdd.setShippingDetail(objCompraDetalleSeleccionada);
				lstEtiquetasAgregar.add(detailLabelAdd);
			}
		}
	}
	
	public void agregarEtiquetasDetalle() {
		for(ShippingDetailLabel detailLabelDel: lstEtiquetasEliminar) {
			for(int i=0; i<lstEtiquetasMain.size(); i++) {
				ShippingDetailLabel detailLabelMain = lstEtiquetasMain.get(i);
				if(detailLabelDel.getShippingDetailLabelId() == detailLabelMain.getShippingDetailLabelId()) {
					lstEtiquetasMain.remove(i);
					break;
				}
			}
		}
		
		lstEtiquetasMain.addAll(lstEtiquetasAgregar);
		
	}
	
	public void openActualizarCantidadBeneficiada() {
		objCompraDetalleLabelEditar = new ShippingDetailLabel();
		objCompraDetalleLabelEditar.setProductLabel(objCompraDetalleLabelSeleccionada.getProductLabel());
		objCompraDetalleLabelEditar.setShippingDetail(objCompraDetalleSeleccionada);
		objCompraDetalleLabelEditar.setShippingDetailLabelId(objCompraDetalleLabelSeleccionada.getShippingDetailLabelId());
		objCompraDetalleLabelEditar.setShippingDetailLabelType(objCompraDetalleLabelSeleccionada.getShippingDetailLabelType());
	}
	
	public void actualizarCantidadBeneficiada() {
		int cantBenefitDetail = objCompraDetalleSeleccionada.getShippingQuantityBenefit();
		int cantBenefitDetailLabel = 0;
		for(ShippingDetailLabel detailLabel: lstEtiquetasMain) {
			cantBenefitDetailLabel += detailLabel.getShippingDetailLabelQuantityBenefit();
		}
		cantBenefitDetailLabel += objCompraDetalleLabelEditar.getShippingDetailLabelQuantityBenefit();
		if(cantBenefitDetailLabel >= cantBenefitDetail) {
			Utilitarios.mensajeError("Campos Obligatorios", "Solo puede ingresar una cantidad totalizada no mayor a " + cantBenefitDetail + ".");
			FacesContext.getCurrentInstance().validationFailed();
		}
		
		
	}
	public void grabarEtiquetasDetalle() {
		String sMensaje = null;
		try { 
			sMensaje = this.compraService.grabarCompraDetalleLabel(lstEtiquetasMain);
			Utilitarios.mensaje("", sMensaje);
			setLstEtiquetasMain(new ArrayList<>());
			cargaTablaEtiquetas();
		} catch (BallartelyException e) {
			sMensaje = "Error en obtenerComprasHead";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	private void obtenerComprasHead() {
		String sMensaje = null;
		try {
			this.lstComprasHeadMain = this.compraService.getListaComprasCabecera();
		} catch (BallartelyException e) {
			sMensaje = "Error en obtenerComprasHead";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
		
	}

	public List<ShippingHead> getLstComprasHeadMain() {
		obtenerComprasHead();
		return lstComprasHeadMain;
	}

	public void setLstComprasHeadMain(List<ShippingHead> lstComprasHeadMain) {
		this.lstComprasHeadMain = lstComprasHeadMain;
	}

	public ShippingHead getObjCompraSeleccionada() {
		return objCompraSeleccionada;
	}

	public void setObjCompraSeleccionada(ShippingHead objCompraSeleccionada) {
		this.objCompraSeleccionada = objCompraSeleccionada;
	}
	
	public ShippingDetail getObjCompraDetalleSeleccionada() {
		return objCompraDetalleSeleccionada;
	}

	public void setObjCompraDetalleSeleccionada(ShippingDetail objCompraDetalleSeleccionada) {
		this.objCompraDetalleSeleccionada = objCompraDetalleSeleccionada;
	}

	public ShippingDetailLabel getObjCompraDetalleLabelSeleccionada() {
		return objCompraDetalleLabelSeleccionada;
	}

	public void setObjCompraDetalleLabelSeleccionada(ShippingDetailLabel objCompraDetalleLabelSeleccionada) {
		this.objCompraDetalleLabelSeleccionada = objCompraDetalleLabelSeleccionada;
	}

	public ShippingDetailLabel getObjCompraDetalleLabelEditar() {
		return objCompraDetalleLabelEditar;
	}

	public void setObjCompraDetalleLabelEditar(ShippingDetailLabel objCompraDetalleLabelEditar) {
		this.objCompraDetalleLabelEditar = objCompraDetalleLabelEditar;
	}

	public List<ShippingDetail> getLstComprasDetailMain() {
		return lstComprasDetailMain;
	}

	public void setLstComprasDetailMain(List<ShippingDetail> lstComprasDetailMain) {
		this.lstComprasDetailMain = lstComprasDetailMain;
	}

	public List<ShippingDetailLabel> getLstEtiquetasMain() {
		return lstEtiquetasMain;
	}

	public void setLstEtiquetasMain(List<ShippingDetailLabel> lstEtiquetasMain) {
		this.lstEtiquetasMain = lstEtiquetasMain;
	}

	public DualListModel<ShippingDetailLabel> getLstEtiquetasSeleccionMain() {
		return lstEtiquetasSeleccionMain;
	}

	public void setLstEtiquetasSeleccionMain(DualListModel<ShippingDetailLabel> lstEtiquetasSeleccionMain) {
		this.lstEtiquetasSeleccionMain = lstEtiquetasSeleccionMain;
	}

	public CompraService getCompraService() {
		return compraService;
	}

	public void setCompraService(CompraService compraService) {
		this.compraService = compraService;
	}

	public EtiquetaProductoService getEtiquetaProductoService() {
		return etiquetaProductoService;
	}

	public void setEtiquetaProductoService(EtiquetaProductoService etiquetaProductoService) {
		this.etiquetaProductoService = etiquetaProductoService;
	}

	public boolean isFlagInhabilitaGestEtiq() {
		return flagInhabilitaGestEtiq;
	}

	public void setFlagInhabilitaGestEtiq(boolean flagInhabilitaGestEtiq) {
		this.flagInhabilitaGestEtiq = flagInhabilitaGestEtiq;
	}	
	
}
