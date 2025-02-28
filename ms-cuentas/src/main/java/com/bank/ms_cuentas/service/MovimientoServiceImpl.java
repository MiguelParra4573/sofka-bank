package com.bank.ms_cuentas.service;

import com.bank.ms_cuentas.exception.CuentaNotFoundException;
import com.bank.ms_cuentas.exception.MovimientoConflictException;
import com.bank.ms_cuentas.exception.MovimientoNotFoundException;
import com.bank.ms_cuentas.exception.SaldoInsuficienteException;
import com.bank.ms_cuentas.model.Cuenta;
import com.bank.ms_cuentas.model.Movimiento;
import com.bank.ms_cuentas.repository.CuentaRepository;
import com.bank.ms_cuentas.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovimientoServiceImpl implements IMovimientoService {

    @Autowired
    private MovimientoRepository repository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public Movimiento crearMovimiento(Movimiento movimiento) {
        Cuenta cuenta = cuentaRepository.findById(movimiento.getCuenta().getId())
                .orElseThrow(() -> new CuentaNotFoundException());

        BigDecimal saldoActual = cuenta.getSaldoInicial();
        BigDecimal nuevoSaldo = saldoActual.add(movimiento.getValor());

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible.");
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);
        movimiento.setCuenta(cuenta);
        movimiento.setSaldo(nuevoSaldo);
        return repository.save(movimiento);
    }

    @Override
    public Page<Movimiento> obtenerMovimientosPorFechaCliente(UUID clienteId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return repository.obtenerMovimientosPorFechaCliente(clienteId, startDate, endDate, pageable);
    }

}