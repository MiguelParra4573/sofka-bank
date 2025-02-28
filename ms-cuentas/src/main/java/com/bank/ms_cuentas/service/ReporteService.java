package com.bank.ms_cuentas.service;

import com.bank.ms_cuentas.dto.ClienteKafkaMessage;
import com.bank.ms_cuentas.dto.ReporteDTO;
import com.bank.ms_cuentas.model.Cuenta;
import com.bank.ms_cuentas.model.Movimiento;
import com.bank.ms_cuentas.repository.CuentaRepository;
import com.bank.ms_cuentas.repository.MovimientoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ClienteProxyService clienteProxyService;

    public List<ReporteDTO> generarReporte(LocalDate fechaInicio, LocalDate fechaFin) {

        List<Cuenta> cuentas = cuentaRepository.findAll();

        List<Movimiento> movimientos = movimientoRepository.findByFechaBetween(fechaInicio, fechaFin);
        return cuentas.stream()
                .map(cuenta -> {
                    List<Movimiento> movimientosCuenta = movimientos.stream()
                            .filter(movimiento -> movimiento.getCuenta().getId().equals(cuenta.getId()))
                            .sorted(Comparator.comparing(Movimiento::getFecha))
                            .collect(Collectors.toList());
                    System.out.printf("clientes: "+ cuenta.getClienteId().toString() + " 1 \n");
                    String clienteInfo = clienteProxyService.obtenerInfoCliente(cuenta.getClienteId().toString());

                    ClienteKafkaMessage clienteMessage = null;
                    try {
                        clienteMessage = objectMapper.readValue(clienteInfo, ClienteKafkaMessage.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    if (clienteInfo == null) {
                        clienteInfo = "Información no disponible";
                    }
                    ReporteDTO reporteDTO = new ReporteDTO();
                    reporteDTO.setCliente(clienteMessage.getNombre());
                    reporteDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
                    reporteDTO.setTipo(cuenta.getTipoCuenta());
                    reporteDTO.setSaldoInicial(cuenta.getSaldoInicial());
                    reporteDTO.setEstado(cuenta.getEstado());
                    reporteDTO.setSaldoDisponible(movimientos.get(movimientos.size() - 1 ).getSaldo());
                    reporteDTO.setMovimientos(movimientos);

                    return reporteDTO;
                })
                .collect(Collectors.toList());
    }

}
