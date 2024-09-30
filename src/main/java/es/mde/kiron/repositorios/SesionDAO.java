package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Sesion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "sesiones", itemResourceRel = "sesion",
    collectionResourceRel = "sesiones")
public interface SesionDAO extends MongoRepository<Sesion, String> {

}
