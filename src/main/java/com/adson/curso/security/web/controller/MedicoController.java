package com.adson.curso.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adson.curso.security.domain.Medico;
import com.adson.curso.security.domain.Usuario;

@Controller
@RequestMapping("medicos")
public class MedicoController {


	// abrir pagina de dados pessoais de medico pelo MEDICO
		@GetMapping({"/dados"})
		public String abrirPorMedico(Medico medico, ModelMap model) {
			
			
			return "medico/cadastro";
		}

	
}
