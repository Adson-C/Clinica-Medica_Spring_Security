package com.adson.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
				return medico.hasNotId()
						? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
						: new ModelAndView("medico/cadastro", "medico", medico);
						
				
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

	// abrir e editar senha
		@GetMapping("/editar/senha")
		public String abrirEditarSenha() {

			return "usuario/editar-senha";
		}
		
	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2, 
			                  @RequestParam("senha3") String s3, @AuthenticationPrincipal User user,
			                  RedirectAttributes attr) {
		
		if (!s1.equals(s2)) {
			attr.addFlashAttribute("falha", "Senhas não conferem , tente novamente.");
			return "redirect:/u/editar/senha";
		}
		
		Usuario u = service.buscarPorEmail(user.getUsername());
		if (!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
			attr.addFlashAttribute("falha", "Senhas atual não confere , tente novamente.");
			return "redirect:/u/editar/senha";
		}
		
		service.alterarSenha(u, s1);
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso !");
		
		return "redirect:/u/editar/senha";
	}
	
	// abrir pagína de novo cadastro de paciente
	@GetMapping("/novo/cadastro")
	public String novoCadastro(Usuario usuario) {

		return "cadastrar-se";
	}

	// pagína de resposta cadastro de paciente
	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado() {

		return "fragments/mensagem";
	}

	// recebe o from da pagína cadastrar-se
	@PostMapping("/cadastro/paciente/salvar")
	public String salvarCadastroPaciente(Usuario usuario, BindingResult result) throws MessagingException {
		try {
			service.salvarCadastroPaciente(usuario);
			
		} catch (DataIntegrityViolationException ex) {
			result.reject("email", "Ops... Este e-mail já existe na base de dados.");
			return "cadastrar-se";
		}
		return "redirect:/u/cadastro/realizado";
		
	}
	

    // recebe a requisicao de confirmacao de cadastro
    @GetMapping("/confirmacao/cadastro")
    public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, 
    												  RedirectAttributes attr) {    	
        service.ativarCadastroPaciente(codigo);
        attr.addFlashAttribute("alerta", "sucesso");
        attr.addFlashAttribute("titulo", "Cadastro Ativado!");
        attr.addFlashAttribute("texto", "Parabéns, seu cadastro está ativo.");
        attr.addFlashAttribute("subtexto", "Siga com seu login/senha");
    	return "redirect:/login";
    } 
    
    //abre a pagina de pedido de redefinir de senha
    @GetMapping("/p/redefinir/senha")
    public String pedidoRedefinirSenha() {
    	
    	return "usuario/pedido-recuperar-senha";
    	
    }
    
    // form de pedido de recuparar senha
	@GetMapping("/p/recuperar/senha")
	public String redefinirSenha(String email, ModelMap model) throws MessagingException {
		service.pedidoRedefinirSenha(email);
		
		model.addAttribute("sucesso", "Em instante você receberar um E-mail para "
				+ "proseguir com a redefinição da sua senha. ");
		model.addAttribute("usuario", new Usuario(email));
		
		return "usuario/recuperar-senha";
	}
    
    // salvar a nova senha via recuperação senha
	@PostMapping("/p/nova/senha")
	public String confirmacaoDeRedefinicaoDeSenha(Usuario usuario, ModelMap model) {
	
		Usuario u = service.buscarPorEmail(usuario.getEmail());
		if (!usuario.getCodigoVerificador().equals(u.getCodigoVerificador())) {
			model.addAttribute("falha", "Código verificador não confere.");
			
			return "usuario/recuperar-senha";
		}
		u.setCodigoVerificador(null);
		service.alterarSenha(u, usuario.getSenha());
		model.addAttribute("alerta", "sucesso");
		model.addAttribute("titulo", "Senha Redefinida!");
		model.addAttribute("texto", "Você já pode logar no sistema.");
		
		return "login";
		
	}
	
	
	
	
}
