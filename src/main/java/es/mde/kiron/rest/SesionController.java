package es.mde.kiron.rest;

import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.repositorios.SesionDAO;
import es.mde.kiron.servicios.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/sesiones")
public class SesionController {
  @Autowired
  private SesionService sesionService;

  @Autowired
  private SesionDAO sesionDAO;

  @PostMapping("/validar")
  public ResponseEntity<Integer> validarSesion(@RequestBody Sesion sesion) {
      int resultado = sesionService.validarSesion(sesion);
      return ResponseEntity.ok(resultado);

  }
}
