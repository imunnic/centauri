package es.mde.kiron.servicios;

import es.mde.kiron.entidades.Ficha;
import es.mde.kiron.entidades.Sesion;
import es.mde.kiron.repositorios.FichaDAO;
import es.mde.kiron.repositorios.SesionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SesionService {

  @Autowired
  private SesionDAO sesionDAO;

  @Autowired
  private FichaDAO fichaDAO;


  public boolean validarSesion(Sesion sesion) {

    Set<Ficha> fichas = sesion.getFichas().stream()
        .map(ficha -> fichaDAO.findById(ficha.getId()).orElseThrow(() -> new RuntimeException("Ficha no encontrada")))
        .collect(Collectors.toSet());

    sesion.setFichas(fichas);
    if (sesion.getFichas().stream().anyMatch(ficha -> ficha.getRpeEstimado() > 7)) {
      return false;
    }

    LocalDate fechaAnterior = sesion.getFecha().minusDays(1);
    List<Sesion> sesionesAnteriores = sesionDAO.findByFechaAndGrupo(fechaAnterior, sesion.getGrupo()); // Asegúrate de tener un método en el DAO para buscar por fecha

    if (!sesionesAnteriores.isEmpty()) {
      for (Sesion sesionAnterior : sesionesAnteriores) {
        if (sesionAnterior.getFichas().stream().anyMatch(ficha -> ficha.getRpeEstimado() >= 8)) {
          return false;
        }

        int sumaRPETiempoAnterior = sesionAnterior.getFichas().stream()
            .mapToInt(ficha -> ficha.getRpeEstimado() * (ficha.getTiempoEstimado() != 0 ? ficha.getTiempoEstimado() : 0))
            .sum();

        int sumaRPETiempoActual = sesion.getFichas().stream()
            .mapToInt(ficha -> ficha.getRpeEstimado() * (ficha.getTiempoEstimado() != 0 ? ficha.getTiempoEstimado() : 0))
            .sum();

        if ((sumaRPETiempoAnterior + sumaRPETiempoActual) > 24000) {
          return false;
        }
      }
    }

    return true;
  }
}

