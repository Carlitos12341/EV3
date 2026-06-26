package taller.cl.duoc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import taller.cl.duoc.model.Producto;
import taller.cl.duoc.service.ProductoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Productos", description = "Gestión de repuestos y productos del taller")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService service;

    @Operation(summary = "Registrar producto", description = "Crea un nuevo repuesto o producto en el catálogo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto registrado exitosamente",
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de producto inválidos")
    })
    @PostMapping
    public ResponseEntity<Producto> guardar(@Valid @RequestBody Producto p) {
        logger.info("POST /api/productos - registrando producto");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(p));
    }

    @Operation(summary = "Listar productos", description = "Retorna todos los productos del catálogo")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        logger.info("GET /api/productos - listando productos");
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar producto por ID", description = "Retorna un producto según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(
            @Parameter(description = "ID del producto", required = true) @PathVariable Long id) {
        logger.info("GET /api/productos/{} - buscando producto", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @Parameter(description = "ID del producto", required = true) @PathVariable Long id,
            @Valid @RequestBody Producto datos) {
        logger.info("PUT /api/productos/{} - actualizando producto", id);
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar producto: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto", required = true) @PathVariable Long id) {
        logger.info("DELETE /api/productos/{} - eliminando producto", id);
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
