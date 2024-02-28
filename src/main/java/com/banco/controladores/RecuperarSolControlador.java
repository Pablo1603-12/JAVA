package com.banco.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.banco.dto.UsuarioDTO;
import com.banco.servicios.IUsuarioServicio;


@Controller
public class RecuperarSolControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;


    @GetMapping("/auth/solicitar-recuperacion")
    public String iniciarRecuperacion(Model model) {
        try {
            model.addAttribute("usuarioDTO", new UsuarioDTO());
            return "ContraseñaSolicitud";
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar la solicitud. Por favor, inténtelo de nuevo.");
            return "ContraseñaSolicitud";
        }
    }


    @PostMapping("/auth/iniciar-recuperacion")
    public String inicioRecuperacion(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
        try {
            boolean envioConExito = usuarioServicio.iniciarProcesoRecuperacion(usuarioDTO.getEmail());

            if (envioConExito) {
                model.addAttribute("mensajeExitoMail", "Proceso de recuperación OK");
                return "login";
            } else {
                model.addAttribute("mensajeErrorMail", "Error en el proceso de recuperación.");
            }
            return "ContraseñaSolicitud";
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar la solicitud. Por favor, inténtelo de nuevo.");
            return "ContraseñaSolicitud";
        }
    }
}
