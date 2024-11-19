package es.mde.kiron.repositorios;

import es.mde.kiron.entidades.PlanDeEntrenamiento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "planes", itemResourceRel = "plan",
    collectionResourceRel = "planes")
public interface PlanDeEntrenamientoDAO extends MongoRepository<PlanDeEntrenamiento, String> {
  List<PlanDeEntrenamiento> findByNombre(String nombre);
  List<PlanDeEntrenamiento> findByObjetivo(String objetivo);
}
