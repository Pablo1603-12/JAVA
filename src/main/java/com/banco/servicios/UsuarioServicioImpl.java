package com.banco.servicios;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.banco.dto.UsuarioDTO;
import com.banco.modelos.Usuario;

import com.banco.modelos.Cambios;
import com.banco.modelos.Movimiento;
import com.banco.modelos.Cuenta;
import com.banco.modelos.ECuenta;
import com.banco.modelos.EMovimiento;
import com.banco.repositorios.CambiosRepository;
import com.banco.repositorios.CuentaRepository;
import com.banco.repositorios.MovimientoRepository;
import com.banco.repositorios.UsuarioRepositorio;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class UsuarioServicioImpl implements IUsuarioServicio {

	@Autowired
	private UsuarioRepositorio repositorio;
	@Autowired
	private CuentaRepository cuentaRepositorio;
	
	@Autowired
	private  MovimientoRepository movimientoRepositorio;
	
	@Autowired
	private  CambiosRepository cambiosRepositorio;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IUsuarioToDao toDao;

	@Autowired
	private IEmailServicio emailServicio;

	@Autowired
	private IUsuarioToDto toDto;
	
	
	@Autowired
	private IFotoServicio fotoServicio;
	
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		
		inicializarUsuarioAdmin();
	}
	
	@Override
	public UsuarioDTO registrarUsuario(UsuarioDTO userDto) {

		try {
			
			Usuario usuarioDaoByEmail = repositorio.findFirstByEmail(userDto.getEmail());

			if (usuarioDaoByEmail != null && usuarioDaoByEmail.isCuentaConfirmada()) {
				userDto.setMensajeError("Usuario ya registrado y confirmado");
				return userDto;
			}
			if (usuarioDaoByEmail != null ) { 
				return null;
			}
		
			
			
			userDto.setClave(passwordEncoder.encode(userDto.getClave()));
			Usuario usuarioDao = toDao.usuarioToDao(userDto);
			usuarioDao.setFechaRegistro(Calendar.getInstance());
			usuarioDao.setRol("ROLE_USER");
	        byte[] fotoPredeterminada = fotoServicio.cargarFotoPredeterminada();
	        usuarioDao.setFoto(fotoPredeterminada);
			if (userDto.isCuentaConfirmada()) {
				usuarioDao.setCuentaConfirmada(true);
				repositorio.save(usuarioDao);
			} else {
				usuarioDao.setCuentaConfirmada(false);
				// Generar token de confirmación
				String token = passwordEncoder.encode(RandomStringUtils.random(30));
				usuarioDao.setToken(token);

				// Guardar el usuario en la base de datos
				repositorio.save(usuarioDao);

				// Enviar el correo de confirmación
				String nombreUsuario = usuarioDao.getNombreApellidos();
				emailServicio.enviarEmailConfirmacion(userDto.getEmail(), nombreUsuario, token);
			}

			return userDto;
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error UsuarioServicioImpl - registrarUsuario()] Argumento no valido al registrar usuario " + iae.getMessage());
		} catch (PersistenceException e) {
			System.out.println("[Error UsuarioServicioImpl - registrarUsuario()] Error de persistencia al registrar usuario " + e.getMessage());
		}
		return null;
	}

	@Override
	public boolean iniciarProcesoRecuperacion(String emailUsuario) {
		try {
			Usuario usuarioExistente = repositorio.findFirstByEmail(emailUsuario);

			if (usuarioExistente != null) {
			
				String token = passwordEncoder.encode(RandomStringUtils.random(30));
				Calendar fechaExpiracion = Calendar.getInstance();
				fechaExpiracion.add(Calendar.MINUTE, 10);
				
				usuarioExistente.setToken(token);
				usuarioExistente.setExpiracionToken(fechaExpiracion);

				
				repositorio.save(usuarioExistente);

				
				String nombreUsuario = usuarioExistente.getNombreApellidos();
				emailServicio.enviarEmailRecuperacion(emailUsuario, nombreUsuario, token);

				return true;

			} else {
				System.out.println("El usuario con email "+ emailUsuario + " no existe");
				return false;
			}
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error UsuarioServicioImpl - iniciarProcesoRecuperacion()] Argumento no valido al iniciar el proceso de recuperación" + iae.getMessage());
			return false;
		} catch (PersistenceException e) {
			System.out.println("[Error UsuarioServicioImpl - iniciarProcesoRecuperacion()] Error de persistencia al iniciar el proceso de recuperación de contraseña" + e.getMessage());
			return false;
		}
	}

	@Override
	public UsuarioDTO obtenerUsuarioPorToken(String token) {
		try {
			Usuario usuarioExistente = repositorio.findByToken(token);

			if (usuarioExistente != null) {
				UsuarioDTO usuario = toDto.usuarioToDto(usuarioExistente);
				return usuario;
			} else {
				System.out.println("No existe el usuario con el token " + token);
				return null;
			}
		} catch (Exception e) {
			System.out.println("[Error UsuarioServicioImpl - obtenerUsuarioPorToken()] Error al obtener usuario por token " + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean modificarContraseñaConToken(UsuarioDTO usuario) {
		try {
			Usuario usuarioExistente = repositorio.findByToken(usuario.getToken());

			if (usuarioExistente != null) {
				String nuevaContraseña = passwordEncoder.encode(usuario.getPassword());
				usuarioExistente.setPassword(nuevaContraseña);
				usuarioExistente.setToken(null);
													
				repositorio.save(usuarioExistente);

				return true;
			}

		} catch (Exception e) {
			System.out.println("[Error UsuarioServicioImpl - modificarContraseñaConToken()] Error al modificar el password con el token " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean confirmarCuenta(String token) {
		try {
			Usuario usuarioExistente = repositorio.findByToken(token);

			if (usuarioExistente != null && !usuarioExistente.isCuentaConfirmada()) {
	
				usuarioExistente.setCuentaConfirmada(true);
				usuarioExistente.setToken(null);
				repositorio.save(usuarioExistente);

				return true;
			} else {
				System.out.println("La cuenta no existe o ya está confirmada");
				return false;
			}
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error UsuarioServicioImpl - confirmarCuenta()] Error al confirmar la cuenta " + iae.getMessage());
			return false;
		} catch (PersistenceException e) {
			System.out.println("[Error UsuarioServicioImpl - confirmarCuenta()] Error de persistencia al confirmar la cuenta" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean estaLaCuentaConfirmada(String email) {
		try {
			Usuario usuarioExistente = repositorio.findFirstByEmail(email);
			if (usuarioExistente != null && usuarioExistente.isCuentaConfirmada()) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("[Error UsuarioServicioImpl - estaLaCuentaConfirmada()] Error al comprobar si la cuenta ya ha sido confirmada" + e.getMessage());
		}	
		return false;
	}

	
	
	
	
	private void inicializarUsuarioAdmin() {
		try {
			if (!repositorio.existsByNombreApellidos("admin")) {
				
				Usuario admin = new Usuario();
				admin.setNombreApellidos("admin");
				admin.setPassword(passwordEncoder.encode("admin"));
				admin.setCuentaConfirmada(true);
				admin.setEmail("admin@admin.com");
				admin.setTelefono("-");
				admin.setRol("ROLE_ADMIN");
				Calendar calendar = Calendar.getInstance();
				admin.setFechaRegistro(calendar);

				repositorio.save(admin);
			}
		} catch (PersistenceException e) {
			System.out.println("[Error UsuarioServicioImpl - inicializarUsuarioAdmin()] Error de persistencia al inicializar el usuario administrador" + e.getMessage());
		}
		
	}


	@Override
	public List<UsuarioDTO> obtenerTodos() {
		return toDto.listaUsuarioToDto(repositorio.findAll());
	}

	@Override
	public UsuarioDTO buscarPorId(long id) {
		try {
			Usuario usuario = repositorio.findById(id).orElse(null);
			if (usuario != null) {
				return toDto.usuarioToDto(usuario);
			}
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error UsuarioServicioImpl - buscarPorId()] Al buscar el usuario por su id " + iae.getMessage());
		}
		return null;
	}

	@Override
	public void eliminar(long id) {
		try {
			Usuario usuario = repositorio.findById(id).orElse(null);
			if (usuario != null) {
				repositorio.delete(usuario);
			}
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error UsuarioServicioImpl - eliminar()] Al eliminar el usuario por su id " + iae.getMessage());
		} 
	}

	@Override
	public void actualizarUsuario(UsuarioDTO usuarioModificado) {

		try {
			Usuario usuarioActual = repositorio.findById(usuarioModificado.getId()).orElse(null);

			usuarioActual.setNombreApellidos(usuarioModificado.getNombre() + " " + usuarioModificado.getApellidos());
			usuarioActual.setTelefono(usuarioModificado.getMovil());
			usuarioActual.setRol(usuarioModificado.getRol());
			usuarioActual.setFoto(fotoServicio.convertirAarrayBytes(usuarioModificado.getFoto()));

			repositorio.save(usuarioActual);
		} catch (PersistenceException pe) {
			System.out.println("[Error UsuarioServicioImpl - actualizarUsuario()] Al modificar el usuario " + pe.getMessage());
			
		}
		
	}
	
	@Override
	public UsuarioDTO buscarPorEmail(String email) {
		try {
			
			UsuarioDTO uDto = new UsuarioDTO();
			Usuario usuario = repositorio.findFirstByEmail(email);
			
			uDto = toDto.usuarioToDto(usuario);
			
			
			if (usuario != null) {
				return uDto;
			}
		} catch (Exception e) {
			System.out.println("[Error UsuarioServicioImpl - buscarPorEmail()] Al buscar el usuario por su email " + e.getMessage());
		}	
		return null;
	}
	
	@Override
    public int contarUsuariosPorRol(String rol) {
        return repositorio.countByRol(rol);
    }

	@Override
	public List<UsuarioDTO> buscarPorCoincidenciaEnEmail(String palabra){
		try {
			List<Usuario> usuarios = repositorio.findByEmailContainsIgnoreCase(palabra);
			if (usuarios != null) {
				return toDto.listaUsuarioToDto(usuarios);
			}
		} catch (Exception e) {
			System.out.println("[Error UsuarioServicioImpl - buscarPorCoincidenciaEnEmail()] Al buscar el usuario por su email " + e.getMessage());
		}
		return null;
	}



}
