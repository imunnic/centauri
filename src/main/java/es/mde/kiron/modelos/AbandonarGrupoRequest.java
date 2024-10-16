package es.mde.kiron.modelos;

public class AbandonarGrupoRequest {
  private String usuarioId;
  private String grupoId;

  public AbandonarGrupoRequest() {
  }

  public String getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(String usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getGrupoId() {
    return grupoId;
  }

  public void setGrupoId(String grupoId) {
    this.grupoId = grupoId;
  }
}
