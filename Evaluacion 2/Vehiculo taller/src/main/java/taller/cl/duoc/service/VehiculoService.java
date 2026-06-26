package taller.cl.duoc.service;

import taller.cl.duoc.model.Vehiculo;
import taller.cl.duoc.repository.VehiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoService.class);

    @Autowired
    private VehiculoRepository repository;

    public Vehiculo registrar(Vehiculo vehiculo) {
        logger.info("Registrando vehiculo con patente: {}", vehiculo.getPatente());
        Vehiculo guardado = repository.save(vehiculo);
        logger.info("Vehiculo registrado con ID: {}", guardado.getId());
        return guardado;
    }

    public List<Vehiculo> listar() {
        logger.info("Listando todos los vehiculos");
        List<Vehiculo> lista = repository.findAll();
        logger.info("Se encontraron {} vehiculos", lista.size());
        return lista;
    }

    public Optional<Vehiculo> buscarPorId(Long id) {
        logger.info("Buscando vehiculo con id: {}", id);
        Optional<Vehiculo> vehiculo = repository.findById(id);
        if (vehiculo.isEmpty()) logger.warn("Vehiculo con id {} no encontrado", id);
        return vehiculo;
    }

    public Vehiculo actualizar(Long id, Vehiculo nuevosDatos) {
        logger.info("Actualizando vehiculo con id: {}", id);
        return repository.findById(id).map(v -> {
            v.setPatente(nuevosDatos.getPatente());
            v.setMarca(nuevosDatos.getMarca());
            v.setClienteId(nuevosDatos.getClienteId());
            Vehiculo actualizado = repository.save(v);
            logger.info("Vehiculo con id {} actualizado exitosamente", id);
            return actualizado;
        }).orElseThrow(() -> {
            logger.error("Vehiculo con id {} no encontrado para actualizar", id);
            return new RuntimeException("Vehículo no encontrado con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando vehiculo con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Vehiculo con id {} no encontrado para eliminar", id);
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Vehiculo con id {} eliminado exitosamente", id);
    }
}
