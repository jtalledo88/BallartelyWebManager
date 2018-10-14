package pe.com.foxsoft.ballartelyweb.batch.scheduler;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ParametroGeneralService;
import pe.com.foxsoft.ballartelyweb.spring.util.ApplicationProperties;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Component
public class BackUpScheduler {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ParametroGeneralService parametroGeneralService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Scheduled(cron = "0 59 23 * * *")
	public void backUpScheduler() {
		try {
			logger.debug("Inicia proceso de backUpScheduler " + Utilitarios.getTime());
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
			logger.debug("Termina proceso de backUpScheduler " + Utilitarios.getTime());
		} catch (BallartelyException e) {
			logger.error("Error en backUpScheduler: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Error en backUpScheduler: " + e.getMessage(), e);
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
	
}
