package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "grupos", itemResourceRel = "grupo",
    collectionResourceRel = "grupos")
public interface GrupoDAO extends MongoRepository<Grupo, String>{
  List<Grupo> findByEncargado(Usuario encargado);
  List<Grupo> findByMiembrosContaining(Usuario miembro);
  Grupo findByNombre(String nombre);
  boolean existsGrupoByNombreIgnoreCase(String nombre);

}
