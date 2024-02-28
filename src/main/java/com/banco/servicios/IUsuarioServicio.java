package com.banco.servicios;

import java.util.List;

import com.banco.dto.UsuarioDTO;

public interface IUsuarioServicio {

	public UsuarioDTO registrarUsuario(UsuarioDTO userDTO);

	public boolean iniciarProcesoRecuperacion(String emailUsuario);

	public UsuarioDTO obtenerUsuarioPorToken(String token);

	public boolean modificarContraseñaConToken(UsuarioDTO usuario);

	boolean confirmarCuenta(String emailUsuario);

	public boolean estaLaCuentaConfirmada(String email);

	public List<UsuarioDTO> obtenerTodos();

	public UsuarioDTO buscarPorId(long id);

	public void eliminar(long id);

	public void actualizarUsuario(UsuarioDTO usuarioModificado);

	public UsuarioDTO buscarPorEmail(String email);

	public int contarUsuariosPorRol(String rol);

	public List<UsuarioDTO> buscarPorCoincidenciaEnEmail(String palabra);

}
