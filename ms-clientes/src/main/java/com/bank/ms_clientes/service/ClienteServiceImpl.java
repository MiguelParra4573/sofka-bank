package com.bank.ms_clientes.service;

import com.bank.ms_clientes.exception.ClienteConflictException;
import com.bank.ms_clientes.exception.ClienteNotFoundException;
import com.bank.ms_clientes.model.Cliente;
import com.bank.ms_clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Cliente crearCliente(Cliente cliente) {
        if (repository.existsByIdentificacion(cliente.getIdentificacion())) {
            throw new ClienteConflictException("El cliente ya existe.");
        }
        cliente.setContraseña(passwordEncoder.encode(cliente.getContraseña()));
        return repository.save(cliente);
    }

    @Override
    public Cliente obtenerClientePorId(UUID id) {
        if (!repository.existsById(id)) {
            throw new ClienteNotFoundException();
        }
        Optional<Cliente> cliente = repository.findById(id);
        if (cliente.isEmpty()) {
            throw new ClienteNotFoundException();
        }
        return cliente.get();
    }

    public Cliente actualizarClienteParcial(UUID id, Map<String, Object> updates) {
        Cliente cliente = repository.findById(id).orElseThrow(ClienteNotFoundException::new);

        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Cliente.class, campo);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, cliente, valor);
            }
        });

        return repository.save(cliente);
    }


    @Override
    public boolean eliminarCliente(UUID id) {
        if (!repository.existsById(id)) {
            throw new ClienteNotFoundException();
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public Page<Cliente> obtenerTodosLosClientes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

}