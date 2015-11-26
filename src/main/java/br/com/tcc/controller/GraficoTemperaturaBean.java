package br.com.tcc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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

@Named
@RequestScoped
public class GraficoTemperaturaBean {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("HH");
	private List<Medida> listaTemperaturas;
	private List<Integer> list;
	private Medida medida;
	private Float maxTemperatura;
	private Float minTemperatura;
	private Float maxDiaTemperatura;
	private Float minDiaTemperatura;
	private Float atual;

	@Inject
	private MedidaRepository medidaRepository;
	private LineChartModel model;

	public void preRender() {
		list = new ArrayList<Integer>();
		model = new LineChartModel();
		this.model.setTitle("Temperaturas");
		this.model.setLegendPosition("e");
		this.model.setAnimate(true);
		this.model.setShowPointLabels(true);
		this.model.getAxes().put(AxisType.X, new CategoryAxis("Horários"));
		Axis yAxis = model.getAxis(AxisType.Y);
		//Axis xAxis = model.getAxis(AxisType.X);
		yAxis.setMin(-40);
		yAxis.setMax(40);
		yAxis.setTickCount(17);
		//xAxis.setTickAngle(90);
		adicionarSerie("Temperatura");
		// maxValorMes();
	}

	private void adicionarSerie(String rotulo) {
		ChartSeries series = new ChartSeries(rotulo);
		List<Medida> lista = medidaRepository.porData();
		for (Medida medida : lista) {
			series.set(DATE_FORMAT.format(medida.getHora()), medida.getValor());
		}
		this.model.addSeries(series);
	}

	@PostConstruct
	public void maxValorMes() {
		this.maxTemperatura = medidaRepository.maxMedidaMes();
		this.minTemperatura = medidaRepository.minMedidaMes();
		this.maxDiaTemperatura = medidaRepository.maxMedidaDia();
		this.minDiaTemperatura = medidaRepository.minMedidaDia();
		this.atual = medidaRepository.temperaturaAtual();
		System.out.println(maxTemperatura);
	}

	public void inicializar() {

	}

	public LineChartModel getModel() {
		return model;
	}

	public List<Medida> getListaTemperaturas() {
		if (this.listaTemperaturas == null) {
			listaTemperaturas = medidaRepository.listarTodos();
		}
		return listaTemperaturas;
	}

	public void setListaTemperaturas(List<Medida> listaTemperaturas) {
		this.listaTemperaturas = listaTemperaturas;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

	public Float getMaxTemperatura() {
		return maxTemperatura;
	}

	public Float getMinTemperatura() {
		return minTemperatura;
	}

	public Float getMaxDiaTemperatura() {
		return maxDiaTemperatura;
	}

	public Float getMinDiaTemperatura() {
		return minDiaTemperatura;
	}

	public Float getAtual() {
		return atual;
	}

	public Medida getMedida() {
		return medida;
	}

	public void setMedida(Medida medida) {
		this.medida = medida;
	}

}
