package com.banco.servicios;

import java.util.List;

import com.banco.dto.UsuarioDTO;
import com.banco.modelos.Usuario;


public interface IUsuarioToDto {

	public UsuarioDTO usuarioToDto(Usuario u);
	
	
	public List<UsuarioDTO> listaUsuarioToDto(List<Usuario> listaUsuario);
}
