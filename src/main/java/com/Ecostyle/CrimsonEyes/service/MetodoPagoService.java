package com.Ecostyle.CrimsonEyes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.model.MetodoPago;
import com.Ecostyle.CrimsonEyes.repository.MetodoPagoRepository;
import com.Ecostyle.CrimsonEyes.util.Util;

@Service
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    private Util util = new Util();

    public List<MetodoPago> listar() {
        return metodoPagoRepository.findAll();
    }

    public MetodoPago obtener(String id) {
        return metodoPagoRepository.findById(id).orElse(new MetodoPago());
    }

    public ResponseEntity<?> almacenar(MetodoPago m) {
        if (m.getId().isBlank()) {
            m.setId(util.generarID());
        } else if (metodoPagoRepository.existsById(m.getId())) {
            return ResponseEntity.ok(java.util.Map.of("estado", "Error", "mensaje", "MetodoPago ya existe"));
        }
        metodoPagoRepository.save(m);
        return ResponseEntity.ok(java.util.Map.of("estado", "OK", "metodoPago", m));
    }

}
