package com.Ecostyle.CrimsonEyes.repository;

import com.Ecostyle.CrimsonEyes.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta,Integer> {
    List<Venta> findByUsuarioEmail(String email);

    @Query("SELECT DISTINCT v FROM Venta v LEFT JOIN FETCH v.detalles WHERE v.id = :id")
    Optional<Venta> findByIdWithDetalles(@Param("id") int id);
}
