package com.banco.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.dto.UsuarioDTO;
import com.banco.servicios.IUsuarioServicio;


@Controller
public class LoginControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    @GetMapping("/auth/iniciar-sesion")
    public String login() {
        return "login";
    }

    @GetMapping("/privada/inicio")
    public String loginCorrecto(Model model, Authentication authentication) {
        try {
            boolean cuentaConfirmada = usuarioServicio.estaLaCuentaConfirmada(authentication.getName());

            if (cuentaConfirmada) {
            	UsuarioDTO usuarioDTO = usuarioServicio.buscarPorEmail(authentication.getName()); 
                model.addAttribute("usuarioDTO", usuarioDTO);
                return "inicio";
            } else {
                model.addAttribute("cuentaNoVerificada", "Error, email no confirmado.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error, su solicitud no ha sido procesada.");
            return "login";
        }
    }

    @GetMapping("/auth/confirmar-cuenta")
    public String confirmarCuenta(Model model, @RequestParam("token") String token) {
        try {
            boolean confirmacionExitosa = usuarioServicio.confirmarCuenta(token);

            if (confirmacionExitosa) {
                model.addAttribute("cuentaVerificada", "Email verificado correctamente");
            } else {
                model.addAttribute("cuentaNoVerificada", "Error, email no verificado");
            }

            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Error, su solicitud no ha sido procesada.");
            return "login";
        }
    }
}
