package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Ecostyle.CrimsonEyes.model.Receta;
import com.Ecostyle.CrimsonEyes.service.RecetaService;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    // Listar TODAS las recetas (solo para admin)
    @GetMapping("/listar")
    public List<Receta> listar() {
        return recetaService.listar();
    }

    // NUEVO: Listar recetas de un usuario específico
    @GetMapping("/listar/{userId}")
    public List<Receta> listarPorUsuario(@PathVariable String userId) {
        return recetaService.listarPorUsuario(userId);
    }

    // Crear nueva receta
    @PostMapping("/almacenar")
    public ResponseEntity<?> almacenar(@RequestBody Receta receta) {
        return recetaService.almacenar(receta);
    }
}