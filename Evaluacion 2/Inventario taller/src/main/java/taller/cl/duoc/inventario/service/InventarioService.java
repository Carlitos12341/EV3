package taller.cl.duoc.inventario.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taller.cl.duoc.inventario.model.Inventario;
import taller.cl.duoc.inventario.repository.InventarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository repository;

    public List<Inventario> obtenerTodos() {
        logger.info("Listando registros de inventario");
        List<Inventario> lista = repository.findAll();
        logger.info("Se encontraron {} registros de inventario", lista.size());
        return lista;
    }

    public Inventario guardarInventario(Inventario inventario) {
        logger.info("Registrando inventario para productoId: {}", inventario.getProductoId());
        Inventario guardado = repository.save(inventario);
        logger.info("Inventario registrado con ID: {}", guardado.getId());
        return guardado;
    }

    public Optional<Inventario> buscarPorId(Long id) {
        logger.info("Buscando inventario con id: {}", id);
        Optional<Inventario> inventario = repository.findById(id);
        if (inventario.isEmpty()) logger.warn("Inventario con id {} no encontrado", id);
        return inventario;
    }

    public Inventario actualizar(Long id, Inventario nuevosDatos) {
        logger.info("Actualizando inventario con id: {}", id);
        return repository.findById(id).map(inv -> {
            inv.setProductoId(nuevosDatos.getProductoId());
            inv.setCantidadFisica(nuevosDatos.getCantidadFisica());
            inv.setStockMinimo(nuevosDatos.getStockMinimo());
            inv.setPasilloUbicacion(nuevosDatos.getPasilloUbicacion());
            Inventario actualizado = repository.save(inv);
            logger.info("Inventario con id {} actualizado exitosamente", id);
            return actualizado;
        }).orElseThrow(() -> {
            logger.error("Inventario con id {} no encontrado para actualizar", id);
            return new RuntimeException("Inventario no encontrado con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando inventario con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Inventario con id {} no encontrado para eliminar", id);
            throw new RuntimeException("Inventario no encontrado con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Inventario con id {} eliminado exitosamente", id);
    }
}
