package com.adson.curso.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.adson.curso.security.domain.Perfil;
import com.adson.curso.security.domain.Usuario;
import com.adson.curso.security.repository.UsuarioRepository;

public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	
	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		
		return repository.findByEmail(email);
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = buscarPorEmail(username);
		
		return new User(
				usuario.getEmail(),
				usuario.getSenha(),
				AuthorityUtils.createAuthorityList(getAuthorities(usuario.getPerfis()))
				);
	}
	
	// metodo de Arrays de strings  pra usar em AuthorityUtils
	private String[] getAuthorities(List<Perfil> perfis) {
		
		String[] authorities = new String[perfis.size()];
		
		for(int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
		
	}
}
