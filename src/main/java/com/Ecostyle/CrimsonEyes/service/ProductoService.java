package com.Ecostyle.CrimsonEyes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.model.Producto;
import com.Ecostyle.CrimsonEyes.repository.ProductoRepository;
import com.Ecostyle.CrimsonEyes.util.Util;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    private Util util = new Util();

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(int id) {
        return productoRepository.findById(id);
    }

    public ResponseEntity<?> almacenar(Producto producto) {
        if (producto.getId() == 0) {
            String idStr = util.generarID();
            try {
                producto.setId(Integer.parseInt(idStr.substring(0, 8)));
            } catch (NumberFormatException e) {
                producto.setId(Math.abs(idStr.hashCode() % 1_000_000));
            }
        } else if (productoRepository.existsById(producto.getId())) {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Producto ya existe"));
        }
        productoRepository.save(producto);
        return ResponseEntity.ok(java.util.Map.of("estado", "OK", "producto", producto));
    }

    public ResponseEntity<?> actualizar(Producto producto) {
        if (!productoRepository.existsById(producto.getId())) {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Producto no existe"));
        }
        productoRepository.save(producto);
        return ResponseEntity.ok(java.util.Map.of("estado", "OK", "producto", producto));
    }

    public ResponseEntity<?> eliminar(int id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Producto no existe"));
        }
        productoRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("estado", "OK", "mensaje", "Producto eliminado"));
    }

}
