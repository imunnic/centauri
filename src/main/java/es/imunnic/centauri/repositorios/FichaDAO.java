package es.imunnic.centauri.repositorios;

import es.imunnic.centauri.entidades.Ficha;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "fichas", itemResourceRel = "ficha",
    collectionResourceRel = "fichas")
public interface FichaDAO extends MongoRepository<Ficha, String> {

}
