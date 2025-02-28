package com.bank.ms_cuentas.repository;

import com.bank.ms_cuentas.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {

    boolean existsByNumeroCuenta(String numeroCuenta);

}
