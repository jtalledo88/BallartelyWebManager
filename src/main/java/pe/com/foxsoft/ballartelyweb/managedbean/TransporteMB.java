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
import pe.com.foxsoft.ballartelyweb.jpa.data.Transport;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.service.TransporteService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class TransporteMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TransporteService transporteService;

	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private Propiedades propiedades;

	private Transport objTransporteMain;
	private Transport objTransporteSearch;

	private List<Transport> lstTransportesMain;
	private List<GeneralParameter> lstEstadosGenerales;
	private List<String> lstCarNumberBUS;
	private List<String> lstDriverNamesBUS;
	private boolean validaListaBuscar = true;
	private int canRegTablaPrincipal;
	private boolean flagConfirmEliTransport = false;

	public TransporteMB() {
		this.objTransporteMain = new Transport();
		this.objTransporteSearch = new Transport();
		this.lstTransportesMain = new ArrayList<>();
		this.lstEstadosGenerales = new ArrayList<>();
		this.lstCarNumberBUS = new ArrayList<>();
		this.lstDriverNamesBUS = new ArrayList<>();
	}

	public void buscarTransportes() {
		try {
			this.validaListaBuscar = false;
			this.lstTransportesMain = this.transporteService.buscarTransportes(this.objTransporteSearch);
			this.canRegTablaPrincipal = this.lstTransportesMain.size();
		} catch (BallartelyException e) {
			String sMensaje = "Error en buscarTransportes";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	public void agregarTransporte() {
		String sMensaje = "";
		
		Transport objTransporte = new Transport();
		try {
			if ("".equals(this.objTransporteMain.getCarMark())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la marca del Transporte.");
				return;
			}
			if ("".equals(this.objTransporteMain.getCarNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de placa del Transporte.");
				return;
			}
			if ("".equals(this.objTransporteMain.getDriverLicenseNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de licencia del conductor.");
				return;
			}
			if ("".equals(this.objTransporteMain.getDriverNames())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el nombre del conductor.");
				return;
			}
			
			objTransporte = this.transporteService.agregarTransporte(this.objTransporteMain);
			sMensaje = Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_SUCCESS, objTransporte.getId());
			Utilitarios.mensaje("", sMensaje);
			setLstTransportesMain(new ArrayList<Transport>());
			this.canRegTablaPrincipal = getListaPrincipalTransportes();
		
		} catch (BallartelyException e) {
			sMensaje = "Error en agregarProveedor";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	public void openAgregarTransporte() {
		this.objTransporteMain = new Transport();
		this.objTransporteMain.setCarMark("");
		this.objTransporteMain.setCarNumber("");
		this.objTransporteMain.setDriverLicenseNumber("");
		this.objTransporteMain.setDriverNames("");
	}

	public void openEditarTransporte() {
		setObjTransporteMain(new Transport());

		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		int itemTransporte = Integer.parseInt((String) paramMap.get("itemTransporte"));
		try {
			this.objTransporteMain = this.transporteService.obtenerTransporte(itemTransporte);
		} catch (BallartelyException e) {
			String sMensaje = "Error en openEditarTransporte";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	public void editarTransporte() {
		String sMensaje = "";
		
		try {
			if ("".equals(this.objTransporteMain.getCarMark())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la marca del Transporte.");
				return;
			}
			if ("".equals(this.objTransporteMain.getCarNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de placa del Transporte.");
				return;
			}
			if ("".equals(this.objTransporteMain.getDriverLicenseNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el número de licencia del conductor.");
				return;
			}
			if ("".equals(this.objTransporteMain.getDriverNames())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el nombre del conductor.");
				return;
			}
			
			Transport objTransporte = this.transporteService.editarTransporte(this.objTransporteMain);
			sMensaje = Utilitarios.reemplazarMensaje(Constantes.MESSAGE_MERGE_SUCCESS, objTransporte.getId());
			Utilitarios.mensaje("", sMensaje);
			this.canRegTablaPrincipal = getListaPrincipalTransportes();
			
		} catch (BallartelyException e) {
			sMensaje = "Error en editarTransporte";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	public void visibleConfirmElimTransporte() {
		this.flagConfirmEliTransport = true;
	}

	public void eliminarTransporte() {
		String sMensaje = "";
		try {
			this.transporteService.eliminarTransporte(this.objTransporteMain);
			sMensaje = Constantes.MESSAGE_REMOVE_SUCCESS;
			setLstTransportesMain(new ArrayList<Transport>());
			this.canRegTablaPrincipal = getListaPrincipalTransportes();
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en eliminarProveedor";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
		this.flagConfirmEliTransport = false;
	}

	public int getListaPrincipalTransportes() {
		int can = 0;
		try {
			this.lstTransportesMain = this.transporteService.getListaTransportes();
			can = this.lstTransportesMain.size();
			for (Transport t : this.lstTransportesMain) {
				if (t.getCarNumber() != null) {
					this.lstCarNumberBUS.add(t.getCarNumber());
				}
				if (t.getDriverNames() != null) {
					this.lstDriverNamesBUS.add(t.getDriverNames());
				}
			}
		} catch (BallartelyException e) {
			String sMensaje = "Error en getListaPrincipalTransportes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
		return can;
	}

	public void obtenerEstadosTransportes() {
		try {
			this.lstEstadosGenerales = this.parametroGeneralService.obtenerListaParametros(propiedades.getComboEstados());
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerEstadosTransportes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}

	public List<String> completeCarNumber(String query) {
		List<String> results = new ArrayList<String>();
		for (String s : this.lstCarNumberBUS) {
			if (Utilitarios.compararCadenas(s, query.trim())) {
				results.add(s);
			}
		}
		return results;
	}

	public List<String> completeDriverNames(String query) {
		List<String> results = new ArrayList<String>();
		for (String s : this.lstDriverNamesBUS) {
			if (Utilitarios.compararCadenas(s, query.trim())) {
				results.add(s);
			}
		}
		return results;
	}

	public Transport getObjTransporteMain() {
		return this.objTransporteMain;
	}

	public void setObjTransporteMain(Transport objTransporteMain) {
		this.objTransporteMain = objTransporteMain;
	}
	
	public Transport getObjTransporteSearch() {
		return objTransporteSearch;
	}

	public void setObjTransporteSearch(Transport objTransporteSearch) {
		this.objTransporteSearch = objTransporteSearch;
	}

	public List<Transport> getLstTransportesMain() {
		if ((this.lstTransportesMain.isEmpty()) && (this.validaListaBuscar)) {
			this.canRegTablaPrincipal = getListaPrincipalTransportes();
		}
		return this.lstTransportesMain;
	}

	public void setLstTransportesMain(List<Transport> lstTransportesMain) {
		this.lstTransportesMain = lstTransportesMain;
	}

	public int getCanRegTablaPrincipal() {
		return this.canRegTablaPrincipal;
	}

	public void setCanRegTablaPrincipal(int canRegTablaPrincipal) {
		this.canRegTablaPrincipal = canRegTablaPrincipal;
	}

	public List<GeneralParameter> getLstEstadosGenerales() {
		obtenerEstadosTransportes();
		return this.lstEstadosGenerales;
	}

	public void setLstEstadosGenerales(List<GeneralParameter> lstEstadosGenerales) {
		this.lstEstadosGenerales = lstEstadosGenerales;
	}
	
	public boolean isFlagConfirmEliTransport() {
		return this.flagConfirmEliTransport;
	}

	public void setFlagConfirmEliTransport(boolean flagConfirmEliTransport) {
		this.flagConfirmEliTransport = flagConfirmEliTransport;
	}

	public List<String> getLstCarNumberBUS() {
		return lstCarNumberBUS;
	}

	public void setLstCarNumberBUS(List<String> lstCarNumberBUS) {
		this.lstCarNumberBUS = lstCarNumberBUS;
	}

	public List<String> getLstDriverNamesBUS() {
		return lstDriverNamesBUS;
	}

	public void setLstDriverNamesBUS(List<String> lstDriverNamesBUS) {
		this.lstDriverNamesBUS = lstDriverNamesBUS;
	}

	public boolean isValidaListaBuscar() {
		return this.validaListaBuscar;
	}

	public void setValidaListaBuscar(boolean validaListaBuscar) {
		this.validaListaBuscar = validaListaBuscar;
	}

	public TransporteService getTransporteService() {
		return transporteService;
	}

	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
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
