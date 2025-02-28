package com.bank.ms_cuentas.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ClienteDTO {

    private UUID clienteId;

    private String nombre;

    private String genero;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;

    private String identificacion;

    private String direccion;

    private String telefono;

    private String contraseña;

    private Boolean estado;


}
