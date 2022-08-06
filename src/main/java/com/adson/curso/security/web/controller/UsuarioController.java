package com.adson.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adson.curso.security.domain.Perfil;
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
	
	// salvar cadastro de usuarios por administrador
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
		
		List<Perfil> perfils = usuario.getPerfis();
		if (perfils.size() > 2 || 
			perfils.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) || 
			perfils.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))) {
		attr.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico.");
		
		attr.addFlashAttribute("usuario", usuario);
			
		}
		else {
			try {
			service.salvarUsuarios(usuario);
			
			   attr.addFlashAttribute("sucesso", "Operação realizada com Sucesso!");
			}
			catch (DataIntegrityViolationException ex) {
			   attr.addFlashAttribute("falha", "Cadastro não realizado, email já existente!5");
			}
		}
		
		return "redirect:/u/novo/cadastro/usuario";
		
	}
	
	// pre edição de credenciais de usuarios
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {

		return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));
	}


}
