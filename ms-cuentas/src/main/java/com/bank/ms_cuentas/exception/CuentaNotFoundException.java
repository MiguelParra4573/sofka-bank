package com.bank.ms_cuentas.exception;

public class CuentaNotFoundException extends RuntimeException {

    public CuentaNotFoundException() {
        super("La cuenta no existe.");
    }

}
