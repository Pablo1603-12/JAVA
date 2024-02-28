package com.banco.controladores;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.dto.UsuarioDTO;
import com.banco.servicios.IUsuarioServicio;

/**
 * Clase que ejerce de controlador de la vista de recuperación de contraseña (recuperar.html) para gestionar
 * las solicitudes relacionadas con la recuperación de contraseña.
 */
@Controller
public class RecuperarContControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    @GetMapping("/auth/recuperar")
    public String mostrarVistaRecuperar(@RequestParam(name = "token") String token, Model model) {
        try {
            UsuarioDTO usuario = usuarioServicio.obtenerUsuarioPorToken(token);
            if (usuario != null) {
                model.addAttribute("usuarioDTO", usuario);
            } else {
                model.addAttribute("mensajeErrorTokenValidez", "El enlace de recuperación no válido o usuario no encontrado");
                return "ContraseñaSolicitud";
            }
            return "recuperar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al mostrar la vista de recuperar");
            return "ContraseñaSolicitud";
        }
    }

   
    @PostMapping("/auth/recuperar")
    public String procesarRecuperacionContraseña(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
        try {
            UsuarioDTO usuarioExistente = usuarioServicio.obtenerUsuarioPorToken(usuarioDTO.getToken());

            if (usuarioExistente == null) {
                model.addAttribute("mensajeErrorTokenValidez", "El enlace de recuperación no válido");
                return "ContraseñaSolicitud";
            }
            if (usuarioExistente.getExpiracionToken().before(Calendar.getInstance())) {
                model.addAttribute("mensajeErrorTokenExpirado", "El enlace de recuperación ha expirado");
                return "ContraseñaSolicitud";
            }

            boolean modificadaPassword = usuarioServicio.modificarContraseñaConToken(usuarioDTO);

            if (modificadaPassword) {
                model.addAttribute("contraseñaModificadaExito", "Contraseña modificada OK");
                return "login";
            } else {
                model.addAttribute("contraseñaModificadaError", "Error al cambiar de contraseña");
                return "ContraseñaSolicitud";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar la solicitud recuperar");
            return "ContraseñaSolicitud";
        }
    }

}
