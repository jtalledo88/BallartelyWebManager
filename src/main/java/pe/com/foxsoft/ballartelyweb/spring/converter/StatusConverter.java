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
		}else if(Constantes.STATUS_INACTIVE.equals(String.valueOf(value))){
			statusDesc = "Inactivo";
		}else if((String.valueOf(value).startsWith(Constantes.STATUS_PRODUCT_COLD))){
			statusDesc = "Frio " + String.valueOf(value).substring(String.valueOf(value).length() - 1);
		}else if((String.valueOf(value).startsWith(Constantes.STATUS_PRODUCT_FRESH))){
			statusDesc = "Fresco";
		}else if(Constantes.DETAIL_LABEL_TYPE_ORIGIN.equals(String.valueOf(value))){
			statusDesc = "Original";
		}else if(Constantes.DETAIL_LABEL_TYPE_ADDITIONAL.equals(String.valueOf(value))){
			statusDesc = "Adicional";
		}
		
		return statusDesc;
	}

}
