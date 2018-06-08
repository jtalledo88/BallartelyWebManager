package pe.com.foxsoft.ballartelyweb.managedbean;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingHead;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.CompraService;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.service.EtiquetaProductoService;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.service.ProveedorService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class CompraMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CompraService compraService;
	
	@Autowired
	private CuentaService cuentaService;
	
	@Autowired
	private ProveedorService proveedorService;
	
	@Autowired
	private EtiquetaProductoService etiquetaProductoService;

	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private Propiedades propiedades;

	private ShippingHead objShippingHeadMain;
	private InputStream isComprobantePago;

	private List<ShippingDetail> lstItemsCompraMain;
	private List<ProductLabel> lstEtiquetasProducto;
	private List<Provider> lstProveedores;
	
	
	private BigDecimal totalCompraBruto;
	private BigDecimal igvGeneral;
	private BigDecimal totalCompraNeto;
	private int shippingTotalQuantityLive;
	
	private boolean flagMuertos;

	public CompraMB() {
		this.objShippingHeadMain = new ShippingHead();
		this.lstItemsCompraMain = new ArrayList<>();
		this.totalCompraBruto = new BigDecimal(0);
		this.totalCompraNeto = new BigDecimal(0);
	}

	public void agregarItemCompra() {
		if(this.shippingTotalQuantityLive >= this.objShippingHeadMain.getShippingTotalQuantityLive()) {
			Utilitarios.mensajeError("Campos Obligatorios", "Cantidad beneciada total igual a la cantidad total viva.");
			return;
		}
		ShippingDetail detail = new ShippingDetail();
		detail.setShippingDetailId(lstItemsCompraMain.size() + 1);
		detail.setShippingAmout(new BigDecimal(0));
		detail.setShippingQuantityBenefit(0);
		detail.setShippingUnitPrice(new BigDecimal(0));
		lstItemsCompraMain.add(detail);
	}

	public void registrarCompra() {
		String sMensaje = "";
		
		try {
			if(objShippingHeadMain.getProvider() == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el proveedor.");
				return;
			}
			if("".equals(objShippingHeadMain.getPaymentDocumentNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el número de comprobante de pago.");
				return;
			}
			if(isComprobantePago == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe subir un archivo.");
				return;
			}
			if(lstItemsCompraMain.size() == 0) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar items para la compra.");
				return;
			}
			if(totalCompraBruto.compareTo(BigDecimal.ZERO) <= 0) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar un saldo para la compra mayor a 0.");
				return;
			}
			
			objShippingHeadMain.setShippingStatus(Constantes.STATUS_PRODUCT_FRESH);
			
			Movement movement = new Movement();
			movement.setAccount(cuentaService.obtenerCuentaPrincipal());
			movement.setMovementAmount(objShippingHeadMain.getShippingTotalAmount());
			movement.setMovementObservation(Constantes.MOVEMENT_OBSERVATION_SHIPPING);
			movement.setMovementQuantity(objShippingHeadMain.getShippingTotalQuantityLive());
			movement.setMovementType(Constantes.MOVEMENT_TYPE_SHIPPING);
			movement.setPaymentDocumentnumber(objShippingHeadMain.getPaymentDocumentNumber());
			movement.setProvider(objShippingHeadMain.getProvider());
			
			sMensaje = compraService.insertarCompra(objShippingHeadMain, lstItemsCompraMain, movement);
			GeneralParameter generalParameterUpload = this.parametroGeneralService.obtenerParametroGeneral(propiedades.getUniqueCodeUpload());
			Utilitarios.guardarArchivo(generalParameterUpload.getParamValue(), objShippingHeadMain.getShippingPaymentFile(), isComprobantePago);
			reiniciarFormulario();
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en agregarCliente";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void editarItem(RowEditEvent event) {
		ShippingDetail detNew = (ShippingDetail)event.getObject();
		
		if(detNew.getShippingQuantityBenefit() == 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar una cantidad beneficiada mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if(detNew.getShippingUnitPrice().compareTo(BigDecimal.ZERO) == 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar un precio unitario mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		
		if((detNew.getShippingQuantityBenefit() + shippingTotalQuantityLive) > this.objShippingHeadMain.getShippingTotalQuantityLive()) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar una cantidad beneficiada menor a la cantidad total viva.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		
		this.totalCompraBruto = new BigDecimal(0);
		this.totalCompraNeto = new BigDecimal(0);
		this.shippingTotalQuantityLive = 0;
		int totalCantidadCompra = 0;
		for(int i=0; i<lstItemsCompraMain.size(); i++) {
			ShippingDetail detOld = lstItemsCompraMain.get(i);
			if(detNew.getShippingDetailId() == detOld.getShippingDetailId()) {
				detOld.setProductLabel(detNew.getProductLabel());
				detOld.setShippingAmout(detNew.getShippingUnitPrice().multiply(new BigDecimal(detNew.getShippingQuantityBenefit())));
				detOld.setShippingQuantityBenefit(detNew.getShippingQuantityBenefit());
				detOld.setShippingUnitPrice(detNew.getShippingUnitPrice());
			}
			totalCompraBruto = totalCompraBruto.add(detOld.getShippingAmout());
			totalCantidadCompra += detOld.getShippingQuantityBenefit();
			this.shippingTotalQuantityLive += detOld.getShippingQuantityBenefit();
		}
		
		totalCompraNeto = totalCompraBruto.add(totalCompraBruto.multiply(igvGeneral));
		
		objShippingHeadMain.setShippingTotalQuantityLive(totalCantidadCompra);
		objShippingHeadMain.setShippingTotalAmount(totalCompraNeto);
	}
	
	public void eliminarItem(RowEditEvent event) {
		ShippingDetail detRemove = (ShippingDetail)event.getObject();
		this.lstItemsCompraMain.remove(detRemove);
		this.totalCompraBruto = new BigDecimal(0);
		this.totalCompraNeto = new BigDecimal(0);
		this.shippingTotalQuantityLive = 0;
		int totalCantidadCompra = 0;
		for(int i=0; i<lstItemsCompraMain.size(); i++) {
			ShippingDetail detOld = lstItemsCompraMain.get(i);
			this.totalCompraBruto = totalCompraBruto.add(detOld.getShippingAmout());
			totalCantidadCompra += detOld.getShippingQuantityBenefit();
			this.shippingTotalQuantityLive += detOld.getShippingQuantityBenefit();
		}
		
		this.totalCompraNeto = totalCompraBruto.add(totalCompraBruto.multiply(igvGeneral));
		
		this.objShippingHeadMain.setShippingTotalQuantityLive(totalCantidadCompra);
		this.objShippingHeadMain.setShippingTotalAmount(totalCompraNeto);
    }

	public List<Provider> completeProvider(String query) {
        List<Provider> lstProveedoresFiltro = new ArrayList<Provider>();
        obtenerProveedores();
        for (int i = 0; i < lstProveedores.size(); i++) {
        	Provider provider = lstProveedores.get(i);
            if(provider.getProviderSocialReason().toLowerCase().indexOf(query.toLowerCase()) != -1) {
            	lstProveedoresFiltro.add(provider);
            }
        }
         
        return lstProveedoresFiltro;
    }
	
	public void subirComprobante(FileUploadEvent event) {
		String sMensaje = null;
        try {
        	objShippingHeadMain.setShippingPaymentFile(event.getFile().getFileName());
			this.isComprobantePago = event.getFile().getInputstream();
			Utilitarios.mensaje("", "Archivo " + event.getFile().getFileName() + " subido con exito.");
		} catch (IOException e) {
			sMensaje = "Error en subirComprobante";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
    }
	
	private void reiniciarFormulario() {
		this.objShippingHeadMain = new ShippingHead();
		this.isComprobantePago = null;
		this.lstItemsCompraMain = new ArrayList<>();
		this.totalCompraBruto = BigDecimal.ZERO;
		this.totalCompraNeto = BigDecimal.ZERO;
		this.shippingTotalQuantityLive = 0;
		this.flagMuertos = false;
	}

	private void obtenerEtiquetasProducto() {
		try {
			this.lstEtiquetasProducto = this.etiquetaProductoService.getListaEtiquetaProductos();
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerEtiquetasProducto";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}
	
	private void obtenerProveedores() {
		try {
			this.lstProveedores = this.proveedorService.getListaProveedores();
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerProveedores";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}

	private void obtenerIgvGeneral() {
		try {
			GeneralParameter generalParameter = this.parametroGeneralService.obtenerParametroGeneral(propiedades.getUniqueCodeIGV());
			this.igvGeneral = new BigDecimal(generalParameter.getParamValue());
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerIgvGeneral";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
		
	}
	
	public CompraService getCompraService() {
		return compraService;
	}

	public void setCompraService(CompraService compraService) {
		this.compraService = compraService;
	}

	public CuentaService getCuentaService() {
		return cuentaService;
	}

	public void setCuentaService(CuentaService cuentaService) {
		this.cuentaService = cuentaService;
	}

	public ProveedorService getProveedorService() {
		return proveedorService;
	}

	public void setProveedorService(ProveedorService proveedorService) {
		this.proveedorService = proveedorService;
	}
	
	public EtiquetaProductoService getEtiquetaProductoService() {
		return etiquetaProductoService;
	}

	public void setEtiquetaProductoService(EtiquetaProductoService etiquetaProductoService) {
		this.etiquetaProductoService = etiquetaProductoService;
	}

	public ParametroGeneralService getParametroGeneralService() {
		return parametroGeneralService;
	}

	public void setParametroGeneralService(ParametroGeneralService parametroGeneralService) {
		this.parametroGeneralService = parametroGeneralService;
	}

	public Propiedades getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Propiedades propiedades) {
		this.propiedades = propiedades;
	}

	public ShippingHead getObjShippingHeadMain() {
		return objShippingHeadMain;
	}

	public void setObjShippingHeadMain(ShippingHead objShippingHeadMain) {
		this.objShippingHeadMain = objShippingHeadMain;
	}

	public List<ShippingDetail> getLstItemsCompraMain() {
		return lstItemsCompraMain;
	}

	public void setLstItemsCompraMain(List<ShippingDetail> lstItemsCompraMain) {
		this.lstItemsCompraMain = lstItemsCompraMain;
	}

	public List<ProductLabel> getLstEtiquetasProducto() {
		obtenerEtiquetasProducto();
		return lstEtiquetasProducto;
	}

	public void setLstEtiquetasProducto(List<ProductLabel> lstEtiquetasProducto) {
		this.lstEtiquetasProducto = lstEtiquetasProducto;
	}

	public List<Provider> getLstProveedores() {
		return lstProveedores;
	}

	public void setLstProveedores(List<Provider> lstProveedores) {
		this.lstProveedores = lstProveedores;
	}

	public BigDecimal getTotalCompraBruto() {
		return totalCompraBruto;
	}

	public void setTotalCompraBruto(BigDecimal totalCompraBruto) {
		this.totalCompraBruto = totalCompraBruto;
	}

	public BigDecimal getIgvGeneral() {
		obtenerIgvGeneral();
		return igvGeneral;
	}

	public void setIgvGeneral(BigDecimal igvGeneral) {
		this.igvGeneral = igvGeneral;
	}

	public BigDecimal getTotalCompraNeto() {
		return totalCompraNeto;
	}

	public void setTotalCompraNeto(BigDecimal totalCompraNeto) {
		this.totalCompraNeto = totalCompraNeto;
	}
	
	public InputStream getIsComprobantePago() {
		return isComprobantePago;
	}

	public void setIsComprobantePago(InputStream isComprobantePago) {
		this.isComprobantePago = isComprobantePago;
	}

	public boolean isFlagMuertos() {
		return flagMuertos;
	}

	public void setFlagMuertos(boolean flagMuertos) {
		this.flagMuertos = flagMuertos;
	}

	public int getShippingTotalQuantityLive() {
		return shippingTotalQuantityLive;
	}

	public void setShippingTotalQuantityLive(int shippingTotalQuantityLive) {
		this.shippingTotalQuantityLive = shippingTotalQuantityLive;
	}
	
}
