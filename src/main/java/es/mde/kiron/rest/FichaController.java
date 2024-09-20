package es.mde.kiron.rest;

import es.mde.kiron.entidades.Ficha;
import es.mde.kiron.modelos.Estado;
import es.mde.kiron.repositorios.FichaDAO;
import es.mde.kiron.repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/fichas")
public class FichaController {

  private final FichaDAO fichaDAO;

  @Autowired
  public FichaController(FichaDAO fichaDAO) {
    this.fichaDAO = fichaDAO;
  }


  @PatchMapping("/{id}/estado")
  public ResponseEntity<Ficha> modificarEstadoFicha(@PathVariable String id, @RequestParam boolean aprobado) {
    Optional<Ficha> fichaOptional = fichaDAO.findById(id);

    if (!fichaOptional.isPresent()) {
      return ResponseEntity.notFound().build(); // Ficha no encontrada
    }

    Ficha ficha = fichaOptional.get();
    if (aprobado) {
      ficha.setEstado(Estado.APROBADO);
    } else {
      ficha.setEstado(Estado.RECHAZADO);
    }

    Ficha fichaActualizada = fichaDAO.save(ficha);
    return ResponseEntity.ok(fichaActualizada);
  }


}

