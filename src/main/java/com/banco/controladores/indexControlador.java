package com.banco.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexControlador {

	@GetMapping({ "/", "/bienvenida" })
	public String bienvenida() {
		return "index";
	}
}