package com.bank.ms_clientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@MappedSuperclass
public class Persona {

    @NotNull(message = "El nombre es obligatorio.")
    @Column(length = 100, nullable = false)
    private String nombre;

    @NotNull(message = "El genero es obligatorio.")
    @Column(length = 25, nullable = false)
    private String genero;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull(message = "La cedula de identificacion es obligatoria.")
    @Column(unique = true, length = 13, nullable = false)
    private String identificacion;

    @NotNull(message = "La direccion es obligatoria.")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String direccion;

    @NotNull(message = "El telefono es obligatorio.")
    @Column(length = 10, nullable = false)
    private String telefono;

    public @NotNull(message = "El nombre es obligatorio.") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotNull(message = "El nombre es obligatorio.") String nombre) {
        this.nombre = nombre;
    }

    public @NotNull(message = "El genero es obligatorio.") String getGenero() {
        return genero;
    }

    public void setGenero(@NotNull(message = "El genero es obligatorio.") String genero) {
        this.genero = genero;
    }

    public @NotNull(message = "La fecha de nacimiento es obligatoria.") LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(@NotNull(message = "La fecha de nacimiento es obligatoria.") LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public @NotNull(message = "La cedula de identificacion es obligatoria.") String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(@NotNull(message = "La cedula de identificacion es obligatoria.") String identificacion) {
        this.identificacion = identificacion;
    }

    public @NotNull(message = "La direccion es obligatoria.") String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NotNull(message = "La direccion es obligatoria.") String direccion) {
        this.direccion = direccion;
    }

    public @NotNull(message = "El telefono es obligatorio.") String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NotNull(message = "El telefono es obligatorio.") String telefono) {
        this.telefono = telefono;
    }
}