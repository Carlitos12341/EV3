package taller.cl.duoc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taller.cl.duoc.model.Cliente;
import taller.cl.duoc.repository.ClienteRepository;
import taller.cl.duoc.service.ClienteService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationTests {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    // ── guardar ───────────────────────────────────────────────────────────────

    @Test
    void testGuardar_deberiaRetornarClienteGuardado() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Pérez");
        cliente.setRut("12345678-9");
        cliente.setEmail("juan@correo.cl");
        when(repository.save(cliente)).thenReturn(cliente);

        // When
        Cliente resultado = service.guardar(cliente);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(repository, times(1)).save(cliente);
    }

    // ── listar ────────────────────────────────────────────────────────────────

    @Test
    void testListar_deberiaRetornarListaDeClientes() {
        // Given
        Cliente c1 = new Cliente();
        c1.setNombre("Juan");
        Cliente c2 = new Cliente();
        c2.setNombre("María");
        when(repository.findAll()).thenReturn(Arrays.asList(c1, c2));

        // When
        List<Cliente> resultado = service.listar();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void testBuscarPorId_cuandoExiste_retornaOptionalConCliente() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Ana");
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        // When
        Optional<Cliente> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Ana", resultado.get().getNombre());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaOptionalVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Cliente> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    // ── actualizar ────────────────────────────────────────────────────────────

    @Test
    void testActualizar_cuandoExiste_retornaClienteActualizado() {
        // Given
        Cliente existente = new Cliente();
        existente.setId(1L);
        existente.setNombre("Juan");
        existente.setRut("11111111-1");
        existente.setEmail("viejo@correo.cl");

        Cliente nuevosDatos = new Cliente();
        nuevosDatos.setNombre("Juan Actualizado");
        nuevosDatos.setRut("11111111-1");
        nuevosDatos.setEmail("nuevo@correo.cl");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Cliente resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());
        assertEquals("nuevo@correo.cl", resultado.getEmail());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Cliente()));
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
