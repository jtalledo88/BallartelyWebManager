package pe.com.foxsoft.ballartelyweb.managedbean;

import javax.annotation.ManagedBean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class EncriptadorMB {
	
	private String processValue;
	private String processResult;

	public void process() {
		try {
			if("".equals(processValue)) {
				Utilitarios.mensajeError("Campos Obligatorios", "Debe llenar valor.");
				return;
			}
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			processResult = passwordEncoder.encode(processValue);
			
		} catch (Exception e) {
			Utilitarios.mensajeError("", "Ocurrió un error: " + e.getMessage());
		}
	}

	public String getProcessValue() {
		return processValue;
	}

	public void setProcessValue(String processValue) {
		this.processValue = processValue;
	}

	public String getProcessResult() {
		return processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}
	
}
