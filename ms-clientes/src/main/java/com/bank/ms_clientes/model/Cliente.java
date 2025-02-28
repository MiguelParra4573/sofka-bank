package com.bank.ms_clientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "cliente")
public class Cliente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID clienteId;

    @NotNull(message = "La contraseña es obligatoria.")
    @Column(length = 255, nullable = false)
    private String contraseña;

    @NotNull(message = "El estado es obligatorio.")
    @Column(nullable = false)
    private Boolean estado;

    public Cliente(UUID clienteId, String identificacion, String telefono, String contraseña) {
        this.clienteId = clienteId;
        super.setIdentificacion(identificacion);
        super.setTelefono(telefono);
        this.contraseña = contraseña;
    }

    public Cliente() {

    }

    @PrePersist
    public void generarClienteId() {
        if (this.clienteId == null) {
            this.clienteId = UUID.randomUUID();
        }
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public @NotNull(message = "La contraseña es obligatoria.") String getContraseña() {
        return contraseña;
    }

    public void setContraseña(@NotNull(message = "La contraseña es obligatoria.") String contraseña) {
        this.contraseña = contraseña;
    }

    public @NotNull(message = "El estado es obligatorio.") Boolean getEstado() {
        return estado;
    }

    public void setEstado(@NotNull(message = "El estado es obligatorio.") Boolean estado) {
        this.estado = estado;
    }
}