package com.banco.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.dto.UsuarioDTO;
import com.banco.modelos.Usuario;

@Service
public class UsuarioToDtoImpl implements IUsuarioToDto {

	
	@Autowired
	private IFotoServicio fotoServicio;
	
	@Override
    public UsuarioDTO usuarioToDto(Usuario u) {
        try {
            UsuarioDTO dto = new UsuarioDTO();
            String[] nombreApellidos = u.getNombreApellidos().split(" ");

            if (nombreApellidos.length > 0) {
                dto.setNombre(nombreApellidos[0]);

                if (nombreApellidos.length > 1) {
                    StringBuilder apellidos = new StringBuilder();
                    for (int i = 1; i < nombreApellidos.length; i++) {
                        apellidos.append(nombreApellidos[i]).append(" ");
                    }
                    dto.setApellidos(apellidos.toString().trim());
                }

                dto.setMovil(u.getTelefono());
                dto.setEmail(u.getEmail());
                dto.setClave(u.getPassword());
                dto.setToken(u.getToken());
                dto.setExpiracionToken(u.getExpiracionToken());
                dto.setId(u.getId());
				dto.setFechaRegistro(u.getFechaRegistro());
				dto.setCuentaConfirmada(u.isCuentaConfirmada());
				dto.setRol(u.getRol());
				
				if(u.getFoto() != null) {
					dto.setFoto(fotoServicio.convertirAbase64(u.getFoto()));
				}
				
				
            }

            return dto;
        } catch (Exception e) {
        	System.out.println(
					"\n[ERROR UsuarioToDtoImpl - usuarioToDto()] - Error al convertir usuario DAO a usuarioDTO (return null): "
							+ e);
			return null;
        }
	}
	
	@Override
	public List<UsuarioDTO> listaUsuarioToDto(List<Usuario> listaUsuario){
		try {
				
			List<UsuarioDTO> listaDto = new ArrayList<>();
			for (Usuario u : listaUsuario) {
				listaDto.add(this.usuarioToDto(u));
			}
			return listaDto;

		} catch (Exception e) {
			System.out.println(
					"\n[ERROR UsuarioToDtoImpl - listauUsuarioToDto()] - Error al convertir lista de usuario DAO a lista de usuarioDTO (return null): "
							+ e);
		}
		return null;
	}

}
