package taller.cl.duoc.notificacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taller.cl.duoc.notificacion.model.Notificacion;
import taller.cl.duoc.notificacion.repository.NotificacionRepository;
import taller.cl.duoc.notificacion.service.NotificacionService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionApplicationTests {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionService service;

    @Test
    void testEnviarYGuardar_deberiaSetearEstadoEnviadoYGuardar() {
        // Given
        Notificacion notificacion = new Notificacion();
        notificacion.setClienteId(1L);
        notificacion.setTipo("EMAIL");
        notificacion.setMensaje("Su vehículo está listo");
        notificacion.setFechaEnvio("2025-06-25");
        when(repository.save(any(Notificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Notificacion resultado = service.enviarYGuardar(notificacion);

        // Then
        assertNotNull(resultado);
        assertEquals("ENVIADO", resultado.getEstado());
        verify(repository, times(1)).save(notificacion);
    }

    @Test
    void testObtenerTodas_deberiaRetornarListaDeNotificaciones() {
        // Given
        Notificacion n1 = new Notificacion();
        n1.setClienteId(1L);
        Notificacion n2 = new Notificacion();
        n2.setClienteId(2L);
        when(repository.findAll()).thenReturn(Arrays.asList(n1, n2));

        // When
        List<Notificacion> resultado = service.obtenerTodas();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_cuandoExiste_retornaNotificacion() {
        // Given
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setTipo("SMS");
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));

        // When
        Optional<Notificacion> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("SMS", resultado.get().getTipo());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Notificacion> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    void testActualizar_cuandoExiste_retornaNotificacionActualizada() {
        // Given
        Notificacion existente = new Notificacion();
        existente.setId(1L);
        existente.setTipo("EMAIL");
        existente.setMensaje("Mensaje viejo");

        Notificacion nuevosDatos = new Notificacion();
        nuevosDatos.setClienteId(1L);
        nuevosDatos.setTipo("SMS");
        nuevosDatos.setMensaje("Mensaje nuevo");
        nuevosDatos.setFechaEnvio("2025-06-25");
        nuevosDatos.setEstado("ENVIADO");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Notificacion resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals("SMS", resultado.getTipo());
        assertEquals("Mensaje nuevo", resultado.getMensaje());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Notificacion()));
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
