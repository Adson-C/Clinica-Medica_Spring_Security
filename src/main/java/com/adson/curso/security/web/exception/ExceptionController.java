package com.adson.curso.security.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.adson.curso.security.execption.AcessoNegadoExecption;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
		
		ModelAndView model = new ModelAndView("error");
		model.addObject("status", 404);
		model.addObject("error", "Operação não pode ser Realizada!");
		model.addObject("message", ex.getMessage());
		return model;
		
	}
	
	@ExceptionHandler(AcessoNegadoExecption.class)
	public ModelAndView acessoNegadoExecption(AcessoNegadoExecption ex) {
		
		ModelAndView model = new ModelAndView("error");
		model.addObject("status", 403);
		model.addObject("error", "Operação não pode ser Realizada!");
		model.addObject("message", ex.getMessage());
		return model;
		
	}
}
