package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecostyle.CrimsonEyes.model.Carrito;
import com.Ecostyle.CrimsonEyes.service.CarritoService;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/listar")
    public List<Carrito> obtenerTodosLosCarritos() {
        return carritoService.listar();
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearCarrito(@RequestBody Carrito carrito) {
        return carritoService.almacenar(carrito);
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<?> obtenerCarritoPorUsuario(@PathVariable String email) {
        Optional<Carrito> carrito = carritoService.obtenerPorUsuarioEmail(email);
        if (carrito.isPresent()) {
            return ResponseEntity.ok(java.util.Map.of("estado", "OK", "carrito", carrito.get()));
        } else {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Carrito no encontrado para el usuario"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCarritoPorId(@PathVariable int id) {
        Optional<Carrito> carrito = carritoService.obtenerPorId(id);
        if (carrito.isPresent()) {
            return ResponseEntity.ok(java.util.Map.of("estado", "OK", "carrito", carrito.get()));
        } else {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Carrito no encontrado"));
        }
    }

    @PostMapping("/finalizar-compra/{id}")
    public ResponseEntity<?> finalizarCompra(@PathVariable int id) {
        return carritoService.finalizarCompra(id);
    }

}
