package com.bank.ms_clientes.exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException() {
        super("Cliente no encontrado.");
    }

}
