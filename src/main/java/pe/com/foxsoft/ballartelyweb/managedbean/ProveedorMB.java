package pe.com.foxsoft.ballartelyweb.managedbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.service.ProveedorService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class ProveedorMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private Propiedades propiedades;

	private Provider objProveedorMain;
	private Provider objProveedorSearch;

	private List<Provider> lstProveedoresMain;
	private List<GeneralParameter> lstEstadosGenerales;
	private List<String> lstRucProveedorBUS;
	private List<String> lstRazSocProveedorBUS;
	private boolean validaListaBuscar = true;
	private int canRegTablaPrincipal;
	private boolean flagConfirmEliProvider = false;

	public ProveedorMB() {
		this.objProveedorMain = new Provider();
		this.objProveedorSearch = new Provider();
		this.lstProveedoresMain = new ArrayList<>();
		this.lstEstadosGenerales = new ArrayList<>();
		this.lstRucProveedorBUS = new ArrayList<>();
		this.lstRazSocProveedorBUS = new ArrayList<>();
	}

	public void buscarProveedores() {
		try {
			this.validaListaBuscar = false;
			this.lstProveedoresMain = this.proveedorService.buscarProveedores(this.objProveedorSearch);
			this.canRegTablaPrincipal = this.lstProveedoresMain.size();
		} catch (BallartelyException e) {
			String sMensaje = "Error en buscarProveedores";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void agregarProveedor() {
		String sMensaje = "";
		
		Provider objProveedor = new Provider();
		try {
			if ("".equals(this.objProveedorMain.getProviderRuc())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el RUC del proveedor.");
				return;
			}
			if ("".equals(this.objProveedorMain.getProviderSocialReason())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la razón social del proveedor.");
				return;
			}
			if ("".equals(this.objProveedorMain.getProviderAddress())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la dirección del proveedor.");
				return;
			}
			if ("".equals(this.objProveedorMain.getProviderPhoneNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de teléfono del proveedor.");
				return;
			}
			if ("".equals(this.objProveedorMain.getProviderStatus())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un estado.");
				return;
			}
			
			objProveedor.setProviderRuc(this.objProveedorMain.getProviderRuc());
			objProveedor.setProviderSocialReason(this.objProveedorMain.getProviderSocialReason());
			objProveedor.setProviderAddress(this.objProveedorMain.getProviderAddress());
			objProveedor.setProviderPhoneNumber(this.objProveedorMain.getProviderPhoneNumber());
			objProveedor.setProviderStatus(this.objProveedorMain.getProviderStatus());
			
			objProveedor = this.proveedorService.agregarProveedor(objProveedor);
			sMensaje = Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_SUCCESS, objProveedor.getProviderId());
			Utilitarios.mensaje("", sMensaje);
			setLstProveedoresMain(new ArrayList<Provider>());
			this.canRegTablaPrincipal = getListaPrincipalProveedores();
		
		} catch (BallartelyException e) {
			sMensaje = "Error en agregarProveedor";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void openAgregarProveedor() {
		this.objProveedorMain = new Provider();
		this.objProveedorMain.setProviderRuc("");
		this.objProveedorMain.setProviderSocialReason("");
		this.objProveedorMain.setProviderAddress("");
		this.objProveedorMain.setProviderPhoneNumber("");
		this.objProveedorMain.setProviderStatus("");
	}

	public void openEditarProveedor() {
		setObjProveedorMain(new Provider());

		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		int itemProveedor = Integer.parseInt((String) paramMap.get("itemProveedor"));
		try {
			this.objProveedorMain = this.proveedorService.obtenerProveedor(itemProveedor);
		} catch (BallartelyException e) {
			String sMensaje = "Error en openEditarProveedor";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void editarProveedor() {
		String sMensaje = "";
		
		try {
			if ("".equals(this.objProveedorMain.getProviderRuc())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el RUC del proveedor.");
				return;
			}
			if ("".equals(this.objProveedorMain.getProviderSocialReason())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la razón social del proveedor.");
				return;
			}
			if ("".equals(this.objProveedorMain.getProviderAddress())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la dirección del proveedor.");
				return;
			}
			if ("".equals(this.objProveedorMain.getProviderPhoneNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de teléfono del proveedor.");
				return;
			}
			if ("".equals(this.objProveedorMain.getProviderStatus())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un estado.");
				return;
			}
			
			Provider objProvider = this.proveedorService.editarProveedor(this.objProveedorMain);
			sMensaje = Utilitarios.reemplazarMensaje(Constantes.MESSAGE_MERGE_SUCCESS, objProvider.getProviderId());
			Utilitarios.mensaje("", sMensaje);
			this.canRegTablaPrincipal = getListaPrincipalProveedores();
			
		} catch (BallartelyException e) {
			sMensaje = "Error en editarProveedor";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void visibleConfirmElimProveedor() {
		this.flagConfirmEliProvider = true;
	}

	public void eliminarProveedor() {
		String sMensaje = "";
		try {
			this.proveedorService.eliminarProveedor(this.objProveedorMain);
			sMensaje = Constantes.MESSAGE_REMOVE_SUCCESS;
			setLstProveedoresMain(new ArrayList<Provider>());
			this.canRegTablaPrincipal = getListaPrincipalProveedores();
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en eliminarProveedor";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
		this.flagConfirmEliProvider = false;
	}

	public int getListaPrincipalProveedores() {
		int can = 0;
		try {
			this.lstProveedoresMain = this.proveedorService.getListaProveedores();
			can = this.lstProveedoresMain.size();
			for (Provider p : this.lstProveedoresMain) {
				if (p.getProviderSocialReason() != null) {
					this.lstRazSocProveedorBUS.add(p.getProviderSocialReason());
				}
				if (p.getProviderRuc() != null) {
					this.lstRucProveedorBUS.add(p.getProviderRuc());
				}
			}
		} catch (BallartelyException e) {
			String sMensaje = "Error en getListaPrincipalProveedores";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
		return can;
	}

	public void obtenerEstadosProveedores() {
		try {
			this.lstEstadosGenerales = this.parametroGeneralService.obtenerListaParametros(propiedades.getComboEstados());
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerEstadosProveedores";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}

	public List<String> completeRazSoc(String query) {
		List<String> results = new ArrayList<String>();
		for (String s : this.lstRazSocProveedorBUS) {
			if (Utilitarios.compararCadenas(s, query.trim())) {
				results.add(s);
			}
		}
		return results;
	}

	public List<String> completeRuc(String query) {
		List<String> results = new ArrayList<String>();
		for (String s : this.lstRucProveedorBUS) {
			if (Utilitarios.compararCadenas(s, query.trim())) {
				results.add(s);
			}
		}
		return results;
	}

	public Provider getObjProveedorMain() {
		return this.objProveedorMain;
	}

	public void setObjProveedorMain(Provider objProveedorMain) {
		this.objProveedorMain = objProveedorMain;
	}
	
	public Provider getObjProveedorSearch() {
		return objProveedorSearch;
	}

	public void setObjProveedorSearch(Provider objProveedorSearch) {
		this.objProveedorSearch = objProveedorSearch;
	}

	public List<Provider> getLstProveedoresMain() {
		if ((this.lstProveedoresMain.isEmpty()) && (this.validaListaBuscar)) {
			this.canRegTablaPrincipal = getListaPrincipalProveedores();
		}
		return this.lstProveedoresMain;
	}

	public void setLstProveedoresMain(List<Provider> lstProveedoresMain) {
		this.lstProveedoresMain = lstProveedoresMain;
	}

	public int getCanRegTablaPrincipal() {
		return this.canRegTablaPrincipal;
	}

	public void setCanRegTablaPrincipal(int canRegTablaPrincipal) {
		this.canRegTablaPrincipal = canRegTablaPrincipal;
	}

	public List<GeneralParameter> getLstEstadosGenerales() {
		obtenerEstadosProveedores();
		return this.lstEstadosGenerales;
	}

	public void setLstEstadosGenerales(List<GeneralParameter> lstEstadosGenerales) {
		this.lstEstadosGenerales = lstEstadosGenerales;
	}
	
	public boolean isFlagConfirmEliProvider() {
		return this.flagConfirmEliProvider;
	}

	public void setFlagConfirmEliProvider(boolean flagConfirmEliProvider) {
		this.flagConfirmEliProvider = flagConfirmEliProvider;
	}

	public List<String> getLstRucProveedorBUS() {
		return lstRucProveedorBUS;
	}

	public void setLstRucProveedorBUS(List<String> lstRucProveedorBUS) {
		this.lstRucProveedorBUS = lstRucProveedorBUS;
	}

	public List<String> getLstRazSocProveedorBUS() {
		return lstRazSocProveedorBUS;
	}

	public void setLstRazSocProveedorBUS(List<String> lstRazSocProveedorBUS) {
		this.lstRazSocProveedorBUS = lstRazSocProveedorBUS;
	}

	public boolean isValidaListaBuscar() {
		return this.validaListaBuscar;
	}

	public void setValidaListaBuscar(boolean validaListaBuscar) {
		this.validaListaBuscar = validaListaBuscar;
	}

	public ProveedorService getProveedorService() {
		return proveedorService;
	}

	public void setProveedorService(ProveedorService proveedorService) {
		this.proveedorService = proveedorService;
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
	
}
