package pe.com.foxsoft.ballartelyweb.managedbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetailSales;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.Invoice;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.FacturaService;
import pe.com.foxsoft.ballartelyweb.spring.service.GuiaService;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class RegistrarFacturaMB {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GuiaService guiaService;
	
	@Autowired
	private FacturaService facturaService;
	
	private List<GuideHead> lstGuideHeadMain;
	private List<GuideDetailSales> lstGuideDetailMain;
	
	private int cantidadRegistrosDetalle;
	private Date currentDate = new Date();
	
	private GuideHead objGuideHeadMain;
	private Invoice objInvoiceMain = new Invoice();

	private boolean facturaRegistrada;

	public RegistrarFacturaMB() {
		lstGuideHeadMain = new ArrayList<>();
		lstGuideDetailMain = new ArrayList<>();
	}
	
	@PostConstruct
    public void init() {
		//Logica despues de construir pagina
	}
	
	public void registrar() {
		String sMensaje = null;
		try {
			if("".equals(objInvoiceMain.getInvoiceSerie())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar la serie.");
				return;
			}
			if("".equals(objInvoiceMain.getInvoiceNumber())) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar el número.");
				return;
			}
			if(objInvoiceMain.getInvoiceEmissionDate() == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar la fecha de emisión.");
				return;
			}
			objInvoiceMain.setGuideHead(objGuideHeadMain);
			if(!objInvoiceMain.getInvoiceSerie().startsWith("F")) {
				objInvoiceMain.setInvoiceSerie("F".concat(objInvoiceMain.getInvoiceSerie()));
			}
			sMensaje = facturaService.insertarFactura(objInvoiceMain);
			Utilitarios.mensaje("", sMensaje);
			reiniciarVista();
			facturaRegistrada = true;
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en registrar, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
			facturaRegistrada = false;
			objInvoiceMain.setInvoiceSerie(objInvoiceMain.getInvoiceSerie().replaceFirst("F", ""));
		}
	}
	
	private void reiniciarVista() {
		this.cantidadRegistrosDetalle = 0;
		this.lstGuideDetailMain = new ArrayList<>();
		this.lstGuideHeadMain = new ArrayList<>();
		this.objGuideHeadMain = null;
		this.objInvoiceMain = new Invoice();
		this.facturaRegistrada = false;
	}

	private void cargaTablaDetalle() {
		String sMensaje = null;
		try {
			if(objGuideHeadMain != null) {
				this.lstGuideDetailMain = this.guiaService.getListaGuiasVentaDetalle(objGuideHeadMain.getId());
				this.cantidadRegistrosDetalle = lstGuideDetailMain.size();
			}
		} catch (BallartelyException e) {
			sMensaje = "Ocurrio un error en cargaTablaDetalle, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}

	private void obtenerGuiasHead() {
		if(objGuideHeadMain != null) {
			this.lstGuideHeadMain = new ArrayList<>();
			this.lstGuideHeadMain.add(objGuideHeadMain);
		}
	}
	
	private void cargarFactura() {
		if(objGuideHeadMain != null && !facturaRegistrada) {
			objInvoiceMain = facturaService.obtenerFacturasPorGuia(objGuideHeadMain).stream().findFirst().orElse(new Invoice());
			if(objInvoiceMain != null && objInvoiceMain.getInvoiceSerie() != null) {
				objInvoiceMain.setInvoiceSerie(objInvoiceMain.getInvoiceSerie().replaceFirst("F", ""));
				facturaRegistrada = true;
			}
		}
	}

	public List<GuideHead> getLstGuideHeadMain() {
		obtenerGuiasHead();
		return lstGuideHeadMain;
	}

	public void setLstGuideHeadMain(List<GuideHead> lstGuideHeadMain) {
		this.lstGuideHeadMain = lstGuideHeadMain;
	}

	public List<GuideDetailSales> getLstGuideDetailMain() {
		cargaTablaDetalle();
		return lstGuideDetailMain;
	}

	public void setLstGuideDetailMain(List<GuideDetailSales> lstGuideDetailMain) {
		this.lstGuideDetailMain = lstGuideDetailMain;
	}

	public GuiaService getGuiaService() {
		return guiaService;
	}

	public void setGuiaService(GuiaService guiaService) {
		this.guiaService = guiaService;
	}

	public FacturaService getFacturaService() {
		return facturaService;
	}

	public void setFacturaService(FacturaService facturaService) {
		this.facturaService = facturaService;
	}

	public GuideHead getObjGuideHeadMain() {
		return objGuideHeadMain;
	}

	public void setObjGuideHeadMain(GuideHead objGuideHeadMain) {
		this.objGuideHeadMain = objGuideHeadMain;
	}

	public Invoice getObjInvoiceMain() {
		cargarFactura();
		return objInvoiceMain;
	}

	public void setObjInvoiceMain(Invoice objInvoiceMain) {
		this.objInvoiceMain = objInvoiceMain;
	}

	public int getCantidadRegistrosDetalle() {
		return cantidadRegistrosDetalle;
	}

	public void setCantidadRegistrosDetalle(int cantidadRegistrosDetalle) {
		this.cantidadRegistrosDetalle = cantidadRegistrosDetalle;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public boolean isFacturaRegistrada() {
		return facturaRegistrada;
	}

	public void setFacturaRegistrada(boolean facturaRegistrada) {
		this.facturaRegistrada = facturaRegistrada;
	}
	
}
