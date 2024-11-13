package es.mde.kiron.rest;

import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.PlanDeEntrenamiento;
import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.repositorios.PlanDeEntrenamientoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/planes")
public class PlanDeEntrenamientoController {
  @Autowired
  PlanDeEntrenamientoDAO planDeEntrenamientoDAO;

  @GetMapping
  public ResponseEntity<List<PlanDeEntrenamientoResponse>> getPlanes() {
    List<PlanDeEntrenamiento> planes = planDeEntrenamientoDAO.findAll();
    List<PlanDeEntrenamientoResponse> planesResponse = new ArrayList<>();
    planesResponse = planes.stream().map(plan -> {
      PlanDeEntrenamientoResponse planResponse = new PlanDeEntrenamientoResponse();
      planResponse.setId(plan.getId());
      planResponse.setNombre(plan.getNombre());
      planResponse.setObjetivo(plan.getObjetivo());
      planResponse.setDescripcion(plan.getDescripcion());
      planResponse.setAutor(plan.getAutor() != null ? plan.getAutor().getNombre() : null);
      return  planResponse;
    }).collect(Collectors.toList());
    return ResponseEntity.ok(planesResponse);
  }
  @GetMapping("/{id}")
  public ResponseEntity<PlanDeEntrenamiento> getPlanes(@PathVariable String id) {
    PlanDeEntrenamiento planDeEntrenamiento = planDeEntrenamientoDAO.findById(id).orElseThrow();
    return ResponseEntity.ok(planDeEntrenamiento);
  }
}
