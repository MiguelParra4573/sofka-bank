package com.bank.ms_cuentas.service;

import com.bank.ms_cuentas.model.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface IMovimientoService {

    Movimiento crearMovimiento(Movimiento movimiento);

    Page<Movimiento> obtenerMovimientosPorFechaCliente(UUID clienteId, LocalDate startDate, LocalDate endDate, Pageable pageable );

}
