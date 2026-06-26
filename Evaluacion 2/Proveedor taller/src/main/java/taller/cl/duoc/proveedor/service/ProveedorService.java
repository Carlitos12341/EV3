package taller.cl.duoc.proveedor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taller.cl.duoc.proveedor.model.Proveedor;
import taller.cl.duoc.proveedor.repository.ProveedorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorService.class);

    @Autowired
    private ProveedorRepository repository;

    public List<Proveedor> obtenerTodos() {
        logger.info("Listando todos los proveedores");
        List<Proveedor> lista = repository.findAll();
        logger.info("Se encontraron {} proveedores", lista.size());
        return lista;
    }

    public Proveedor guardarProveedor(Proveedor proveedor) {
        logger.info("Registrando proveedor: {}", proveedor.getRazonSocial());
        Proveedor guardado = repository.save(proveedor);
        logger.info("Proveedor registrado con ID: {}", guardado.getId());
        return guardado;
    }

    public Optional<Proveedor> buscarPorId(Long id) {
        logger.info("Buscando proveedor con id: {}", id);
        Optional<Proveedor> proveedor = repository.findById(id);
        if (proveedor.isEmpty()) logger.warn("Proveedor con id {} no encontrado", id);
        return proveedor;
    }

    public Proveedor actualizar(Long id, Proveedor nuevosDatos) {
        logger.info("Actualizando proveedor con id: {}", id);
        return repository.findById(id).map(p -> {
            p.setRutEmpresa(nuevosDatos.getRutEmpresa());
            p.setRazonSocial(nuevosDatos.getRazonSocial());
            p.setContactoNombre(nuevosDatos.getContactoNombre());
            p.setEmail(nuevosDatos.getEmail());
            p.setTelefono(nuevosDatos.getTelefono());
            Proveedor actualizado = repository.save(p);
            logger.info("Proveedor con id {} actualizado exitosamente", id);
            return actualizado;
        }).orElseThrow(() -> {
            logger.error("Proveedor con id {} no encontrado para actualizar", id);
            return new RuntimeException("Proveedor no encontrado con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando proveedor con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Proveedor con id {} no encontrado para eliminar", id);
            throw new RuntimeException("Proveedor no encontrado con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Proveedor con id {} eliminado exitosamente", id);
    }
}
