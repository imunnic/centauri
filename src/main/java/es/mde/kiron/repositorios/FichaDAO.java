package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Ficha;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "fichas", itemResourceRel = "ficha",
    collectionResourceRel = "fichas")
public interface FichaDAO extends MongoRepository<Ficha, String> {

}
