package taller.cl.duoc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taller.cl.duoc.model.Producto;
import taller.cl.duoc.repository.ProductoRepository;
import taller.cl.duoc.service.ProductoService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationTests {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @Test
    void testGuardar_deberiaRetornarProductoGuardado() {
        // Given
        Producto producto = new Producto();
        producto.setNombre("Filtro de Aceite");
        producto.setStock(10);
        producto.setPrecioUnitario(5000.0);
        when(repository.save(producto)).thenReturn(producto);

        // When
        Producto resultado = service.guardar(producto);

        // Then
        assertNotNull(resultado);
        assertEquals("Filtro de Aceite", resultado.getNombre());
        verify(repository, times(1)).save(producto);
    }

    @Test
    void testListar_deberiaRetornarListaDeProductos() {
        // Given
        Producto p1 = new Producto();
        p1.setNombre("Bujía");
        Producto p2 = new Producto();
        p2.setNombre("Correa");
        when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // When
        List<Producto> resultado = service.listar();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_cuandoExiste_retornaProducto() {
        // Given
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aceite Motor");
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        Optional<Producto> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Aceite Motor", resultado.get().getNombre());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Producto> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    void testActualizar_cuandoExiste_retornaProductoActualizado() {
        // Given
        Producto existente = new Producto();
        existente.setId(1L);
        existente.setNombre("Bujía");
        existente.setStock(5);
        existente.setPrecioUnitario(2000.0);

        Producto nuevosDatos = new Producto();
        nuevosDatos.setNombre("Bujía Platinum");
        nuevosDatos.setStock(20);
        nuevosDatos.setPrecioUnitario(3500.0);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Producto resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals("Bujía Platinum", resultado.getNombre());
        assertEquals(20, resultado.getStock());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Producto()));
        verify(repository, never()).save(any());
    }

    @Test
    void testEliminar_cuandoExiste_eliminaCorrectamente() {
        // Given
        when(repository.existsById(1L)).thenReturn(true);

        // When
        service.eliminar(1L);

        // Then
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}
