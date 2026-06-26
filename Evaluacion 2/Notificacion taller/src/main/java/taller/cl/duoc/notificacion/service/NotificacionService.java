package taller.cl.duoc.notificacion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taller.cl.duoc.notificacion.model.Notificacion;
import taller.cl.duoc.notificacion.repository.NotificacionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private NotificacionRepository repository;

    public List<Notificacion> obtenerTodas() {
        logger.info("Listando todas las notificaciones");
        List<Notificacion> lista = repository.findAll();
        logger.info("Se encontraron {} notificaciones", lista.size());
        return lista;
    }

    public Notificacion enviarYGuardar(Notificacion notificacion) {
        logger.info("Enviando notificación tipo '{}' al clienteId: {}", notificacion.getTipo(), notificacion.getClienteId());
        notificacion.setEstado("ENVIADO");
        Notificacion guardada = repository.save(notificacion);
        logger.info("Notificación enviada y guardada con ID: {}", guardada.getId());
        return guardada;
    }

    public Optional<Notificacion> buscarPorId(Long id) {
        logger.info("Buscando notificación con id: {}", id);
        Optional<Notificacion> notificacion = repository.findById(id);
        if (notificacion.isEmpty()) logger.warn("Notificación con id {} no encontrada", id);
        return notificacion;
    }

    public Notificacion actualizar(Long id, Notificacion nuevosDatos) {
        logger.info("Actualizando notificación con id: {}", id);
        return repository.findById(id).map(n -> {
            n.setClienteId(nuevosDatos.getClienteId());
            n.setTipo(nuevosDatos.getTipo());
            n.setMensaje(nuevosDatos.getMensaje());
            n.setFechaEnvio(nuevosDatos.getFechaEnvio());
            n.setEstado(nuevosDatos.getEstado());
            Notificacion actualizada = repository.save(n);
            logger.info("Notificación con id {} actualizada exitosamente", id);
            return actualizada;
        }).orElseThrow(() -> {
            logger.error("Notificación con id {} no encontrada para actualizar", id);
            return new RuntimeException("Notificación no encontrada con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando notificación con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Notificación con id {} no encontrada para eliminar", id);
            throw new RuntimeException("Notificación no encontrada con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Notificación con id {} eliminada exitosamente", id);
    }
}
