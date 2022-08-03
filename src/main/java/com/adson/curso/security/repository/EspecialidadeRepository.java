package com.adson.curso.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adson.curso.security.domain.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
	
	
}
