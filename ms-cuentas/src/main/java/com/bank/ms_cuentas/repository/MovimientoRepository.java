package com.bank.ms_cuentas.repository;

import com.bank.ms_cuentas.model.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, UUID>, JpaSpecificationExecutor<Movimiento> {

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.clienteId = :clienteId AND m.fecha BETWEEN :startDate AND :endDate")
    Page<Movimiento> obtenerMovimientosPorFechaCliente(
            @Param("clienteId") UUID clienteId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    List<Movimiento> findByFechaBetween(LocalDate startDate, LocalDate endDate );

}
