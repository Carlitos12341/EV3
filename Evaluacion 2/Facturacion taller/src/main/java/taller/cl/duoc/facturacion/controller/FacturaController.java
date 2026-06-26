package taller.cl.duoc.facturacion.controller;

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
import taller.cl.duoc.facturacion.model.Factura;
import taller.cl.duoc.facturacion.service.FacturaService;

import java.util.List;

@Tag(name = "Facturas", description = "Gestión de facturación del taller")
@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    @Autowired
    private FacturaService service;

    @Operation(summary = "Crear factura", description = "Registra una nueva factura asociada a una reparación")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Factura creada exitosamente",
            content = @Content(schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "400", description = "Datos de factura inválidos")
    })
    @PostMapping
    public ResponseEntity<Factura> crear(@Valid @RequestBody Factura factura) {
        logger.info("POST /api/facturas - creando factura");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarFactura(factura));
    }

    @Operation(summary = "Listar facturas", description = "Retorna la lista de todas las facturas")
    @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Factura>> listar() {
        logger.info("GET /api/facturas - listando facturas");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @Operation(summary = "Buscar factura por ID", description = "Retorna una factura según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Factura encontrada",
            content = @Content(schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Factura> buscarPorId(
            @Parameter(description = "ID de la factura", required = true) @PathVariable Long id) {
        logger.info("GET /api/facturas/{} - buscando factura", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar factura", description = "Actualiza los datos de una factura existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Factura actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Factura> actualizar(
            @Parameter(description = "ID de la factura", required = true) @PathVariable Long id,
            @Valid @RequestBody Factura datos) {
        logger.info("PUT /api/facturas/{} - actualizando factura", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar factura: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar factura", description = "Elimina una factura del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Factura eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la factura", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/facturas/{} - eliminando factura", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar factura: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
