package pe.com.foxsoft.ballartelyweb.spring.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import pe.com.foxsoft.ballartelyweb.spring.domain.TipoParametro;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

public class Utilitarios {
	private static String NUMEROS = "0123456789";
	private static String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
	private static String ESPECIALES = "��@";

	public static String getRuc() {
		return MAYUSCULAS + MINUSCULAS;
	}

	public static String getContrasenia() {
		return getContrasenia(20);
	}

	public static String getContrasenia(int length) {
		return getContrasenia(NUMEROS + MAYUSCULAS + MINUSCULAS + ESPECIALES, length);
	}

	private static String getContrasenia(String key, int length) {
		String pswd = "";
		for (int i = 0; i < length; i++) {
			pswd = pswd + key.charAt((int) (Math.random() * key.length()));
		}
		return pswd;
	}

	public static Timestamp getFechaActual() {
		Date fecha = new Date();
		Timestamp time = new Timestamp(fecha.getTime());
		return time;
	}

	public static String formatoFecha(Date fecha) {
		String fechaActualizacion = "";
		String horaActualizacion = "";

		Calendar calFechaAct = Calendar.getInstance();
		calFechaAct.setTimeInMillis(fecha.getTime());
		fechaActualizacion = calFechaAct.get(5) + "/" + calFechaAct.get(2) + "/" + calFechaAct.get(1);
		horaActualizacion = calFechaAct.get(10) + ":" + calFechaAct.get(12) + ":" + calFechaAct.get(13);

		return fechaActualizacion + " " + horaActualizacion;
	}

	public static boolean compararCadenas(String fijo, String prmt) {
		if (fijo.toUpperCase().toString().startsWith(prmt.toUpperCase())) {
			return true;
		}
		return false;
	}

	public static boolean compararNumeros(String fijo, String prmt) {
		if (fijo.toString().startsWith(prmt.toString())) {
			return true;
		}
		return false;
	}

	public static void mensaje(String titulo, String mensaje) {
		FacesContext ct = FacesContext.getCurrentInstance();
		ct.addMessage(null, new FacesMessage(titulo, mensaje));
	}

	public static void mensajeInfo(String titulo, String mensaje) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje));
	}

	public static void mensajeError(String titutlo, String mensaje) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, titutlo, mensaje));
	}

	public static void putObjectInSession(String value, Object var) {
		FacesContext faces = FacesContext.getCurrentInstance();
		if(faces == null)
			return;
		ExternalContext context = faces.getExternalContext();
		HttpSession session = (HttpSession) context.getSession(true);
		session.setAttribute(value, var);
	}

	public static Object getObjectInSession(String value) {
		FacesContext faces = FacesContext.getCurrentInstance();
		if(faces == null)
			return null;
		ExternalContext context = faces.getExternalContext();
		HttpSession sessionhttp = (HttpSession) context.getSession(true);
		return sessionhttp.getAttribute(value);
	}

	public static void removeObjectInSession(String value) {
		FacesContext faces = FacesContext.getCurrentInstance();
		if(faces == null)
			return;
		ExternalContext context = faces.getExternalContext();
		HttpSession session = (HttpSession) context.getSession(false);
		session.removeAttribute(value);
		session.invalidate();
	}
	
	public static void redirectPage(String page) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + page);
		} catch (IOException e) {
			
		}
	}

	public static String remove2(String input) {
		String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

		Pattern pattern = Pattern.compile("\\P{ASCII}+");
		return pattern.matcher(normalized).replaceAll("");
	}

	public static List<TipoParametro> obtenerListaTipoParametros(String cadena) throws BallartelyException {
		try {
			List<TipoParametro> lstValores = new ArrayList<TipoParametro>();
			String[] arrCadena = cadena.split(",");
			TipoParametro tipoParametro = null;
			for (String s : arrCadena) {
				String[] arrValores = s.split(":");
				tipoParametro = new TipoParametro();
				tipoParametro.setItem(arrValores[0]);
				tipoParametro.setDescription(arrValores[1]);

				lstValores.add(tipoParametro);
			}

			return lstValores;
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public static void guardarArchivo(String directorio, String archivo, InputStream data) throws BallartelyException {
		OutputStream out = null;
		try {
			File parent = new File(directorio);
			File file = new File(parent, archivo);
			out = FileUtils.openOutputStream(file);
			FileUtils.copyInputStreamToFile(data, file);
			
		} catch (IOException e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					
				}
				
			}
		}
	}
	
	public static InputStream obtenerArchivo(String directorio, String archivo) throws BallartelyException {
		InputStream in = null;
		try {
			File parent = new File(directorio);
			File file = new File(parent, archivo);
			in = FileUtils.openInputStream(file);
			return in;
		} catch (IOException e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public static String obtenerExtension(String fileName) {
		String[] vals = StringUtils.split(fileName, ".");
		return vals[vals.length - 1];
	}

	public static String vacioComoNulo(String value) {
		return StringUtils.trimToNull(value);
	}

	public static String obtenerTipoArchivo(String fileName) {
		String ext = obtenerExtension(fileName);
		String tipo = null;
		switch(ext) {
		case "jpg":
			tipo = "image/jpg";
			break;
		case "jpeg":
			tipo = "image/jpeg";
			break;
		case "png":
			tipo = "image/png";
			break;
		case "gif":
			tipo = "image/gif";
			break;
		}
		return tipo;
	}

	public static String reemplazarMensaje(String mensaje, Object... val) {
		return String.format(mensaje, val);
	}

	public static int getEntero(String value) {
		if(value == null) 
			return 0;
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static BigDecimal getDecimal(String value) {
		if(value == null) 
			return BigDecimal.ZERO;
		try {
			return new BigDecimal(value);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	public static String rellenarCadena(String cadena, int longi, String caracter) {
		return StringUtils.leftPad(cadena, longi, caracter);
	}
	
}
