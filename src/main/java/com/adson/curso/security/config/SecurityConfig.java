package com.adson.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.adson.curso.security.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UsuarioService service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	

        http.authorizeRequests()
        // acessos publicos liberatos
                .antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
                .antMatchers("/", "/home").permitAll()
                
                // acessos privados para admin
                .antMatchers("/u/**").hasAuthority("ADMIN")
                
                // acessos privados medicos
                .antMatchers("/medicos/**").hasAuthority("MEDICO")
                
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login-error")
                    .permitAll()
                .and()
                     .logout()
                .logoutSuccessUrl("/");

    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
    
    
}
