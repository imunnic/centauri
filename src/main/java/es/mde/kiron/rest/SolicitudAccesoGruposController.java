package es.mde.kiron.rest;

import es.mde.kiron.entidades.Ejercicio;
import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.entidades.SolicitudAccesoGrupo;
import es.mde.kiron.repositorios.EjercicioDAO;
import es.mde.kiron.repositorios.SesionDAO;
import es.mde.kiron.repositorios.SolicitudAccesoGrupoDAO;
import es.mde.kiron.servicios.SesionService;
import es.mde.kiron.servicios.SolicitudAccesoGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/solicitud-acceso-grupos")
public class SolicitudAccesoGruposController {
  @Autowired
  private SolicitudAccesoGrupoService solicitudService;

  @Autowired
  private SolicitudAccesoGrupoDAO solicitudDAO;

  @PostMapping("/rechazar")
  public ResponseEntity<SolicitudAccesoGrupo> rechazar(@RequestBody SolicitudAccesoGrupo solicitud) {
    SolicitudAccesoGrupo solicitudAccesoGrupo = solicitudDAO.findById(solicitud.getId()).orElseThrow();
    solicitudService.agregarMiembroGrupo(solicitudAccesoGrupo, false);
    return ResponseEntity.ok(solicitudAccesoGrupo);
  }
  @PostMapping("/validar")
  public ResponseEntity<SolicitudAccesoGrupo> validar(@RequestBody SolicitudAccesoGrupo solicitud) {
    SolicitudAccesoGrupo solicitudAccesoGrupo = solicitudDAO.findById(solicitud.getId()).orElseThrow();
    solicitudService.agregarMiembroGrupo(solicitudAccesoGrupo, true);
    return ResponseEntity.ok(solicitudAccesoGrupo);
  }
}
