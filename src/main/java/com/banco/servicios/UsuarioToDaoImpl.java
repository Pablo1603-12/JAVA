package com.banco.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.dto.UsuarioDTO;
import com.banco.modelos.Usuario;

@Service
public class UsuarioToDaoImpl implements IUsuarioToDao {

	@Autowired
	private IFotoServicio fotoServicio;
	
	@Override
	public Usuario usuarioToDao(UsuarioDTO usuarioDTO) {

		Usuario usuarioDao = new Usuario();

		try {
			usuarioDao.setId(usuarioDTO.getId());
			usuarioDao.setNombreApellidos(usuarioDTO.getNombre() + " " + usuarioDTO.getApellidos());
			usuarioDao.setEmail(usuarioDTO.getEmail());
			usuarioDao.setPassword(usuarioDTO.getClave());
			usuarioDao.setTelefono(usuarioDTO.getMovil());
			if(usuarioDTO.getFoto() != null) {
				usuarioDao.setFoto(fotoServicio.convertirAarrayBytes(usuarioDTO.getFoto()));
			}
			
			return usuarioDao;

		} catch (Exception e) {
			System.out.println(
					"\n[ERROR UsuarioToDaoImpl - toUsuarioDao()] - Al convertir usuarioDTO a usuarioDAO (return null): "
							+ e);
			return null;
		}

	}

	@Override
	public List<Usuario> listUsuarioToDao(List<UsuarioDTO> listaUsuarioDTO) {

		List<Usuario> listaUsuarioDao = new ArrayList<>();

		try {
			for (UsuarioDTO usuarioDTO : listaUsuarioDTO) {
				listaUsuarioDao.add(usuarioToDao(usuarioDTO));
			}

			return listaUsuarioDao;

		} catch (Exception e) {
			System.out.println(
					"\n[ERROR UsuarioToDaoImpl - toListUsuarioDao()] - Al convertir lista de usuarioDTO a lista de usuarioDAO (return null): "
							+ e);
		}
		return null;
	}


}
