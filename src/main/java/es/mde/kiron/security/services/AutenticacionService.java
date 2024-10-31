package es.mde.kiron.security.services;

import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.repositorios.UsuarioDAO;
import es.mde.kiron.security.entities.UsuarioExterno;
import es.mde.kiron.security.models.AutenticacionResponse;
import es.mde.kiron.security.models.LoginRequest;
import es.mde.kiron.security.models.RegistroResponse;
import es.mde.kiron.security.models.Rol;
import es.mde.kiron.security.repositories.UsuarioExternoDAO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author JOSE LUIS PUENTES ALAMOS
 * Añadido el método renovarToken por IGNACIO OVIDIO MUÑOZ NICOLÁS
 */
@Service
public class AutenticacionService {
  private final UsuarioExternoDAO USUARIOEXTERNODAO;
  private final UsuarioDAO USUARIODAO;
  private final JwtService JWTSERVICE;
  private final PasswordEncoder ENCODER;
  private final AuthenticationManager MANAGER;

  public AutenticacionService(UsuarioExternoDAO usuarioExternoDAO, UsuarioDAO usuarioDAO, JwtService JWTSERVICE, PasswordEncoder ENCODER,
      AuthenticationManager MANAGER) {
    this.USUARIOEXTERNODAO = usuarioExternoDAO;
    this.USUARIODAO = usuarioDAO;
    this.JWTSERVICE = JWTSERVICE;
    this.ENCODER = ENCODER;
    this.MANAGER = MANAGER;
  }

  public AutenticacionResponse login(LoginRequest request) {
    this.MANAGER.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    UsuarioExterno usuarioExterno = this.USUARIOEXTERNODAO.findByUsername(request.getUsername()).orElseThrow();
    Usuario usuario = this.USUARIODAO.findByNombre(request.getUsername()).orElseThrow();
    Rol rol = usuarioExterno.getRol();
    String token = this.JWTSERVICE.getToken(usuarioExterno);
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

    return new AutenticacionResponse(this.JWTSERVICE.getToken(usuarioExterno), usuarioExterno.getUsername(),
        usuarioExterno.getRol());

  }

  public AutenticacionResponse renovarToken(String token) {
    String username = JWTSERVICE.getUsernameFromToken(token);
    UsuarioExterno usuarioExterno = USUARIOEXTERNODAO.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    String nuevoToken = JWTSERVICE.renovarToken(token, usuarioExterno);
    return new AutenticacionResponse(nuevoToken, usuarioExterno.getUsername(), usuarioExterno.getRol());
  }
}

