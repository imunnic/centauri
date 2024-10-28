package es.mde.kiron.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.entidades.SolicitudAccesoGrupo;
import es.mde.kiron.repositorios.GrupoDAO;
import es.mde.kiron.repositorios.SesionDAO;
import es.mde.kiron.repositorios.SolicitudAccesoGrupoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GruposListener extends AbstractMongoEventListener<Grupo> {

  private static final Logger logger = LoggerFactory.getLogger(GruposListener.class);

  @Autowired
  GrupoDAO grupoDAO;

  @Autowired
  SolicitudAccesoGrupoDAO solicitudAccesoGrupoDAO;

  @Autowired
  SesionDAO sesionDAO;

  @Override
  public void onAfterSave(AfterSaveEvent<Grupo> event) {
    String id = event.getDocument().getObjectId("_id").toString();
    logger.info("Se ha guardado el grupo con ID: " + id);
    super.onAfterSave(event);
  }

  @Override
  public void onBeforeDelete(BeforeDeleteEvent<Grupo> event) {
    String id = event.getDocument().getObjectId("_id").toString();
    Grupo grupo = grupoDAO.findById(id).orElse(null);
    List<SolicitudAccesoGrupo> solicitudes = solicitudAccesoGrupoDAO.findByGrupo(grupo);
    solicitudAccesoGrupoDAO.deleteAll(solicitudes);
    List<Sesion> sesionesFuturas = sesionDAO.findByGrupoIgnoreCase(grupo.getNombre());
    sesionesFuturas = sesionesFuturas.stream()
                                     .filter(s -> s.getFecha().isAfter(LocalDate.now()))
                                     .toList();
    sesionDAO.deleteAll(sesionesFuturas);
    logger.info("Grupo con ID " + id + " eliminado. Eliminadas " + solicitudes.size() +
        " asociadas y " + sesionesFuturas.size() + " sesiones futuras");
    super.onBeforeDelete(event);
  }
}
