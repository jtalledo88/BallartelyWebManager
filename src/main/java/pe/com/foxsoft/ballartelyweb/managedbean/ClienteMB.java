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

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.data.Client;
import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ClienteService;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class ClienteMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private CuentaService cuentaService;

	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private Propiedades propiedades;

	private Client objClienteMain;
	private Client objClienteSearch;

	private List<Client> lstClientesMain;
	private List<GeneralParameter> lstEstadosGenerales;
	private List<GeneralParameter> lstTiposDocGenerales;
	private List<GeneralParameter> lstTiposCliGenerales;
	private List<String> lstNumDocClienteBUS;
	private List<String> lstNomClienteBUS;
	private boolean validaListaBuscar = true;
	private int canRegTablaPrincipal;
	private boolean flagConfirmEliClient = false;

	public ClienteMB() {
		this.objClienteMain = new Client();
		this.objClienteSearch = new Client();
		this.lstClientesMain = new ArrayList<>();
		this.lstEstadosGenerales = new ArrayList<>();
		this.lstTiposDocGenerales = new ArrayList<>();
		this.lstTiposCliGenerales = new ArrayList<>();
		this.lstNumDocClienteBUS = new ArrayList<>();
		this.lstNomClienteBUS = new ArrayList<>();
	}

	public void buscarClientes() {
		try {
			this.validaListaBuscar = false;
			this.lstClientesMain = this.clienteService.buscarClientes(this.objClienteSearch);
			this.canRegTablaPrincipal = this.lstClientesMain.size();
		} catch (BallartelyException e) {
			String sMensaje = "Error en buscarClientes";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void agregarCliente() {
		String sMensaje = "";
		
		Client objCliente = new Client();
		Account objAccount = new Account();
		try {
			if ("".equals(this.objClienteMain.getDocumentType())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un tipo de documento.");
				return;
			}
			if ("".equals(this.objClienteMain.getDocumentNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de documento del cliente.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientNames())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el nombre o razón social del cliente.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientAddress())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la dirección del cliente.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientPhoneNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de teléfono del cliente.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientStatus())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un estado.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientType())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un tipo de cliente.");
				return;
			}
			
			
			objCliente.setDocumentType(this.objClienteMain.getDocumentType());
			objCliente.setDocumentNumber(this.objClienteMain.getDocumentNumber());
			objCliente.setClientNames(this.objClienteMain.getClientNames());
			objCliente.setClientAddress(this.objClienteMain.getClientAddress());
			objCliente.setClientPhoneNumber(this.objClienteMain.getClientPhoneNumber());
			objCliente.setClientStatus(this.objClienteMain.getClientStatus());
			objCliente.setClientType(this.objClienteMain.getClientType());
			
			objCliente = this.clienteService.agregarCliente(objCliente);
			
			objAccount.setAccountType(Constantes.ACCOUNT_TYPE_C);
			objAccount.setAccountStatus(Constantes.STATUS_ACTIVE);
			objAccount.setClient(objCliente);
			this.cuentaService.agregarCuenta(objAccount);
			sMensaje = String.format(Constantes.MESSAGE_PERSIST_SUCCESS, objCliente.getClientId());
			Utilitarios.mensaje("", sMensaje);
			setLstClientesMain(new ArrayList<Client>());
			this.canRegTablaPrincipal = getListaPrincipalClientes();
		
		} catch (BallartelyException e) {
			sMensaje = "Error en agregarCliente";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void openAgregarCliente() {
		this.objClienteMain = new Client();
		this.objClienteMain.setDocumentType("");
		this.objClienteMain.setDocumentNumber("");
		this.objClienteMain.setClientNames("");
		this.objClienteMain.setClientAddress("");
		this.objClienteMain.setClientPhoneNumber("");
		this.objClienteMain.setClientType("");
		this.objClienteMain.setClientStatus("");
	}

	public void openEditarCliente() {
		setObjClienteMain(new Client());

		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		int itemCliente = Integer.parseInt((String) paramMap.get("itemCliente"));
		try {
			this.objClienteMain = this.clienteService.obtenerCliente(itemCliente);
		} catch (BallartelyException e) {
			String sMensaje = "Error en openEditarCliente";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void editarCliente() {
		String sMensaje = "";
		
		try {
			if ("".equals(this.objClienteMain.getDocumentType())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un tipo de documento.");
				return;
			}
			if ("".equals(this.objClienteMain.getDocumentNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de documento del cliente.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientNames())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el nombre o razón social del cliente.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientAddress())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la dirección del cliente.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientPhoneNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de teléfono del cliente.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientStatus())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un estado.");
				return;
			}
			if ("".equals(this.objClienteMain.getClientType())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un tipo de cliente.");
				return;
			}
			
			Client objClienteAct = this.clienteService.editarCliente(this.objClienteMain);
			sMensaje = String.format(Constantes.MESSAGE_MERGE_SUCCESS, objClienteAct.getClientId());
			Utilitarios.mensaje("", sMensaje);
			this.canRegTablaPrincipal = getListaPrincipalClientes();
			
		} catch (BallartelyException e) {
			sMensaje = "Error en editarCliente";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void visibleConfirmElimCliente() {
		this.flagConfirmEliClient = true;
	}

	public void eliminarCliente() {
		String sMensaje = "";
		try {
			this.clienteService.eliminarCliente(this.objClienteMain);
			sMensaje = Constantes.MESSAGE_REMOVE_SUCCESS;
			setLstClientesMain(new ArrayList<Client>());
			this.canRegTablaPrincipal = getListaPrincipalClientes();
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en eliminarCliente";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
		this.flagConfirmEliClient = false;
	}

	public int getListaPrincipalClientes() {
		int can = 0;
		try {
			this.lstClientesMain = this.clienteService.getListaClientes();
			can = this.lstClientesMain.size();
			for (Client c : this.lstClientesMain) {
				if (c.getClientNames() != null) {
					this.lstNomClienteBUS.add(c.getClientNames());
				}
				if (c.getDocumentNumber() != null) {
					this.lstNumDocClienteBUS.add(c.getDocumentNumber());
				}
			}
		} catch (BallartelyException e) {
			String sMensaje = "Error en getListaPrincipalClientes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
		return can;
	}

	public void obtenerEstadosClientes() {
		try {
			this.lstEstadosGenerales = this.parametroGeneralService.obtenerListaParametros(propiedades.getComboEstados());
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerEstadosClientes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void obtenerTiposDocumentoClientes() {
		try {
			this.lstTiposDocGenerales = this.parametroGeneralService.obtenerListaParametros(propiedades.getComboTiposDocumento());
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerTiposDocumentoClientes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void obtenerTiposClientes() {
		try {
			this.lstTiposCliGenerales = this.parametroGeneralService.obtenerListaParametros(propiedades.getComboTiposCliente());
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerTiposClientes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}

	public List<String> completeNom(String query) {
		List<String> results = new ArrayList<String>();
		for (String s : this.lstNomClienteBUS) {
			if (Utilitarios.compararCadenas(s, query.trim())) {
				results.add(s);
			}
		}
		return results;
	}

	public List<String> completeNumDoc(String query) {
		List<String> results = new ArrayList<String>();
		for (String s : this.lstNumDocClienteBUS) {
			if (Utilitarios.compararCadenas(s, query.trim())) {
				results.add(s);
			}
		}
		return results;
	}

	public Client getObjClienteMain() {
		return this.objClienteMain;
	}

	public void setObjClienteMain(Client objClienteMain) {
		this.objClienteMain = objClienteMain;
	}
	
	public Client getObjClienteSearch() {
		return objClienteSearch;
	}

	public void setObjClienteSearch(Client objClienteSearch) {
		this.objClienteSearch = objClienteSearch;
	}

	public List<Client> getLstClientesMain() {
		if ((this.lstClientesMain.isEmpty()) && (this.validaListaBuscar)) {
			this.canRegTablaPrincipal = getListaPrincipalClientes();
		}
		return this.lstClientesMain;
	}

	public void setLstClientesMain(List<Client> lstClientesMain) {
		this.lstClientesMain = lstClientesMain;
	}

	public int getCanRegTablaPrincipal() {
		return this.canRegTablaPrincipal;
	}

	public void setCanRegTablaPrincipal(int canRegTablaPrincipal) {
		this.canRegTablaPrincipal = canRegTablaPrincipal;
	}

	public List<GeneralParameter> getLstEstadosGenerales() {
		obtenerEstadosClientes();
		return this.lstEstadosGenerales;
	}

	public void setLstEstadosGenerales(List<GeneralParameter> lstEstadosGenerales) {
		this.lstEstadosGenerales = lstEstadosGenerales;
	}
	
	public List<GeneralParameter> getLstTiposDocGenerales() {
		obtenerTiposDocumentoClientes();
		return lstTiposDocGenerales;
	}

	public void setLstTiposDocGenerales(List<GeneralParameter> lstTiposDocGenerales) {
		this.lstTiposDocGenerales = lstTiposDocGenerales;
	}

	public List<GeneralParameter> getLstTiposCliGenerales() {
		obtenerTiposClientes();
		return lstTiposCliGenerales;
	}

	public void setLstTiposCliGenerales(List<GeneralParameter> lstTiposCliGenerales) {
		this.lstTiposCliGenerales = lstTiposCliGenerales;
	}
	
	public boolean isFlagConfirmEliClient() {
		return this.flagConfirmEliClient;
	}

	public void setFlagConfirmEliClient(boolean flagConfirmEliClient) {
		this.flagConfirmEliClient = flagConfirmEliClient;
	}

	public List<String> getLstNumDocClienteBUS() {
		return lstNumDocClienteBUS;
	}

	public void setLstNumDocClienteBUS(List<String> lstNumDocClienteBUS) {
		this.lstNumDocClienteBUS = lstNumDocClienteBUS;
	}

	public List<String> getLstNomClienteBUS() {
		return lstNomClienteBUS;
	}

	public void setLstNomClienteBUS(List<String> lstNomClienteBUS) {
		this.lstNomClienteBUS = lstNomClienteBUS;
	}

	public boolean isValidaListaBuscar() {
		return this.validaListaBuscar;
	}

	public void setValidaListaBuscar(boolean validaListaBuscar) {
		this.validaListaBuscar = validaListaBuscar;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public CuentaService getCuentaService() {
		return cuentaService;
	}

	public void setCuentaService(CuentaService cuentaService) {
		this.cuentaService = cuentaService;
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
