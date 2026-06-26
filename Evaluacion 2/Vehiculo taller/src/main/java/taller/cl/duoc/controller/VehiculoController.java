package taller.cl.duoc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import taller.cl.duoc.model.Vehiculo;
import taller.cl.duoc.service.VehiculoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vehículos", description = "Gestión de vehículos registrados en el taller")
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoController.class);

    @Autowired
    private VehiculoService service;

    @Operation(summary = "Registrar vehículo", description = "Registra un nuevo vehículo asociado a un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vehículo registrado exitosamente",
            content = @Content(schema = @Schema(implementation = Vehiculo.class))),
        @ApiResponse(responseCode = "400", description = "Datos de vehículo inválidos (patente, marca o clienteId)")
    })
    @PostMapping
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody Vehiculo vehiculo) {
        logger.info("POST /api/vehiculos - creando vehiculo");
        return new ResponseEntity<>(service.registrar(vehiculo), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar vehículos", description = "Retorna la lista de todos los vehículos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Vehiculo>> listarTodos() {
        logger.info("GET /api/vehiculos - listando vehiculos");
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar vehículo por ID", description = "Retorna un vehículo según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehículo encontrado",
            content = @Content(schema = @Schema(implementation = Vehiculo.class))),
        @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> buscarPorId(
            @Parameter(description = "ID del vehículo", required = true) @PathVariable Long id) {
        logger.info("GET /api/vehiculos/{} - buscando vehiculo", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar vehículo", description = "Actualiza los datos de un vehículo existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehículo actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(
            @Parameter(description = "ID del vehículo", required = true) @PathVariable Long id,
            @Valid @RequestBody Vehiculo detalles) {
        logger.info("PUT /api/vehiculos/{} - actualizando vehiculo", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, detalles));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar vehiculo: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar vehículo", description = "Elimina un vehículo del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vehículo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del vehículo", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/vehiculos/{} - eliminando vehiculo", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar vehiculo: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
