package es.mde.kiron.rest;

import es.mde.kiron.entidades.Ficha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;


@Component
public class FichaListener extends AbstractMongoEventListener<Ficha> {

  private static final Logger logger = LoggerFactory.getLogger(GruposListener.class);


  @Override
  public void onAfterSave(AfterSaveEvent<Ficha> event) {
    String id = event.getDocument().getObjectId("_id").toString();
    logger.info("Se ha guardado la ficha con ID: " + id);
    super.onAfterSave(event);
  }

  @Override
  public void onBeforeDelete(BeforeDeleteEvent<Ficha> event) {
    String id = event.getDocument().getObjectId("_id").toString();
    logger.info("Ficha con ID " + id + " eliminada.");
    super.onBeforeDelete(event);
  }
}
