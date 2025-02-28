package com.bank.ms_clientes.service;

import com.bank.ms_clientes.exception.ClienteConflictException;
import com.bank.ms_clientes.exception.ClienteNotFoundException;
import com.bank.ms_clientes.model.Cliente;
import com.bank.ms_clientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        cliente = new Cliente();
        cliente.setClienteId(clienteId);
        cliente.setIdentificacion("123456789");
        cliente.setContraseña("password");
    }

    @Test
    void crearCliente_DebeCrearCliente_CuandoNoExisteIdentificacion() {
        when(repository.existsByIdentificacion(cliente.getIdentificacion())).thenReturn(false);
        when(passwordEncoder.encode(cliente.getContraseña())).thenReturn("hashedPassword");
        when(repository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.crearCliente(cliente);

        assertNotNull(resultado);
        assertEquals("hashedPassword", resultado.getContraseña());
        verify(repository).save(cliente);
    }

    @Test
    void crearCliente_DebeLanzarException_CuandoIdentificacionExiste() {
        when(repository.existsByIdentificacion(cliente.getIdentificacion())).thenReturn(true);

        assertThrows(ClienteConflictException.class, () -> clienteService.crearCliente(cliente));
        verify(repository, never()).save(any());
    }

    @Test
    void obtenerClientePorId_DebeRetornarCliente_CuandoExiste() {
        when(repository.existsById(clienteId)).thenReturn(true);
        when(repository.findById(clienteId)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.obtenerClientePorId(clienteId);

        assertNotNull(resultado);
        assertEquals(clienteId, resultado.getClienteId());
    }

    @Test
    void obtenerClientePorId_DebeLanzarException_CuandoNoExiste() {
        when(repository.existsById(clienteId)).thenReturn(false);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.obtenerClientePorId(clienteId));
    }

    @Test
    void actualizarClienteParcial_DebeActualizarCampos_CuandoClienteExiste() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("identificacion", "987654321");

        when(repository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(repository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.actualizarClienteParcial(clienteId, updates);

        assertEquals("987654321", resultado.getIdentificacion());
        verify(repository).save(cliente);
    }

    @Test
    void actualizarClienteParcial_DebeLanzarException_CuandoClienteNoExiste() {
        when(repository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ClienteNotFoundException.class, () -> clienteService.actualizarClienteParcial(clienteId, new HashMap<>()));
    }

    @Test
    void eliminarCliente_DebeEliminarCliente_CuandoExiste() {
        when(repository.existsById(clienteId)).thenReturn(true);
        doNothing().when(repository).deleteById(clienteId);

        boolean resultado = clienteService.eliminarCliente(clienteId);

        assertTrue(resultado);
        verify(repository).deleteById(clienteId);
    }

    @Test
    void eliminarCliente_DebeLanzarException_CuandoNoExiste() {
        when(repository.existsById(clienteId)).thenReturn(false);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.eliminarCliente(clienteId));
    }

    @Test
    void obtenerTodosLosClientes_DebeRetornarPaginaDeClientes() {
        Page<Cliente> page = new PageImpl<>(Collections.singletonList(cliente));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Cliente> resultado = clienteService.obtenerTodosLosClientes(0, 10);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
    }
}
