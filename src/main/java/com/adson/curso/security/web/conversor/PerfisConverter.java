package com.adson.curso.security.web.conversor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.adson.curso.security.domain.Perfil;

@Component
public class PerfisConverter implements Converter<String[], List<Perfil>>{

	@Override
	public List<Perfil> convert(String[] source) {
		
		List<Perfil> perfils = new ArrayList<>();
		
		for (String id : source) {
			if(!id.equals("0")) {
			perfils.add(new Perfil(Long.parseLong(id)));
			}
		}
		
		return perfils;
	}

	
}
