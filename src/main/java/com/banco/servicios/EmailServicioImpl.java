package com.banco.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio que implementa los metodos de la interface {@link IEmailServicio} y
 * en esta clase es donde se entra al detalle de la logica de dichos métodos
 * para la gestión de envio de emails.
 */
@Service
public class EmailServicioImpl implements IEmailServicio {

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void enviarEmailRecuperacion(String emailDestino, String nombreUsuario, String token) {

		try {
			MimeMessage mensaje = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

			helper.setFrom("pablomarquinezjimenez3@gmail.com");
			helper.setTo(emailDestino);
			helper.setSubject("Recuperación de contraseña");

			String urlDominio = "http://localhost:8080";
			String urlDeRecuperacion = String.format("%s/auth/recuperar?token=%s", urlDominio, token);
			String cuerpoMensaje = String.format(
					"﻿<!DOCTYPE html> <html lang='es'> <body> <div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 10px; background-color: #fff; font-family: Arial, sans-serif;'>"
							+ " <h1 style='color: #004d40; text-align: center; font-size: 24px;'>Restablecer Contraseña - Banco Altair</h1>"
							+ " <p style='margin-bottom: 20px; text-align: justify;'>Estimado/a&nbsp;<b>%s</b>,</p> <p style='margin-bottom: 20px; text-align: justify;'>"
							+ "Recibió este correo porque se solicitó un restablecimiento de contraseña para su cuenta en Banco Altair. Por favor, haga clic en el siguiente enlace para cambiar su contraseña:</p>"
							+ " <div style='text-align: center;'> <a style='padding: 10px 20px; border-radius: 5px; background-color: #004d40; color: white; text-decoration: none; display: inline-block;' href='%s' target='_blank'>Cambiar Contraseña</a> </div>"
							+ " <p style='margin-top: 20px; text-align: justify;'>Si no solicitó este restablecimiento de contraseña, puede ignorar este correo de forma segura.</p>"
							+ " <p style='text-align: center;'>Gracias por utilizar nuestros servicios.</p> </div> </body> </html>",
					nombreUsuario, urlDeRecuperacion);

			helper.setText(cuerpoMensaje, true);

			javaMailSender.send(mensaje);

		} catch (MailException me) {
			System.out.println(
					"[Error EmailServicioImpl - enviarEmailRecuperacion()] Ha ocurrido un error al enviar el email! "
							+ me.getMessage());
		} catch (MessagingException e) {
			System.out.println(
					"[Error EmailServicioImpl - enviarEmailRecuperacion()] Ha ocurrido un error al enviar el email! "
							+ e.getMessage());
		}

	}

	@Override
	public void enviarEmailConfirmacion(String emailDestino, String nombreUsuario, String token) {
		try {
			MimeMessage mensaje = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

			helper.setFrom("pablomarquinezjimenez3@gmail.com");
			helper.setTo(emailDestino);
			helper.setSubject("Confirmación de cuenta en Banco Altair");

			String urlDominio = "http://localhost:8080";
			String urlDeConfirmacion = String.format("%s/auth/confirmar-cuenta?token=%s", urlDominio, token);

			String cuerpoMensaje = String.format(
				    "﻿<!DOCTYPE html> <html lang='es'> <body> <div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 10px; background-color: #fff; font-family: Arial, sans-serif;'>"
				    + " <h1 style='color: #004d40; text-align: center; font-size: 24px;'>Confirmar Cuenta - Banco Altair</h1>"
				    + " <p style='margin-bottom: 20px; text-align: justify;'>Estimado/a&nbsp;<b>%s</b>,</p> <p style='margin-bottom: 20px; text-align: justify;'>"
				    + "Bienvenido/a a Banco Altair. Para confirmar tu cuenta, haz clic en el botón que aparece a continuación:</p>"
				    + " <div style='text-align: center;'> <a style='padding: 10px 20px; border-radius: 5px; background-color: #004d40; color: white; text-decoration: none; display: inline-block;' href='%s' target='_blank'>Confirmar cuenta</a> </div>"
				    + " <p style='margin-top: 20px; text-align: justify;'>Gracias por unirte a Banco Altair.</p>"
				    + " </div> </body> </html>",
				    nombreUsuario, urlDeConfirmacion);

			helper.setText(cuerpoMensaje, true);

			javaMailSender.send(mensaje);

		} catch (MailException me) {
			System.out.println(
					"[Error EmailServicioImpl - enviarEmailConfirmacion()] Ha ocurrido un error al enviar el email! "
							+ me.getMessage());
		} catch (MessagingException e) {
			System.out.println(
					"[Error EmailServicioImpl - enviarEmailConfirmacion()] Ha ocurrido un error al enviar el email! "
							+ e.getMessage());
		}
	}

}
