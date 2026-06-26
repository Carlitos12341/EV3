package taller.cl.duoc.inventario.controller;

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
import taller.cl.duoc.inventario.model.Inventario;
import taller.cl.duoc.inventario.service.InventarioService;

import java.util.List;

@Tag(name = "Inventario", description = "Gestión del inventario de repuestos del taller")
@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    private static final Logger logger = LoggerFactory.getLogger(InventarioController.class);

    @Autowired
    private InventarioService service;

    @Operation(summary = "Registrar inventario", description = "Crea un nuevo registro de inventario para un producto")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Inventario registrado exitosamente",
            content = @Content(schema = @Schema(implementation = Inventario.class))),
        @ApiResponse(responseCode = "400", description = "Datos de inventario inválidos")
    })
    @PostMapping
    public ResponseEntity<Inventario> crear(@Valid @RequestBody Inventario inventario) {
        logger.info("POST /api/inventarios - registrando inventario");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarInventario(inventario));
    }

    @Operation(summary = "Listar inventarios", description = "Retorna todos los registros de inventario")
    @ApiResponse(responseCode = "200", description = "Lista de inventarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Inventario>> listar() {
        logger.info("GET /api/inventarios - listando inventarios");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar inventario por ID", description = "Retorna un registro de inventario según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario encontrado",
            content = @Content(schema = @Schema(implementation = Inventario.class))),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id) {
        logger.info("GET /api/inventarios/{} - buscando inventario", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar inventario", description = "Actualiza un registro de inventario existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id,
            @Valid @RequestBody Inventario datos) {
        logger.info("PUT /api/inventarios/{} - actualizando inventario", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar inventario: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar inventario", description = "Elimina un registro de inventario por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Inventario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/inventarios/{} - eliminando inventario", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar inventario: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
