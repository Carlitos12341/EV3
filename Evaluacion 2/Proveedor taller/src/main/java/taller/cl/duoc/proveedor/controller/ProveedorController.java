package taller.cl.duoc.proveedor.controller;

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
import taller.cl.duoc.proveedor.model.Proveedor;
import taller.cl.duoc.proveedor.service.ProveedorService;

import java.util.List;

@Tag(name = "Proveedores", description = "Gestión de proveedores de repuestos del taller")
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorController.class);

    @Autowired
    private ProveedorService service;

    @Operation(summary = "Registrar proveedor", description = "Crea un nuevo proveedor en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente",
            content = @Content(schema = @Schema(implementation = Proveedor.class))),
        @ApiResponse(responseCode = "400", description = "Datos de proveedor inválidos")
    })
    @PostMapping
    public ResponseEntity<Proveedor> crear(@Valid @RequestBody Proveedor proveedor) {
        logger.info("POST /api/proveedores - registrando proveedor");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarProveedor(proveedor));
    }

    @Operation(summary = "Listar proveedores", description = "Retorna la lista de todos los proveedores registrados")
    @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Proveedor>> listar() {
        logger.info("GET /api/proveedores - listando proveedores");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar proveedor por ID", description = "Retorna un proveedor según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Proveedor encontrado",
            content = @Content(schema = @Schema(implementation = Proveedor.class))),
        @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(
            @Parameter(description = "ID del proveedor", required = true) @PathVariable Long id) {
        logger.info("GET /api/proveedores/{} - buscando proveedor", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar proveedor", description = "Actualiza los datos de un proveedor existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Proveedor actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(
            @Parameter(description = "ID del proveedor", required = true) @PathVariable Long id,
            @Valid @RequestBody Proveedor datos) {
        logger.info("PUT /api/proveedores/{} - actualizando proveedor", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar proveedor: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Proveedor eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del proveedor", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/proveedores/{} - eliminando proveedor", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar proveedor: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
