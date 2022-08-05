package com.adson.curso.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adson.curso.security.domain.Usuario;
import com.adson.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {
	

	@Autowired
	private UsuarioService service;

	// abrir cadastro de usuario (medico/admin/paciente)
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
		
		return "usuario/cadastro";
	}
	
	// abrir lista de usuario
	@GetMapping("/lista")
	public String listarUsuario() {

		return "usuario/lista";
	}
	
	// listar usuario Datatables
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuarioDatatables(HttpServletRequest request) {

		return ResponseEntity.ok(service.buscarTodos(request));
	}

}
