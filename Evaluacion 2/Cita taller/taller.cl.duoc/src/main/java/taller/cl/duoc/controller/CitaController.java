package taller.cl.duoc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import taller.cl.duoc.model.Cita;
import taller.cl.duoc.service.CitaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Citas", description = "Gestión de citas del taller mecánico")
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private static final Logger logger = LoggerFactory.getLogger(CitaController.class);

    @Autowired
    private CitaService service;

    @Operation(summary = "Agendar cita", description = "Crea una nueva cita verificando disponibilidad del mecánico")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cita agendada exitosamente",
            content = @Content(schema = @Schema(implementation = Cita.class))),
        @ApiResponse(responseCode = "400", description = "Datos de cita inválidos"),
        @ApiResponse(responseCode = "503", description = "Mecánico no disponible o servicio no alcanzable")
    })
    @PostMapping
    public ResponseEntity<Cita> agendar(@Valid @RequestBody Cita c) {
        logger.info("POST /api/citas - agendando cita");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agendar(c));
    }

    @Operation(summary = "Listar citas", description = "Retorna la lista de todas las citas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Cita>> listar() {
        logger.info("GET /api/citas - listando citas");
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar cita por ID", description = "Retorna una cita según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita encontrada",
            content = @Content(schema = @Schema(implementation = Cita.class))),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(
            @Parameter(description = "ID de la cita", required = true) @PathVariable Long id) {
        logger.info("GET /api/citas/{} - buscando cita", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar cita", description = "Actualiza los datos de una cita existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizar(
            @Parameter(description = "ID de la cita", required = true) @PathVariable Long id,
            @Valid @RequestBody Cita datos) {
        logger.info("PUT /api/citas/{} - actualizando cita", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar cita: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar cita", description = "Elimina una cita del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cita eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la cita", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/citas/{} - eliminando cita", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar cita: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
