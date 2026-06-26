package taller.cl.duoc.mecanico.controller;

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
import taller.cl.duoc.mecanico.model.Mecanico;
import taller.cl.duoc.mecanico.service.MecanicoService;

import java.util.List;

@Tag(name = "Mecánicos", description = "Gestión de mecánicos del taller")
@RestController
@RequestMapping("/api/mecanicos")
public class MecanicoController {

    private static final Logger logger = LoggerFactory.getLogger(MecanicoController.class);

    @Autowired
    private MecanicoService service;

    @Operation(summary = "Registrar mecánico", description = "Crea un nuevo mecánico en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Mecánico creado exitosamente",
            content = @Content(schema = @Schema(implementation = Mecanico.class))),
        @ApiResponse(responseCode = "400", description = "Datos de mecánico inválidos")
    })
    @PostMapping
    public ResponseEntity<Mecanico> crear(@Valid @RequestBody Mecanico mecanico) {
        logger.info("POST /api/mecanicos - registrando mecánico");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarMecanico(mecanico));
    }

    @Operation(summary = "Listar mecánicos", description = "Retorna la lista de todos los mecánicos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de mecánicos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Mecanico>> listar() {
        logger.info("GET /api/mecanicos - listando mecánicos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar mecánico por ID", description = "Retorna un mecánico según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mecánico encontrado",
            content = @Content(schema = @Schema(implementation = Mecanico.class))),
        @ApiResponse(responseCode = "404", description = "Mecánico no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Mecanico> buscarPorId(
            @Parameter(description = "ID del mecánico", required = true) @PathVariable Long id) {
        logger.info("GET /api/mecanicos/{} - buscando mecánico", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar mecánico", description = "Actualiza los datos de un mecánico existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mecánico actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Mecánico no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Mecanico> actualizar(
            @Parameter(description = "ID del mecánico", required = true) @PathVariable Long id,
            @Valid @RequestBody Mecanico datos) {
        logger.info("PUT /api/mecanicos/{} - actualizando mecánico", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar mecánico: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar mecánico", description = "Elimina un mecánico del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Mecánico eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Mecánico no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del mecánico", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/mecanicos/{} - eliminando mecánico", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar mecánico: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
