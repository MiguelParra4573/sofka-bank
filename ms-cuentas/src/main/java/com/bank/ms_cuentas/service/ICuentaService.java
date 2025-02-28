package com.bank.ms_cuentas.service;

import com.bank.ms_cuentas.model.Cuenta;

import java.util.UUID;

public interface ICuentaService {

    Cuenta crearCuenta(Cuenta cuenta);

    Cuenta obtenerCuentaPorId(UUID id);

    boolean eliminarCuenta(UUID id);

}
