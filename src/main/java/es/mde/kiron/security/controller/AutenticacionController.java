package es.mde.kiron.security.controller;

import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.security.entities.UsuarioExterno;
import es.mde.kiron.security.models.AutenticacionResponse;
import es.mde.kiron.security.models.CambioPasswordRequest;
import es.mde.kiron.security.models.LoginRequest;
import es.mde.kiron.security.models.RegistroResponse;
import es.mde.kiron.security.repositories.InvitacionRegistroDAO;
import es.mde.kiron.security.services.AutenticacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/autenticacion")
public class AutenticacionController {
  private final AutenticacionService AUTHSERVICE;
  private final InvitacionRegistroDAO INVITACIONDAO;

  public AutenticacionController(AutenticacionService AUTHSERVICE, InvitacionRegistroDAO invitacion) {
    this.AUTHSERVICE = AUTHSERVICE;
    this.INVITACIONDAO = invitacion;
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<AutenticacionResponse> login(@RequestBody LoginRequest request){
    return ResponseEntity.ok(AUTHSERVICE.login(request));
  }

  @PostMapping("/registro/{invitacionId}")
  @ResponseBody
  public ResponseEntity<AutenticacionResponse> register(@RequestBody RegistroResponse request,
      @PathVariable String invitacionId){
    ResponseEntity<AutenticacionResponse> respuesta =
        new ResponseEntity<>(new AutenticacionResponse(), HttpStatus.BAD_REQUEST);
    if (INVITACIONDAO.existsById(invitacionId)) {
    respuesta = ResponseEntity.ok(AUTHSERVICE.registro(request)) ;
    }
    return respuesta;
  }

  @PostMapping("/renovacion")
  @ResponseBody
  public ResponseEntity<AutenticacionResponse> renovarToken(
      @RequestHeader("Authorization") String token) {
    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    return ResponseEntity.ok(AUTHSERVICE.renovarToken(token));
  }

  @PostMapping("/cambiar-password")
  public ResponseEntity<String> cambiarPassword(@RequestBody CambioPasswordRequest request) {
    return AUTHSERVICE.cambiarContrasena(
        request.getUsername(), request.getPasswordAntigua(), request.getPasswordNueva());
  }

  @PostMapping("/cambiar-nombre-usuario")
  public ResponseEntity<String> cambiarNombreUsuario(@RequestParam String nombre, @RequestHeader("Authorization") String token) {
    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
      return AUTHSERVICE.cambiarNombreUsuario(nombre, token);
    } else {
      return new ResponseEntity<>("Autorización incorrecta", HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(@RequestBody UsuarioExterno usuario) {
    try {
      AUTHSERVICE.resetPassword(usuario.getEmail());
      return ResponseEntity.ok("La contraseña se ha restablecido y se ha enviado al correo electrónico.");
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error en el servidor");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
    }
  }

}
