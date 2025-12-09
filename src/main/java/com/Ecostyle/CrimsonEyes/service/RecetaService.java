package com.Ecostyle.CrimsonEyes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.dto.RecetaResponse;
import com.Ecostyle.CrimsonEyes.model.Receta;
import com.Ecostyle.CrimsonEyes.repository.RecetaRepository;
import com.Ecostyle.CrimsonEyes.util.Util;


@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public List<Receta> listar() {
        return recetaRepository.findAll();
    }

    public List<Receta> listarPorUsuario(String userId) {
        return recetaRepository.findByUserId(userId);
    }

    public ResponseEntity<?> almacenar(Receta receta) {
        Util util = new Util();
        RecetaResponse response = new RecetaResponse();

        if(receta.getUserId() == null || receta.getUserId().isEmpty()) {
            response.setEstado("Error");
            response.setMensaje("El userId es requerido");
            response.setReceta(new Receta());
            return ResponseEntity.badRequest().body(response);
        }

        Receta validacion = recetaRepository.findByTitleAndUserId(
                receta.getTitle(),
                receta.getUserId()
        );

        if(validacion == null) {
            receta.setId(util.generarID());
            recetaRepository.save(receta);

            response.setEstado("ok");
            response.setMensaje("Receta: " + receta.getTitle() + " almacenada con éxito");
            response.setReceta(receta);
            return ResponseEntity.ok(response);

        } else {
            response.setEstado("Error");
            response.setMensaje("Ya tienes una receta con el título: " + receta.getTitle());
            response.setReceta(new Receta());
            return ResponseEntity.ok(response);
        }
    }
}