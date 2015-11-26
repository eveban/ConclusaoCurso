/*Classe Responsável pelas regras de Negócio*/
package br.com.tcc.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.tcc.modelo.Usuario;
import br.com.tcc.repository.UsuarioRepository;
import br.com.tcc.util.jpa.Transactional;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		Usuario emailExistente = usuarioRepository.porEmail(usuario.getEmail());
		if (emailExistente != null && !emailExistente.equals(usuario)) {
			throw new NegocioException("Já existe um usuário com o e-mail informado");
		}
		return usuarioRepository.guardar(usuario);
	}

}
