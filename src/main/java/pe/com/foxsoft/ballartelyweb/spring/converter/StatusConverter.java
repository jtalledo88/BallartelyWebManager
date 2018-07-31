package pe.com.foxsoft.ballartelyweb.spring.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Named
public class StatusConverter implements Converter{
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null)
			return null;
		String statusDesc = null;
		if(Constantes.STATUS_ACTIVE.equals(String.valueOf(value))) {
			statusDesc = "Activo";
		}else if(Constantes.STATUS_INACTIVE.equals(String.valueOf(value))) {
			statusDesc = "Inactivo";
		}else if(Constantes.STATUS_PRODUCT_COLD.equals(String.valueOf(value))) {
			statusDesc = "Frio";
		}else if(Constantes.STATUS_PRODUCT_FRESH.equals(String.valueOf(value))) {
			statusDesc = "Fresco";
		}else if(Constantes.BENEFIED_YES.equals(String.valueOf(value))) {
			statusDesc = "SI";
		}else if(Constantes.BENEFIED_NO.equals(String.valueOf(value))) {
			statusDesc = "NO";
		}
		
		return statusDesc;
	}

}
