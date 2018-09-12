package pe.com.foxsoft.ballartelyweb.spring.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import pe.com.foxsoft.ballartelyweb.jpa.data.Transport;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.TransporteService;

@Named
public class TransportConverter implements Converter{

	@Inject
	private TransporteService transporteService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null || "".equals(value))
			return null;
		
		Transport transport = null;
		try {
			transport = this.transporteService.obtenerTransporte(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BallartelyException e) {
			e.printStackTrace();
		}
		
		return transport;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null || "".equals(value))
			return null;

		return String.valueOf(((Transport)value).getId());
	}

	public TransporteService getTransporteService() {
		return transporteService;
	}

	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}

}
