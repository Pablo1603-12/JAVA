package com.banco.dto;

import java.util.Calendar;
import java.util.Objects;


public class UsuarioDTO {

	// ATRIBUTOS
	private long id;
	private String nombre;
	private String apellidos;
	private String movil;
	private String email;
	private String clave;
	private String token;
	private String foto;
	private String password;
	private Calendar expiracionToken;
	private Calendar fechaRegistro;
	private boolean cuentaConfirmada;
	private String rol;
	private String mensajeError = "ERROR";


	// CONSTRUCTORES
	public UsuarioDTO() {
	}

	// GETTERS Y SETTERS
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Calendar getExpiracionToken() {
		return expiracionToken;
	}

	public void setExpiracionToken(Calendar expiracionToken) {
		this.expiracionToken = expiracionToken;
	}

	public Calendar getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Calendar fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public boolean isCuentaConfirmada() {
		return cuentaConfirmada;
	}

	public void setCuentaConfirmada(boolean cuentaConfirmada) {
		this.cuentaConfirmada = cuentaConfirmada;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apellidos, clave, cuentaConfirmada, email, expiracionToken,
				fechaRegistro, id, mensajeError, nombre, password, rol,
				movil, token, foto);
	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		UsuarioDTO ususario = (UsuarioDTO) obj;
//		return Objects.equals(apellidos, ususario.apellidos)
//				&& Objects.equals(clave, ususario.clave) && cuentaConfirmada == ususario.cuentaConfirmada
//				&& Objects.equals(email, ususario.email)
//				&& Objects.equals(expiracionToken, ususario.expiracionToken)
//				&& Objects.equals(fechaRegistro, ususario.fechaRegistro) && id == ususario.id
//				&& Objects.equals(mensajeError, ususario.mensajeError) && Objects.equals(nombre, ususario.nombre)
//				&& Objects.equals(password, ususario.password) 
//				&& Objects.equals(rol, ususario.rol) && Objects.equals(movil, ususario.movil)
//				&& Objects.equals(token, ususario.token) && Objects.equals(foto, ususario.foto);
//	}
//
//	@Override
//	public String toString() {
//		return "UsuarioDTO [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos
//				+ ", tlfUsuario=" + movil + ", emailUsuario=" + email + ", claveUsuario=" + clave
//				+ ", token=" + token + ", password=" + password + ", expiracionToken="
//				+ expiracionToken + ", fechaRegistro=" + fechaRegistro + ", cuentaConfirmada=" + cuentaConfirmada
//				+ ", rol=" + rol +  ", mensajeError="
//				+ mensajeError + ", foto=" + foto + "]";
//	}


}
