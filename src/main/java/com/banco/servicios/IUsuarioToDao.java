package com.banco.servicios;

import java.util.List;

import com.banco.dto.UsuarioDTO;
import com.banco.modelos.Usuario;


public interface IUsuarioToDao {
	
	
	public Usuario usuarioToDao(UsuarioDTO usuarioDTO);
	

	public List<Usuario> listUsuarioToDao(List<UsuarioDTO>listaUsuarioDTO);
	


}
