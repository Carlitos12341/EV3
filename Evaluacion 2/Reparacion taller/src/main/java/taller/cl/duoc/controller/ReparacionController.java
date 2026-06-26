package taller.cl.duoc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import taller.cl.duoc.model.Reparacion;
import taller.cl.duoc.service.ReparacionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reparaciones", description = "Gestión de reparaciones realizadas en el taller")
@RestController
@RequestMapping("/api/reparaciones")
public class ReparacionController {

    private static final Logger logger = LoggerFactory.getLogger(ReparacionController.class);

    @Autowired
    private ReparacionService service;

    @Operation(summary = "Registrar reparación", description = "Registra una nueva reparación verificando que la cita exista")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reparación registrada exitosamente",
            content = @Content(schema = @Schema(implementation = Reparacion.class))),
        @ApiResponse(responseCode = "400", description = "Datos de reparación inválidos"),
        @ApiResponse(responseCode = "503", description = "La cita no existe o el servicio ms-cita no está disponible")
    })
    @PostMapping
    public ResponseEntity<Reparacion> crear(@Valid @RequestBody Reparacion r) {
        logger.info("POST /api/reparaciones - registrando reparacion");
        return new ResponseEntity<>(service.finalizar(r), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar reparaciones", description = "Retorna la lista de todas las reparaciones registradas")
    @ApiResponse(responseCode = "200", description = "Lista de reparaciones obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Reparacion>> listar() {
        logger.info("GET /api/reparaciones - listando reparaciones");
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar reparación por ID", description = "Retorna una reparación según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reparación encontrada",
            content = @Content(schema = @Schema(implementation = Reparacion.class))),
        @ApiResponse(responseCode = "404", description = "Reparación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reparacion> buscarPorId(
            @Parameter(description = "ID de la reparación", required = true) @PathVariable Long id) {
        logger.info("GET /api/reparaciones/{} - buscando reparacion", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar reparación", description = "Actualiza los datos de una reparación existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reparación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reparación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reparacion> actualizar(
            @Parameter(description = "ID de la reparación", required = true) @PathVariable Long id,
            @Valid @RequestBody Reparacion datos) {
        logger.info("PUT /api/reparaciones/{} - actualizando reparacion", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar reparacion: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar reparación", description = "Elimina una reparación del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reparación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reparación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la reparación", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/reparaciones/{} - eliminando reparacion", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar reparacion: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
