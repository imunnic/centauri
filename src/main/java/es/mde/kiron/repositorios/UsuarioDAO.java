package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(path = "usuarios", itemResourceRel = "usuario",
    collectionResourceRel = "usuarios")
public interface UsuarioDAO extends MongoRepository<Usuario, String> {
  Optional<Usuario> findByNombre(String username);
  boolean existsByNombre(String nombre);

  @Override
  @RestResource(exported = false)
  void delete(Usuario entity);
}

