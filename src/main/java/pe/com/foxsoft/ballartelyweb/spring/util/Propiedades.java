package pe.com.foxsoft.ballartelyweb.spring.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("/ballartelyweb.properties")
public class Propiedades {

	@Value("${ballartelyweb.parameters.types}")
	private String tiposParametro;
	
	@Value("${ballartelyweb.combo.status}")
	private String comboEstados;
	
	@Value("${ballartelyweb.combo.doctype}")
	private String comboTiposDocumento;

	@Value("${ballartelyweb.combo.clienttype}")
	private String comboTiposCliente;
	
	@Value("${ballartelyweb.uniquecode.igv}")
	private String uniqueCodeIGV;
	
	@Value("${ballartelyweb.uniquecode.upload}")
	private String uniqueCodeUpload;
	
	public String getTiposParametro() {
		return tiposParametro;
	}

	public void setTiposParametro(String tiposParametro) {
		this.tiposParametro = tiposParametro;
	}

	public String getComboEstados() {
		return comboEstados;
	}

	public void setComboEstados(String comboEstados) {
		this.comboEstados = comboEstados;
	}

	public String getComboTiposDocumento() {
		return comboTiposDocumento;
	}

	public void setComboTiposDocumento(String comboTiposDocumento) {
		this.comboTiposDocumento = comboTiposDocumento;
	}

	public String getComboTiposCliente() {
		return comboTiposCliente;
	}

	public void setComboTiposCliente(String comboTiposCliente) {
		this.comboTiposCliente = comboTiposCliente;
	}

	public String getUniqueCodeIGV() {
		return uniqueCodeIGV;
	}

	public void setUniqueCodeIGV(String uniqueCodeIGV) {
		this.uniqueCodeIGV = uniqueCodeIGV;
	}

	public String getUniqueCodeUpload() {
		return uniqueCodeUpload;
	}

	public void setUniqueCodeUpload(String uniqueCodeUpload) {
		this.uniqueCodeUpload = uniqueCodeUpload;
	}
	
}
