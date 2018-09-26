package pe.com.foxsoft.ballartelyweb.managedbean;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.service.ReporteService;
import pe.com.foxsoft.ballartelyweb.spring.util.Propiedades;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@ManagedBean
public class ReporteMB implements Serializable{
	
	private static final long serialVersionUID = 1L;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ReporteService reporteService;
	
	@Autowired
	private Propiedades propiedades;
	
	private LineChartModel modelRVD;

	public ReporteMB() {
		
	}
	
	@PostConstruct
    public void init() {
		crearRVDModel();
    }
	
	private void crearRVDModel() {
		try {
			modelRVD = new LineChartModel();
			ChartSeries data = new ChartSeries();
		    data.setLabel("Ventas");
		    Map<String, Object> mapReport = this.reporteService.getReporteVentasPorDia();
		    long numberMin = 0;
		    long numberMax = 0;
		    for (Entry<String, Object> entry : mapReport.entrySet()) {
		    	numberMin = (Long)entry.getValue();
		    	data.set(entry.getKey(), numberMin);
		    	if(numberMin > numberMax) {
		    		numberMax = numberMin;
		    	}
			}
		 
		    modelRVD.addSeries(data);
		    
			modelRVD.setTitle("Reporte Ventas x Día");
			modelRVD.setLegendPosition("e");
			modelRVD.setShowPointLabels(false);
			modelRVD.getAxes().put(AxisType.X, new CategoryAxis("Días"));
			Axis yAxis = modelRVD.getAxis(AxisType.Y);
	        yAxis.setLabel("Cantidad");
	        yAxis.setMin(0);
	        yAxis.setTickInterval("10");
	        yAxis.setMax(numberMax + 10);
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en crearRVDModel, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
    }

	public ReporteService getReporteService() {
		return reporteService;
	}

	public void setReporteService(ReporteService reporteService) {
		this.reporteService = reporteService;
	}

	public Propiedades getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Propiedades propiedades) {
		this.propiedades = propiedades;
	}

	public LineChartModel getModelRVD() {
		return modelRVD;
	}
	
}
