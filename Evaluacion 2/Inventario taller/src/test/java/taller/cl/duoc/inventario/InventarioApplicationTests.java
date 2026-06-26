package taller.cl.duoc.inventario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taller.cl.duoc.inventario.model.Inventario;
import taller.cl.duoc.inventario.repository.InventarioRepository;
import taller.cl.duoc.inventario.service.InventarioService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioApplicationTests {

    @Mock
    private InventarioRepository repository;

    @InjectMocks
    private InventarioService service;

    @Test
    void testGuardarInventario_deberiaRetornarInventarioGuardado() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(1L);
        inventario.setCantidadFisica(50);
        inventario.setStockMinimo(10);
        inventario.setPasilloUbicacion("A3");
        when(repository.save(inventario)).thenReturn(inventario);

        // When
        Inventario resultado = service.guardarInventario(inventario);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getProductoId());
        assertEquals(50, resultado.getCantidadFisica());
        verify(repository, times(1)).save(inventario);
    }

    @Test
    void testObtenerTodos_deberiaRetornarListaDeInventarios() {
        // Given
        Inventario i1 = new Inventario();
        i1.setProductoId(1L);
        Inventario i2 = new Inventario();
        i2.setProductoId(2L);
        when(repository.findAll()).thenReturn(Arrays.asList(i1, i2));

        // When
        List<Inventario> resultado = service.obtenerTodos();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_cuandoExiste_retornaInventario() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(5L);
        inventario.setCantidadFisica(30);
        when(repository.findById(1L)).thenReturn(Optional.of(inventario));

        // When
        Optional<Inventario> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(5L, resultado.get().getProductoId());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Inventario> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    void testActualizar_cuandoExiste_retornaInventarioActualizado() {
        // Given
        Inventario existente = new Inventario();
        existente.setId(1L);
        existente.setProductoId(1L);
        existente.setCantidadFisica(10);
        existente.setStockMinimo(5);
        existente.setPasilloUbicacion("A1");

        Inventario nuevosDatos = new Inventario();
        nuevosDatos.setProductoId(1L);
        nuevosDatos.setCantidadFisica(100);
        nuevosDatos.setStockMinimo(20);
        nuevosDatos.setPasilloUbicacion("B2");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Inventario resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals(100, resultado.getCantidadFisica());
        assertEquals("B2", resultado.getPasilloUbicacion());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Inventario()));
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
