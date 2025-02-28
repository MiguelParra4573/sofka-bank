package com.bank.ms_cuentas.exception;

public class MovimientoConflictException extends RuntimeException{
    public MovimientoConflictException(String message) {
        super(message);
    }
}
