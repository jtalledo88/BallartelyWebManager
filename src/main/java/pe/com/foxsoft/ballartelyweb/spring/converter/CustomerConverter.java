package pe.com.foxsoft.ballartelyweb.spring.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import pe.com.foxsoft.ballartelyweb.jpa.data.Customer;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ClienteService;

@Named
public class CustomerConverter implements Converter{

	@Inject
	private ClienteService clienteService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null || "".equals(value))
			return null;
		
		Customer customer = null;
		try {
			customer = this.clienteService.obtenerCliente(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BallartelyException e) {
			e.printStackTrace();
		}
		
		return customer;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null)
			return null;

		return String.valueOf(((Customer)value).getId());
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

}
