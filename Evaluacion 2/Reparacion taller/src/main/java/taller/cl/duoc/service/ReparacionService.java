package taller.cl.duoc.service;

import taller.cl.duoc.model.Reparacion;
import taller.cl.duoc.repository.ReparacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class ReparacionService {

    private static final Logger logger = LoggerFactory.getLogger(ReparacionService.class);

    @Autowired
    private ReparacionRepository repository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${ms-cita.url}")
    private String msCitaUrl;

    // ── CRUD ──────────────────────────────────────────

    public Reparacion finalizar(Reparacion r) {
        logger.info("Iniciando registro de reparación para Cita ID: {}", r.getCitaId());
        // Verifica que la cita existe antes de registrar
        verificarCitaExiste(r.getCitaId());
        Reparacion guardada = repository.save(r);
        logger.info("Reparación finalizada exitosamente con ID: {} y costo: ${}", guardada.getId(), r.getCostoTotal());
        return guardada;
    }

    public List<Reparacion> listar() {
        logger.info("Listando todas las reparaciones");
        List<Reparacion> lista = repository.findAll();
        logger.info("Se encontraron {} reparaciones", lista.size());
        return lista;
    }

    public Optional<Reparacion> buscarPorId(Long id) {
        logger.info("Buscando reparacion con id: {}", id);
        Optional<Reparacion> rep = repository.findById(id);
        if (rep.isEmpty()) logger.warn("Reparacion con id {} no encontrada", id);
        return rep;
    }

    public Reparacion actualizar(Long id, Reparacion nuevosDatos) {
        logger.info("Actualizando reparacion con id: {}", id);
        return repository.findById(id).map(r -> {
            r.setCitaId(nuevosDatos.getCitaId());
            r.setMecanicoResponsable(nuevosDatos.getMecanicoResponsable());
            r.setDetalle(nuevosDatos.getDetalle());
            r.setCostoTotal(nuevosDatos.getCostoTotal());
            Reparacion actualizada = repository.save(r);
            logger.info("Reparacion con id {} actualizada exitosamente", id);
            return actualizada;
        }).orElseThrow(() -> {
            logger.error("Reparacion con id {} no encontrada para actualizar", id);
            return new RuntimeException("Reparacion no encontrada con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando reparacion con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Reparacion con id {} no encontrada para eliminar", id);
            throw new RuntimeException("Reparacion no encontrada con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Reparacion con id {} eliminada exitosamente", id);
    }

    // ── COMUNICACION CON MS-CITA ──────────────────────

    private void verificarCitaExiste(Long citaId) {
        try {
            logger.info("Verificando existencia de cita id {} en ms-cita", citaId);
            webClientBuilder.build()
                    .get()
                    .uri(msCitaUrl + "/api/citas/" + citaId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            logger.info("Cita id {} verificada correctamente", citaId);
        } catch (WebClientResponseException e) {
            logger.error("Cita con id {} no existe en ms-cita: {}", citaId, e.getMessage());
            throw new RuntimeException("La cita con id " + citaId + " no existe");
        } catch (Exception e) {
            logger.error("Error al comunicarse con ms-cita: {}", e.getMessage());
            throw new RuntimeException("No se pudo verificar la cita. Asegúrese que ms-cita esté corriendo.");
        }
    }
}
