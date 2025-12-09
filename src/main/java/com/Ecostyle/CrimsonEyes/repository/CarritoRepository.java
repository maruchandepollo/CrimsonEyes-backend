package com.Ecostyle.CrimsonEyes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecostyle.CrimsonEyes.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Optional<Carrito> findByUsuarioEmail(String email);
}
