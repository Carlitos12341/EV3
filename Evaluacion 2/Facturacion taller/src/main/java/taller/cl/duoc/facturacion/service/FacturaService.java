package taller.cl.duoc.facturacion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taller.cl.duoc.facturacion.model.Factura;
import taller.cl.duoc.facturacion.repository.FacturaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);

    @Autowired
    private FacturaRepository repository;

    public List<Factura> obtenerTodas() {
        logger.info("Listando todas las facturas");
        List<Factura> lista = repository.findAll();
        logger.info("Se encontraron {} facturas", lista.size());
        return lista;
    }

    public Factura guardarFactura(Factura factura) {
        logger.info("Registrando factura para reparacionId: {}", factura.getReparacionId());
        Factura guardada = repository.save(factura);
        logger.info("Factura registrada con ID: {}", guardada.getId());
        return guardada;
    }

    public Optional<Factura> buscarPorId(Long id) {
        logger.info("Buscando factura con id: {}", id);
        Optional<Factura> factura = repository.findById(id);
        if (factura.isEmpty()) logger.warn("Factura con id {} no encontrada", id);
        return factura;
    }

    public Factura actualizar(Long id, Factura nuevosDatos) {
        logger.info("Actualizando factura con id: {}", id);
        return repository.findById(id).map(f -> {
            f.setReparacionId(nuevosDatos.getReparacionId());
            f.setClienteId(nuevosDatos.getClienteId());
            f.setMontoTotal(nuevosDatos.getMontoTotal());
            f.setEstadoPago(nuevosDatos.getEstadoPago());
            Factura actualizada = repository.save(f);
            logger.info("Factura con id {} actualizada exitosamente", id);
            return actualizada;
        }).orElseThrow(() -> {
            logger.error("Factura con id {} no encontrada para actualizar", id);
            return new RuntimeException("Factura no encontrada con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando factura con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Factura con id {} no encontrada para eliminar", id);
            throw new RuntimeException("Factura no encontrada con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Factura con id {} eliminada exitosamente", id);
    }
}
