package es.imunnic.centauri.repositorios;

import es.imunnic.centauri.entidades.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "usuarios", itemResourceRel = "usuario",
    collectionResourceRel = "usuarios")
public interface UsuarioDAO extends MongoRepository<Usuario, String> {
  Optional<Usuario> findByNombre(String nombre);


}

