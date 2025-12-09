package com.Ecostyle.CrimsonEyes.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.model.Carrito;
import com.Ecostyle.CrimsonEyes.model.Usuario;
import com.Ecostyle.CrimsonEyes.repository.CarritoRepository;
import com.Ecostyle.CrimsonEyes.repository.ItemCarritoRepository;
import com.Ecostyle.CrimsonEyes.util.Util;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    private Util util = new Util();

    public List<Carrito> listar() {
        return carritoRepository.findAll();
    }

    public ResponseEntity<?> almacenar(Carrito c) {
        if (carritoRepository.existsById(c.getId())) {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Carrito ya existe"));
        }
        carritoRepository.save(c);
        return ResponseEntity.ok(java.util.Map.of("estado", "OK", "carrito", c));
    }

    // Crear carrito automáticamente para un usuario
    public Carrito crearCarritoParaUsuario(Usuario usuario) {
        Carrito carrito = new Carrito();
        carrito.setId(Math.abs(util.generarID().hashCode() % 1_000_000));
        carrito.setUsuario(usuario);
        carrito.setFecha(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        carrito.setEstado("activo");
        carritoRepository.save(carrito);
        return carrito;
    }

    // Obtener carrito de un usuario por su email
    public Optional<Carrito> obtenerPorUsuarioEmail(String email) {
        return carritoRepository.findByUsuarioEmail(email);
    }

    // Obtener carrito por ID
    public Optional<Carrito> obtenerPorId(int id) {
        return carritoRepository.findById(id);
    }

    // Finalizar compra (cambiar estado y vaciar carrito)
    public ResponseEntity<?> finalizarCompra(int carritoId) {
        Optional<Carrito> carritoOptional = carritoRepository.findById(carritoId);

        if (!carritoOptional.isPresent()) {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Carrito no encontrado"));
        }

        Carrito carrito = carritoOptional.get();

        // Cambiar estado a pagado
        carrito.setEstado("pagado");
        carritoRepository.save(carrito);

        // Vaciar carrito (eliminar todos los items)
        itemCarritoRepository.deleteAll(itemCarritoRepository.findByCarritoId(carritoId));

        return ResponseEntity.ok(java.util.Map.of(
            "estado", "OK",
            "mensaje", "Compra finalizada correctamente",
            "carrito", carrito
        ));
    }

}
