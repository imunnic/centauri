package es.mde.kiron.security.controller;

import es.mde.kiron.security.models.AutenticacionResponse;
import es.mde.kiron.security.models.LoginRequest;
import es.mde.kiron.security.models.RegistroResponse;
import es.mde.kiron.security.repositories.InvitacionRegistroDAO;
import es.mde.kiron.security.services.AutenticacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
