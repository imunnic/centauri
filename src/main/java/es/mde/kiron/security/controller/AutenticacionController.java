package es.mde.kiron.security.controller;

import es.mde.kiron.security.models.AutenticacionResponse;
import es.mde.kiron.security.models.LoginRequest;
import es.mde.kiron.security.models.RegistroResponse;
import es.mde.kiron.security.services.AutenticacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autenticacion")
public class AutenticacionController {
  private final AutenticacionService AUTHSERVICE;

  public AutenticacionController(AutenticacionService AUTHSERVICE) {
    this.AUTHSERVICE = AUTHSERVICE;
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<AutenticacionResponse> login(@RequestBody LoginRequest request){
    return ResponseEntity.ok(AUTHSERVICE.login(request));
  }

  @PostMapping("/registro")
  @ResponseBody
  public ResponseEntity<AutenticacionResponse> register(@RequestBody RegistroResponse request){
    return ResponseEntity.ok(AUTHSERVICE.registro(request));
  }

}
