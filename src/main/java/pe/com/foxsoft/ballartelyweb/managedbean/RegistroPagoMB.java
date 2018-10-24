package pe.com.foxsoft.ballartelyweb.managedbean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.data.Customer;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ClienteService;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.service.MovimientoService;
import pe.com.foxsoft.ballartelyweb.spring.service.ProveedorService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class RegistroPagoMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CuentaService cuentaService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private MovimientoService movimientoService;
	
	@Autowired
	private ProveedorService proveedorService;
	
	@Autowired
	private Propiedades propiedades;
	
	private Customer objCustomer;
	private Account objAccountCustomer;
	private Provider objProvider;
	
	private BigDecimal amountAccount;
	private BigDecimal amountToPay;
	private boolean accountClient;
	
	private List<Customer> lstClientes;
	private List<Account> lstAccountsCustomer;
	private List<Provider> lstProveedores;

	public RegistroPagoMB() {
		this.lstAccountsCustomer = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		cargaTipoCuenta(null);
	}
	
	public void registrarPago() {
		String sMensaje = "";
		
		try {
			
			if(this.accountClient && this.objCustomer == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar el cliente.");
				return;
			}
			if(this.objAccountCustomer == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar una cuenta.");
				return;
			}
			if(this.amountAccount == null || BigDecimal.ZERO.compareTo(this.amountAccount) == 0) {
				Utilitarios.mensajeError("Campos Obligatorios", "El monto de deuda es 0.");
				return;
			}
			if(this.amountToPay == null || BigDecimal.ZERO.compareTo(this.amountToPay) == 0) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe ingresar un monto mayor a 0.");
				return;
			}
			
			Movement movement = new Movement();
			movement.setAccount(this.objAccountCustomer);
			movement.setMovementAmount(this.amountToPay);
			movement.setMovementObservation(Constantes.MOVEMENT_OBSERVATION_PAY);
			movement.setMovementQuantity(0);
			movement.setMovementType(Constantes.MOVEMENT_TYPE_PAY);
			movement.setPaymentDocumentnumber(null);
			movement.setCustomer(this.objCustomer);
			movement.setProvider(this.objProvider);
			
			movement = this.movimientoService.agregarMovimiento(movement);
			sMensaje = Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_SUCCESS, movement.getId());
			reiniciarFormulario();
			Utilitarios.mensaje("", sMensaje);
		} catch (BallartelyException e) {
			sMensaje = "Error en registrarPago";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}

	public void cargaTipoCuenta(AjaxBehaviorEvent event) {
		this.amountAccount = null;
		this.objCustomer = null;
		if(this.accountClient) {
			this.objProvider = null;
			this.lstAccountsCustomer = new ArrayList<>();
		}else {
			cargaCuentaPrincipal();
		}
		
	}
	
	public void cargaCuentasCliente(AjaxBehaviorEvent event) {
		this.amountAccount = null;
		cargaCuentasCliente();
		if(Constantes.CUSTOMER_TYPE_MOROSO.equals(this.objCustomer.getCustomerType())) {
			Utilitarios.mensajeInfo("Advertencia", "El cliente seleccionado tiene calificación morosa.");
		}
	}
	
	private void cargaCuentasCliente() {
		try {
			this.lstAccountsCustomer = this.cuentaService.obtenerCuentas(this.objCustomer.getId());
		} catch (BallartelyException e) {
			String sMensaje = "Error en cargaCuentasCliente";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
		
	}
	
	private void cargaCuentaPrincipal() {
		try {
			this.lstAccountsCustomer = new ArrayList<>();
			this.lstAccountsCustomer.add(this.cuentaService.obtenerCuentaPrincipal());
			
		} catch (BallartelyException e) {
			String sMensaje = "Error en cargaCuentasCliente";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
		
	}

	public void cargaSaldoCuenta(AjaxBehaviorEvent event) {
		try {
			if(this.objAccountCustomer == null || (this.objProvider == null && !this.accountClient)) {
				this.amountAccount = BigDecimal.ZERO;
			}else {
				if(this.accountClient) {
					this.amountAccount = this.cuentaService.getAmountAccountDataBase(this.objAccountCustomer.getId());
				}else {
					this.amountAccount = this.cuentaService.getAmountAccountPrincipalDataBase(this.objAccountCustomer.getId(), this.objProvider.getId());
				}
			}
		} catch (BallartelyException e) {
			String sMensaje = "Error en cargaSaldoCuenta";
			this.logger.error(e.getMessage());
			throw new FacesException(sMensaje, e);
		}
	}
	
	public List<Provider> completeProvider(String query) {
        List<Provider> lstProveedoresFiltro = new ArrayList<Provider>();
        obtenerProveedores();
        for (int i = 0; i < lstProveedores.size(); i++) {
        	Provider provider = lstProveedores.get(i);
            if(provider.getProviderSocialReason().toLowerCase().indexOf(query.toLowerCase()) != -1) {
            	lstProveedoresFiltro.add(provider);
            }
        }
         
        return lstProveedoresFiltro;
    }

	public List<Customer> completeCustomer(String query) {
        List<Customer> lstClientesFiltro = new ArrayList<Customer>();
        obtenerClientes();
        for (int i = 0; i < lstClientes.size(); i++) {
        	Customer customer = lstClientes.get(i);
            if(customer.getCustomerNames().toLowerCase().indexOf(query.toLowerCase()) != -1) {
            	lstClientesFiltro.add(customer);
            }
        }
         
        return lstClientesFiltro;
    }
	
	private void obtenerProveedores() {
		try {
			this.lstProveedores = this.proveedorService.getListaProveedores();
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerProveedores";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}
	
	private void reiniciarFormulario() {
		this.amountToPay = null;
		cargaSaldoCuenta(null);
	}
	
	private void obtenerClientes() {
		try {
			this.lstClientes = this.clienteService.getListaClientes();
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerClientes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}

	public CuentaService getCuentaService() {
		return cuentaService;
	}

	public void setCuentaService(CuentaService cuentaService) {
		this.cuentaService = cuentaService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public Propiedades getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Propiedades propiedades) {
		this.propiedades = propiedades;
	}

	public List<Customer> getLstClientes() {
		return lstClientes;
	}

	public void setLstClientes(List<Customer> lstClientes) {
		this.lstClientes = lstClientes;
	}

	public List<Account> getLstAccountsCustomer() {
		return lstAccountsCustomer;
	}

	public void setLstAccountsCustomer(List<Account> lstAccountsCustomer) {
		this.lstAccountsCustomer = lstAccountsCustomer;
	}

	public BigDecimal getAmountAccount() {
		return amountAccount;
	}

	public void setAmountAccount(BigDecimal amountAccount) {
		this.amountAccount = amountAccount;
	}

	public boolean isAccountClient() {
		return accountClient;
	}

	public void setAccountClient(boolean accountClient) {
		this.accountClient = accountClient;
	}

	public BigDecimal getAmountToPay() {
		return amountToPay;
	}

	public void setAmountToPay(BigDecimal amountToPay) {
		this.amountToPay = amountToPay;
	}

	public Customer getObjCustomer() {
		return objCustomer;
	}

	public void setObjCustomer(Customer objCustomer) {
		this.objCustomer = objCustomer;
	}

	public Account getObjAccountCustomer() {
		return objAccountCustomer;
	}

	public void setObjAccountCustomer(Account objAccountCustomer) {
		this.objAccountCustomer = objAccountCustomer;
	}

	public MovimientoService getMovimientoService() {
		return movimientoService;
	}

	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}

	public ProveedorService getProveedorService() {
		return proveedorService;
	}

	public void setProveedorService(ProveedorService proveedorService) {
		this.proveedorService = proveedorService;
	}

	public List<Provider> getLstProveedores() {
		return lstProveedores;
	}

	public void setLstProveedores(List<Provider> lstProveedores) {
		this.lstProveedores = lstProveedores;
	}

	public Provider getObjProvider() {
		return objProvider;
	}

	public void setObjProvider(Provider objProvider) {
		this.objProvider = objProvider;
	}
	
}
