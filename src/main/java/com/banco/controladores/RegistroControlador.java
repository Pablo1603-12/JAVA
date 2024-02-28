package com.banco.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.banco.dto.UsuarioDTO;
import com.banco.servicios.IUsuarioServicio;


@Controller
public class RegistroControlador {

	@Autowired
	private IUsuarioServicio usuarioServicio;

	
	@GetMapping("/auth/crear-cuenta")
	public String registrarGet(Model model) {
		try {
			model.addAttribute("usuarioDTO", new UsuarioDTO());
			return "registro";
		} catch (Exception e) {
			model.addAttribute("error", "Error al procesar la solicitud. Por favor, inténtelo de nuevo.");
			return "registro";
		}
	}


	@PostMapping("/auth/crear-cuenta")
	public String registrarPost(@ModelAttribute UsuarioDTO usuarioDTO, Model model, Authentication authentication) {
		try {

			UsuarioDTO nuevoUsuario = usuarioServicio.registrarUsuario(usuarioDTO);

	        String rolDelUsuario = "";
	        if (authentication != null && authentication.isAuthenticated()) {
	            rolDelUsuario = authentication.getAuthorities().iterator().next().getAuthority();
	        }
			if (nuevoUsuario == null) {
				model.addAttribute("usuarioRegistradoPeroNoConfirmado",
						"Ya existe un usuario con ese email sin confirmar");
				return "registro";
			} else if (nuevoUsuario.getMensajeError().equals("Usuario ya registrado y confirmado")) {
				model.addAttribute("emailYaRegistrado", "Ya existe un usuario con ese email");
				return "registro";
			} else if (nuevoUsuario != null && !rolDelUsuario.equals("ROLE_ADMIN")) {
				model.addAttribute("mensajeRegistroExitoso", "Registro del nuevo usuario OK");
				return "login";
			} else {
				model.addAttribute("mensajeRegistroExitoso", "Registro del nuevo usuario OK");
				model.addAttribute("usuarios", usuarioServicio.obtenerTodos());
				return "administracionUsuarios";
			}
		} catch (Exception e) {
			model.addAttribute("error", "Error al procesar la solicitud. Por favor, inténtelo de nuevo.");
			return "registro";
		}
	}

}
