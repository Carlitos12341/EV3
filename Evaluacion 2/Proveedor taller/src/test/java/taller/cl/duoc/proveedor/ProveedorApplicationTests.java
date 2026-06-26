package taller.cl.duoc.proveedor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taller.cl.duoc.proveedor.model.Proveedor;
import taller.cl.duoc.proveedor.repository.ProveedorRepository;
import taller.cl.duoc.proveedor.service.ProveedorService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorApplicationTests {

    @Mock
    private ProveedorRepository repository;

    @InjectMocks
    private ProveedorService service;

    @Test
    void testGuardarProveedor_deberiaRetornarProveedorGuardado() {
        // Given
        Proveedor proveedor = new Proveedor();
        proveedor.setRutEmpresa("76543210-K");
        proveedor.setRazonSocial("AutoPartes SA");
        proveedor.setContactoNombre("Diego López");
        proveedor.setEmail("diego@autopartes.cl");
        proveedor.setTelefono("+56912345678");
        when(repository.save(proveedor)).thenReturn(proveedor);

        // When
        Proveedor resultado = service.guardarProveedor(proveedor);

        // Then
        assertNotNull(resultado);
        assertEquals("AutoPartes SA", resultado.getRazonSocial());
        verify(repository, times(1)).save(proveedor);
    }

    @Test
    void testObtenerTodos_deberiaRetornarListaDeProveedores() {
        // Given
        Proveedor p1 = new Proveedor();
        p1.setRazonSocial("Empresa A");
        Proveedor p2 = new Proveedor();
        p2.setRazonSocial("Empresa B");
        when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // When
        List<Proveedor> resultado = service.obtenerTodos();

        // Then
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_cuandoExiste_retornaProveedor() {
        // Given
        Proveedor proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setRazonSocial("Repuestos Chile");
        when(repository.findById(1L)).thenReturn(Optional.of(proveedor));

        // When
        Optional<Proveedor> resultado = service.buscarPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Repuestos Chile", resultado.get().getRazonSocial());
    }

    @Test
    void testBuscarPorId_cuandoNoExiste_retornaVacio() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Proveedor> resultado = service.buscarPorId(99L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    void testActualizar_cuandoExiste_retornaProveedorActualizado() {
        // Given
        Proveedor existente = new Proveedor();
        existente.setId(1L);
        existente.setRazonSocial("Empresa Vieja");
        existente.setEmail("viejo@correo.cl");

        Proveedor nuevosDatos = new Proveedor();
        nuevosDatos.setRutEmpresa("12345678-9");
        nuevosDatos.setRazonSocial("Empresa Nueva");
        nuevosDatos.setContactoNombre("Nuevo Contacto");
        nuevosDatos.setEmail("nuevo@correo.cl");
        nuevosDatos.setTelefono("+56911111111");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // When
        Proveedor resultado = service.actualizar(1L, nuevosDatos);

        // Then
        assertEquals("Empresa Nueva", resultado.getRazonSocial());
        assertEquals("nuevo@correo.cl", resultado.getEmail());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testActualizar_cuandoNoExiste_lanzaRuntimeException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new Proveedor()));
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
