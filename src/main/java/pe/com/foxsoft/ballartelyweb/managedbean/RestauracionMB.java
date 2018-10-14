package pe.com.foxsoft.ballartelyweb.managedbean;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.util.ApplicationProperties;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class RestauracionMB {
	
	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	private String objSelectedFile;
	
	private List<String> lstFiles;
	
	public void process() {
		OutputStream os = null;
		try {
			if(this.objSelectedFile == null || "".equals(this.objSelectedFile)) {
				Utilitarios.mensajeInfo("Validación", "Seleccione un archivo de restauracion.");
				return;
			}
			String path = this.parametroGeneralService.obtenerParametroGeneral(Constantes.BACKUP_PATH_CODE).getParamValue();
			String mysql = this.parametroGeneralService.obtenerParametroGeneral(Constantes.MYSQL_CODE).getParamValue();
			String usr = applicationProperties.getDbUser();
			String pwd = applicationProperties.getDbPassword();
			String dbname = Constantes.SCHEMA_BD;
			String command = mysql + "mysql -u " + usr + " -p" + pwd + " " + dbname;
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(command);
			os = p.getOutputStream();
			FileUtils.copyFile(new File(new File(path), this.objSelectedFile), os);
			Utilitarios.mensaje("", "Base de datos restaurada.: " + this.objSelectedFile);
		} catch (BallartelyException e) {
			Utilitarios.mensajeError("", "Ocurrió un error: " + e.getMessage());
		} catch (IOException e) {
			Utilitarios.mensajeError("", "Ocurrió un error: " + e.getMessage());
		} finally {
			if(os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void cargarFiles() {
		try {
			String path = this.parametroGeneralService.obtenerParametroGeneral(Constantes.BACKUP_PATH_CODE).getParamValue();
			File dir = new File(path);
			lstFiles = new ArrayList<>();
			String[] arr = dir.list();
			for(String f: arr) {
				lstFiles.add(f);
			}
		} catch (BallartelyException e) {
			Utilitarios.mensajeError("", "Ocurrió un error: " + e.getMessage());
		}
		
		
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

	public String getObjSelectedFile() {
		return objSelectedFile;
	}

	public void setObjSelectedFile(String objSelectedFile) {
		this.objSelectedFile = objSelectedFile;
	}

	public List<String> getLstFiles() {
		cargarFiles();
		return lstFiles;
	}

	public void setLstFiles(List<String> lstFiles) {
		this.lstFiles = lstFiles;
	}
	
}
