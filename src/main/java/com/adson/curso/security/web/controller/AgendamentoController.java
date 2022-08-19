package com.adson.curso.security.web.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adson.curso.security.domain.Agendamento;
import com.adson.curso.security.domain.Especialidade;
import com.adson.curso.security.domain.Paciente;
import com.adson.curso.security.domain.PerfilTipo;
import com.adson.curso.security.service.AgendamentoService;
import com.adson.curso.security.service.EspecialidadeService;
import com.adson.curso.security.service.PacienteService;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {
	
	@Autowired
	private AgendamentoService service;
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private EspecialidadeService especialidadeService;
	
	// abrir paginas de agendamentos)
	@GetMapping({"/agendar"})
	public String agendarConsulta(Agendamento agendamento) {
		
		return "agendamento/cadastro";
		
		}
	
	@GetMapping("/horario/medico/{id}/data/{data}")
	public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
			                             @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {
		
		return ResponseEntity.ok(service.buscarHorariosNaoAgendadosPorMedicoIdEDtata(id, data));
	}

	// salvar agendamentos )
	@PostMapping ({ "/salvar" })
	public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User use) {

		Paciente paciente = pacienteService.buscarPorUsuarioEmail(use.getUsername());
		String titulo = agendamento.getEspecialidade().getTitulo();
		Especialidade especialidade = especialidadeService
				.buscarPorTitulo(new String[] {titulo})
				.stream().findFirst().get();
		agendamento.setEspecialidade(especialidade);
		agendamento.setPaciente(paciente);
		service.salvar(agendamento);
		attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso.");
		
		return "redirect:/agendamentos/agendar";

	}
	
	// abrir paginas de historico de agendamento do paciente)
	@GetMapping({ "/historico/paciente", "/historico/consultas" })
	public String historico() {

		return "agendamento/historico-paciente";
	}

	// localizar o historico de agendamentos por usuario logado
	@GetMapping("/datatables/server/historico")
	public ResponseEntity<?> historicoAgendamentosPorPaciente(HttpServletRequest request, @AuthenticationPrincipal User user) {

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))) {
			
			return ResponseEntity.ok(service.buscarHistoricoPorPacienteEmail(user.getUsername(), request));
		}
		
		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))) {
			
			return ResponseEntity.ok(service.buscarHistoricoPorMedicoEmail(user.getUsername(), request));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	// localizar agendamento por id e envia-lo para a pegina de cadastro
	@GetMapping("/editar/consulta/{id}")
	public String preEditarConsultaPaciente(@PathVariable("id") Long id,
			                                ModelMap model, @AuthenticationPrincipal User user ) {
		
		Agendamento agendamento = service.buscarPorIdEUsuario(id, user.getUsername());
		
		model.addAttribute("agendamento", agendamento);
		
		return "agendamento/cadastro";
		
	}
	
	@PostMapping("/editar")
	public String editarConsulta(Agendamento agendamento, RedirectAttributes attr , @AuthenticationPrincipal User user) {
		
		String titulo = agendamento.getEspecialidade().getTitulo();
		Especialidade especialidade = especialidadeService
				.buscarPorTitulo(new String[] {titulo})
				.stream().findFirst().get();
		agendamento.setEspecialidade(especialidade);
		
		service.editar(agendamento, user.getUsername());
		attr.addFlashAttribute("sucesso", "Sua consulta foi alterada com sucesso");
		return "redirect:/agendamentos/agendar";
	}

}
