package com.adson.curso.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adson.curso.security.domain.Agendamento;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {
	
	// abrir paginas de agendamentos)
	@GetMapping({"/agendar"})
	public String agendarConsulta(Agendamento agendamento) {
		
		return "agendamento/cadastro";
		
		}
		
	}
	
