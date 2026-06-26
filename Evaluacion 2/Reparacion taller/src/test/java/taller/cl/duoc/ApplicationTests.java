package taller.cl.duoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import taller.cl.duoc.model.Reparacion;
import taller.cl.duoc.repository.ReparacionRepository;
import taller.cl.duoc.service.ReparacionService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationTests {

    @Mock
    private ReparacionRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private ReparacionService service;

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        ReflectionTestUtils.setField(service, "msCitaUrl", "http://localhost:8080");
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    // ── finalizar (con verificación de cita) ──────────────────────────────────

    @Test
    @SuppressWarnings("unchecked")
    void testFinalizar_cuandoCitaExiste_guardaReparacion() {
        // Given
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(new Object()));
        Reparacion reparacion = new Reparacion();
        reparacion.setCitaId(1L);
        reparacion.setMecanicoResponsable("Carlos");
        reparacion.setDetalle("Cambio de aceite");
        reparacion.setCostoTotal(30000.0);
        when(repository.save(reparacion)).thenReturn(reparacion);

        // When
        Reparacion resultado = service.finalizar(reparacion);

        // Then
        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getMecanicoResponsable());
        verify(repository, times(1)).save(reparacion);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testFinalizar_cuandoCitaNoExiste_lanzaRuntimeException() {
        // Given
        when(responseSpec.bodyToMono(Object.class))
                .thenReturn(Mono.error(WebClientResponseException.create(404, "Not Found", null, null, null)));
        Reparacion reparacion = new Reparacion();
        reparacion.setCitaId(99L);

        // When & Then
        assertThrows(RuntimeException.class, () -> service.finalizar(reparacion));
        verify(repository, never()).save(any());
    }

    // ── listar ────────────────────────────────────────────────────────────────

    @Test
    void testListar_deberiaRetornarListaDeReparaciones() {
        // Given
        Reparacion r1 = new Reparacion();
        r1.setCitaId(1L);
        Reparacion r2 = new Reparacion();
        r2.setCitaId(2L);
        when(repository.findAll()).thenReturn(Arrays.asList(r1, r2));

        // When
        List<Reparacion> resultado = service.listar();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void testBuscarPorId_cuandoExiste_retornaReparacion() {
        // Given
        Reparacion reparacion = new Reparacion();
        reparacion.setId(1L);
        reparacion.setDetalle("Frenos revisados");
        when(repository.findById(1L)).thenReturn(Optional.of(reparacion));

        // When
        Optional<Reparacion> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Frenos revisados", resultado.get().getDetalle());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Reparacion> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    // ── actualizar ────────────────────────────────────────────────────────────

    @Test
    void testActualizar_cuandoExiste_retornaReparacionActualizada() {
        // Given
        Reparacion existente = new Reparacion();
        existente.setId(1L);
        existente.setCitaId(1L);
        existente.setMecanicoResponsable("Pedro");
        existente.setDetalle("Detalle viejo");
        existente.setCostoTotal(20000.0);

        Reparacion nuevosDatos = new Reparacion();
        nuevosDatos.setCitaId(1L);
        nuevosDatos.setMecanicoResponsable("Pablo");
        nuevosDatos.setDetalle("Cambio pastillas de freno");
        nuevosDatos.setCostoTotal(45000.0);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Reparacion resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals("Pablo", resultado.getMecanicoResponsable());
        assertEquals(45000.0, resultado.getCostoTotal());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Reparacion()));
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
