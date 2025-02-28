package com.bank.ms_cuentas.controller;

import com.bank.ms_cuentas.dto.ReporteDTO;
import com.bank.ms_cuentas.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public ResponseEntity<?> generarReporte(@RequestParam("fecha") String rangoFechas) {
        try {
            String[] fechas = rangoFechas.split(":"); // Asumiendo formato "yyyy-MM-dd:yyyy-MM-dd"
            LocalDate fechaInicio = LocalDate.parse(fechas[0]);
            LocalDate fechaFin = LocalDate.parse(fechas[1]);

            List<ReporteDTO> reporte = reporteService.generarReporte(fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}