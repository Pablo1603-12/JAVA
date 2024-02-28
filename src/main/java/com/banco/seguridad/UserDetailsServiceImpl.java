package com.banco.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.banco.modelos.Usuario;
import com.banco.repositorios.UsuarioRepositorio;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepositorio usuarioRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    System.out.printf("\nIntento de inicio de sesión para el usuario: %s\n", username);

		Usuario user = usuarioRepository.findFirstByEmail(username);
		
		UserBuilder builder = null;
		if (user != null) {
	    	System.out.printf("\nUsuario encontrado: %s\n", user.getEmail());

			builder = User.withUsername(username);
			builder.disabled(false);
			builder.password(user.getPassword());
			builder.authorities(user.getRol());
		} else {
	    	System.out.println("Usuario no encontrado");
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		return builder.build();
	}

}

