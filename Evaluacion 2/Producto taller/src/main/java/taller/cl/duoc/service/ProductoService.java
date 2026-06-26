package taller.cl.duoc.service;

import taller.cl.duoc.model.Producto;
import taller.cl.duoc.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository repository;

    public Producto guardar(Producto p) {
        logger.info("Registrando producto: {} con stock: {}", p.getNombre(), p.getStock());
        Producto guardado = repository.save(p);
        logger.info("Producto registrado con ID: {}", guardado.getId());
        return guardado;
    }

    public List<Producto> listar() {
        logger.info("Listando todos los productos");
        List<Producto> lista = repository.findAll();
        logger.info("Se encontraron {} productos", lista.size());
        return lista;
    }

    public Optional<Producto> buscarPorId(Long id) {
        logger.info("Buscando producto con id: {}", id);
        Optional<Producto> producto = repository.findById(id);
        if (producto.isEmpty()) logger.warn("Producto con id {} no encontrado", id);
        return producto;
    }

    public Producto actualizar(Long id, Producto nuevosDatos) {
        logger.info("Actualizando producto con id: {}", id);
        return repository.findById(id).map(p -> {
            p.setNombre(nuevosDatos.getNombre());
            p.setStock(nuevosDatos.getStock());
            p.setPrecioUnitario(nuevosDatos.getPrecioUnitario());
            Producto actualizado = repository.save(p);
            logger.info("Producto con id {} actualizado exitosamente", id);
            return actualizado;
        }).orElseThrow(() -> {
            logger.error("Producto con id {} no encontrado para actualizar", id);
            return new RuntimeException("Producto no encontrado con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando producto con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Producto con id {} no encontrado para eliminar", id);
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Producto con id {} eliminado exitosamente", id);
    }
}
