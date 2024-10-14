package es.mde.kiron.servicios;

import es.mde.kiron.entidades.Grupo;
import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.repositorios.GrupoDAO;
import es.mde.kiron.repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
}
