package taller.cl.duoc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taller.cl.duoc.model.Vehiculo;
import taller.cl.duoc.repository.VehiculoRepository;
import taller.cl.duoc.service.VehiculoService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationTests {

    @Mock
    private VehiculoRepository repository;

    @InjectMocks
    private VehiculoService service;

    // ── registrar ─────────────────────────────────────────────────────────────

    @Test
    void testRegistrar_deberiaRetornarVehiculoGuardado() {
        // Given
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente("AB1234");
        vehiculo.setMarca("Toyota");
        vehiculo.setClienteId(1L);
        when(repository.save(vehiculo)).thenReturn(vehiculo);

        // When
        Vehiculo resultado = service.registrar(vehiculo);

        // Then
        assertNotNull(resultado);
        assertEquals("AB1234", resultado.getPatente());
        assertEquals("Toyota", resultado.getMarca());
        verify(repository, times(1)).save(vehiculo);
    }

    // ── listar ────────────────────────────────────────────────────────────────

    @Test
    void testListar_deberiaRetornarListaDeVehiculos() {
        // Given
        Vehiculo v1 = new Vehiculo();
        v1.setPatente("AB1234");
        Vehiculo v2 = new Vehiculo();
        v2.setPatente("CD5678");
        when(repository.findAll()).thenReturn(Arrays.asList(v1, v2));

        // When
        List<Vehiculo> resultado = service.listar();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void testBuscarPorId_cuandoExiste_retornaVehiculo() {
        // Given
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setPatente("AB1234");
        when(repository.findById(1L)).thenReturn(Optional.of(vehiculo));

        // When
        Optional<Vehiculo> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("AB1234", resultado.get().getPatente());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Vehiculo> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    // ── actualizar ────────────────────────────────────────────────────────────

    @Test
    void testActualizar_cuandoExiste_retornaVehiculoActualizado() {
        // Given
        Vehiculo existente = new Vehiculo();
        existente.setId(1L);
        existente.setPatente("AB1234");
        existente.setMarca("Toyota");
        existente.setClienteId(1L);

        Vehiculo nuevosDatos = new Vehiculo();
        nuevosDatos.setPatente("AB1234");
        nuevosDatos.setMarca("Honda");
        nuevosDatos.setClienteId(2L);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Vehiculo resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals("Honda", resultado.getMarca());
        assertEquals(2L, resultado.getClienteId());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Vehiculo()));
        verify(repository, never()).save(any());
    }

    // ── eliminar ──────────────────────────────────────────────────────────────

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
