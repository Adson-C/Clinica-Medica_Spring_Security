package com.adson.curso.security.repository.projection;

import com.adson.curso.security.domain.Especialidade;
import com.adson.curso.security.domain.Medico;
import com.adson.curso.security.domain.Paciente;

public interface HistoricoPaciente {

	Long getId();
	
	Paciente getPaciente();
	
	String getDataConsulta();
	
	Medico getMedico();
	
	Especialidade getEspecialidade();
}
