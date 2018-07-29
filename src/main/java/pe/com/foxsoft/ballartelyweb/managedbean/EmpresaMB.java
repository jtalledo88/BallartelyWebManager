package pe.com.foxsoft.ballartelyweb.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.FacesException;

import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.data.Client;
import pe.com.foxsoft.ballartelyweb.jpa.data.Enterprise;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.service.EmpresaService;
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

	private Enterprise empresa = new Enterprise();
	private List<Account> lstCuentaPrincipal;
	private double saldoCuentaTotal = 10.05;
	
	private ScheduleModel calendarioEventos;
	
	public EmpresaMB() {
		lstCuentaPrincipal = new ArrayList<>();
		calendarioEventos = new CalendarioModel();
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
			Account account = new Account();
			account.setAccountType(Constantes.ACCOUNT_TYPE_P);
			account.setClient(new Client());
			account.setAccountStatus(Constantes.STATUS_ACTIVE);
			lstCuentaPrincipal = cuentaService.obtenerCuentas(account);
		} catch (BallartelyException e) {
			sMensaje = "Error en openVerCuenta";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void openVerMovimientos() {
		
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

	public double getSaldoCuentaTotal() {
		return saldoCuentaTotal;
	}

	public void setSaldoCuentaTotal(double saldoCuentaTotal) {
		this.saldoCuentaTotal = saldoCuentaTotal;
	}

	public ScheduleModel getCalendarioEventos() {
		return calendarioEventos;
	}

	public void setCalendarioEventos(ScheduleModel calendarioEventos) {
		this.calendarioEventos = calendarioEventos;
	}
	
	
	
}
