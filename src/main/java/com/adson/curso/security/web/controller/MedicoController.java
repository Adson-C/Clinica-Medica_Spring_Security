package com.adson.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adson.curso.security.domain.Medico;
import com.adson.curso.security.service.MedicoService;

@Controller
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	private MedicoService service;

	// abrir pagina de dados pessoais de medico pelo MEDICO
	@GetMapping({ "/dados" })
	public String abrirPorMedico(Medico medico, ModelMap model) {

		return "medico/cadastro";
	}

	// salvar medico
	@PostMapping({ "/salvar" })
	public String salvar(Medico medico, RedirectAttributes attr) {

		service.salvar(medico);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		attr.addFlashAttribute("medico", medico);
		return "redirect:/medicos/dados";
	}

	// editar medico
	@PostMapping({ "/editar" })
	public String editar(Medico medico, RedirectAttributes attr) {
		
		service.editar(medico);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		attr.addFlashAttribute("medico", medico);
		return "redirect:/medicos/dados";
	}

}
