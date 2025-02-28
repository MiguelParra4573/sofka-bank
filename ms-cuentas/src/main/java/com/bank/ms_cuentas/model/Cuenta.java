package com.bank.ms_cuentas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "cuenta")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "El numero de cuenta es obligatorio.")
    @Column(nullable = false, unique = true)
    private String numeroCuenta;

    //TODO: Implementar abastracción de tipo de cuenta, usar patron de diseño abstrac factory
    @NotNull(message = "El tipo de cuenta es obligatorio.")
    @Column(length = 20, nullable = false)
    private String tipoCuenta;

    @NotNull(message = "El saldo inical es obligatorio.")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado es obligatorio.")
    @Column(nullable = false)
    private Boolean estado;

    @NotNull(message = "El cliente es obligatorio.")
    @Column(nullable = false)
    private UUID clienteId;


    @PrePersist
    public void generarCuentaId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotNull(message = "El numero de cuenta es obligatorio.") String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(@NotNull(message = "El numero de cuenta es obligatorio.") String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public @NotNull(message = "El tipo de cuenta es obligatorio.") String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(@NotNull(message = "El tipo de cuenta es obligatorio.") String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public @NotNull(message = "El saldo inical es obligatorio.") BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(@NotNull(message = "El saldo inical es obligatorio.") BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public @NotNull(message = "El estado es obligatorio.") Boolean getEstado() {
        return estado;
    }

    public void setEstado(@NotNull(message = "El estado es obligatorio.") Boolean estado) {
        this.estado = estado;
    }

    public @NotNull(message = "El cliente es obligatorio.") UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(@NotNull(message = "El cliente es obligatorio.") UUID clienteId) {
        this.clienteId = clienteId;
    }
}
