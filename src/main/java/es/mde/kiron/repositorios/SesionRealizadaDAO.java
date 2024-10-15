package es.mde.kiron.repositorios;


import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.entidades.SesionRealizada;
import es.mde.kiron.entidades.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "sesiones-realizadas", itemResourceRel = "sesionRealizada",
    collectionResourceRel = "sesionesRealizadas")
public interface SesionRealizadaDAO extends MongoRepository<SesionRealizada, String> {
  List<SesionRealizada> findByUsuario(Usuario usuario);
  List<SesionRealizada> findBySesion(Sesion sesion);
  List<SesionRealizada> findByUsuarioAndSesion(Usuario usuario, Sesion sesion);
}
