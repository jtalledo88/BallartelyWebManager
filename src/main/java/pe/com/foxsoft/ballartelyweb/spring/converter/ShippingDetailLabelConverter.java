package pe.com.foxsoft.ballartelyweb.spring.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetailLabel;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.CompraService;
import pe.com.foxsoft.ballartelyweb.spring.service.EtiquetaProductoService;

@Named
public class ShippingDetailLabelConverter implements Converter{

	@Inject
	private CompraService compraService;
	
	@Inject
	private EtiquetaProductoService etiquetaProductoService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null || "".equals(value))
			return null;
		
		ShippingDetailLabel detailLabel = null;
		try {
			String[] ids = value.split(",");
			if("0".equals(ids[0])) {
				ProductLabel productLabel = etiquetaProductoService.obtenerEtiquetaProducto(Integer.parseInt(ids[1]));
				detailLabel = new ShippingDetailLabel();
				detailLabel.setProductLabel(productLabel);
			}else {
				detailLabel = compraService.getComprasDetalleLabel(Integer.parseInt(ids[0]));
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BallartelyException e) {
			e.printStackTrace();
		}
		
		return detailLabel;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null)
			return null;
		ShippingDetailLabel detailLabel = (ShippingDetailLabel)value;
		
		return String.valueOf(detailLabel.getShippingDetailLabelId() + "," + detailLabel.getProductLabel().getProductLabelId());
	}

	public CompraService getCompraService() {
		return compraService;
	}

	public void setCompraService(CompraService compraService) {
		this.compraService = compraService;
	}
	
	public EtiquetaProductoService getEtiquetaProductoService() {
		return etiquetaProductoService;
	}

	public void setEtiquetaProductoService(EtiquetaProductoService etiquetaProductoService) {
		this.etiquetaProductoService = etiquetaProductoService;
	}
	
}
