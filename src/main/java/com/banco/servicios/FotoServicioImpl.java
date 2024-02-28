package com.banco.servicios;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.stereotype.Service;


@Service
public class FotoServicioImpl implements IFotoServicio {

	@Override
	public String convertirAbase64(byte[] datosImg) {
		if (datosImg != null && datosImg.length > 0) {
			return Base64.getEncoder().encodeToString(datosImg);
		}
		return null;
	}

	@Override
	public byte[] convertirAarrayBytes(String imgBase64) {
		if (imgBase64 != null && !imgBase64.isEmpty()) {
			return Base64.getDecoder().decode(imgBase64);
		}
		return null;
	}
	
	@Override
	public byte[] cargarFotoPredeterminada() {
		try {
			String rutaFotoPredeterminada = "src/main/resources/static/css/assets/fotoYO.jpg";
		    Path path = Paths.get(rutaFotoPredeterminada);
		    return Files.readAllBytes(path);
		} catch(IOException e) {
			System.out.println("[ERROR FotoServicioImpl - cargarFotoPredeterminada()] - Al cargar la imagen predeterminada: " + e);
			return null;
		}
	}


}
