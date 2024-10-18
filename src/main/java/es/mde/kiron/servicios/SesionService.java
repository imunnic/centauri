package es.mde.kiron.servicios;

import es.mde.kiron.entidades.Ficha;
import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.entidades.SesionRealizada;
import es.mde.kiron.entidades.Usuario;
import es.mde.kiron.modelos.ParteSesion;
import es.mde.kiron.modelos.ResumenSesionesGrupo;
import es.mde.kiron.repositorios.FichaDAO;
import es.mde.kiron.repositorios.GrupoDAO;
import es.mde.kiron.repositorios.SesionDAO;
import es.mde.kiron.repositorios.SesionRealizadaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SesionService {

  @Autowired
  private SesionDAO sesionDAO;

  @Autowired
  private SesionRealizadaDAO sesionRealizadaDAO;

  @Autowired
  private FichaDAO fichaDAO;

  @Autowired
  private GrupoDAO grupoDAO;

  /**
   * Método para validar que la sesión de entrenamiento no exceda los parámetros óptimos
   * establecidos.
   * Verifica lo siguiente:
   * <ul>
   *   <li>Que el RPE (Rate of Perceived Exertion) de ninguna ficha supere el valor de 7.</li>
   *   <li>Que el RPE de las fichas en la parte fundamental de la sesión no exceda una media de 7
   *   en un intervalo de 60 minutos.</li>
   *   <li>Que la sesión del día anterior no contenga fichas con un RPE de 8 o superior.</li>
   *   <li>Que el RPE medio combinado de la sesión actual y la del día anterior no supere una media
   *   de 7 en un intervalo de 60 minutos.</li>
   * </ul>
   *
   * @param sesion La sesión a validar.
   * @return 0 si la sesión es válida, 1 si alguna ficha tiene un RPE superior a 7,
   *         2 si la sesión anterior tiene fichas con RPE ≥ 8,
   *         3 si la suma del RPE multiplicado por el tiempo en segundos de la sesión actual excede
   *         25200,
   *         4 si la media combinada del RPE multiplicado por el tiempo en segundos de la sesión
   *         actual y la anterior supera 25200.
   */
  public int validarSesion(Sesion sesion) {

    Set<Ficha> fichas = sesion.getFichas().stream()
        .map(ficha -> fichaDAO.findById(ficha.getId()).orElseThrow(() -> new RuntimeException("Ficha no encontrada")))
        .collect(Collectors.toSet());

    sesion.setFichas(fichas);
    if (sesion.getFichas().stream().anyMatch(ficha -> ficha.getRpeEstimado() > 7)) {
      return 1;
    }

    LocalDate fechaAnterior = sesion.getFecha().minusDays(1);
    List<Sesion> sesionesAnteriores = sesionDAO.findByFechaAndGrupo(fechaAnterior, sesion.getGrupo());

    if (!sesionesAnteriores.isEmpty()) {
      for (Sesion sesionAnterior : sesionesAnteriores) {
        if (sesionAnterior.getFichas().stream().anyMatch(ficha -> ficha.getRpeEstimado() >= 8)) {
          return 2;
        }

        int sumaRPETiempoAnterior = sesionAnterior.getFichas().stream()
            .filter(ficha -> ficha.getParteSesion() == ParteSesion.FUNDAMENTAL)
            .mapToInt(ficha -> ficha.getRpeEstimado() * (ficha.getTiempoEstimado() != 0 ? ficha.getTiempoEstimado() : 0))
            .sum();

        int sumaRPETiempoActual = sesion.getFichas().stream()
            .mapToInt(ficha -> ficha.getRpeEstimado() * (ficha.getTiempoEstimado() != 0 ? ficha.getTiempoEstimado() : 0))
            .sum();

        if (sumaRPETiempoActual > 25200) {
          return 3;
        }

        if (((sumaRPETiempoAnterior + sumaRPETiempoActual)/2) > 25200) {
          return 4;
        }
      }
    }

    return 0;
  }

}

