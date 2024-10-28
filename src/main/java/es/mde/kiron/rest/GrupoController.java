package es.mde.kiron.rest;

import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.modelos.AbandonarGrupoRequest;
import es.mde.kiron.repositorios.GrupoDAO;
import es.mde.kiron.repositorios.SesionDAO;
import es.mde.kiron.servicios.GrupoService;
import es.mde.kiron.servicios.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/grupos")
public class GrupoController {
  @Autowired
  private GrupoService grupoService;

  @Autowired
  private GrupoDAO grupoDAO;

  @GetMapping("/encontrarGruposSinUsuario")
  public ResponseEntity<List<Grupo>> validarSesion(@RequestParam String usuarioId) {
    List<Grupo> gruposSinUsuario = grupoService.encontrarGrupoSinUsuario(usuarioId);
    return ResponseEntity.ok(gruposSinUsuario);
  }
  @PatchMapping("/abandonar-grupo")
  public ResponseEntity<Boolean> abandonarGrupo(@RequestBody AbandonarGrupoRequest request) {
    boolean abandonado = grupoService.abandonarGrupo(request);
    return new ResponseEntity<>(abandonado, abandonado ? HttpStatus.OK :
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
