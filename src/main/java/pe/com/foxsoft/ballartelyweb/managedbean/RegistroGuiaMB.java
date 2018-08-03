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

import pe.com.foxsoft.ballartelyweb.jpa.data.Enterprise;
import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.GuiaService;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.service.EmpresaService;
import pe.com.foxsoft.ballartelyweb.spring.service.EtiquetaProductoService;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.service.ProveedorService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class RegistroGuiaMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GuiaService guiaService;
	
	@Autowired
	private CuentaService cuentaService;
	
	@Autowired
	private ProveedorService proveedorService;
	
	@Autowired
	private EtiquetaProductoService etiquetaProductoService;

	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private Propiedades propiedades;

	private GuideHead objGuideHeadMain;
	private InputStream isGuide;

	private List<GuideDetail> lstItemsGuideMain;
	private List<Provider> lstProveedores;

	public RegistroGuiaMB() {
		this.objGuideHeadMain = new GuideHead();
		this.objGuideHeadMain.setProvider(new Provider());
		this.lstItemsGuideMain = new ArrayList<>();
		agregarItemGuia();
	}

	private void agregarItemGuia() {
		GuideDetail detail = new GuideDetail();
		detail.setBoxesQuantity(0);
		detail.setProductQuantity(0);
		detail.setInitialWeight(BigDecimal.ZERO);
		detail.setTaraWeight(BigDecimal.ZERO);
		detail.setNetWeight(BigDecimal.ZERO);
		detail.setAverage(BigDecimal.ZERO);
		detail.setDeadQuantity(0);
		detail.setDeadWeight(BigDecimal.ZERO);
		detail.setUnitCost(BigDecimal.ZERO);
		detail.setProductDescription(null);
		lstItemsGuideMain.add(detail);
	}
	
	private void cargarEmpresa() {
		try {
			String userName = (String)Utilitarios.getObjectInSession("userNameSes");
			Enterprise enterprise = empresaService.getEnterprise(userName);
			this.objGuideHeadMain.setEnterprise(enterprise);
			this.objGuideHeadMain.setEndPoint(enterprise.getAddress() + " " + enterprise.getDistrict());
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
			movement.setMovementAmount(lstItemsGuideMain.get(0).getUnitCost().multiply(
					lstItemsGuideMain.get(0).getNetWeight().subtract(lstItemsGuideMain.get(0).getDeadWeight())));
			movement.setMovementObservation(Constantes.MOVEMENT_OBSERVATION_GUIDE);
			movement.setMovementQuantity(lstItemsGuideMain.get(0).getProductQuantity() - lstItemsGuideMain.get(0).getDeadQuantity());
			movement.setMovementType(Constantes.MOVEMENT_TYPE_GUIDE);
			movement.setPaymentDocumentnumber(objGuideHeadMain.getGuideNumber());
			movement.setProvider(objGuideHeadMain.getProvider());
			
			sMensaje = guiaService.insertarGuia(objGuideHeadMain, lstItemsGuideMain, movement);
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
	
	public void cargaPuntoPartida(AjaxBehaviorEvent event) {
		this.objGuideHeadMain.setStartPoint(this.objGuideHeadMain.getProvider().getProviderAddress());
	}

	public void editarItem(RowEditEvent event) {
		GuideDetail detail = (GuideDetail)event.getObject();
		
		if(detail.getBoxesQuantity() == 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar una cantidad de jabas mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if(detail.getProductQuantity() == 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar una cantidad de pollos mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if(detail.getInitialWeight().compareTo(BigDecimal.ZERO) == 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el peso bruto mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if(detail.getTaraWeight().compareTo(BigDecimal.ZERO) == 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el peso de tara mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if(detail.getUnitCost().compareTo(BigDecimal.ZERO) == 0) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el precio por KG mayor a 0.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		if("".equals(detail.getProductDescription())) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar la descripción del producto.");
			FacesContext.getCurrentInstance().validationFailed();
			return;
		}
		
		lstItemsGuideMain.get(0).setBoxesQuantity(detail.getBoxesQuantity());
		lstItemsGuideMain.get(0).setProductQuantity(detail.getProductQuantity());
		lstItemsGuideMain.get(0).setInitialWeight(detail.getInitialWeight());
		lstItemsGuideMain.get(0).setTaraWeight(detail.getTaraWeight());
		lstItemsGuideMain.get(0).setNetWeight(detail.getInitialWeight().subtract(detail.getTaraWeight()));
		lstItemsGuideMain.get(0).setAverage(detail.getAverage());
		lstItemsGuideMain.get(0).setUnitCost(detail.getUnitCost());
		lstItemsGuideMain.get(0).setProductDescription(detail.getProductDescription());
		
	}
	
	public void eliminarItem(RowEditEvent event) {
		lstItemsGuideMain = new ArrayList<>();
		agregarItemGuia();
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
	
	private void obtenerProveedores() {
		try {
			this.lstProveedores = this.proveedorService.getListaProveedores();
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerProveedores";
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

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
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

	public List<GuideDetail> getLstItemsGuideMain() {
		return lstItemsGuideMain;
	}

	public void setLstItemsGuideMain(List<GuideDetail> lstItemsGuideMain) {
		this.lstItemsGuideMain = lstItemsGuideMain;
	}

	public List<Provider> getLstProveedores() {
		return lstProveedores;
	}

	public void setLstProveedores(List<Provider> lstProveedores) {
		this.lstProveedores = lstProveedores;
	}
	
	public InputStream getIsGuide() {
		return isGuide;
	}

	public void setIsGuide(InputStream isGuide) {
		this.isGuide = isGuide;
	}

}
