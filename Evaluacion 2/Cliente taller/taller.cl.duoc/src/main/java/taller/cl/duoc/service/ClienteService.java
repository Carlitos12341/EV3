package taller.cl.duoc.service;

import taller.cl.duoc.model.Cliente;
import taller.cl.duoc.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository repository;

    public Cliente guardar(Cliente c) {
        logger.info("Registrando nuevo cliente: {}", c.getNombre());
        Cliente guardado = repository.save(c);
        logger.info("Cliente registrado con ID: {}", guardado.getId());
        return guardado;
    }

    public List<Cliente> listar() {
        logger.info("Consultando lista de clientes");
        List<Cliente> lista = repository.findAll();
        logger.info("Se encontraron {} clientes", lista.size());
        return lista;
    }

    public Optional<Cliente> buscarPorId(Long id) {
        logger.info("Buscando cliente con id: {}", id);
        Optional<Cliente> cliente = repository.findById(id);
        if (cliente.isEmpty()) logger.warn("Cliente con id {} no encontrado", id);
        return cliente;
    }

    public Cliente actualizar(Long id, Cliente nuevosDatos) {
        logger.info("Actualizando cliente con id: {}", id);
        return repository.findById(id).map(c -> {
            c.setNombre(nuevosDatos.getNombre());
            c.setRut(nuevosDatos.getRut());
            c.setEmail(nuevosDatos.getEmail());
            Cliente actualizado = repository.save(c);
            logger.info("Cliente con id {} actualizado exitosamente", id);
            return actualizado;
        }).orElseThrow(() -> {
            logger.error("Cliente con id {} no encontrado para actualizar", id);
            return new RuntimeException("Cliente no encontrado con id: " + id);
        });
    }

    public void eliminar(Long id) {
        logger.info("Eliminando cliente con id: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Cliente con id {} no encontrado para eliminar", id);
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
        repository.deleteById(id);
        logger.info("Cliente con id {} eliminado exitosamente", id);
    }
}
