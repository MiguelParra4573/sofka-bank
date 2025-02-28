package com.bank.ms_cuentas.dto;

import com.bank.ms_cuentas.model.Movimiento;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MovimientoDTO {

    private UUID idCliente;

    private UUID idCuenta;

    private UUID idMovimiento;

    private LocalDate fecha;

    private String tipoMovimiento;

    private BigDecimal valor;

    private BigDecimal saldo;

    public MovimientoDTO(Movimiento movimiento) {
        this.idCliente = movimiento.getCuenta().getClienteId();
        this.idCuenta = movimiento.getCuenta().getId();
        this.idMovimiento = movimiento.getId();
        this.fecha = movimiento.getFecha();
        this.tipoMovimiento = movimiento.getTipoMovimiento();
        this.valor = movimiento.getValor();
        this.saldo = movimiento.getSaldo();
    }

    public UUID getIdCliente() {
        return idCliente;
    }

    public UUID getIdCuenta() {
        return idCuenta;
    }

    public UUID getIdMovimiento() {
        return idMovimiento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
