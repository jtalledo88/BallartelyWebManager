package pe.com.foxsoft.ballartelyweb.managedbean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.data.Enterprise;
import pe.com.foxsoft.ballartelyweb.jpa.data.EnterpriseTransport;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.Transport;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.service.EmpresaService;
import pe.com.foxsoft.ballartelyweb.spring.service.MovimientoService;
import pe.com.foxsoft.ballartelyweb.spring.service.TransporteService;
import pe.com.foxsoft.ballartelyweb.spring.util.CalendarioModel;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class EmpresaMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private CuentaService cuentaService;
	
	@Autowired
	private MovimientoService movimientoService;
	
	@Autowired
	private TransporteService transporteService;

	private Enterprise empresa = new Enterprise();
	private List<Account> lstCuentaPrincipal;
	private List<Movement> lstMovements;
	
	private int canRegTablaPrincipal;
	private BigDecimal saldoCuentaTotal;
	
	private ScheduleModel calendarioEventos;
	
	private DualListModel<Transport> lstTransports;
	
	@PostConstruct
    public void init() {
		this.lstTransports = new DualListModel<>();
	}
	
	public EmpresaMB() {
		lstCuentaPrincipal = new ArrayList<>();
	}
	
	public void logOut() {
		
	}
	
	public void editarEmpresa() {
		String sMensaje = null;
		if("".equals(empresa.getRuc())) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el ruc.");
			return;
		}
		if("".equals(empresa.getSocialReason())) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la razón social.");
			return;
		}
		if("".equals(empresa.getAddress())) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar la dirección.");
			return;
		}
		if("".equals(empresa.getDistrict())) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar el distrito.");
			return;
		}
		
		try {
			empresaService.editarEmpresa(empresa);
			sMensaje = Constantes.MESSAGE_MERGE_SUCCESS;
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en editarEmpresa";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void openVerCuenta() {
		String sMensaje = null;
		try {
			lstCuentaPrincipal = new ArrayList<>();
			Account accountPrincipal = cuentaService.obtenerCuentaPrincipal();
			lstCuentaPrincipal.add(accountPrincipal);
			saldoCuentaTotal = cuentaService.getAmountAccountPrincipalDataBase(accountPrincipal.getId());
		} catch (BallartelyException e) {
			sMensaje = "Error en openVerCuenta";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void openGestionTransportes() {
		try {
			List<Transport> lstSource = this.transporteService.getListaTransportesDisponibles();
			List<Transport> lstTarget = this.transporteService.getListaTransportesEmpresa(this.empresa.getId());
			this.lstTransports = new DualListModel<>(lstSource, lstTarget);
		} catch (BallartelyException e) {
			String sMensaje = "Error en openGestionTransportes";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}
	
	public void openVerMovimientos() {
		try {
			Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

			int itemCuenta = Integer.parseInt((String) paramMap.get("itemAccount"));
			
			this.lstMovements = this.movimientoService.getListaMovimientosCuenta(itemCuenta, null, null);
			this.canRegTablaPrincipal = this.lstMovements.size();
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en openVerMovimientos, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
		
		
	}
	
	public void gestionarTransportes() {
		String sMensaje = null;
		try {
			List<Transport> lstTarget = this.lstTransports.getTarget();
			List<EnterpriseTransport> lstEnterpriseTransports = new ArrayList<>();
			for(Transport t: lstTarget) {
				EnterpriseTransport objEnterpriseTransport = new EnterpriseTransport();
				objEnterpriseTransport.setEnterprise(empresa);
				objEnterpriseTransport.setTransport(t);
				
				lstEnterpriseTransports.add(objEnterpriseTransport);
			}
			sMensaje = this.transporteService.gestionarTransporteEmpresa(this.empresa.getId(), lstEnterpriseTransports);
			Utilitarios.mensajeInfo("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en gestionarTransportes";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
	}
	
	private void cargarEmpresa() {
		try {
			String userName = (String)Utilitarios.getObjectInSession("userNameSes");
			empresa = empresaService.getEnterprise(userName);
		} catch (BallartelyException e) {
			this.logger.error(e.getMessage());
		}
	}
	
	public String mostrarNombreUsuario() {
		Authentication authentication =
		        SecurityContextHolder.getContext().getAuthentication();
		Utilitarios.putObjectInSession("userNameSes", authentication.getName());
		    return "Bienvenid@ " + authentication.getName() + ".";
	}
	
	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public CuentaService getCuentaService() {
		return cuentaService;
	}

	public void setCuentaService(CuentaService cuentaService) {
		this.cuentaService = cuentaService;
	}
	
	public TransporteService getTransporteService() {
		return transporteService;
	}

	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}

	public Enterprise getEmpresa() {
		cargarEmpresa();
		return empresa;
	}

	public void setEmpresa(Enterprise empresa) {
		cargarEmpresa();
		this.empresa = empresa;
	}
	
	public List<Account> getLstCuentaPrincipal() {
		return lstCuentaPrincipal;
	}

	public void setLstCuentaPrincipal(List<Account> lstCuentaPrincipal) {
		this.lstCuentaPrincipal = lstCuentaPrincipal;
	}
	
	public DualListModel<Transport> getLstTransports() {
		return lstTransports;
	}

	public void setLstTransports(DualListModel<Transport> lstTransports) {
		this.lstTransports = lstTransports;
	}

	public BigDecimal getSaldoCuentaTotal() {
		return saldoCuentaTotal;
	}

	public void setSaldoCuentaTotal(BigDecimal saldoCuentaTotal) {
		this.saldoCuentaTotal = saldoCuentaTotal;
	}

	public ScheduleModel getCalendarioEventos() {
		calendarioEventos = new CalendarioModel(this.movimientoService);
		return calendarioEventos;
	}

	public void setCalendarioEventos(ScheduleModel calendarioEventos) {
		this.calendarioEventos = calendarioEventos;
	}

	public MovimientoService getMovimientoService() {
		return movimientoService;
	}

	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}

	public List<Movement> getLstMovements() {
		return lstMovements;
	}

	public void setLstMovements(List<Movement> lstMovements) {
		this.lstMovements = lstMovements;
	}

	public int getCanRegTablaPrincipal() {
		return canRegTablaPrincipal;
	}

	public void setCanRegTablaPrincipal(int canRegTablaPrincipal) {
		this.canRegTablaPrincipal = canRegTablaPrincipal;
	}
	
	
	
}
