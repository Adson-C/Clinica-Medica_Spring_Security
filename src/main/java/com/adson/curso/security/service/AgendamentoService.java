package com.adson.curso.security.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adson.curso.security.domain.Horario;
import com.adson.curso.security.repository.AgendamentoRepository;

@Service
public class AgendamentoService {
	
	@Autowired
	private AgendamentoRepository repository;

	@Transactional(readOnly = true)
	public List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEDtata(Long id, LocalDate data) {
		
		return repository.findByMedicoIdAndDataNotHorarioAgendado(id, data);
	}

	
}
