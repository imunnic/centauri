package es.mde.kiron.servicios;

import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.entidades.SesionRealizada;
import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.modelos.ResumenSesionesGrupo;
import es.mde.kiron.repositorios.GrupoDAO;
import es.mde.kiron.repositorios.SesionDAO;
import es.mde.kiron.repositorios.SesionRealizadaDAO;
import es.mde.kiron.repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SesionRealizadaService {
  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private UsuarioDAO usuarioDAO;

  @Autowired
  private SesionDAO sesionDAO;

  @Autowired
  private GrupoDAO grupoDAO;

  @Autowired
  private SesionRealizadaDAO sesionRealizadaDAO;

  public List<SesionRealizada> findByUsuarioAndSesionGrupo(String usuarioId, String grupo) {

    Usuario usuario = usuarioDAO.findById(usuarioId).orElseThrow();

    List<Sesion> sesiones = sesionDAO.findByGrupoIgnoreCase(grupo);

    if (sesiones.isEmpty()) {
      return Collections.emptyList();
    }

    List<String> sesionIds = sesiones.stream()
        .map(Sesion::getId)
        .collect(Collectors.toList());

    Criteria criteria = Criteria.where("usuario").is(usuario)
        .and("sesion").in(sesionIds);

    Query query = new Query(criteria);

    return mongoTemplate.find(query, SesionRealizada.class);
  }


  public List<ResumenSesionesGrupo> getSesionesPreviasDeGrupo(String nombreGrupo){
    List<ResumenSesionesGrupo> listaResumen = new ArrayList<>();
    LocalDate fechaActual = LocalDate.now();

    List<Sesion> sesionesPreviasDeGrupo = sesionDAO.findByFechaBeforeAndGrupo(fechaActual, nombreGrupo);

    Set<Usuario> usuariosGrupo = grupoDAO.findByNombre(nombreGrupo).getMiembros();

    for (Sesion sesion : sesionesPreviasDeGrupo) {
      List<SesionRealizada> sesionesRealizadasGrupo = new ArrayList<>();

      for (Usuario usuario : usuariosGrupo) {
        List<SesionRealizada> sesionRealizadas = sesionRealizadaDAO.findByUsuarioAndSesion(usuario, sesion);
        sesionesRealizadasGrupo.addAll(sesionRealizadas);
      }

      double rpeSum = 0;
      double tiempoSum = 0;
      int totalSesiones = sesionesRealizadasGrupo.size();

      if (totalSesiones > 0) {
        rpeSum = sesionesRealizadasGrupo.stream().mapToInt(SesionRealizada::getRpe).sum();
        tiempoSum = sesionesRealizadasGrupo.stream().mapToInt(SesionRealizada::getTiempo).sum();
      }

      int rpeMedio = totalSesiones > 0 ? (int) (rpeSum / totalSesiones) : 0;
      int tiempoMedio = totalSesiones > 0 ? (int) (tiempoSum / totalSesiones) : 0;

      ResumenSesionesGrupo resumen = new ResumenSesionesGrupo();
      resumen.setNombre(sesion.getNombre());
      resumen.setSesionId(sesion.getId());
      resumen.setRpeMedio(rpeMedio);
      resumen.setTiempoMedio(tiempoMedio);

      listaResumen.add(resumen);
    }

    return listaResumen;
  }
}
