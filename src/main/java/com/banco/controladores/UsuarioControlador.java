package com.banco.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.banco.dto.UsuarioDTO;
import com.banco.servicios.IFotoServicio;
import com.banco.servicios.IUsuarioServicio;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UsuarioControlador {

	@Autowired
	private IUsuarioServicio usuarioServicio;

	@Autowired
	private IFotoServicio fotoServicio;

	@GetMapping("/privada/administracion-usuarios")
	public String usuarios(@RequestParam(value = "busqueda", required = false) String busqueda, Model model,
			HttpServletRequest request, Authentication authentication) {
		try {

			List<UsuarioDTO> usuarios = new ArrayList<>();

			if (busqueda != null && !busqueda.isEmpty()) {
				usuarios = usuarioServicio.buscarPorCoincidenciaEnEmail(busqueda);
				if (usuarios.size() > 0) {
					model.addAttribute("usuarios", usuarios);
				} else {
					model.addAttribute("usuarioNoEncontrado", "No se encontró usuarios con la busqueda introducida");
					model.addAttribute("usuarios", usuarioServicio.obtenerTodos());
				}
			} else {
				model.addAttribute("usuarios", usuarioServicio.obtenerTodos());
			}

			if (request.isUserInRole("ROLE_ADMIN")) {
				return "administracionUsuarios";
			}

			model.addAttribute("noAdmin", "No eres admin");
			model.addAttribute("nombreUsuario", authentication.getName());
			return "index";
		} catch (Exception e) {
			model.addAttribute("Error", "Ocurrió un error al obtener la lista de usuarios");
			return "index";
		}
	}

	@GetMapping("/privada/eliminar-usuario/{id}")
	public String eliminarUsuario(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request) {
		try {
			UsuarioDTO usuario = usuarioServicio.buscarPorId(id);
			List<UsuarioDTO> usuarios = usuarioServicio.obtenerTodos();

			String emailUsuarioActual = request.getUserPrincipal().getName();

			if (emailUsuarioActual.equals(usuario.getEmail())) {
				model.addAttribute("noTePuedesEliminar", "No puedes eliminarte a ti mismo como administrador");
				model.addAttribute("usuarios", usuarios);
				return "administracionUsuarios";
			} else {
				int adminsRestantes = usuarioServicio.contarUsuariosPorRol("ROLE_ADMIN");
				if (usuario.getRol().equals("ROLE_ADMIN") && adminsRestantes == 1) {
					model.addAttribute("noSePuedeEliminar", "No se puede eliminar al último admin");
					model.addAttribute("usuarios", usuarios);
					return "administracionUsuarios";
				}

				usuarioServicio.eliminar(id);
				model.addAttribute("eliminacionCorrecta", "El usuario se ha eliminado correctamente");
				model.addAttribute("usuarios", usuarioServicio.obtenerTodos());
				return "administracionUsuarios";
			}

		} catch (Exception e) {
			model.addAttribute("Error", "Ocurrió un error al eliminar el usuario");
			return "index";
		}
	}

	@GetMapping("/privada/editar/{id}")
	public String editarUsuario(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request) {
		try {

			UsuarioDTO usuarioDTO = usuarioServicio.buscarPorId(id);
			if (usuarioDTO == null) {
				return "administracionUsuarios";
			}
			model.addAttribute("usuarioDTO", usuarioDTO);
			return "editar";

		} catch (Exception e) {
			model.addAttribute("Error", "Ocurrió un error al obtener el usuario para editar");
			return "index";
		}
	}

	@PostMapping("/privada/procesar-editar")
	public String editarForm(@RequestParam("id") Long id, @RequestParam("nombre") String nombre,
			@RequestParam("apellidos") String apellidos, @RequestParam("movil") String telefono,
			@RequestParam("rol") String rol, @RequestParam("foto") MultipartFile archivo, Model model) {
		try {
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setId(id);
			usuarioDTO.setNombre(nombre);
			usuarioDTO.setApellidos(apellidos);
			usuarioDTO.setMovil(telefono);

			if (rol.equals("Administrador")) {
				usuarioDTO.setRol("ROLE_ADMIN");
			} else {
				usuarioDTO.setRol(rol);
			}

			if (!archivo.isEmpty()) {
				String fotoUsuario = fotoServicio.convertirAbase64(archivo.getBytes());
				usuarioDTO.setFoto(fotoUsuario);
			} else {
				UsuarioDTO usuarioActualDTO = usuarioServicio.buscarPorId(id);
				String fotoActual = usuarioActualDTO.getFoto();
				usuarioDTO.setFoto(fotoActual);
			}

			usuarioServicio.actualizarUsuario(usuarioDTO);
			model.addAttribute("edicionCorrecta", "El Usuario se ha editado correctamente");
			model.addAttribute("usuarios", usuarioServicio.obtenerTodos());
			return "administracionUsuarios";
		} catch (Exception e) {
			model.addAttribute("Error", "Ocurrió un error al editar el usuario");
			return "index	";
		}
	}

}
