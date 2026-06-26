package taller.cl.duoc.facturacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taller.cl.duoc.facturacion.model.Factura;
import taller.cl.duoc.facturacion.repository.FacturaRepository;
import taller.cl.duoc.facturacion.service.FacturaService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaApplicationTests {

    @Mock
    private FacturaRepository repository;

    @InjectMocks
    private FacturaService service;

    @Test
    void testGuardarFactura_deberiaRetornarFacturaGuardada() {
        // Given
        Factura factura = new Factura();
        factura.setReparacionId(1L);
        factura.setClienteId(2L);
        factura.setMontoTotal(150000.0);
        factura.setEstadoPago("PENDIENTE");
        when(repository.save(factura)).thenReturn(factura);

        // When
        Factura resultado = service.guardarFactura(factura);

        // Then
        assertNotNull(resultado);
        assertEquals(150000.0, resultado.getMontoTotal());
        assertEquals("PENDIENTE", resultado.getEstadoPago());
        verify(repository, times(1)).save(factura);
    }

    @Test
    void testObtenerTodas_deberiaRetornarListaDeFacturas() {
        // Given
        Factura f1 = new Factura();
        f1.setReparacionId(1L);
        Factura f2 = new Factura();
        f2.setReparacionId(2L);
        when(repository.findAll()).thenReturn(Arrays.asList(f1, f2));

        // When
        List<Factura> resultado = service.obtenerTodas();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_cuandoExiste_retornaFactura() {
        // Given
        Factura factura = new Factura();
        factura.setId(1L);
        factura.setMontoTotal(80000.0);
        when(repository.findById(1L)).thenReturn(Optional.of(factura));

        // When
        Optional<Factura> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(80000.0, resultado.get().getMontoTotal());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Factura> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    void testActualizar_cuandoExiste_retornaFacturaActualizada() {
        // Given
        Factura existente = new Factura();
        existente.setId(1L);
        existente.setMontoTotal(100000.0);
        existente.setEstadoPago("PENDIENTE");

        Factura nuevosDatos = new Factura();
        nuevosDatos.setReparacionId(1L);
        nuevosDatos.setClienteId(1L);
        nuevosDatos.setMontoTotal(120000.0);
        nuevosDatos.setEstadoPago("PAGADO");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Factura resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals(120000.0, resultado.getMontoTotal());
        assertEquals("PAGADO", resultado.getEstadoPago());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Factura()));
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
