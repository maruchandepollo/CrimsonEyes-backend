package com.Ecostyle.CrimsonEyes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecostyle.CrimsonEyes.model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    List<DetalleVenta> findByVentaId(int ventaId);
}

