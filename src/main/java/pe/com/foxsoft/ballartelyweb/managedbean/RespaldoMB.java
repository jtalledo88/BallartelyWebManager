package pe.com.foxsoft.ballartelyweb.managedbean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.ManagedBean;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.util.ApplicationProperties;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class RespaldoMB {
	
	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	private String processResult;

	public void process() {
		try {
			String path = this.parametroGeneralService.obtenerParametroGeneral(Constantes.BACKUP_PATH_CODE).getParamValue();
			String mysql = this.parametroGeneralService.obtenerParametroGeneral(Constantes.MYSQL_CODE).getParamValue();
			String file = path + "BK_" + Utilitarios.getTime() + ".sql";
			String usr = applicationProperties.getDbUser();
			String pwd = applicationProperties.getDbPassword();
			String dbname = Constantes.SCHEMA_BD;
			String command = mysql + "mysqldump -u " + usr + " -p" + pwd + " " + dbname;
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(command);
			InputStream is = p.getInputStream();
			FileUtils.copyInputStreamToFile(is, new File(file));
			Utilitarios.mensaje("", "Archivo de respaldo generado: " + file);
		} catch (BallartelyException e) {
			Utilitarios.mensajeError("", "Ocurrió un error: " + e.getMessage());
		} catch (IOException e) {
			Utilitarios.mensajeError("", "Ocurrió un error: " + e.getMessage());
		}
	}

	public String getProcessResult() {
		return processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public ParametroGeneralService getParametroGeneralService() {
		return parametroGeneralService;
	}

	public void setParametroGeneralService(ParametroGeneralService parametroGeneralService) {
		this.parametroGeneralService = parametroGeneralService;
	}

	public ApplicationProperties getApplicationProperties() {
		return applicationProperties;
	}

	public void setApplicationProperties(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
}
