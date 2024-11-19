package es.mde.kiron.security.services;

import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.repositorios.UsuarioDAO;
import es.mde.kiron.security.entities.UsuarioExterno;
import es.mde.kiron.security.models.AutenticacionResponse;
import es.mde.kiron.security.models.LoginRequest;
import es.mde.kiron.security.models.RegistroResponse;
import es.mde.kiron.security.models.Rol;
import es.mde.kiron.security.repositories.UsuarioExternoDAO;
import es.mde.kiron.servicios.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.NoSuchElementException;

/**
 * @author JOSE LUIS PUENTES ALAMOS
 * Añadido el método renovarToken, cambiarConstrasena, cambiarNombreUsuario, resetPassword y
 * generarConstrasenaAleatoria por IGNACIO OVIDIO MUÑOZ NICOLÁS
 */
@Service
public class AutenticacionService {
  private final UsuarioExternoDAO USUARIOEXTERNODAO;
  private final UsuarioDAO USUARIODAO;
  private final JwtService JWTSERVICE;
  private final PasswordEncoder ENCODER;
  private final AuthenticationManager MANAGER;
  private final EmailService EMAILSERVICE;
  private static final Logger logger = LoggerFactory.getLogger(Usuario.class);

  public AutenticacionService(UsuarioExternoDAO usuarioExternoDAO, UsuarioDAO usuarioDAO, JwtService JWTSERVICE, PasswordEncoder ENCODER,
      AuthenticationManager MANAGER, EmailService emailService) {
    this.USUARIOEXTERNODAO = usuarioExternoDAO;
    this.USUARIODAO = usuarioDAO;
    this.JWTSERVICE = JWTSERVICE;
    this.ENCODER = ENCODER;
    this.MANAGER = MANAGER;
    this.EMAILSERVICE = emailService;
  }

  public AutenticacionResponse login(LoginRequest request) {
    this.MANAGER.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    UsuarioExterno usuarioExterno = this.USUARIOEXTERNODAO.findByUsername(request.getUsername()).orElseThrow();
    Usuario usuario = this.USUARIODAO.findByNombre(request.getUsername()).orElseThrow();
    Rol rol = usuarioExterno.getRol();
    String token = this.JWTSERVICE.getToken(usuarioExterno);
    logger.info("Login del usuario con ID: " + usuario.getId());
    return new AutenticacionResponse(token, request.getUsername(), usuarioExterno.getRol(), usuario);
  }

  public AutenticacionResponse registro(RegistroResponse request) {
    UsuarioExterno usuarioExterno = new UsuarioExterno();
    usuarioExterno.setUsername(request.getUsername());
    usuarioExterno.setPassword(ENCODER.encode(request.getPassword()));
    usuarioExterno.setEmail(request.getEmail());
    usuarioExterno.setRol(Rol.USUARIO);
    Usuario usuario = new Usuario(usuarioExterno.getUsername(), usuarioExterno.getRol());
    this.USUARIOEXTERNODAO.save(usuarioExterno);
    this.USUARIODAO.save(usuario);
    logger.info("Registro del usuario con ID: " + usuario.getId());
    return new AutenticacionResponse(this.JWTSERVICE.getToken(usuarioExterno), usuarioExterno.getUsername(),
        usuarioExterno.getRol());

  }

  public AutenticacionResponse renovarToken(String token) {
    String username = JWTSERVICE.getUsernameFromToken(token);
    UsuarioExterno usuarioExterno = USUARIOEXTERNODAO.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    Usuario usuario = this.USUARIODAO.findByNombre(usuarioExterno.getUsername())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    String nuevoToken = JWTSERVICE.renovarToken(token, usuarioExterno);
    logger.info("Renovacion del login del usuario con ID: " + usuario.getId());
    return new AutenticacionResponse(nuevoToken,
                                     usuarioExterno.getUsername(),
                                     usuarioExterno.getRol(),
                                     usuario);
  }

  public ResponseEntity<String> cambiarContrasena(String username, String contrasenaAntigua, String contrasenaNueva) {
    UsuarioExterno usuarioExterno = USUARIOEXTERNODAO.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    if (!ENCODER.matches(contrasenaAntigua, usuarioExterno.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("La contraseña antigua no coincide.");
    }
    usuarioExterno.setPassword(ENCODER.encode(contrasenaNueva));
    USUARIOEXTERNODAO.save(usuarioExterno);
    logger.info("Cambio de contraseña para el usuario con ID: " + usuarioExterno.getId());
    return ResponseEntity.ok("Contraseña actualizada correctamente.");
  }

  public ResponseEntity<String> cambiarNombreUsuario(String nombre, String token){
    String username = JWTSERVICE.getUsernameFromToken(token);
    UsuarioExterno usuarioExterno = USUARIOEXTERNODAO.findByUsername(username).orElseThrow(
        () -> new RuntimeException("Usuario no encontrado"));
    Usuario usuario = USUARIODAO.findByNombre(username).orElseThrow(
        () -> new RuntimeException("Usuario no encontrado"));
    usuarioExterno.setUsername(nombre);
    usuario.setNombre(nombre);
    USUARIODAO.save(usuario);
    USUARIOEXTERNODAO.save(usuarioExterno);
    logger.info("Cambio de nombre para el usuario con ID: " + usuarioExterno.getId());
    return ResponseEntity.ok("Nombre actualizado correctamente a " + nombre);
  }

  public void resetPassword(String email) {
    UsuarioExterno usuarioExterno = this.USUARIOEXTERNODAO.findByEmail(email)
        .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    String nuevaPassword = generarContrasenaAleatoria();
    String passwordEncriptada = ENCODER.encode(nuevaPassword);
    logger.info("La contraseña del usuario con ID:" + usuarioExterno.getId() + " se va a cambiar");
    try{
    EMAILSERVICE.recuperarPassword(usuarioExterno.getEmail(), nuevaPassword);
    usuarioExterno.setPassword(passwordEncriptada);
    USUARIOEXTERNODAO.save(usuarioExterno);
    }catch(Exception e){
      logger.error("La contraseña del usuario con ID:" + usuarioExterno.getId()
          + " no se ha podido cambiar");
      throw new RuntimeException(e.getMessage());
    }
  }
  
  private String generarContrasenaAleatoria() {
    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom random = new SecureRandom();
    StringBuilder contrasena = new StringBuilder(10);
    for (int i = 0; i < 10; i++) {
      int indice = random.nextInt(caracteres.length());
      contrasena.append(caracteres.charAt(indice));
    }
    return contrasena.toString();
  }

}

