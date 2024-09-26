package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Equipamiento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "equipamientos", itemResourceRel = "equipamiento",
    collectionResourceRel = "equipamientos")
public interface EquipamientoDAO extends MongoRepository<Equipamiento, String> {
}
