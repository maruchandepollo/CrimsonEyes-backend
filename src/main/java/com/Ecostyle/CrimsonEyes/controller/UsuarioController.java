
package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecostyle.CrimsonEyes.dto.LoginDTO;
import com.Ecostyle.CrimsonEyes.dto.UsuarioDTO;
import com.Ecostyle.CrimsonEyes.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/listar")
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }


    @GetMapping("/perfil/{email}")
    public UsuarioDTO obtenerPerfil(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email);
    }


    @PostMapping("/agregar")
    public ResponseEntity<?> almacenar(@RequestBody UsuarioDTO dto) {
        return usuarioService.almacenar(dto);
    }


    @PostMapping("/login")
    public UsuarioDTO validarCredenciales(@RequestBody LoginDTO dto) {
        return usuarioService.validarCredenciales(dto);
    }


    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO dto) {
        return usuarioService.almacenar(dto);
    }


    @PutMapping("/editar/{email}")
    public ResponseEntity<?> editarPerfil(@PathVariable String email, @RequestBody UsuarioDTO dto) {
        return usuarioService.editarPerfil(email, dto);
    }
}