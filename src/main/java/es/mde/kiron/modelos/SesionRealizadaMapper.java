package es.mde.kiron.modelos;

import es.mde.kiron.entidades.SesionRealizada;

import java.util.List;
import java.util.stream.Collectors;

public class SesionRealizadaMapper {

  public static List<SesionRealizadaResponse> convertirLista(List<SesionRealizada> sesionesRealizadas) {
    return sesionesRealizadas.stream()
        .map(SesionRealizadaMapper::convertirASesionRealizadaResponse)
        .collect(Collectors.toList());
  }

  private static SesionRealizadaResponse convertirASesionRealizadaResponse(SesionRealizada sesionRealizada) {
    SesionRealizadaResponse response = new SesionRealizadaResponse();
    response.setId(sesionRealizada.getId());
    response.setUsuarioNombre(sesionRealizada.getUsuario().getNombre());
    response.setSesionId(sesionRealizada.getSesionId());
    response.setNombre(sesionRealizada.getNombreSesion());
    response.setGrupo(sesionRealizada.getSesion().getGrupo());
    response.setFecha(sesionRealizada.getFechaSesion());
    response.setRpe(sesionRealizada.getRpe());
    response.setDuracion(sesionRealizada.getTiempo());
    response.setComentarios(sesionRealizada.getComentarios());
    return response;
  }
}
