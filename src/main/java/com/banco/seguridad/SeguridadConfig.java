package com.banco.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SeguridadConfig {

	@Autowired
    private UserDetailsService userDetailsService;

  
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

   
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	   
	    return authProvider;
	}


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	    return authConfig.getAuthenticationManager();
	}

  
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// Configura las reglas de autorización para las peticiones HTTP.
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/bienvenida", "/css/**", "/script/**", "/auth/**").permitAll()
                    						   .anyRequest().authenticated()// Exige autenticación para cualquier otra petición HTTP.
            )
            // Configura el proceso de inicio de sesión y la página de inicio de sesión.
            .formLogin(login -> login.loginPage("/auth/iniciar-sesion") // Establece la página de inicio de sesión personalizada.
                     				 .defaultSuccessUrl("/privada/inicio", true) // Establece la URL de redirección después de un inicio de sesión exitoso.
                     				 .loginProcessingUrl("/auth/procesar-inciarsesion") // Establece la URL de procesamiento del formulario de inicio de sesión.
            )
            // Configura el proceso de cierre de sesión.
            .logout(logout -> logout.logoutUrl("/auth/cerrar-sesion") // Establece la URL de cierre de sesión personalizada.
                    				.logoutSuccessUrl("/bienvenida") // Establece la URL de redirección después de un cierre de sesión exitoso.
            );
        
        // Configura un proveedor de autenticación personalizado.
        http.authenticationProvider(authenticationProvider());

        // Construye y devuelve la cadena de filtros de seguridad configurada.
        return http.build();
    }
    
    
}
