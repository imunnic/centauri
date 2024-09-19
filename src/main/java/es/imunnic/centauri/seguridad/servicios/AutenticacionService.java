package es.imunnic.centauri.seguridad.servicios;

import es.imunnic.centauri.entidades.Usuario;
import es.imunnic.centauri.repositorios.UsuarioDAO;
import es.imunnic.centauri.seguridad.entidades.UsuarioExterno;
import es.imunnic.centauri.seguridad.modelos.AutenticacionResponse;
import es.imunnic.centauri.seguridad.modelos.LoginRequest;
import es.imunnic.centauri.seguridad.modelos.RegistroResponse;
import es.imunnic.centauri.seguridad.modelos.Rol;
import es.imunnic.centauri.seguridad.repositorios.UsuarioExternoDAO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author JOSE LUIS PUENTES ALAMOS
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
    Usuario usuario = this.USUARIODAO.findByNombre(request.getUsername()).orElseThrow(); //TODO contemplar que el usuario no exista
//    UserDetails user = usuarioExterno;
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
}

