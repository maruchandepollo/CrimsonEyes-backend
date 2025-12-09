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

<<<<<<< HEAD
=======
    // Listar TODAS las recetas (solo para admin)
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
    @GetMapping("/listar")
    public List<Receta> listar() {
        return recetaService.listar();
    }

<<<<<<< HEAD
=======
    // NUEVO: Listar recetas de un usuario específico
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
    @GetMapping("/listar/{userId}")
    public List<Receta> listarPorUsuario(@PathVariable String userId) {
        return recetaService.listarPorUsuario(userId);
    }

<<<<<<< HEAD
=======
    // Crear nueva receta
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
    @PostMapping("/almacenar")
    public ResponseEntity<?> almacenar(@RequestBody Receta receta) {
        return recetaService.almacenar(receta);
    }
}