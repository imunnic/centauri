package es.mde.kiron.servicios;

import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.SolicitudAccesoGrupo;
import es.mde.kiron.repositorios.GrupoDAO;
import es.mde.kiron.repositorios.SolicitudAccesoGrupoDAO;
import es.mde.kiron.repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudAccesoGrupoService {

  @Autowired
  SolicitudAccesoGrupoDAO solicitudAccesoDAO;

  @Autowired
  GrupoDAO grupoDAO;

  @Autowired
  UsuarioDAO usuarioDAO;

  public boolean agregarMiembroGrupo(SolicitudAccesoGrupo solicitud, boolean aceptar){
    if(aceptar){
      Grupo grupo = solicitud.getGrupo();
      grupo.getMiembros().add(solicitud.getUsuario());
      grupoDAO.save(grupo);
      solicitudAccesoDAO.delete(solicitud);
      return true;
    } else {
      solicitudAccesoDAO.delete(solicitud);
      return false;
    }

  }

}
