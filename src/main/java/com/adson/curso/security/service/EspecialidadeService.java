package com.adson.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adson.curso.security.domain.Especialidade;
import com.adson.curso.security.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService {
	
	@Autowired
	private EspecialidadeRepository repository;

	@Transactional(readOnly = false)
	public void salvar(Especialidade especialidade) {
		
		repository.save(especialidade);
	}
	
	
	
}
