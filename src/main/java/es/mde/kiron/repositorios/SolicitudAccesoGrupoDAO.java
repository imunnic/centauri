package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.SolicitudAccesoGrupo;
import es.mde.kiron.entidades.Usuario;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RepositoryRestResource(path = "solicitud-acceso-grupos", itemResourceRel = "solicitudAccesoGrupo",
    collectionResourceRel = "solicitudAccesoGrupos")
public interface SolicitudAccesoGrupoDAO extends MongoRepository<SolicitudAccesoGrupo, String> {
  List<SolicitudAccesoGrupo> findByGrupo(Grupo grupo);
  List<SolicitudAccesoGrupo> findByUsuario(Usuario usuario);
}
