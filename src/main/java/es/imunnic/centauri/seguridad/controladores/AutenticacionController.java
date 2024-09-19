package es.imunnic.centauri.seguridad.controladores;

import es.imunnic.centauri.seguridad.modelos.AutenticacionResponse;
import es.imunnic.centauri.seguridad.modelos.LoginRequest;
import es.imunnic.centauri.seguridad.modelos.RegistroResponse;
import es.imunnic.centauri.seguridad.servicios.AutenticacionService;
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
