package pe.com.foxsoft.ballartelyweb.managedbean;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.Customer;
import pe.com.foxsoft.ballartelyweb.jpa.data.Enterprise;
import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideCotization;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetailSales;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.jpa.data.Transport;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ClienteService;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.service.EmpresaService;
import pe.com.foxsoft.ballartelyweb.spring.service.EtiquetaProductoService;
import pe.com.foxsoft.ballartelyweb.spring.service.GuiaService;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.service.TransporteService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class RegistroVentaMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GuiaService guiaService;
	
	@Autowired
	private CuentaService cuentaService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EtiquetaProductoService etiquetaProductoService;

	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private TransporteService transporteService;
	
	@Autowired
	private Propiedades propiedades;

	private GuideHead objGuideHeadMain;
	private GuideHead objGuiaCompraSeleccion;
	private InputStream isGuide;
	private BigDecimal precioUnitarioGuia;

	private List<GuideDetailSales> lstItemsGuideMain;
	private List<Customer> lstClientes;
	private List<Transport> lstTransports;
	private List<ProductLabel> lstProducts;
	private List<GuideHead> lstGuidesBuy;

	public RegistroVentaMB() {
		this.objGuideHeadMain = new GuideHead();
		this.objGuideHeadMain.setProvider(new Provider());
		this.lstItemsGuideMain = new ArrayList<>();
		this.lstTransports = new ArrayList<>();
		this.lstProducts = new ArrayList<>();
		this.lstGuidesBuy = new ArrayList<>();
		agregarItemGuia();
	}

	public void agregarItemGuia() {
		GuideDetailSales detail = new GuideDetailSales();
		detail.setId(getItemId());
		detail.setMetricUnit(Constantes.METRIC_UNIT_KG);
		detail.setProductQuantity(0);
		detail.setTotalCost(BigDecimal.ZERO);
		detail.setTotalWeight(BigDecimal.ZERO);
		
		lstItemsGuideMain.add(detail);
	}
	
	private Integer getItemId() {
		if(lstItemsGuideMain.size() == 0) {
			return 1;
		}
		Integer current = lstItemsGuideMain.get(lstItemsGuideMain.size() - 1).getId();
		return current + 1;
	}

	private void cargarEmpresa() {
		try {
			String userName = (String)Utilitarios.getObjectInSession("userNameSes");
			Enterprise enterprise = empresaService.getEnterprise(userName);
			this.objGuideHeadMain.setEnterprise(enterprise);
			this.objGuideHeadMain.setStartPoint(enterprise.getAddress() + " " + enterprise.getDistrict());
			this.lstTransports = this.transporteService.getListaTransportesEmpresa(enterprise.getId());
		} catch (BallartelyException e) {
			this.logger.error(e.getMessage());
		}
	}

	public void registrarGuia() {
		String sMensaje = "";
		
		try {
			if("".equals(objGuideHeadMain.getGuideNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el número de guía.");
				return;
			}
			if(objGuideHeadMain.getEmissionDate() == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar la fecha de la guía.");
				return;
			}
			if("".equals(objGuideHeadMain.getStartPoint())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el punto de partida.");
				return;
			}
			if("".equals(objGuideHeadMain.getEndPoint())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el punto de llegada.");
				return;
			}
			if("".equals(objGuideHeadMain.getInvoiceNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el número de factura.");
				return;
			}
			if("".equals(objGuideHeadMain.getReason())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un motivo.");
				return;
			}
			if(objGuideHeadMain.getProvider() == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el proveedor.");
				return;
			}
			
			if(isGuide == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe subir la guía digitalizada.");
				return;
			}
			
			
			objGuideHeadMain.setGuideStatus(Constantes.STATUS_PRODUCT_FRESH);
			objGuideHeadMain.setGuideBenefied(Constantes.BENEFIED_NO);
			objGuideHeadMain.setGuideCotized(Constantes.COTIZED_NO);
			
			Movement movement = new Movement();
			movement.setAccount(cuentaService.obtenerCuentaPrincipal());
			movement.setMovementAmount(getMovementAmount());
			movement.setMovementObservation(Constantes.MOVEMENT_OBSERVATION_GUIDE);
			movement.setMovementQuantity(getMovementQuantity());
			movement.setMovementType(Constantes.MOVEMENT_TYPE_GUIDE);
			movement.setPaymentDocumentnumber(objGuideHeadMain.getGuideNumber());
			movement.setProvider(objGuideHeadMain.getProvider());
			
			sMensaje = guiaService.insertarGuiaVenta(objGuideHeadMain, lstItemsGuideMain, movement);
			GeneralParameter generalParameterUpload = this.parametroGeneralService.obtenerParametroGeneral(propiedades.getUniqueCodeUpload());
			String guideFile = Constantes.MOVEMENT_TYPE_GUIDE + "_" + objGuideHeadMain.getGuideNumber() + "." + objGuideHeadMain.getGuideFile();
			objGuideHeadMain.setGuideFile(guideFile);
			Utilitarios.guardarArchivo(generalParameterUpload.getParamValue(), objGuideHeadMain.getGuideFile(), isGuide);
			reiniciarFormulario();
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en agregarCliente";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	private Integer getMovementQuantity() {
		Integer quantity = 0;
		for(GuideDetailSales detail: lstItemsGuideMain) {
			quantity += detail.getProductQuantity();
		}
		return quantity;
	}

	private BigDecimal getMovementAmount() {
		BigDecimal amount = BigDecimal.ZERO;
		for(GuideDetailSales detail: lstItemsGuideMain) {
			amount = amount.add(detail.getTotalCost());
		}
		return amount;
	}

	public void cargaPuntoLlegada(AjaxBehaviorEvent event) {
		this.objGuideHeadMain.setEndPoint(this.objGuideHeadMain.getCustomer().getCustomerAddress());
	}

	public void editarItem(RowEditEvent event) {
		GuideDetailSales detail = (GuideDetailSales)event.getObject();
		
		if(this.precioUnitarioGuia == null || this.precioUnitarioGuia.compareTo(BigDecimal.ZERO) <= 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el costo de guía mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if(detail.getProductLabel() == null) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un producto.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if(!validarEtiquetaProducto(detail)) {
			Utilitarios.mensajeError("Campos Obligatorios", "Producto ya seleccionado.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
	
		if(detail.getProductQuantity() == 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar una cantidad de pollos mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if(detail.getTotalWeight() == null || detail.getTotalWeight().compareTo(BigDecimal.ZERO) <= 0) {
			BigDecimal productWeight = detail.getProductLabel().getProductLabelWeight();
			Integer quantity = detail.getProductQuantity();
			detail.setTotalWeight(productWeight.multiply(BigDecimal.valueOf(quantity)));
		}
		if(detail.getTotalCost() == null || detail.getTotalCost().compareTo(BigDecimal.ZERO) <= 0) {
			BigDecimal totalWeight = detail.getTotalWeight();
			detail.setTotalCost(totalWeight.multiply(this.precioUnitarioGuia));
		}
		
		
	}
	
	private boolean validarEtiquetaProducto(GuideDetailSales detail) {
		int occurs = 0;
		for(GuideDetailSales det: lstItemsGuideMain) {
			if(det.getProductLabel().getId() == detail.getProductLabel().getId()) {
				occurs++;
			}
		}
		return occurs <= 1;
	}

	public void eliminarItem(RowEditEvent event) {
		GuideDetailSales detail = (GuideDetailSales)event.getObject();
		this.lstItemsGuideMain.remove(detail);
    }

	public List<Customer> completeCustomer(String query) {
        List<Customer> lstClientesFiltro = new ArrayList<Customer>();
        obtenerClientes();
        for (int i = 0; i < lstClientes.size(); i++) {
        	Customer customer = lstClientes.get(i);
            if(customer.getCustomerNames().toLowerCase().indexOf(query.toLowerCase()) != -1) {
            	lstClientesFiltro.add(customer);
            }
        }
         
        return lstClientesFiltro;
    }
	
	public void openSeleccionarPrecioGuia() {
		try {
			this.objGuiaCompraSeleccion = null;
			this.lstGuidesBuy = this.guiaService.getListaGuiasCabecera();
		} catch (BallartelyException e) {
			String sMensaje = "Error en openSeleccionarPrecioGuia";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void seleccionarPrecioGuia() {
		try {
			GuideCotization guideCotization = this.guiaService.getCotization(this.objGuiaCompraSeleccion);
			this.precioUnitarioGuia = guideCotization.getTotalUnitCost();
		} catch (BallartelyException e) {
			String sMensaje = "Error en seleccionarPrecioGuia";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void subirComprobante(FileUploadEvent event) {
		String sMensaje = null;
        try {
        	objGuideHeadMain.setGuideFile(Utilitarios.obtenerExtension(event.getFile().getFileName()));
			this.isGuide = event.getFile().getInputstream();
			Utilitarios.mensaje("", "Archivo " + event.getFile().getFileName() + " subido con exito.");
		} catch (IOException e) {
			sMensaje = "Error en subirComprobante";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
    }
	
	private void reiniciarFormulario() {
		this.objGuideHeadMain = new GuideHead();
		this.objGuideHeadMain.setProvider(new Provider());
		this.isGuide = null;
		this.lstItemsGuideMain = new ArrayList<>();
		agregarItemGuia();
	}
	
	private void obtenerClientes() {
		try {
			this.lstClientes = this.clienteService.getListaClientes();
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerClientes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}
	
	private void obtenerProductos() {
		try {
			this.lstProducts = this.etiquetaProductoService.getListaEtiquetaProductos();
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerProductos";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}
	
	public GuiaService getGuiaService() {
		return guiaService;
	}

	public void setGuiaService(GuiaService guiaService) {
		this.guiaService = guiaService;
	}

	public CuentaService getCuentaService() {
		return cuentaService;
	}

	public void setCuentaService(CuentaService cuentaService) {
		this.cuentaService = cuentaService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
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

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	public TransporteService getTransporteService() {
		return transporteService;
	}

	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}
	public Propiedades getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Propiedades propiedades) {
		this.propiedades = propiedades;
	}

	public GuideHead getObjGuideHeadMain() {
		cargarEmpresa();
		return objGuideHeadMain;
	}

	public void setObjGuideHeadMain(GuideHead objGuideHeadMain) {
		cargarEmpresa();
		this.objGuideHeadMain = objGuideHeadMain;
	}

	public List<GuideDetailSales> getLstItemsGuideMain() {
		return lstItemsGuideMain;
	}

	public void setLstItemsGuideMain(List<GuideDetailSales> lstItemsGuideMain) {
		this.lstItemsGuideMain = lstItemsGuideMain;
	}

	public List<Customer> getLstClientes() {
		return lstClientes;
	}

	public void setLstClientes(List<Customer> lstClientes) {
		this.lstClientes = lstClientes;
	}
	
	public InputStream getIsGuide() {
		return isGuide;
	}

	public void setIsGuide(InputStream isGuide) {
		this.isGuide = isGuide;
	}

	public List<Transport> getLstTransports() {
		return lstTransports;
	}

	public void setLstTransports(List<Transport> lstTransports) {
		this.lstTransports = lstTransports;
	}

	public List<ProductLabel> getLstProducts() {
		obtenerProductos();
		return lstProducts;
	}

	public void setLstProducts(List<ProductLabel> lstProducts) {
		this.lstProducts = lstProducts;
	}

	public List<GuideHead> getLstGuidesBuy() {
		return lstGuidesBuy;
	}

	public void setLstGuidesBuy(List<GuideHead> lstGuidesBuy) {
		this.lstGuidesBuy = lstGuidesBuy;
	}

	public GuideHead getObjGuiaCompraSeleccion() {
		return objGuiaCompraSeleccion;
	}

	public void setObjGuiaCompraSeleccion(GuideHead objGuiaCompraSeleccion) {
		this.objGuiaCompraSeleccion = objGuiaCompraSeleccion;
	}

	public BigDecimal getPrecioUnitarioGuia() {
		return precioUnitarioGuia;
	}

	public void setPrecioUnitarioGuia(BigDecimal precioUnitarioGuia) {
		this.precioUnitarioGuia = precioUnitarioGuia;
	}
	
}
