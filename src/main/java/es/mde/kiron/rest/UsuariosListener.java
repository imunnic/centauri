package es.mde.kiron.rest;

import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.repositorios.UsuarioDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class UsuariosListener extends AbstractMongoEventListener<Usuario> {

  private static final Logger logger = LoggerFactory.getLogger(GruposListener.class);

  @Autowired
  private UsuarioDAO USUARIODAO;

  @Override
  public void onAfterSave(AfterSaveEvent<Usuario> event) {
    String id = event.getDocument().getObjectId("_id").toString();
    logger.info("Modificado usuario con ID: " + id);
    super.onAfterSave(event);
  }
}
