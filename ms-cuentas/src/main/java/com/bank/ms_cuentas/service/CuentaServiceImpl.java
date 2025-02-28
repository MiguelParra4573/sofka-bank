package com.bank.ms_cuentas.service;


import com.bank.ms_cuentas.exception.CuentaConflictException;

import com.bank.ms_cuentas.exception.CuentaNotFoundException;
import com.bank.ms_cuentas.model.Cuenta;
import com.bank.ms_cuentas.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CuentaServiceImpl implements ICuentaService {

    @Autowired
    private CuentaRepository repository;

    @Override
    public Cuenta crearCuenta(Cuenta cuenta) {
        if (repository.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
            throw new CuentaConflictException("La cuenta ya existe.");
        }
        return repository.save(cuenta);
    }

    @Override
    public Cuenta obtenerCuentaPorId(UUID id) {
        Optional<Cuenta> cuenta = repository.findById(id);
        if (cuenta.isEmpty()) {
            throw new CuentaNotFoundException();
        }
        return cuenta.get();
    }

    public Cuenta actualizarCuentaParcial(UUID id, Map<String, Object> updates) {
        Cuenta cuenta = repository.findById(id).orElseThrow(CuentaNotFoundException::new);

        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Cuenta.class, campo);
            if (field != null) {
                field.setAccessible(true);
                // Validar si el campo es de tipo BigDecimal y el valor es Double o Integer
                if (field.getType().equals(BigDecimal.class) && valor != null) {
                    if (valor instanceof Double || valor instanceof Integer) {
                        valor = new BigDecimal(valor.toString());
                    }
                }
                ReflectionUtils.setField(field, cuenta, valor);
            }
        });

        return repository.save(cuenta);
    }

    @Override
    public boolean eliminarCuenta(UUID id) {
        if (!repository.existsById(id)) {
            throw new CuentaNotFoundException();
        }
        repository.deleteById(id);
        return true;
    }

}
