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

import com.adson.curso.security.domain.Medico;
import com.adson.curso.security.domain.Perfil;
import com.adson.curso.security.domain.PerfilTipo;
import com.adson.curso.security.domain.Usuario;
import com.adson.curso.security.service.MedicoService;
import com.adson.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {
	

	@Autowired
	private UsuarioService service;

	@Autowired
	private MedicoService medicoservice;

	
	
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

	// pre edição de cadastro de usuarios
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId,
			                                           @PathVariable("perfis") Long[] perfisId) {
			Usuario us = service.buscarPorIdEPerfis(usuarioId, perfisId);
			
			if(us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
				!us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod())) ) {
			
				return new ModelAndView("usuario/cadastro", "usuario", us);
			}
			else if (us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
				
				Medico medico = medicoservice.buscarPOrUsuarioId(usuarioId);
				
				return new ModelAndView("especialidade/especialidade");
				
			}
			else if (us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
				
				ModelAndView model = new ModelAndView("error");
				model.addObject("status", 403);
				model.addObject("error", "Àrea Restrita!");
				model.addObject("message", "Os dados de paciente são restritos a ele.");
				return model;
			}
		return new ModelAndView("redirect:/u/lista");
	}


}
