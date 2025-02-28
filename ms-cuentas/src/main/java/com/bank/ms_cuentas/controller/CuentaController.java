package com.bank.ms_cuentas.controller;

import com.bank.ms_cuentas.exception.CuentaConflictException;
import com.bank.ms_cuentas.exception.CuentaNotFoundException;
import com.bank.ms_cuentas.model.Cuenta;
import com.bank.ms_cuentas.service.CuentaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaServiceImpl service;

    @GetMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> helloWorld() {
        return new ResponseEntity<String>("Micro Cuenta corriendo!!!", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> crearCuenta(@Valid @RequestBody Cuenta cuenta, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Cuenta cuentaCreada = service.crearCuenta(cuenta);
            return new ResponseEntity<>(cuentaCreada, HttpStatus.CREATED);
        } catch (CuentaConflictException e) {
            return new ResponseEntity<>("El numero de cuenta ya existe", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtenerCuenta(@PathVariable UUID id) {
        try {
            Cuenta cuenta = service.obtenerCuentaPorId(id);
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        } catch (CuentaConflictException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmenteCuenta(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        try {
            Cuenta cuentaActualizada = service.actualizarCuentaParcial(id, updates);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (CuentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta no encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar la cuenta");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable UUID id) {
        try {
            boolean eliminado = service.eliminarCuenta(id);
            if (eliminado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (CuentaConflictException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
