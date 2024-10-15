package es.mde.kiron.servicios;

import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.entidades.SesionRealizada;
import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.repositorios.SesionDAO;
import es.mde.kiron.repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SesionRealizadaService {
  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private UsuarioDAO usuarioDAO;

  @Autowired
  private SesionDAO sesionDAO;

  public List<SesionRealizada> findByUsuarioAndSesionGrupo(String usuarioId, String grupo) {
    // Obtener el usuario
    Usuario usuario = usuarioDAO.findById(usuarioId).orElseThrow();

    // Obtener la lista de sesiones por el grupo (ignorar mayúsculas/minúsculas)
    List<Sesion> sesiones = sesionDAO.findByGrupoIgnoreCase(grupo);

    // Si no se encontraron sesiones, devolver lista vacía
    if (sesiones.isEmpty()) {
      return Collections.emptyList();
    }

    // Obtener los IDs de las sesiones
    List<String> sesionIds = sesiones.stream()
        .map(Sesion::getId)  // Obtener los IDs de las sesiones
        .collect(Collectors.toList());

    // Crear criterio para la consulta usando el ID del usuario y los IDs de las sesiones
    Criteria criteria = Criteria.where("usuario").is(usuario)
        .and("sesion").in(sesionIds);  // Usar el operador 'in' para verificar coincidencias en la lista de IDs

    // Construir la consulta con el criterio
    Query query = new Query(criteria);

    // Ejecutar la consulta y devolver los resultados
    return mongoTemplate.find(query, SesionRealizada.class);
  }
}
