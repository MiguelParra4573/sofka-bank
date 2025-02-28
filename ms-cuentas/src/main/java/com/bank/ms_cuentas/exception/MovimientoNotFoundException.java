package com.bank.ms_cuentas.exception;

public class MovimientoNotFoundException extends RuntimeException {

    public MovimientoNotFoundException() {
        super("Movimiento no encontrado.");
    }

}
