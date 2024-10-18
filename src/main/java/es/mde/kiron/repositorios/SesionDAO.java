package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Sesion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "sesiones", itemResourceRel = "sesion",
    collectionResourceRel = "sesiones")
public interface SesionDAO extends MongoRepository<Sesion, String> {
  List<Sesion> findByGrupoIgnoreCase(String grupo);
  List<Sesion> findByFechaAndGrupo(LocalDate fecha, String grupo);
  List<Sesion> findByFechaBeforeAndGrupo(LocalDate fecha, String grupo);
}
