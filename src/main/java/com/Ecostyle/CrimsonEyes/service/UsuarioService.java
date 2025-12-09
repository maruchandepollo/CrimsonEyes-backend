package com.Ecostyle.CrimsonEyes.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.dto.LoginDTO;
import com.Ecostyle.CrimsonEyes.dto.UsuarioDTO;
import com.Ecostyle.CrimsonEyes.dto.UsuarioResponse;
import com.Ecostyle.CrimsonEyes.model.Persona;
import com.Ecostyle.CrimsonEyes.model.Usuario;
import com.Ecostyle.CrimsonEyes.repository.PersonaRepository;
import com.Ecostyle.CrimsonEyes.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private CarritoService carritoService;

    public ResponseEntity<?> almacenar(UsuarioDTO dto) {
        UsuarioResponse response = new UsuarioResponse();

        if (usuarioRepository.existsById(dto.getEmail())) {
            response.setEstado("Error");
            response.setMensaje("El usuario con email: " + dto.getEmail() + " ya existe");
            return ResponseEntity.ok(response);
        }
        if (dto.getRut() == null || dto.getRut().trim().isEmpty()) {
            Persona persona = new Persona();
            persona.setNombre(dto.getNombre());
            persona.setRut("TEMP-" + dto.getEmail().replaceAll("[@.]", "-"));
            personaRepository.save(persona);

            Usuario usuario = new Usuario();
            usuario.setEmail(dto.getEmail());
            usuario.setPassword(dto.getPassword());
            usuario.setPersona(persona);

            usuarioRepository.save(usuario);

            // Crear carrito automáticamente
            carritoService.crearCarritoParaUsuario(usuario);

            // Obtener usuario guardado
            Usuario usuarioGuardado = usuarioRepository.findById(dto.getEmail()).orElse(null);
            UsuarioDTO dtoGuardado = new UsuarioDTO();
            if (usuarioGuardado != null) {
                dtoGuardado.setEmail(usuarioGuardado.getEmail());
                dtoGuardado.setPassword("*************");
                if (usuarioGuardado.getPersona() != null) {
                    dtoGuardado.setRut(usuarioGuardado.getPersona().getRut());
                    dtoGuardado.setNombre(usuarioGuardado.getPersona().getNombre());
                }
            }

            System.out.println("[UsuarioService] usuarioGuardado (simple) -> "
                    + (usuarioGuardado != null ? usuarioGuardado.getEmail() : "null"));

            response.setEstado("OK");
            response.setMensaje("Usuario creado correctamente (registro simple)!");
            response.setUsuarioDTO(dtoGuardado);
            return ResponseEntity.ok(response);
        }

        if (personaRepository.existsById(dto.getRut())) {
            response.setEstado("Error");
            response.setMensaje("Rut: " + dto.getRut() + " ya asociado a otro usuario");
            return ResponseEntity.ok(response);
        }
        Persona persona = new Persona();
        Usuario usuario = new Usuario();

        persona.setRut(dto.getRut());
        persona.setNombre(dto.getNombre());

        personaRepository.save(persona);

        usuario.setEmail(dto.getEmail());
        usuario.setPassword((dto.getPassword()));
        usuario.setPersona(persona);

        usuarioRepository.save(usuario);

        // Crear carrito automáticamente
        carritoService.crearCarritoParaUsuario(usuario);

        // Obtener usuario guardado
        Usuario usuarioGuardado = usuarioRepository.findById(dto.getEmail()).orElse(null);
        UsuarioDTO dtoGuardado = new UsuarioDTO();
        if (usuarioGuardado != null) {
            dtoGuardado.setEmail(usuarioGuardado.getEmail());
            dtoGuardado.setPassword("*************");
            if (usuarioGuardado.getPersona() != null) {
                dtoGuardado.setRut(usuarioGuardado.getPersona().getRut());
                dtoGuardado.setNombre(usuarioGuardado.getPersona().getNombre());
            } else {
                dtoGuardado.setRut("");
                dtoGuardado.setNombre("");
            }
        }

        System.out.println("[UsuarioService] usuarioGuardado -> "
                + (usuarioGuardado != null ? usuarioGuardado.getEmail() : "null") + ", persona -> "
                + (usuarioGuardado != null && usuarioGuardado.getPersona() != null
                        ? usuarioGuardado.getPersona().getRut()
                        : "null"));

        response.setEstado("OK");
        response.setMensaje("Usuario creado correctamente!");
        response.setUsuarioDTO(dtoGuardado);
        return ResponseEntity.ok(response);
    }

    public List<UsuarioDTO> listar() {
        List<UsuarioDTO> listaFinal = new ArrayList<>();
        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Usuario u : usuarios) {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setPassword("*************");
            dto.setEmail(u.getEmail());

            Persona persona = u.getPersona();
            if (persona != null) {
                dto.setRut(persona.getRut());
                dto.setNombre(persona.getNombre());
            } else {
                dto.setRut("");
                dto.setNombre("");
            }

            listaFinal.add(dto);
        }
        return listaFinal;
    }

    public UsuarioDTO obtenerPorEmail(String email) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        if (!usuarioRepository.existsById(email)) {
            return usuarioDTO; // Retorna vacío si no existe
        }

        Usuario usuario = usuarioRepository.findById(email).get();

        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setPassword("*************");

        if (usuario.getPersona() != null) {
            usuarioDTO.setRut(usuario.getPersona().getRut());
            usuarioDTO.setNombre(usuario.getPersona().getNombre());
        } else {
            usuarioDTO.setRut("");
            usuarioDTO.setNombre("");
        }

        return usuarioDTO;
    }


    public UsuarioDTO validarCredenciales(LoginDTO dto) {
        boolean validador = usuarioRepository.existsByEmailAndPassword(dto.getEmail(), dto.getPassword());
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        if (validador) {
            Usuario usuario = this.usuarioRepository.findById(dto.getEmail()).get();

            usuarioDTO.setEmail(dto.getEmail());
            usuarioDTO.setPassword("*************");
            usuarioDTO.setRut(usuario.getPersona().getRut());
            usuarioDTO.setNombre(usuario.getPersona().getNombre());

            return usuarioDTO;
        } else {
            return new UsuarioDTO();
        }
    }

    public ResponseEntity<?> editarPerfil(String email, UsuarioDTO dto) {
        UsuarioResponse response = new UsuarioResponse();


        // Validar que el usuario existe
        if (!usuarioRepository.existsById(email)) {
            response.setEstado("Error");
            response.setMensaje("Usuario no encontrado");
            System.out.println("[UsuarioService] Error: Usuario no encontrado - " + email);
            return ResponseEntity.ok(response);
        }

        Usuario usuario = usuarioRepository.findById(email).get();
        System.out.println("[UsuarioService] Usuario encontrado: " + usuario.getEmail());

        // Validar si intenta cambiar el email
        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && !dto.getEmail().equals(email)) {
            System.out.println("[UsuarioService] Intento de cambiar email de: " + email + " a: " + dto.getEmail());

            // Verificar que el nuevo email no esté en uso
            if (usuarioRepository.existsById(dto.getEmail())) {
                response.setEstado("Error");
                response.setMensaje("El email: " + dto.getEmail() + " ya está registrado");
                System.out.println("[UsuarioService] Error: Email ya registrado - " + dto.getEmail());
                return ResponseEntity.ok(response);
            }

            // Crear nuevo usuario con el nuevo email
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setEmail(dto.getEmail());
            nuevoUsuario.setPassword(usuario.getPassword());
            nuevoUsuario.setPersona(usuario.getPersona());

            // Actualizar contraseña si se proporciona
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                nuevoUsuario.setPassword(dto.getPassword());
                System.out.println(" Contraseña actualizada");
            }

            usuarioRepository.save(nuevoUsuario);
            System.out.println(" Nuevo usuario guardado: " + nuevoUsuario.getEmail());

            // Eliminar el usuario antiguo
            usuarioRepository.delete(usuario);
            System.out.println(" Usuario antiguo eliminado: " + usuario.getEmail());

            // Actualizar usuario de referencia
            usuario = nuevoUsuario;
        } else {
            System.out.println("[UsuarioService] No hay cambio de email");

            // Solo actualizar contraseña si se proporciona y el email no cambió
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                usuario.setPassword(dto.getPassword());
                System.out.println("[UsuarioService] Contraseña actualizada");
            }

            usuarioRepository.save(usuario);
            System.out.println("[UsuarioService] Usuario actualizado: " + usuario.getEmail());
        }

        // Actualizar datos de persona si existen
        if (usuario.getPersona() != null) {
            Persona personaAntigua = usuario.getPersona();
            System.out.println("[UsuarioService] Persona encontrada: " + personaAntigua.getRut());

            boolean necesitaCambioRUT = dto.getRut() != null && !dto.getRut().isEmpty() && !dto.getRut().equals(personaAntigua.getRut());

            if (necesitaCambioRUT) {
                System.out.println("[UsuarioService] Intento de cambiar RUT de: " + personaAntigua.getRut() + " a: " + dto.getRut());

                // Verificar que el nuevo RUT no esté asociado a otra persona
                if (personaRepository.existsById(dto.getRut())) {
                    response.setEstado("Error");
                    response.setMensaje("El RUT: " + dto.getRut() + " ya está asociado a otra persona");
                    System.out.println("[UsuarioService] Error: RUT ya existe - " + dto.getRut());
                    return ResponseEntity.ok(response);
                }

                // Crear nueva Persona con el nuevo RUT
                Persona nuevaPersona = new Persona();
                nuevaPersona.setRut(dto.getRut());
                nuevaPersona.setNombre(dto.getNombre() != null && !dto.getNombre().isEmpty() ? dto.getNombre() : personaAntigua.getNombre());

                personaRepository.save(nuevaPersona);
                System.out.println("[UsuarioService] Nueva persona creada con RUT: " + dto.getRut());

                // Actualizar el usuario para que apunte a la nueva persona
                usuario.setPersona(nuevaPersona);
                usuarioRepository.save(usuario);
                System.out.println("[UsuarioService] Usuario actualizado para apuntar a nueva persona");

                // Eliminar la persona antigua
                personaRepository.delete(personaAntigua);
                System.out.println("[UsuarioService] Persona antigua eliminada: " + personaAntigua.getRut());
            } else {
                // Solo actualizar el nombre si el RUT no cambia
                if (dto.getNombre() != null && !dto.getNombre().isEmpty()) {
                    personaAntigua.setNombre(dto.getNombre());
                    System.out.println("[UsuarioService] Nombre actualizado a: " + dto.getNombre());
                }

                personaRepository.save(personaAntigua);
                System.out.println("[UsuarioService] Persona actualizada");
            }
        }

        // Retornar datos actualizados
        UsuarioDTO dtoActualizado = new UsuarioDTO();
        dtoActualizado.setEmail(usuario.getEmail());
        dtoActualizado.setPassword("*************");
        if (usuario.getPersona() != null) {
            dtoActualizado.setRut(usuario.getPersona().getRut());
            dtoActualizado.setNombre(usuario.getPersona().getNombre());
        }

        response.setEstado("OK");
        response.setMensaje("Perfil actualizado correctamente!");
        response.setUsuarioDTO(dtoActualizado);

        System.out.println("[UsuarioService] Response final:");
        System.out.println("[UsuarioService] Estado: " + response.getEstado());
        System.out.println("[UsuarioService] Email: " + dtoActualizado.getEmail());
        System.out.println("[UsuarioService] Nombre: " + dtoActualizado.getNombre());
        System.out.println("[UsuarioService] Rut: " + dtoActualizado.getRut());
        System.out.println("[UsuarioService] Mensaje: " + response.getMensaje());

        return ResponseEntity.ok(response);
    }



}
