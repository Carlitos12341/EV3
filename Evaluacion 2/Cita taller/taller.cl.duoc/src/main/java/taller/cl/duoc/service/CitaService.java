package taller.cl.duoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import taller.cl.duoc.model.Cita;
import taller.cl.duoc.repository.CitaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class CitaService {

    @Autowired
    private CitaRepository repository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${ms-mecanico.url}")
    private String msMecanicoUrl;

    @Value("${ms-notificacion.url}")
    private String msNotificacionUrl;

    // 1. MÉTODO PARA CREAR/AGENDAR CITAS (CONECTADO)
    public Cita agendar(Cita cita) {
        Map mecanico = null;

        try {
            // 1. Intentamos buscar al mecánico vía WebClient
            mecanico = webClientBuilder.build()
                    .get()
                    .uri(msMecanicoUrl + "/api/mecanicos/" + cita.getMecanicoId())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // Espera la respuesta
        } catch (Exception e) {
            // Si el microservicio responde 404, 500 o está caído, saltará inmediatamente aquí
            throw new RuntimeException("Error de conexión: El microservicio de mecánicos no respondió correctamente o el ID no existe.");
        }

        // 2. Si la llamada no falló, pero el mapa volvió vacío o la propiedad 'disponible' es falsa o nula
        if (mecanico == null || mecanico.get("disponible") == null || !(Boolean) mecanico.get("disponible")) {
            throw new RuntimeException("Operación abortada: El mecánico con ID " + cita.getMecanicoId() + " no existe o no está disponible.");
        }

        // 3. Si pasó las pruebas de arriba, recién ahí se guarda en la base de datos
        Cita citaGuardada = repository.save(cita);

        // ... (el resto de tu código de notificación se mantiene igual abajo)
        return citaGuardada;
    }

    // 2. MÉTODO LISTAR
    public List<Cita> listar() {
        return repository.findAll();
    }

    // 3. MÉTODO BUSCAR POR ID (Arregla el error de la línea 42 del controlador)
    public Optional<Cita> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // 4. MÉTODO ACTUALIZAR (Arregla el error de la línea 51 del controlador)
    public Cita actualizar(Long id, Cita datosNuevos) {
        return repository.findById(id).map(cita -> {
            cita.setFecha(datosNuevos.getFecha());
            cita.setMotivo(datosNuevos.getMotivo());
            cita.setVehiculoId(datosNuevos.getVehiculoId());
            cita.setClienteId(datosNuevos.getClienteId());
            cita.setMecanicoId(datosNuevos.getMecanicoId());
            return repository.save(cita);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
    }

    // 5. MÉTODO ELIMINAR (Arregla el error de la línea 64 del controlador)
    public void eliminar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Cita no encontrada con ID: " + id);
        }
    }
}