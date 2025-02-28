package com.bank.ms_cuentas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "movimiento")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private UUID id;

    @NotNull(message = "El fecha es obligatoria.")
    @Column(nullable = false)
    private LocalDate fecha;

    //TODO: Implementar abstraccion de tipo de movimiento, patron de diseño abstrac factory.
    @NotNull(message = "El tipo de movimiento es obligatorio.")
    @Column(length = 20, nullable = false)
    private String tipoMovimiento;

    @NotNull(message = "El valor del movimiento es obligatorio.")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @NotNull(message = "El saldo es obligatorio.")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numero_cuenta", nullable = false)  // Nombre de la columna FK
    private Cuenta cuenta;  // Relación con Cuenta

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotNull(message = "El fecha es obligatoria.") LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(@NotNull(message = "El fecha es obligatoria.") LocalDate fecha) {
        this.fecha = fecha;
    }

    public @NotNull(message = "El tipo de movimiento es obligatorio.") String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(@NotNull(message = "El tipo de movimiento es obligatorio.") String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public @NotNull(message = "El valor del movimiento es obligatorio.") BigDecimal getValor() {
        return valor;
    }

    public void setValor(@NotNull(message = "El valor del movimiento es obligatorio.") BigDecimal valor) {
        this.valor = valor;
    }

    public @NotNull(message = "El saldo es obligatorio.") BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(@NotNull(message = "El saldo es obligatorio.") BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
