package br.com.tcc.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.tcc.modelo.Medida;
import br.com.tcc.repository.MedidaRepository;
import br.com.tcc.repository.filter.MedidaFilter;

@ViewScoped
@Named
public class PesquisaTemperaturaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private MedidaFilter filtro;
	private Medida medida;

	@Inject
	private MedidaRepository medidaRepository;

	private List<Medida> filtrados;

	public PesquisaTemperaturaBean() {

		filtro = new MedidaFilter();
	}

	public void pesquisar() {
		filtrados = medidaRepository.filtrados(filtro);
	}

	public MedidaFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(MedidaFilter filtro) {
		this.filtro = filtro;
	}

	public Medida getMedida() {
		return medida;
	}

	public void setMedida(Medida medida) {
		this.medida = medida;
	}

	public List<Medida> getFiltrados() {
		return filtrados;
	}

}
