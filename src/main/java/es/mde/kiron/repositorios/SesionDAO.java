package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Sesion;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "sesiones", itemResourceRel = "sesion",
    collectionResourceRel = "sesiones")
public interface SesionDAO extends MongoRepository<Sesion, String> {
  List<Sesion> findByGrupoIgnoreCase(String grupo);
}