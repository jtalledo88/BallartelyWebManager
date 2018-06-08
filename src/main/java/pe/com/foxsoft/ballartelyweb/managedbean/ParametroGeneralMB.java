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
import pe.com.foxsoft.ballartelyweb.spring.domain.TipoParametro;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class ParametroGeneralMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private Propiedades propiedades;

	private GeneralParameter objParametroGeneralMain;
	private GeneralParameter objParametroGeneralSearch;

	private List<GeneralParameter> lstParametrosGeneralesMain;
	private List<TipoParametro> lstTiposParametros;
	private List<String> lstDescParametroGeneralBUS;
	private List<String> lstCodParametroGeneralBUS;
	private boolean validaListaBuscar = true;
	private int canRegTablaPrincipal;
	private boolean flagConfirmEliParamGen = false;

	public ParametroGeneralMB() {
		this.objParametroGeneralMain = new GeneralParameter();
		this.objParametroGeneralSearch = new GeneralParameter();
		this.lstParametrosGeneralesMain = new ArrayList<GeneralParameter>();
		this.lstCodParametroGeneralBUS = new ArrayList<String>();
		this.lstDescParametroGeneralBUS = new ArrayList<String>();
		this.lstTiposParametros = new ArrayList<TipoParametro>();
	}

	public void buscarParametrosGenerales() {
		try {
			this.validaListaBuscar = false;
			this.lstParametrosGeneralesMain = this.parametroGeneralService.buscarParametrosGenerales(this.objParametroGeneralSearch);
			this.canRegTablaPrincipal = this.lstParametrosGeneralesMain.size();
		} catch (BallartelyException e) {
			String sMensaje = "Error en buscarParametrosGenerales";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void agregarParametroGeneral() {
		String sMensaje = "";
		
		GeneralParameter objParametroGeneral = new GeneralParameter();
		try {
			if ("".equals(this.objParametroGeneralMain.getParamDescription())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la descripción del parametro.");
				return;
			} 
			if ("".equals(this.objParametroGeneralMain.getParamCode())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el código del parametro.");
				return;
			} 
			if ("".equals(this.objParametroGeneralMain.getParamStatus())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un estado.");
				return;
			}
			if ("".equals(this.objParametroGeneralMain.getParamType())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un tipo.");
				return;
			}
			if ("".equals(this.objParametroGeneralMain.getParamValue())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el valor del parametro.");
				return;
			}
			
			objParametroGeneral.setParamType(this.objParametroGeneralMain.getParamType());
			objParametroGeneral.setParamCode(this.objParametroGeneralMain.getParamCode().toUpperCase());
			objParametroGeneral.setParamDescription(this.objParametroGeneralMain.getParamDescription());
			objParametroGeneral.setParamValue(this.objParametroGeneralMain.getParamValue());
			objParametroGeneral.setParamStatus(this.objParametroGeneralMain.getParamStatus());
			
			objParametroGeneral = this.parametroGeneralService.agregarParametroGeneral(objParametroGeneral);
			sMensaje = String.format(Constantes.MESSAGE_PERSIST_SUCCESS, objParametroGeneral.getParamId());
			Utilitarios.mensaje("", sMensaje);
			setLstParametrosGeneralesMain(new ArrayList<GeneralParameter>());
			this.canRegTablaPrincipal = getListaPrincipalParametroGeneral();
		
		} catch (BallartelyException e) {
			sMensaje = "Error en agregarParametroGeneral";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void openAgregarParametroGeneral() {
		this.objParametroGeneralMain.setParamType("");
		this.objParametroGeneralMain.setParamCode("");
		this.objParametroGeneralMain.setParamDescription("");
		this.objParametroGeneralMain.setParamValue("");
		this.objParametroGeneralMain.setParamStatus("");
	}

	public void openEditarParametroGeneral() {
		setObjParametroGeneralMain(new GeneralParameter());

		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		int itemParametroGeneral = Integer.parseInt((String) paramMap.get("itemParametroGeneral"));
		try {
			this.objParametroGeneralMain = this.parametroGeneralService.obtenerParametroGeneral(itemParametroGeneral);
		} catch (BallartelyException e) {
			String sMensaje = "Error en openEditarParametroGeneral";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void editarParametroGeneral() {
		String sMensaje = "";
		
		try {
			if ("".equals(this.objParametroGeneralMain.getParamDescription())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la descripción del parametro.");
				return;
			} 
			if ("".equals(this.objParametroGeneralMain.getParamCode())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el código del parametro.");
				return;
			} 
			if ("".equals(this.objParametroGeneralMain.getParamStatus())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un estado.");
				return;
			}
			if ("".equals(this.objParametroGeneralMain.getParamType())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar un tipo.");
				return;
			}
			if ("".equals(this.objParametroGeneralMain.getParamValue())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el valor del parametro.");
				return;
			}
			
			GeneralParameter objParametroGeneralAct = this.parametroGeneralService.editarParametroGeneral(this.objParametroGeneralMain);
			sMensaje = String.format(Constantes.MESSAGE_MERGE_SUCCESS, objParametroGeneralAct.getParamId());
			Utilitarios.mensaje("", sMensaje);
			this.canRegTablaPrincipal = getListaPrincipalParametroGeneral();
			
		} catch (BallartelyException e) {
			sMensaje = "Error en editarParametroGeneral";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void visibleConfirmElimParametroGeneral() {
		this.flagConfirmEliParamGen = true;
	}

	public void eliminarParametroGeneral() {
		String sMensaje = "";
		try {
			this.parametroGeneralService.eliminarParametroGeneral(this.objParametroGeneralMain);
			sMensaje = Constantes.MESSAGE_REMOVE_SUCCESS;
			setLstParametrosGeneralesMain(new ArrayList<GeneralParameter>());
			this.canRegTablaPrincipal = getListaPrincipalParametroGeneral();
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en eliminarParametroGeneral";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
		this.flagConfirmEliParamGen = false;
	}

	public int getListaPrincipalParametroGeneral() {
		int can = 0;
		try {
			this.lstParametrosGeneralesMain = this.parametroGeneralService.getListaParametrosGenerales();
			can = this.lstParametrosGeneralesMain.size();
			for (GeneralParameter p : this.lstParametrosGeneralesMain) {
				if (p.getParamCode() != null) {
					this.lstCodParametroGeneralBUS.add(p.getParamCode());
				}
				if (p.getParamDescription() != null) {
					this.lstDescParametroGeneralBUS.add(p.getParamDescription());
				}
			}
		} catch (BallartelyException e) {
			String sMensaje = "Error en eliminarParametroGeneral";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
		return can;
	}

	public void obtenerTiposParametros() {
		try {
			this.lstTiposParametros = Utilitarios.obtenerListaTipoParametros(propiedades.getTiposParametro());
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerTiposParametros";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}

	public List<String> completeDesc(String query) {
		List<String> results = new ArrayList<String>();
		for (String s : this.lstDescParametroGeneralBUS) {
			if (Utilitarios.compararCadenas(s, query.trim())) {
				results.add(s);
			}
		}
		return results;
	}

	public List<String> completeCod(String query) {
		List<String> results = new ArrayList<String>();
		for (String s : this.lstCodParametroGeneralBUS) {
			if (Utilitarios.compararCadenas(s, query.trim())) {
				results.add(s);
			}
		}
		return results;
	}

	public GeneralParameter getObjParametroGeneralMain() {
		return this.objParametroGeneralMain;
	}

	public void setObjParametroGeneralMain(GeneralParameter objParametroGeneralMain) {
		this.objParametroGeneralMain = objParametroGeneralMain;
	}
	
	public GeneralParameter getObjParametroGeneralSearch() {
		return objParametroGeneralSearch;
	}

	public void setObjParametroGeneralSearch(GeneralParameter objParametroGeneralSearch) {
		this.objParametroGeneralSearch = objParametroGeneralSearch;
	}

	public List<GeneralParameter> getLstParametrosGeneralesMain() {
		if ((this.lstParametrosGeneralesMain.isEmpty()) && (this.validaListaBuscar)) {
			this.canRegTablaPrincipal = getListaPrincipalParametroGeneral();
		}
		return this.lstParametrosGeneralesMain;
	}

	public void setLstParametrosGeneralesMain(List<GeneralParameter> lstParametrosGeneralesMain) {
		this.lstParametrosGeneralesMain = lstParametrosGeneralesMain;
	}

	public int getCanRegTablaPrincipal() {
		return this.canRegTablaPrincipal;
	}

	public void setCanRegTablaPrincipal(int canRegTablaPrincipal) {
		this.canRegTablaPrincipal = canRegTablaPrincipal;
	}

	public List<TipoParametro> getLstTiposParametros() {
		if (this.lstTiposParametros.isEmpty()) {
			obtenerTiposParametros();
		}
		return this.lstTiposParametros;
	}

	public void setLstTiposParametros(List<TipoParametro> lstTiposParametros) {
		this.lstTiposParametros = lstTiposParametros;
	}
	
	public boolean isFlagConfirmEliParamGen() {
		return this.flagConfirmEliParamGen;
	}

	public void setFlagConfirmEliParamGen(boolean flagConfirmEliParamGen) {
		this.flagConfirmEliParamGen = flagConfirmEliParamGen;
	}

	public List<String> getLstDescParametroGeneralBUS() {
		return this.lstDescParametroGeneralBUS;
	}

	public void setLstDescParametroGeneralBUS(List<String> lstDescParametroGeneralBUS) {
		this.lstDescParametroGeneralBUS = lstDescParametroGeneralBUS;
	}

	public List<String> getLstCodParametroGeneralBUS() {
		return this.lstCodParametroGeneralBUS;
	}

	public void setLstCodParametroGeneralBUS(List<String> lstCodParametroGeneralBUS) {
		this.lstCodParametroGeneralBUS = lstCodParametroGeneralBUS;
	}

	public boolean isValidaListaBuscar() {
		return this.validaListaBuscar;
	}

	public void setValidaListaBuscar(boolean validaListaBuscar) {
		this.validaListaBuscar = validaListaBuscar;
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
