package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;
import java.util.Optional;

import com.Ecostyle.CrimsonEyes.dto.VentaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecostyle.CrimsonEyes.service.VentaService;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<VentaDTO> listarVentas() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVentaPorId(@PathVariable int id) {
        Optional<VentaDTO> venta = ventaService.obtenerPorId(id);
        if (venta.isPresent()) {
            return ResponseEntity.ok(venta.get());
        } else {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "Venta no encontrada"));
        }
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<?> obtenerVentasPorUsuario(@PathVariable String email) {
        List<VentaDTO> ventas = ventaService.obtenerPorUsuario(email);
        if (!ventas.isEmpty()) {
            return ResponseEntity.ok(ventas);
        } else {
            return ResponseEntity.ok(java.util.Map.of("estado", "Info", "mensaje", "No hay ventas para este usuario"));
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearVenta(@RequestBody VentaDTO ventaDTO) {
        return ventaService.crearVenta(ventaDTO);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable int id, @RequestBody java.util.Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("estado", "Error", "mensaje", "El estado es requerido"));
        }
        return ventaService.actualizarEstado(id, nuevoEstado);
    }
}

