package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecostyle.CrimsonEyes.model.Producto;
import com.Ecostyle.CrimsonEyes.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> obtenerProductos() {
        return productoService.listar();
    }

    @GetMapping("/listar")
    public List<Producto> obtenerTodosLosProductos() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable int id) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Producto no encontrado"));
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        return productoService.almacenar(producto);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarProducto(@RequestBody Producto producto) {
        return productoService.actualizar(producto);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable int id) {

        return productoService.eliminar(id);
    }

}
