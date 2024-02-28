package com.banco.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banco.modelos.Usuario;


public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

	
	public Usuario findFirstByEmail(String email);
	

	public Usuario findByToken(String token);
	
	
	public boolean existsByNombreApellidos(String nombreApellidos);
	
	
	@Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol")
    public int countByRol(@Param("rol") String rol);
	

	public List<Usuario> findByEmailContainsIgnoreCase(String palabraClave);
	
	
	


}
