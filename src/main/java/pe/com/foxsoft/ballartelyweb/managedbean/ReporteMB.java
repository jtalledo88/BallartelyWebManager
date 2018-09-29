package pe.com.foxsoft.ballartelyweb.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pe.com.foxsoft.ballartelyweb.spring.domain.ColumnModel;
import pe.com.foxsoft.ballartelyweb.spring.domain.Reporte;
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
	private BarChartModel modelRVC;
	private PieChartModel modelRVP;
	
	private List<Reporte> lstReportVD;
	private List<Reporte> lstReportVC;
	private List<Reporte> lstReportVP;
	
	private List<ColumnModel> columnsRVD;
	private List<ColumnModel> columnsRVC;
	private List<ColumnModel> columnsRVP;

	public ReporteMB() {
		
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
	        //Crea lista
	        lstReportVD = new ArrayList<>();
	        lstReportVD.add(new Reporte(mapReport));
	        columnsRVD = new ArrayList<>();
	        crearColumnas(columnsRVD, mapReport);
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en crearRVDModel, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
    }
	
	@SuppressWarnings("unchecked")
	private void crearRVCModel() {
		try {
			modelRVC = new BarChartModel();
		    Map<String, Object> mapReport = this.reporteService.getReporteVentasPorCliente();
		    long numberMin = 0;
		    long numberMax = 0;
		    Map<String, Object> mapData = new LinkedHashMap<>();
		    for (Entry<String, Object> entry : mapReport.entrySet()) {
		    	ChartSeries serie = new ChartSeries();
		    	serie.setLabel(entry.getKey());
		    	long numberCount = 0;
		    	List<Object[]> lstEntry = (List<Object[]>) entry.getValue();
		    	for(Object[] arr: lstEntry) {
		    		numberMin = (Long)arr[2];
		    		numberCount += numberMin;
		    		serie.set(arr[1], numberMin);
			    	if(numberMin > numberMax) {
			    		numberMax = numberMin;
			    	}
		    	}
		    	mapData.put(entry.getKey(), numberCount);
		    	modelRVC.addSeries(serie);
			}
		 
			modelRVC.setTitle("Reporte Ventas x Cliente");
			modelRVC.setAnimate(true);
			modelRVC.setLegendPosition("ne");
			Axis yAxis = modelRVC.getAxis(AxisType.Y);
	        yAxis.setMin(0);
	        yAxis.setTickInterval("10");
	        yAxis.setMax(numberMax + 10);
	        //Crea lista
	        lstReportVC = new ArrayList<>();
	        lstReportVC.add(new Reporte(mapData));
	        columnsRVC = new ArrayList<>();
	        crearColumnas(columnsRVC, mapData);
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en crearRVCModel, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
    }
	
	private void crearRVPModel() {
		try {
			modelRVP = new PieChartModel();
		    Map<String, Object> mapReport = this.reporteService.getReporteVentasPorPeriodo();
		    for (Entry<String, Object> entry : mapReport.entrySet()) {
		    	modelRVP.set(entry.getKey(), (Long)entry.getValue());
			}
		    
		    modelRVP.setTitle("Reporte Ventas x Periodo");
		    modelRVP.setLegendPosition("w");
		    modelRVP.setShadow(false);
		    //Crea lista
	        lstReportVP = new ArrayList<>();
	        lstReportVP.add(new Reporte(mapReport));
	        columnsRVP = new ArrayList<>();
	        crearColumnas(columnsRVP, mapReport);
		} catch (BallartelyException e) {
			String sMensaje = "Ocurrió un error en crearRVPModel, intente nuevamente.";
			this.logger.error(e.getMessage());
			Utilitarios.mensajeError("Excepcion", sMensaje);
		}
    }
	
	private void crearColumnas(List<ColumnModel> columns, Map<String, Object> mapData) {
         
		for (Entry<String, Object> entry : mapData.entrySet()) {
             columns.add(new ColumnModel(entry.getKey(), entry.getKey()));
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
		crearRVDModel();
		return modelRVD;
	}

	public BarChartModel getModelRVC() {
		crearRVCModel();
		return modelRVC;
	}

	public PieChartModel getModelRVP() {
		crearRVPModel();
		return modelRVP;
	}

	public List<Reporte> getLstReportVD() {
		return lstReportVD;
	}

	public void setLstReportVD(List<Reporte> lstReportVD) {
		this.lstReportVD = lstReportVD;
	}

	public List<ColumnModel> getColumnsRVD() {
		return columnsRVD;
	}

	public void setColumnsRVD(List<ColumnModel> columnsRVD) {
		this.columnsRVD = columnsRVD;
	}

	public List<Reporte> getLstReportVC() {
		return lstReportVC;
	}

	public void setLstReportVC(List<Reporte> lstReportVC) {
		this.lstReportVC = lstReportVC;
	}

	public List<ColumnModel> getColumnsRVC() {
		return columnsRVC;
	}

	public void setColumnsRVC(List<ColumnModel> columnsRVC) {
		this.columnsRVC = columnsRVC;
	}

	public List<Reporte> getLstReportVP() {
		return lstReportVP;
	}

	public void setLstReportVP(List<Reporte> lstReportVP) {
		this.lstReportVP = lstReportVP;
	}

	public List<ColumnModel> getColumnsRVP() {
		return columnsRVP;
	}

	public void setColumnsRVP(List<ColumnModel> columnsRVP) {
		this.columnsRVP = columnsRVP;
	}
	
}
