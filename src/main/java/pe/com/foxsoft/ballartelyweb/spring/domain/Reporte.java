package pe.com.foxsoft.ballartelyweb.spring.domain;

import java.util.Map;

public class Reporte {
	
	private Map<String, Object> data;

	
	public Reporte(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	
}
