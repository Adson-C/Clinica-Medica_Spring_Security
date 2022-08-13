package com.adson.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adson.curso.security.domain.Paciente;
import com.adson.curso.security.repository.PacienteRepository;

@Service
public class PacienteService {
	
	@Autowired
	private PacienteRepository repository;
	
	@Transactional(readOnly = true)
	public Paciente buscarPorUsuarioEmail(String email) {
		
		return repository.findByUsuarioEmail(email).orElse(new Paciente());
	}

}
