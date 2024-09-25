package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.Ejercicio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "ejercicios", itemResourceRel = "ejercicio",
    collectionResourceRel = "ejercicios")
public interface EjercicioDAO extends MongoRepository<Ejercicio, String> {
  List<Ejercicio> findByMusculosPrincipalesContainingIgnoreCase(@Param("nombre") String nombre);
  List<Ejercicio> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
}
