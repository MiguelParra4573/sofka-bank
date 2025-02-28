package com.bank.ms_clientes.exception;

public class ClienteConflictException extends RuntimeException {
    public ClienteConflictException(String message) {
        super(message);
    }
}