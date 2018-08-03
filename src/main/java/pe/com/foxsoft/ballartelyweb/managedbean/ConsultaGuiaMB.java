package pe.com.foxsoft.ballartelyweb.managedbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.GuiaService;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class ConsultaGuiaMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GuiaService guiaService;
	
	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private Propiedades propiedades;
	
	private GuideHead objGuideHeadMain;
	private Date emissionDateStartSearch;
	private Date emissionDateEndSearch;
	private Date creationDateStartSearch;
	private Date creationDateEndSearch;
	
	private StreamedContent guideImageSelected;
	
	private List<GuideHead> lstGuideHeadMain;
	private List<GuideDetail> lstItemsGuideMain;
	
	private boolean validaListaBuscar = true;
	private int canRegTablaPrincipal;

	public ConsultaGuiaMB() {
		objGuideHeadMain = new GuideHead();
		lstGuideHeadMain = new ArrayList<>();
		lstItemsGuideMain = new ArrayList<>();
	}

	public void buscarGuias() {
		try {
			this.validaListaBuscar = false;
			this.lstGuideHeadMain = this.guiaService.getListaGuiasCabecera(objGuideHeadMain, 
					emissionDateStartSearch, emissionDateEndSearch, creationDateStartSearch, creationDateEndSearch);
			this.canRegTablaPrincipal = this.lstGuideHeadMain.size();
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en buscarGuias, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}


	public void openVerDetalle() {
		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		int itemGuia = Integer.parseInt((String) paramMap.get("itemGuiaDetalle"));
		try {
			lstItemsGuideMain = guiaService.getListaGuiasDetalle(itemGuia);
		} catch (BallartelyException e) {
			String sMensaje = "Error en openEditarProveedor";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void openVerImagen() {
		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		int itemGuia = Integer.parseInt((String) paramMap.get("itemGuiaArchivo"));
		String fileName = null;
		try {
			for(GuideHead guideHead: lstGuideHeadMain) {
				if(itemGuia == guideHead.getId()) {
					fileName = guideHead.getGuideFile();
					break;
				}
			}
			GeneralParameter generalParameterUpload = this.parametroGeneralService.obtenerParametroGeneral(propiedades.getUniqueCodeUpload());
			
			guideImageSelected = new DefaultStreamedContent(Utilitarios.obtenerArchivo(
					generalParameterUpload.getParamValue(), fileName), Utilitarios.obtenerTipoArchivo(fileName));
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en openVerImagen, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}
	
	public int getListaPrincipalGuias() {
		int can = 0;
		try {
			this.lstGuideHeadMain = this.guiaService.getListaGuiasCabecera();
			can = this.lstGuideHeadMain.size();

		} catch (BallartelyException e) {
			String sMensaje = "Error en getListaPrincipalGuias";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
		return can;
	}

	public List<GuideHead> getLstGuideHeadMain() {
		if (this.validaListaBuscar) {
			this.canRegTablaPrincipal = getListaPrincipalGuias();
		}
		return lstGuideHeadMain;
	}

	public void setLstGuideHeadMain(List<GuideHead> lstGuideHeadMain) {
		this.lstGuideHeadMain = lstGuideHeadMain;
	}

	public int getCanRegTablaPrincipal() {
		return this.canRegTablaPrincipal;
	}

	public void setCanRegTablaPrincipal(int canRegTablaPrincipal) {
		this.canRegTablaPrincipal = canRegTablaPrincipal;
	}

	public boolean isValidaListaBuscar() {
		return this.validaListaBuscar;
	}

	public void setValidaListaBuscar(boolean validaListaBuscar) {
		this.validaListaBuscar = validaListaBuscar;
	}

	public GuideHead getObjGuideHeadMain() {
		return objGuideHeadMain;
	}

	public void setObjGuideHeadMain(GuideHead objGuideHeadMain) {
		this.objGuideHeadMain = objGuideHeadMain;
	}

	public GuiaService getGuiaService() {
		return guiaService;
	}

	public void setGuiaService(GuiaService guiaService) {
		this.guiaService = guiaService;
	}

	public Date getEmissionDateStartSearch() {
		return emissionDateStartSearch;
	}

	public void setEmissionDateStartSearch(Date emissionDateStartSearch) {
		this.emissionDateStartSearch = emissionDateStartSearch;
	}

	public Date getEmissionDateEndSearch() {
		return emissionDateEndSearch;
	}

	public void setEmissionDateEndSearch(Date emissionDateEndSearch) {
		this.emissionDateEndSearch = emissionDateEndSearch;
	}

	public Date getCreationDateStartSearch() {
		return creationDateStartSearch;
	}

	public void setCreationDateStartSearch(Date creationDateStartSearch) {
		this.creationDateStartSearch = creationDateStartSearch;
	}

	public Date getCreationDateEndSearch() {
		return creationDateEndSearch;
	}

	public void setCreationDateEndSearch(Date creationDateEndSearch) {
		this.creationDateEndSearch = creationDateEndSearch;
	}

	public StreamedContent getGuideImageSelected() {
		return guideImageSelected;
	}

	public void setGuideImageSelected(StreamedContent guideImageSelected) {
		this.guideImageSelected = guideImageSelected;
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

	public List<GuideDetail> getLstItemsGuideMain() {
		return lstItemsGuideMain;
	}

	public void setLstItemsGuideMain(List<GuideDetail> lstItemsGuideMain) {
		this.lstItemsGuideMain = lstItemsGuideMain;
	}
	
}
