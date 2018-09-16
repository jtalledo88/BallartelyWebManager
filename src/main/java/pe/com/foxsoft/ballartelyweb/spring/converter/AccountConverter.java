package pe.com.foxsoft.ballartelyweb.spring.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.CuentaService;

@Named
public class AccountConverter implements Converter{

	@Inject
	private CuentaService cuentaService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null || "".equals(value))
			return null;
		
		Account account = null;
		try {
			account = this.cuentaService.obtenerCuenta(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BallartelyException e) {
			e.printStackTrace();
		}
		
		return account;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null || "".equals(value))
			return null;

		return String.valueOf(((Account)value).getId());
	}

	public CuentaService getCuentaService() {
		return cuentaService;
	}

	public void setCuentaService(CuentaService cuentaService) {
		this.cuentaService = cuentaService;
	}

	

}
