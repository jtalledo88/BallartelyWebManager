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
import pe.com.foxsoft.ballartelyweb.jpa.data.User;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.service.UsuarioService;
import pe.com.foxsoft.ballartelyweb.spring.util.CalendarioModel;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class UsuarioMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private CuentaService cuentaService;

	private User usuario = new User();
	private List<Account> lstCuentaPrincipal;
	private double saldoCuentaTotal = 10.05;
	
	private ScheduleModel calendarioEventos;
	
	public UsuarioMB() {
		lstCuentaPrincipal = new ArrayList<>();
		calendarioEventos = new CalendarioModel();
	}
	
	public void logOut() {
		
	}
	
	public void editarUsuario() {
		String sMensaje = null;
		if("".equals(usuario.getUserCompleteNames())) {
			Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar los nombres del usuario.");
			return;
		}
		
		try {
			usuarioService.editarUsuario(usuario);
		} catch (BallartelyException e) {
			sMensaje = "Error en editarUsuario";
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
			sMensaje = "Error en editarUsuario";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void openVerMovimientos() {
		
	}
	
	public String mostrarNombreUsuario() {
		Authentication authentication =
		        SecurityContextHolder.getContext().getAuthentication();

		    return "Bienvenid@ " + authentication.getName() + ".";
	}
	
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public CuentaService getCuentaService() {
		return cuentaService;
	}

	public void setCuentaService(CuentaService cuentaService) {
		this.cuentaService = cuentaService;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
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
