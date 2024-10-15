package es.mde.kiron.rest;

import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.entidades.SesionRealizada;
import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.modelos.SesionRealizadaMapper;
import es.mde.kiron.modelos.SesionRealizadaResponse;
import es.mde.kiron.repositorios.SesionDAO;
import es.mde.kiron.repositorios.SesionRealizadaDAO;
import es.mde.kiron.servicios.SesionRealizadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/sesiones-realizadas")
public class SesionRealizadaController {

  @Autowired
  private SesionRealizadaDAO sesionRealizadaDAO;

  @Autowired
  private SesionDAO sesionDAO;

  @Autowired
  private SesionRealizadaService sesionRealizadaService;

  @PostMapping
  public ResponseEntity<?> crearSesionRealizada(@RequestBody SesionRealizada sesionRealizada) {
    Sesion sesion = sesionDAO.findById(sesionRealizada.getSesion().getId()).orElseThrow();
    LocalDate fechaSesion = sesion.getFecha();

    if (fechaSesion.isAfter(LocalDate.now())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La fecha de la sesión no puede ser futura.");
    }

    SesionRealizada nuevaSesionRealizada = sesionRealizadaDAO.save(sesionRealizada);
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSesionRealizada);
  }
  @GetMapping("/buscar-sesion-usuario-grupo")
  public ResponseEntity<List<SesionRealizadaResponse>> buscarSesionesPorUsuarioYGrupo(@RequestParam("usuarioId")
  String usuarioId,
      @RequestParam("grupo") String grupo) {
    List<SesionRealizada> lista = sesionRealizadaService.findByUsuarioAndSesionGrupo(usuarioId, grupo);
    List<SesionRealizadaResponse> listaRespuesta = SesionRealizadaMapper.convertirLista(lista);
    return new ResponseEntity<>(listaRespuesta, HttpStatus.OK)  ;
  }
}

