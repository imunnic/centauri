package es.mde.kiron.security.rest;

import es.mde.kiron.security.entities.InvitacionRegistro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class InvitacionRegistroListener extends AbstractMongoEventListener<InvitacionRegistro> {

  private static final Logger logger = LoggerFactory.getLogger(InvitacionRegistro.class);

  @Override
  public void onAfterSave(AfterSaveEvent<InvitacionRegistro> event) {
    String id = event.getDocument().getObjectId("_id").toString();
    logger.info("Se ha creado la invitación " + id + ", se eliminará en 24 horas");
    super.onAfterSave(event);
  }

  @Override
  public void onAfterDelete(AfterDeleteEvent<InvitacionRegistro> event) {
    String id = event.getDocument().getObjectId("_id").toString();
    logger.info("Se ha eliminado la invitación " + id );
    super.onAfterDelete(event);
  }
}
