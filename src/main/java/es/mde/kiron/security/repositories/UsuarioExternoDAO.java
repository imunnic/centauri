package es.mde.kiron.security.repositories;

import es.mde.kiron.security.entities.UsuarioExterno;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "users", itemResourceRel = "user",
    collectionResourceRel = "users")
public interface UsuarioExternoDAO extends MongoRepository<UsuarioExterno, String> {
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  Optional<UsuarioExterno> findByUsername(String username);
  Optional<UsuarioExterno> findByUsernameOrEmail(String username, String email);

  @RestResource(exported = false)
  <S extends UsuarioExterno> List<S> findAll(Example<S> example);

  @RestResource(exported = false)
  <S extends UsuarioExterno> S save(S entity);

  @RestResource(exported = false)
  void delete(UsuarioExterno entity);

}
