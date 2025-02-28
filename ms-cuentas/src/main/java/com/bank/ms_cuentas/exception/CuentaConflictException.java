package com.bank.ms_cuentas.exception;

public class CuentaConflictException extends RuntimeException{
    public CuentaConflictException(String message) {
        super(message);
    }
}
