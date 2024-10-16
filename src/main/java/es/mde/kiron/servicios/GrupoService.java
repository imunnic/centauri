package es.mde.kiron.servicios;

import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.modelos.AbandonarGrupoRequest;
import es.mde.kiron.repositorios.GrupoDAO;
import es.mde.kiron.repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {
  @Autowired
  private UsuarioDAO usuarioDAO;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private GrupoDAO grupoDAO;

  public List<Grupo> encontrarGrupoSinUsuario(String usuarioId){
    Usuario usuario = usuarioDAO.findById(usuarioId).orElseThrow();
    Query query = new Query();
    query.addCriteria(Criteria.where("miembros").ne(usuarioId));
    return mongoTemplate.find(query, Grupo.class);
  }

  public boolean abandonarGrupo(AbandonarGrupoRequest request){
    Usuario usuario = usuarioDAO.findById(request.getUsuarioId()).orElseThrow();
    Grupo grupo = grupoDAO.findById(request.getGrupoId()).orElseThrow();
    boolean removed;
    if (grupo.getEncargado().getId().equals(request.getUsuarioId())){
      removed = false;
    } else {
      removed = grupo.getMiembros().removeIf(m -> m.getId().equals(usuario.getId()));
      if (removed) {
        grupoDAO.save(grupo);
      }
    }
    return removed;
  }
}
