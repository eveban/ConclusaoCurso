package br.com.tcc.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.tcc.modelo.Grupo;
import br.com.tcc.modelo.Usuario;
import br.com.tcc.repository.UsuarioRepository;
import br.com.tcc.util.cdi.CDIServiceLocator;

/**
 * @author Everson Silva Classe para Fornecedor os detalhes do usuário Classe
 *         Bean do Spring Security
 *
 */
public class AppUserDetailsService implements UserDetailsService {

	/**
	 * Metodo rece um parametro e retorna o UserDetails, no caso os dados do
	 * usuário passado no parametro
	 */
	@Override
	public UserDetails loadUserByUsername(String user)
			throws UsernameNotFoundException {

		/**
		 * Como não é um Bean CDI, não pode ser injetado, com isso utiliza a
		 * classe CDIServiceLocator para instanciar a classe de repositórios
		 */
		UsuarioRepository usuarios = CDIServiceLocator
				.getBean(UsuarioRepository.class);
		Usuario usuario = usuarios.porUsuario(user);

		/**
		 * Classe UsuarioSistema extend User que no fim também implementa a
		 * UserDetails
		 */
		UsuarioSistema usarioSistema = null;

		if (usuario != null) {
			usarioSistema = new UsuarioSistema(usuario, getGrupos(usuario));
		}

		return usarioSistema;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Grupo grupo : usuario.getGrupos()) {
			authorities.add(new SimpleGrantedAuthority(grupo.getNome()
					.toUpperCase()));
		}

		return authorities;
	}

}
