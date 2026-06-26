package taller.cl.duoc.mecanico.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taller.cl.duoc.mecanico.model.Mecanico;
import taller.cl.duoc.mecanico.repository.MecanicoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MecanicoService {

    private static final Logger logger = LoggerFactory.getLogger(MecanicoService.class);

    @Autowired
    private MecanicoRepository repository;

    public List<Mecanico> obtenerTodos() {
        logger.info("Listando todos los mecánicos");
        List<Mecanico> lista = repository.findAll();
        logger.info("Se encontraron {} mecánicos", lista.size());
        return lista;
    }

    public Mecanico guardarMecanico(Mecanico mecanico) {
        logger.info("Registrando mecánico: {}", mecanico.getNombreCompleto());
        Mecanico guardado = repository.save(mecanico);
        logger.info("Mecánico registrado con ID: {}", guardado.getId());
        return guardado;
    }

    public Optional<Mecanico> buscarPorId(Long id) {
        logger.info("Buscando mecánico con id: {}", id);
        Optional<Mecanico> mecanico = repository.findById(id);
        if (mecanico.isEmpty()) logger.warn("Mecánico con id {} no encontrado", id);
        return mecanico;
    }

    public Mecanico actualizar(Long id, Mecanico nuevosDatos) {
        logger.info("Actualizando mecánico con id: {}", id);
        return repository.findById(id).map(m -> {
            m.setNombreCompleto(nuevosDatos.getNombreCompleto());
            m.setDisponible(nuevosDatos.getDisponible());
            Mecanico actualizado = repository.save(m);
            logger.info("Mecánico con id {} actualizado exitosamente", id);
            return actualizado;
        }).orElseThrow(() -> {
            logger.error("Mecánico con id {} no encontrado para actualizar", id);
            return new RuntimeException("Mecánico no encontrado con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando mecánico con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Mecánico con id {} no encontrado para eliminar", id);
            throw new RuntimeException("Mecánico no encontrado con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Mecánico con id {} eliminado exitosamente", id);
    }
}
