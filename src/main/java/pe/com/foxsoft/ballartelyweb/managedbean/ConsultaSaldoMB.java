package pe.com.foxsoft.ballartelyweb.managedbean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.FacesException;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.data.Customer;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ClienteService;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class ConsultaSaldoMB {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CuentaService cuentaService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private Propiedades propiedades;
	
	private Customer objCustomer;
	private Account objAccountCustomer;
	
	private Date fechaConsulta;
	private BigDecimal saldoConsulta;
	
	private List<Account> lstAccountsCustomer;
	private List<Customer> lstClientes;
	
	private boolean validaListaBuscar = true;

	public ConsultaSaldoMB() {
		
	}

	public void consultarSaldo() {
		try {
			if(objAccountCustomer == null) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe seleccionar la cuenta del cliente.");
				return;
			}
			
			this.validaListaBuscar = false;
			this.saldoConsulta = cuentaService.getAmountAccountDataBase(objAccountCustomer.getId());
			this.fechaConsulta = new Date();
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en consultarSaldo, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
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
	
	private void obtenerClientes() {
		try {
			this.lstClientes = this.clienteService.getListaClientes();
		} catch (BallartelyException e) {
			String sMensaje = "Error en obtenerClientes";
			this.logger.error(e.getMessage(), e);
			throw new FacesException(sMensaje, e);
		}
	}
	
	public void cargaCuentasCliente(AjaxBehaviorEvent event) {
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

	public Customer getObjCustomer() {
		return objCustomer;
	}

	public void setObjCustomer(Customer objCustomer) {
		this.objCustomer = objCustomer;
	}

	public Date getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	public BigDecimal getSaldoConsulta() {
		return saldoConsulta;
	}

	public void setSaldoConsulta(BigDecimal saldoConsulta) {
		this.saldoConsulta = saldoConsulta;
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

	public Propiedades getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Propiedades propiedades) {
		this.propiedades = propiedades;
	}

	public Account getObjAccountCustomer() {
		return objAccountCustomer;
	}

	public void setObjAccountCustomer(Account objAccountCustomer) {
		this.objAccountCustomer = objAccountCustomer;
	}

	public List<Account> getLstAccountsCustomer() {
		return lstAccountsCustomer;
	}

	public void setLstAccountsCustomer(List<Account> lstAccountsCustomer) {
		this.lstAccountsCustomer = lstAccountsCustomer;
	}
	
}
