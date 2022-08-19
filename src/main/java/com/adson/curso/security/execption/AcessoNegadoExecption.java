package com.adson.curso.security.execption;

@SuppressWarnings("serial")
public class AcessoNegadoExecption extends RuntimeException {

	public AcessoNegadoExecption(String message) {
		super(message);
	}

	
}
