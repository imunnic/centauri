package es.mde.kiron.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndex(def="{'grupo':1,'usuario':1}", unique = true)
public class SolicitudAccesoGrupo {
  @Id
  private String id;
  @DBRef
  private Grupo grupo;
  @DBRef
  private Usuario usuario;

  public SolicitudAccesoGrupo() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Grupo getGrupo() {
    return grupo;
  }

  public void setGrupo(Grupo grupo) {
    this.grupo = grupo;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public String getNombre(){
    return "Solicitud de " + getUsuario().getNombre() + " al grupo " + getGrupo().getNombre();
  }
}
