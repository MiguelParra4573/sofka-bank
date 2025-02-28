package com.bank.ms_cuentas.service;

import com.bank.ms_cuentas.dto.ReporteDTO;
import com.bank.ms_cuentas.model.Cuenta;
import com.bank.ms_cuentas.model.Movimiento;
import com.bank.ms_cuentas.repository.CuentaRepository;
import com.bank.ms_cuentas.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"cliente-topic"})
class ReporteServiceIntegrationTest {

    @InjectMocks
    private ReporteService reporteService;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private ClienteProxyService clienteProxyService;

    private Cuenta cuenta;
    private Movimiento movimiento;
    private UUID cuentaId;
    private UUID movimientoId;

    @BeforeEach
    void setUp() {
        cuentaId = UUID.randomUUID();
        movimientoId = UUID.randomUUID();

        cuenta = new Cuenta();
        cuenta.setId(cuentaId);
        cuenta.setNumeroCuenta("123456");
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setSaldoInicial(new BigDecimal("1000.0"));
        cuenta.setEstado(Boolean.TRUE);
        cuenta.setClienteId(UUID.randomUUID());

        movimiento = new Movimiento();
        movimiento.setId(movimientoId);
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDate.now());
        movimiento.setSaldo(new BigDecimal("1500.0"));
    }

    @Test
    void testGenerarReporte() {
        when(cuentaRepository.findAll()).thenReturn(List.of(cuenta));
        when(movimientoRepository.findByFechaBetween(any(), any())).thenReturn(List.of(movimiento));
        when(clienteProxyService.obtenerInfoCliente(cuenta.getClienteId().toString()))
                .thenReturn("{\"nombre\":\"Juan Perez\"}");

        List<ReporteDTO> reportes = reporteService.generarReporte(LocalDate.now().minusDays(1), LocalDate.now());

        assertNotNull(reportes);
        assertEquals(1, reportes.size());
        assertEquals("Juan Perez", reportes.get(0).getCliente());
        assertEquals("123456", reportes.get(0).getNumeroCuenta());
    }
}
