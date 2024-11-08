package es.mde.kiron.rest;

import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.repositorios.UsuarioDAO;
import es.mde.kiron.security.entities.UsuarioExterno;
import es.mde.kiron.security.repositories.UsuarioExternoDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

  private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

  @Autowired
  UsuarioDAO usuarioDAO;
  @Autowired
  UsuarioExternoDAO usuarioExternoDAO;

  @GetMapping("/existe-email")
  public ResponseEntity<Boolean> existeEmail(@RequestParam String email) {
    boolean resultado = usuarioExternoDAO.existsByEmail(email);
    return ResponseEntity.ok(resultado);
  }

  @PatchMapping("/{id}/cambiar-email")
  public ResponseEntity<Boolean> cambiarEmail(@RequestParam String email,
      @PathVariable String id) {
    Usuario usuario = usuarioDAO.findById(id).orElseThrow();
    UsuarioExterno usuarioExterno =
        usuarioExternoDAO.findByUsername(usuario.getNombre()).orElseThrow();
    usuarioExterno.setEmail(email);
    usuarioExternoDAO.save(usuarioExterno);
    logger.info("El usuario con ID: " + id + " ha cambiado de email");
    boolean resultado = usuarioExternoDAO.existsByEmail(email);
    return ResponseEntity.ok(resultado);
  }
}
