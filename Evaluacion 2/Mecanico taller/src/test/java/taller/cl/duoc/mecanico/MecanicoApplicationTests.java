package taller.cl.duoc.mecanico;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taller.cl.duoc.mecanico.model.Mecanico;
import taller.cl.duoc.mecanico.repository.MecanicoRepository;
import taller.cl.duoc.mecanico.service.MecanicoService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MecanicoApplicationTests {

    @Mock
    private MecanicoRepository repository;

    @InjectMocks
    private MecanicoService service;

    // ── guardarMecanico ───────────────────────────────────────────────────────

    @Test
    void testGuardarMecanico_deberiaRetornarMecanicoGuardado() {
        // Given
        Mecanico mecanico = new Mecanico();
        mecanico.setNombreCompleto("Carlos Soto");
        mecanico.setDisponible(true);
        when(repository.save(mecanico)).thenReturn(mecanico);

        // When
        Mecanico resultado = service.guardarMecanico(mecanico);

        // Then
        assertNotNull(resultado);
        assertEquals("Carlos Soto", resultado.getNombreCompleto());
        assertTrue(resultado.getDisponible());
        verify(repository, times(1)).save(mecanico);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────────────

    @Test
    void testObtenerTodos_deberiaRetornarListaDeMecanicos() {
        // Given
        Mecanico m1 = new Mecanico();
        m1.setNombreCompleto("Pedro");
        Mecanico m2 = new Mecanico();
        m2.setNombreCompleto("María");
        when(repository.findAll()).thenReturn(Arrays.asList(m1, m2));

        // When
        List<Mecanico> resultado = service.obtenerTodos();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void testBuscarPorId_cuandoExiste_retornaMecanico() {
        // Given
        Mecanico mecanico = new Mecanico();
        mecanico.setId(1L);
        mecanico.setNombreCompleto("Ana Ruiz");
        mecanico.setDisponible(true);
        when(repository.findById(1L)).thenReturn(Optional.of(mecanico));

        // When
        Optional<Mecanico> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Ana Ruiz", resultado.get().getNombreCompleto());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Mecanico> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    // ── actualizar ────────────────────────────────────────────────────────────

    @Test
    void testActualizar_cuandoExiste_retornaMecanicoActualizado() {
        // Given
        Mecanico existente = new Mecanico();
        existente.setId(1L);
        existente.setNombreCompleto("Luis");
        existente.setDisponible(false);

        Mecanico nuevosDatos = new Mecanico();
        nuevosDatos.setNombreCompleto("Luis Actualizado");
        nuevosDatos.setDisponible(true);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Mecanico resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals("Luis Actualizado", resultado.getNombreCompleto());
        assertTrue(resultado.getDisponible());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Mecanico()));
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
