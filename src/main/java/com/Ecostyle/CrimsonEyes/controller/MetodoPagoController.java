package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecostyle.CrimsonEyes.model.MetodoPago;
import com.Ecostyle.CrimsonEyes.service.MetodoPagoService;

@RestController
@RequestMapping("/metodos-pago")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @GetMapping("/listar")
    public List<MetodoPago> obtenerTodosLosMetodos() {
        return metodoPagoService.listar();
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearMetodoPago(@RequestBody MetodoPago metodo) {
        return metodoPagoService.almacenar(metodo);
    }

}
