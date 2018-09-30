package pe.com.foxsoft.ballartelyweb.managedbean;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.FacesException;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.data.Customer;
import pe.com.foxsoft.ballartelyweb.jpa.data.Enterprise;
import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetailSales;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.Transport;
import pe.com.foxsoft.ballartelyweb.spring.domain.ProductGuide;
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
	private GuideDetailSales objItemGuiaEliminar;
	private Account objCuentaCliente;
	private InputStream isGuide;
	private boolean flagCargaProductGuide;
	private BigDecimal amountAccount;

	private List<GuideDetailSales> lstItemsGuideMain;
	private List<Customer> lstClientes;
	private List<Transport> lstTransports;
	private List<ProductLabel> lstProducts;
	private List<GuideHead> lstGuidesBuy;
	private TreeNode rootProductGuide;
	private List<ProductGuide> lstProductGuideStock;
	private List<Account> lstAccountsCustomer;

	public RegistroVentaMB() {
		this.objGuideHeadMain = new GuideHead();
		this.lstItemsGuideMain = new ArrayList<>();
		this.lstTransports = new ArrayList<>();
		this.lstProducts = new ArrayList<>();
		this.lstGuidesBuy = new ArrayList<>();
		this.rootProductGuide = new DefaultTreeNode();
		this.lstAccountsCustomer = new ArrayList<>();
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

	private TreeNode createRootProductGuide() throws BallartelyException {
		obtenerProductos();
		List<ProductGuide> lstProductGuide = this.guiaService.getListaProductoGuia();
		TreeNode root = new DefaultTreeNode(new ProductGuide(), null);
		for(ProductLabel productLabel: this.lstProducts) {
			TreeNode nodeProduct = new DefaultTreeNode(new ProductGuide(productLabel.getId(), productLabel.getProductLabelDescription()),  root);
			List<ProductGuide> lstProductGuideFilt = getProductGuideList(lstProductGuide, productLabel.getId());
			for(ProductGuide productGuide: lstProductGuideFilt) {
				new DefaultTreeNode(productGuide, nodeProduct);
			}
		}
		return root;
	}
	
	private List<ProductGuide> getProductGuideList(List<ProductGuide> lstProductGuide, Integer productLabelId) {
		List<ProductGuide> lstProductGuideResult = new ArrayList<>();
		for(ProductGuide productGuide: lstProductGuide) {
			if(productGuide.getId() == productLabelId) {
				lstProductGuideResult.add(productGuide);
			}
		}
		return lstProductGuideResult;
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
			if("".equals(objGuideHeadMain.getReason())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un motivo.");
				return;
			}
			if(objGuideHeadMain.getCustomer() == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar el cliente.");
				return;
			}
			if(objGuideHeadMain.getTransport() == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar el transportista.");
				return;
			}
			if(isGuide == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe subir la guía digitalizada.");
				return;
			}
			if(lstItemsGuideMain.isEmpty()) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar items.");
				return;
			}
			
			objGuideHeadMain.setGuideType(Constantes.GUIDE_TYPE_SALES);
			
			Movement movement = new Movement();
			movement.setAccount(this.objCuentaCliente);
			movement.setMovementAmount(getMovementAmount());
			movement.setMovementObservation(Constantes.MOVEMENT_OBSERVATION_GUIDE_SALES);
			movement.setMovementQuantity(getMovementQuantity());
			movement.setMovementType(Constantes.MOVEMENT_TYPE_SALES);
			movement.setPaymentDocumentnumber(objGuideHeadMain.getGuideNumber());
			movement.setCustomer(objGuideHeadMain.getCustomer());
			
			String guideFile = Constantes.MOVEMENT_TYPE_SALES + "_" + objGuideHeadMain.getGuideNumber() + "." + objGuideHeadMain.getGuideFile();
			objGuideHeadMain.setGuideFile(guideFile);
			sMensaje = guiaService.insertarGuiaVenta(objGuideHeadMain, lstItemsGuideMain, lstProductGuideStock,movement);
			GeneralParameter generalParameterUpload = this.parametroGeneralService.obtenerParametroGeneral(propiedades.getUniqueCodeUpload());
			Utilitarios.guardarArchivo(generalParameterUpload.getParamValue(), objGuideHeadMain.getGuideFile(), isGuide);
			reiniciarFormulario();
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en registrarGuia";
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
	
	public void eliminarItemGuia() {
		this.lstItemsGuideMain.remove(this.objItemGuiaEliminar);
	}

	public void cargaPuntoLlegada(AjaxBehaviorEvent event) {
		this.amountAccount = null;
		this.objGuideHeadMain.setEndPoint(this.objGuideHeadMain.getCustomer().getCustomerAddress());
		cargaCuentasCliente();
		if(Constantes.CUSTOMER_TYPE_MOROSO.equals(this.objGuideHeadMain.getCustomer().getCustomerType())) {
			Utilitarios.mensajeInfo("Advertencia", "El cliente seleccionado tiene calificación morosa.");
		}
	}
	
	private void cargaCuentasCliente() {
		try {
			this.lstAccountsCustomer = this.cuentaService.obtenerCuentas(this.objGuideHeadMain.getCustomer().getId());
		} catch (BallartelyException e) {
			String sMensaje = "Error en cargaSaldoCuenta";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
		
	}

	public void cargaSaldoCuenta() {
		try {
			this.amountAccount = this.cuentaService.getAmountAccountDataBase(this.objCuentaCliente.getId());
		} catch (BallartelyException e) {
			String sMensaje = "Error en cargaSaldoCuenta";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
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
			if(!this.flagCargaProductGuide) {
				this.rootProductGuide = createRootProductGuide();
				this.flagCargaProductGuide = true;
			}
		} catch (BallartelyException e) {
			String sMensaje = "Error en openSeleccionarPrecioGuia";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void seleccionarPrecioGuia() {
		try {
			this.lstItemsGuideMain = new ArrayList<>();
			List<TreeNode> lstNodeProductLabel = this.rootProductGuide.getChildren();
			int idx = 0;
			lstProductGuideStock = new ArrayList<>();
			for(TreeNode nodeProductLabel: lstNodeProductLabel) {
				GuideDetailSales guideDetailSales = new GuideDetailSales();
				Integer cantidad = 0;
				BigDecimal pesoTotal = BigDecimal.ZERO;
				BigDecimal costoTotal = BigDecimal.ZERO;
				ProductGuide productLabel = (ProductGuide)nodeProductLabel.getData();
				guideDetailSales.setId(++idx);
				guideDetailSales.setProductLabel(this.etiquetaProductoService.obtenerEtiquetaProducto(productLabel.getId()));
				guideDetailSales.setGuideHead(this.objGuideHeadMain);
				List<TreeNode> lstNodeProductGuide = nodeProductLabel.getChildren();
				for(TreeNode nodeProductGuide: lstNodeProductGuide) {
					ProductGuide productGuide = (ProductGuide)nodeProductGuide.getData();
					cantidad += productGuide.getStockInput();
					BigDecimal peso = guideDetailSales.getProductLabel().getProductLabelWeight().multiply(BigDecimal.valueOf(productGuide.getStockInput()));
					pesoTotal = pesoTotal.add(peso);
					costoTotal = costoTotal.add(productGuide.getCostProduct().multiply(peso));
					if(productGuide.getStockInput() > 0) {
						lstProductGuideStock.add(productGuide);
					}
				}
				if(cantidad == 0) {
					continue;
				}
				guideDetailSales.setProductQuantity(cantidad);
				guideDetailSales.setMetricUnit(Constantes.METRIC_UNIT_KG);
				guideDetailSales.setTotalWeight(pesoTotal);
				guideDetailSales.setTotalCost(costoTotal);
				this.lstItemsGuideMain.add(guideDetailSales);
			}
			
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
		this.isGuide = null;
		this.lstItemsGuideMain = new ArrayList<>();
		this.lstProducts = new ArrayList<>();
		this.lstTransports = new ArrayList<>();
		this.rootProductGuide = new DefaultTreeNode();
		this.objItemGuiaEliminar = null;
		this.flagCargaProductGuide = false;
		this.lstAccountsCustomer = new ArrayList<>();
		this.objCuentaCliente = null;
		this.amountAccount = null;
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
			this.lstProducts = this.etiquetaProductoService.getListaEtiquetaProductosVenta();
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

	public TreeNode getRootProductGuide() {
		return rootProductGuide;
	}

	public void setRootProductGuide(TreeNode rootProductGuide) {
		this.rootProductGuide = rootProductGuide;
	}

	public GuideDetailSales getObjItemGuiaEliminar() {
		return objItemGuiaEliminar;
	}

	public void setObjItemGuiaEliminar(GuideDetailSales objItemGuiaEliminar) {
		this.objItemGuiaEliminar = objItemGuiaEliminar;
	}

	public List<Account> getLstAccountsCustomer() {
		return lstAccountsCustomer;
	}

	public void setLstAccountsCustomer(List<Account> lstAccountsCustomer) {
		this.lstAccountsCustomer = lstAccountsCustomer;
	}

	public Account getObjCuentaCliente() {
		return objCuentaCliente;
	}

	public void setObjCuentaCliente(Account objCuentaCliente) {
		this.objCuentaCliente = objCuentaCliente;
	}

	public BigDecimal getAmountAccount() {
		return amountAccount;
	}

	public void setAmountAccount(BigDecimal amountAccount) {
		this.amountAccount = amountAccount;
	}
	
}
