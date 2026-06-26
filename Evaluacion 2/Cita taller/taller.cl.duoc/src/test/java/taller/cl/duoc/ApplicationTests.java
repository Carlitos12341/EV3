package taller.cl.duoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import taller.cl.duoc.model.Cita;
import taller.cl.duoc.repository.CitaRepository;
import taller.cl.duoc.service.CitaService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationTests {

    @Mock
    private CitaRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private CitaService service;

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
        ReflectionTestUtils.setField(service, "msMecanicoUrl", "http://localhost:8084");
        ReflectionTestUtils.setField(service, "msNotificacionUrl", "http://localhost:8085");
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    // ── agendar ───────────────────────────────────────────────────────────────

    @Test
    @SuppressWarnings("unchecked")
    void testAgendar_cuandoMecanicoDisponible_guardaYRetornaCita() {
        // Given
        Map<String, Object> mecanicoMap = new HashMap<>();
        mecanicoMap.put("disponible", true);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mecanicoMap));

        Cita cita = new Cita();
        cita.setMecanicoId(1L);
        cita.setFecha("2025-07-01");
        cita.setMotivo("Cambio de aceite");
        when(repository.save(cita)).thenReturn(cita);

        // When
        Cita resultado = service.agendar(cita);

        // Then
        assertNotNull(resultado);
        assertEquals("2025-07-01", resultado.getFecha());
        verify(repository, times(1)).save(cita);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAgendar_cuandoMecanicoNoDisponible_lanzaRuntimeException() {
        // Given
        Map<String, Object> mecanicoMap = new HashMap<>();
        mecanicoMap.put("disponible", false);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mecanicoMap));

        Cita cita = new Cita();
        cita.setMecanicoId(99L);

        // When & Then
        assertThrows(RuntimeException.class, () -> service.agendar(cita));
        verify(repository, never()).save(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAgendar_cuandoServicioMecanicoNoResponde_lanzaRuntimeException() {
        // Given
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new RuntimeException("Connection refused")));

        Cita cita = new Cita();
        cita.setMecanicoId(1L);

        // When & Then
        assertThrows(RuntimeException.class, () -> service.agendar(cita));
        verify(repository, never()).save(any());
    }

    // ── listar ────────────────────────────────────────────────────────────────

    @Test
    void testListar_deberiaRetornarListaDeCitas() {
        // Given
        Cita c1 = new Cita();
        c1.setMotivo("Revisión");
        Cita c2 = new Cita();
        c2.setMotivo("Frenos");
        when(repository.findAll()).thenReturn(Arrays.asList(c1, c2));

        // When
        List<Cita> resultado = service.listar();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void testBuscarPorId_cuandoExiste_retornaCita() {
        // Given
        Cita cita = new Cita();
        cita.setId(1L);
        cita.setMotivo("Revisión general");
        when(repository.findById(1L)).thenReturn(Optional.of(cita));

        // When
        Optional<Cita> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Revisión general", resultado.get().getMotivo());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Cita> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    // ── actualizar ────────────────────────────────────────────────────────────

    @Test
    void testActualizar_cuandoExiste_retornaCitaActualizada() {
        // Given
        Cita existente = new Cita();
        existente.setId(1L);
        existente.setFecha("2025-06-01");
        existente.setMotivo("Motivo viejo");

        Cita nuevosDatos = new Cita();
        nuevosDatos.setFecha("2025-07-15");
        nuevosDatos.setMotivo("Frenos delanteros");
        nuevosDatos.setVehiculoId(1L);
        nuevosDatos.setClienteId(1L);
        nuevosDatos.setMecanicoId(2L);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Cita resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals("2025-07-15", resultado.getFecha());
        assertEquals("Frenos delanteros", resultado.getMotivo());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Cita()));
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
