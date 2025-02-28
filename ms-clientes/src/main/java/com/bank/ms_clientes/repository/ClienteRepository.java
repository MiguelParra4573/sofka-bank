package com.bank.ms_clientes.repository;

import com.bank.ms_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    boolean existsByIdentificacion(String identificacion);

}