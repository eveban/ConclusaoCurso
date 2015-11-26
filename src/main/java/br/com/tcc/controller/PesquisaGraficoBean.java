package br.com.tcc.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.com.tcc.modelo.Medida;
import br.com.tcc.repository.MedidaRepository;
import br.com.tcc.repository.filter.MedidaFilter;

@Named
@RequestScoped
public class PesquisaGraficoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("HH");
	private LineChartModel model;
	private List<Integer> list;
	private MedidaFilter filtro;

	private String data;

	@Inject
	private MedidaRepository medidaRepository;

	public PesquisaGraficoBean() {
		filtro = new MedidaFilter();
	}

	public void preRender() {
		model = new LineChartModel();
		this.model.setTitle("Temperaturas");
		this.model.setLegendPosition("e");
		this.model.setAnimate(true);
		this.model.setShowPointLabels(true);
		this.model.getAxes().put(AxisType.X, new CategoryAxis("Horários"));
		Axis yAxis = model.getAxis(AxisType.Y);
		// Axis xAxis = model.getAxis(AxisType.X);

		yAxis.setMin(-40);
		yAxis.setMax(40);
		yAxis.setTickCount(17);
		// xAxis.setTickAngle(90);
		
		adicionarSerie("Temperatura");
		// maxValorMes();
	}

	public void pesquisar() {
		/*
		 * model = new LineChartModel(); this.model.setTitle("Temperaturas");
		 * this.model.setLegendPosition("e"); this.model.setAnimate(true);
		 * this.model.getAxes().put(AxisType.X, new
		 * CategoryAxis("Temperaturas")); Axis yAxis =
		 * model.getAxis(AxisType.Y); Axis xAxis = model.getAxis(AxisType.X);
		 * yAxis.setMin(-40); yAxis.setMax(40); yAxis.setTickCount(17);
		 * xAxis.setTickAngle(90);
		 * 
		 * adicionarSerie("Temperatura");
		 */
		preRender();
	}

	private void adicionarSerie(String rotulo) {
		ChartSeries series = new ChartSeries(rotulo);
		List<Medida> filtrados = medidaRepository.filtrados(filtro);

		for (Medida medida : filtrados) {
			series.set(DATE_FORMAT.format(medida.getHora()), medida.getValor());
		}

		this.model.addSeries(series);
	}

	public LineChartModel getModel() {
		return model;
	}

	public void setModel(LineChartModel model) {
		this.model = model;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

	public MedidaFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(MedidaFilter filtro) {
		this.filtro = filtro;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
