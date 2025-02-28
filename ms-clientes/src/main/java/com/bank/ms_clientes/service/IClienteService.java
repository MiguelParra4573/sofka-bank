package com.bank.ms_clientes.service;

import com.bank.ms_clientes.model.Cliente;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IClienteService {

    Cliente crearCliente(Cliente cliente);

    Cliente obtenerClientePorId(UUID id);

    boolean eliminarCliente(UUID id);

    Page<Cliente> obtenerTodosLosClientes(int page, int size);
}
