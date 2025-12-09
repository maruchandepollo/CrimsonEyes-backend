package com.Ecostyle.CrimsonEyes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Ecostyle.CrimsonEyes.model.ItemCarrito;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Integer> {

    @Query("SELECT i FROM ItemCarrito i WHERE i.carrito.id = :carritoId")
    List<ItemCarrito> findByCarritoId(@Param("carritoId") int carritoId);

    @Query("SELECT i FROM ItemCarrito i WHERE i.carrito.id = :carritoId AND i.producto.id = :productoId")
    Optional<ItemCarrito> findByCarritoIdAndProductoId(@Param("carritoId") int carritoId, @Param("productoId") int productoId);

    @Query("SELECT COUNT(i) > 0 FROM ItemCarrito i WHERE i.carrito.id = :carritoId AND i.producto.id = :productoId")
    boolean existsByCarritoIdAndProductoId(@Param("carritoId") int carritoId, @Param("productoId") int productoId);

    @Query("DELETE FROM ItemCarrito i WHERE i.carrito.id = :carritoId")
    void deleteByCarritoId(@Param("carritoId") int carritoId);
}