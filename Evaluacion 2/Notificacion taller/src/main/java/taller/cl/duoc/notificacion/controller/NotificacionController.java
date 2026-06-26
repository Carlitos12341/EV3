package taller.cl.duoc.notificacion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taller.cl.duoc.notificacion.model.Notificacion;
import taller.cl.duoc.notificacion.service.NotificacionService;

import java.util.List;

@Tag(name = "Notificaciones", description = "Gestión de notificaciones enviadas a clientes")
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionController.class);

    @Autowired
    private NotificacionService service;

    @Operation(summary = "Enviar notificación", description = "Crea y envía una nueva notificación a un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Notificación enviada exitosamente",
            content = @Content(schema = @Schema(implementation = Notificacion.class))),
        @ApiResponse(responseCode = "400", description = "Datos de notificación inválidos")
    })
    @PostMapping
    public ResponseEntity<Notificacion> crear(@Valid @RequestBody Notificacion notificacion) {
        logger.info("POST /api/notificaciones - enviando notificación");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.enviarYGuardar(notificacion));
    }

    @Operation(summary = "Listar notificaciones", description = "Retorna todas las notificaciones registradas")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        logger.info("GET /api/notificaciones - listando notificaciones");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @Operation(summary = "Buscar notificación por ID", description = "Retorna una notificación según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación encontrada",
            content = @Content(schema = @Schema(implementation = Notificacion.class))),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(
            @Parameter(description = "ID de la notificación", required = true) @PathVariable Long id) {
        logger.info("GET /api/notificaciones/{} - buscando notificación", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar notificación", description = "Actualiza los datos de una notificación existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(
            @Parameter(description = "ID de la notificación", required = true) @PathVariable Long id,
            @Valid @RequestBody Notificacion datos) {
        logger.info("PUT /api/notificaciones/{} - actualizando notificación", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar notificación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la notificación", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/notificaciones/{} - eliminando notificación", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar notificación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
