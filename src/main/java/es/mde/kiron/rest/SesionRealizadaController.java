package es.mde.kiron.rest;

import es.mde.kiron.entidades.SesionRealizada;
import es.mde.kiron.repositorios.SesionRealizadaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/sesiones-realizadas")
public class SesionRealizadaController {

  @Autowired
  private SesionRealizadaDAO sesionRealizadaDAO;

  @PostMapping
  public ResponseEntity<?> crearSesionRealizada(@RequestBody SesionRealizada sesionRealizada) {
    LocalDate fechaSesion = sesionRealizada.getSesion().getFecha();

    // Validación de la fecha
    if (fechaSesion.isAfter(LocalDate.now())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La fecha de la sesión no puede ser futura.");
    }

    // Guardar la sesión realizada
    SesionRealizada nuevaSesionRealizada = sesionRealizadaDAO.save(sesionRealizada);
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSesionRealizada);
  }
}

