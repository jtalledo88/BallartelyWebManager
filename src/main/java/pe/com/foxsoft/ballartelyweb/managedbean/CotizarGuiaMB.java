package pe.com.foxsoft.ballartelyweb.managedbean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

import org.primefaces.event.CellEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.GeneralParameter;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideCotization;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.GuiaService;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class CotizarGuiaMB {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GuiaService guiaService;
	
	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private Propiedades propiedades;
	
	private List<GuideHead> lstGuideHeadMain;
	private List<GuideDetail> lstGuideDetailMain;
	private List<GeneralParameter> lstGastosMain;
	private List<GuideCotization> lstGuideCotizedMain;
	
	private int cantidadVivosDetalle;
	private BigDecimal totalVivosDetalle;
	private BigDecimal totalDeshidratadoDetalle;
	private BigDecimal cantTotalGastos;
	
	private GuideHead objGuideHeadMain;

	private boolean flagEdit;

	private boolean flagCotized;

	public CotizarGuiaMB() {
		lstGuideHeadMain = new ArrayList<>();
		lstGuideDetailMain = new ArrayList<>();
		lstGastosMain = new ArrayList<>();
		lstGuideCotizedMain = new ArrayList<>();
		flagCotized = false;
	}
	
	@PostConstruct
    public void init() {
		//Logica despues de construir pagina
	}
	
	public void cotizarGuia() {
		String sMensaje = null;
		try {
			if(lstGastosMain == null || lstGastosMain.isEmpty()) {
				Utilitarios.mensajeInfo("Error de validación", "Listado de gastos no disponible.");
				return;
			}
			
			if(this.cantTotalGastos.compareTo(BigDecimal.ZERO) == 0) {
				Utilitarios.mensajeInfo("Error de validación", "Ingrese gastos.");
				return;
			}
			GuideCotization guideCotization = new GuideCotization();
			guideCotization.setGuideHead(objGuideHeadMain);
			guideCotization.setUnitCostGuide(lstGuideDetailMain.get(0).getUnitCost());
			guideCotization.setTotalDecrease(obtenerMerma());
			guideCotization.setTotalExpenses(this.cantTotalGastos);
			guideCotization.setTotalWeightDehydrated(this.totalDeshidratadoDetalle);
			guideCotization.setTotalUnitCost(obtenerCostoTotalUnitario(guideCotization));
			objGuideHeadMain.setGuideCotized(Constantes.COTIZED_YES);
			sMensaje = guiaService.cotizarGuia(objGuideHeadMain, guideCotization);
			Utilitarios.mensaje("", sMensaje);
			reiniciarVista();
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en beneficiarGuia, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}
	
	private BigDecimal obtenerCostoTotalUnitario(GuideCotization guideCotization) {
		BigDecimal costoTotalUnitario = guideCotization.getUnitCostGuide().multiply(guideCotization.getTotalWeightDehydrated());
		costoTotalUnitario = costoTotalUnitario.add(guideCotization.getTotalExpenses());
		costoTotalUnitario = costoTotalUnitario.subtract(guideCotization.getTotalDecrease());
		costoTotalUnitario = costoTotalUnitario.divide(guideCotization.getTotalWeightDehydrated(), RoundingMode.HALF_UP);
		
		return costoTotalUnitario;
	}

	private BigDecimal obtenerMerma() throws BallartelyException {
		GeneralParameter pPorcMerma = this.parametroGeneralService.obtenerParametroGeneral(Constantes.PORCENTAJE_MERMA_CODE);
		BigDecimal valPorcMerma = Utilitarios.getDecimal(pPorcMerma.getParamValue());
		
		return valPorcMerma.multiply(BigDecimal.valueOf(this.cantidadVivosDetalle));
	}

	private void reiniciarVista() {
		this.totalVivosDetalle = BigDecimal.ZERO;
		this.cantTotalGastos = BigDecimal.ZERO;
		this.lstGastosMain = new ArrayList<>();
		this.lstGuideDetailMain = new ArrayList<>();
		this.lstGuideHeadMain = new ArrayList<>();
		cargarListaCotizacion();
	}

	private void cargarListaCotizacion() {
		String sMensaje = null;
		try {
			this.lstGuideCotizedMain = this.guiaService.getListaGuiaCotizacion(objGuideHeadMain.getId());
			flagCotized = true;
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en cargarListaCotizacion, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	private void cargaTablaDetalle() {
		String sMensaje = null;
		try {
			BigDecimal totalVivos = BigDecimal.ZERO;
			BigDecimal totalMuertos = BigDecimal.ZERO;
			int cantVivos = 0;
			int cantMuertos = 0;
			if(objGuideHeadMain != null) {
				this.lstGuideDetailMain = this.guiaService.getListaGuiasDetalle(objGuideHeadMain.getId());
				if(lstGuideDetailMain != null) {
					for(GuideDetail gDetail: lstGuideDetailMain) {
						totalVivos = totalVivos.add(gDetail.getNetWeight());
						totalMuertos = totalMuertos.add(gDetail.getDeadWeight());
						cantVivos += gDetail.getProductQuantity();
						cantMuertos += gDetail.getDeadQuantity();
					}
				}
				this.totalVivosDetalle = totalVivos.subtract(totalMuertos);
				this.cantidadVivosDetalle = cantVivos - cantMuertos;
				cargarTotalDeshidratado();
			}
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en cargaTablaDetalle, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}
	
	private void cargarTotalDeshidratado() throws BallartelyException{
		GeneralParameter pPorcDesh = this.parametroGeneralService.obtenerParametroGeneral(Constantes.PORCENTAJE_DESHIDRATADO_CODE);
		BigDecimal valPorcDesh = Utilitarios.getDecimal(pPorcDesh.getParamValue());
		this.totalDeshidratadoDetalle = this.totalVivosDetalle.subtract(this.totalVivosDetalle.multiply(valPorcDesh).divide(BigDecimal.valueOf(100)));
		this.totalDeshidratadoDetalle = this.totalDeshidratadoDetalle.setScale(2, RoundingMode.HALF_UP);
	}

	private void cargaTablaGastos() {
		String sMensaje = null;
		try {
			if(objGuideHeadMain != null) {
				this.lstGastosMain = this.parametroGeneralService.obtenerListaParametros(propiedades.getUniqueCodeExpend());
				for(GeneralParameter gasto: lstGastosMain) {
					if(Constantes.GASTO_BENEFICIO_CODE.equals(gasto.getParamCode())) {
						GeneralParameter paramCostoBeneficio = 
								this.parametroGeneralService.obtenerParametroGeneral(Constantes.GASTO_BENEFICIO_PORCENTAJE_CODE);
						BigDecimal valCostoBeneficio = Utilitarios.getDecimal(paramCostoBeneficio.getParamValue());
						gasto.setParamValue(valCostoBeneficio.multiply(BigDecimal.valueOf(cantidadVivosDetalle)).toString());
						this.cantTotalGastos = Utilitarios.getDecimal(gasto.getParamValue());
						break;
					}
				}
			}
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en cargaTablaGastos, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	public void onGastoCellEdit(CellEditEvent cellEditEvent) {
		BigDecimal oldCant = Utilitarios.getDecimal((String)cellEditEvent.getOldValue());
		BigDecimal newCant = Utilitarios.getDecimal((String)cellEditEvent.getNewValue());
		cantTotalGastos = cantTotalGastos.add(newCant);
		cantTotalGastos = cantTotalGastos.subtract(oldCant);
		flagEdit = true;
	}

	private void obtenerGuiasHead() {
		if(objGuideHeadMain != null) {
			this.lstGuideHeadMain = new ArrayList<>();
			this.lstGuideHeadMain.add(objGuideHeadMain);
			if(Constantes.COTIZED_YES.equals(objGuideHeadMain.getGuideCotized())) {
				flagCotized = true;
			}
		}else {
			flagCotized = false;
		}
	}

	public List<GuideHead> getLstGuideHeadMain() {
		obtenerGuiasHead();
		return lstGuideHeadMain;
	}

	public void setLstGuideHeadMain(List<GuideHead> lstGuideHeadMain) {
		this.lstGuideHeadMain = lstGuideHeadMain;
	}

	public List<GuideDetail> getLstGuideDetailMain() {
		cargaTablaDetalle();
		return lstGuideDetailMain;
	}

	public void setLstGuideDetailMain(List<GuideDetail> lstGuideDetailMain) {
		this.lstGuideDetailMain = lstGuideDetailMain;
	}

	public List<GeneralParameter> getLstGastosMain() {
		if(!flagEdit) {
			cargaTablaGastos();
		}
		return lstGastosMain;
	}

	public void setLstGastosMain(List<GeneralParameter> lstGastosMain) {
		this.lstGastosMain = lstGastosMain;
	}

	public List<GuideCotization> getLstGuideCotizedMain() {
		if(objGuideHeadMain != null && Constantes.COTIZED_YES.equals(objGuideHeadMain.getGuideCotized())) {
			cargarListaCotizacion();
		}
		return lstGuideCotizedMain;
	}

	public void setLstGuideCotizedMain(List<GuideCotization> lstGuideCotizedMain) {
		this.lstGuideCotizedMain = lstGuideCotizedMain;
	}

	public GuiaService getGuiaService() {
		return guiaService;
	}

	public void setGuiaService(GuiaService guiaService) {
		this.guiaService = guiaService;
	}

	public ParametroGeneralService getParametroGeneralService() {
		return parametroGeneralService;
	}

	public void setParametroGeneralService(ParametroGeneralService parametroGeneralService) {
		this.parametroGeneralService = parametroGeneralService;
	}

	public GuideHead getObjGuideHeadMain() {
		return objGuideHeadMain;
	}

	public void setObjGuideHeadMain(GuideHead objGuideHeadMain) {
		this.objGuideHeadMain = objGuideHeadMain;
	}

	public BigDecimal getTotalVivosDetalle() {
		return totalVivosDetalle;
	}

	public void setTotalVivosDetalle(BigDecimal totalVivosDetalle) {
		this.totalVivosDetalle = totalVivosDetalle;
	}

	public int getCantidadVivosDetalle() {
		return cantidadVivosDetalle;
	}

	public void setCantidadVivosDetalle(int cantidadVivosDetalle) {
		this.cantidadVivosDetalle = cantidadVivosDetalle;
	}

	public BigDecimal getTotalDeshidratadoDetalle() {
		return totalDeshidratadoDetalle;
	}

	public void setTotalDeshidratadoDetalle(BigDecimal totalDeshidratadoDetalle) {
		this.totalDeshidratadoDetalle = totalDeshidratadoDetalle;
	}

	public BigDecimal getCantTotalGastos() {
		return cantTotalGastos;
	}

	public void setCantTotalGastos(BigDecimal cantTotalGastos) {
		this.cantTotalGastos = cantTotalGastos;
	}

	public boolean isFlagCotized() {
		return flagCotized;
	}

	public void setFlagCotized(boolean flagCotized) {
		this.flagCotized = flagCotized;
	}

	public Propiedades getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Propiedades propiedades) {
		this.propiedades = propiedades;
	}
	
}
