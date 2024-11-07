package es.mde.kiron.security.repositories;

import es.mde.kiron.security.entities.InvitacionRegistro;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "invitaciones", itemResourceRel = "invitacion",
    collectionResourceRel = "invitaciones")
public interface InvitacionRegistroDAO extends MongoRepository<InvitacionRegistro,String> {

  @Override
  @RestResource(exported = false)
  <S extends InvitacionRegistro> List<S> findAll(Example<S> example);

  @Override
  @RestResource(exported = false)
  void delete(InvitacionRegistro entity);
}
