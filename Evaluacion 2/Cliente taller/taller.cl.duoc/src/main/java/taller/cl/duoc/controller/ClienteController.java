package taller.cl.duoc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import taller.cl.duoc.model.Cliente;
import taller.cl.duoc.service.ClienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clientes", description = "Gestión de clientes del taller mecánico")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteService service;

    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
            content = @Content(schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "400", description = "Datos de cliente inválidos")
    })
    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody Cliente c) {
        logger.info("POST /api/clientes - creando cliente");
        return new ResponseEntity<>(service.guardar(c), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar clientes", description = "Retorna la lista de todos los clientes registrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        logger.info("GET /api/clientes - listando clientes");
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna un cliente según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
            content = @Content(schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(
            @Parameter(description = "ID del cliente", required = true) @PathVariable Long id) {
        logger.info("GET /api/clientes/{} - buscando cliente", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(
            @Parameter(description = "ID del cliente", required = true) @PathVariable Long id,
            @Valid @RequestBody Cliente datos) {
        logger.info("PUT /api/clientes/{} - actualizando cliente", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar cliente: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del cliente", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/clientes/{} - eliminando cliente", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar cliente: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
