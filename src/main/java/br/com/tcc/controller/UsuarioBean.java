package br.com.tcc.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.tcc.modelo.Grupo;
import br.com.tcc.modelo.Usuario;
import br.com.tcc.repository.GrupoRepository;
import br.com.tcc.repository.UsuarioRepository;
import br.com.tcc.repository.filter.UsuarioFilter;
import br.com.tcc.service.UsuarioService;
import br.com.tcc.util.jsf.FacesUtil;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	private UsuarioFilter filtro;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private GrupoRepository grupoRepository;
	
	private Usuario[] usuarioSelecionado;

	private List<Usuario> usuarioFiltrado;
	private List<Grupo> grupos;
	private Grupo novoGrupo;

	public UsuarioBean() {
		limpar();
	}

	public void pesquisarUsuarios() {
		usuarioFiltrado = usuarioRepository.filtrado(filtro);
	}

	public void salvar() {
		this.usuario = usuarioService.salvar(this.usuario);
		FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
		limpar();
	}

	public void excluir() {
		usuarioRepository.remover(usuario);
		usuarioFiltrado.remove(usuario);
		FacesUtil.addInfoMessage("Usuário " + usuario.getNome()
				+ " excluído com sucesso!");
	}

	private void limpar() {
		usuario = new Usuario();
		filtro = new UsuarioFilter();
		grupos = new ArrayList<>();
	}

	public void adicionarGrupo() {
		this.usuario.getGrupos().add(novoGrupo);
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			System.out.println("Carregando combo de Grupos no preRender");
			this.grupos = grupoRepository.buscarGrupos();
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarioFiltrado() {
		return usuarioFiltrado;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public Grupo getNovoGrupo() {
		return novoGrupo;
	}

	public void setNovoGrupo(Grupo novoGrupo) {
		this.novoGrupo = novoGrupo;
	}

	public UsuarioFilter getFiltro() {
		return filtro;
	}

	public Usuario[] getUsuarioSelecionado() {
		System.out.println("Teste de usuario selecionado: "+usuarioSelecionado.length);
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario[] usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}
	
	

}
