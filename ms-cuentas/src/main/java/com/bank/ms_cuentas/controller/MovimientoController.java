package com.bank.ms_cuentas.controller;

import com.bank.ms_cuentas.dto.MovimientoDTO;
import com.bank.ms_cuentas.exception.CuentaConflictException;
import com.bank.ms_cuentas.model.Movimiento;
import com.bank.ms_cuentas.service.MovimientoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoServiceImpl service;

    @GetMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> helloWorld() {
        return new ResponseEntity<String>("Micro movimientos corriendo!!!", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearMovimiento(@Valid @RequestBody Movimiento movimiento, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Movimiento movimientoCreado = service.crearMovimiento(movimiento);
            return new ResponseEntity<>(movimientoCreado, HttpStatus.CREATED);
        } catch (CuentaConflictException e) {
            return new ResponseEntity<>("La transaacción no se realizo por favor, intente de nuevo dentro de un momento!", HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/{clienteId}")
    public ResponseEntity<?> obtenerMovimientos(
            @PathVariable UUID clienteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable ) {
        Page<Movimiento> movimientos = service.obtenerMovimientosPorFechaCliente(clienteId, startDate, endDate, pageable);

        List<MovimientoDTO> movimientosDTO = movimientos.getContent().stream()
                .map(MovimientoDTO::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("movimientos", movimientosDTO);
        response.put("currentPage", movimientos.getNumber());
        response.put("totalItems", movimientos.getTotalElements());
        response.put("totalPages", movimientos.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
