package es.mde.kiron.rest;

import es.mde.kiron.entidades.PlanDeEntrenamiento;
import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.modelos.AgregarSesionesRequest;
import es.mde.kiron.repositorios.PlanDeEntrenamientoDAO;
import es.mde.kiron.repositorios.SesionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/planes")
public class PlanDeEntrenamientoController {
  @Autowired
  PlanDeEntrenamientoDAO planDeEntrenamientoDAO;
  @Autowired
  SesionDAO sesionDAO;

  @GetMapping("/respuesta")
  public ResponseEntity<List<PlanDeEntrenamientoResponse>> getPlanes() {
    List<PlanDeEntrenamiento> planes = planDeEntrenamientoDAO.findAll();
    List<PlanDeEntrenamientoResponse> planesResponse = new ArrayList<>();
    planesResponse = planes.stream().map(plan -> {
      PlanDeEntrenamientoResponse planResponse = new PlanDeEntrenamientoResponse();
      planResponse.setId(plan.getId());
      planResponse.setNombre(plan.getNombre());
      planResponse.setObjetivo(plan.getObjetivo());
      planResponse.setDescripcion(plan.getDescripcion());
      planResponse.setAutor(plan.getAutor() != null ? plan.getAutor() : null);
      return  planResponse;
    }).collect(Collectors.toList());
    return ResponseEntity.ok(planesResponse);
  }

  @GetMapping("/{id}/completo")
  public ResponseEntity<PlanDeEntrenamiento> getPlanes(@PathVariable String id) {
    PlanDeEntrenamiento planDeEntrenamiento = planDeEntrenamientoDAO.findById(id).orElseThrow();
    return ResponseEntity.ok(planDeEntrenamiento);
  }

  @PostMapping("/{id}/agregarSesiones")
  public ResponseEntity<Boolean> getPlanes(@PathVariable String id, @RequestBody
  AgregarSesionesRequest request) {
    PlanDeEntrenamiento plan = planDeEntrenamientoDAO.findById(id).orElseThrow();
    plan.getSesiones().forEach(sesion -> {
      Sesion nuevaSesion = new Sesion();
      nuevaSesion.setNombre(sesion.getNombre());
      nuevaSesion.setFichas(sesion.getFichas());
      nuevaSesion.setGrupo(request.getGrupo());
      LocalDate fechaSesion = request.getFecha().plusDays(sesion.getDia() - 1);
      nuevaSesion.setFecha(fechaSesion);
      nuevaSesion.setUnidad(request.getUnidad());
      sesionDAO.save(nuevaSesion);
    });
    return ResponseEntity.ok(true);
  }


}
