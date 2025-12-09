package com.Ecostyle.CrimsonEyes.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.dto.DetalleVentaDTO;
import com.Ecostyle.CrimsonEyes.dto.VentaDTO;
import com.Ecostyle.CrimsonEyes.model.DetalleVenta;
<<<<<<< HEAD
import com.Ecostyle.CrimsonEyes.model.MetodoPago;
=======
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
import com.Ecostyle.CrimsonEyes.model.Producto;
import com.Ecostyle.CrimsonEyes.model.Usuario;
import com.Ecostyle.CrimsonEyes.model.Venta;
import com.Ecostyle.CrimsonEyes.repository.DetalleVentaRepository;
<<<<<<< HEAD
import com.Ecostyle.CrimsonEyes.repository.MetodoPagoRepository;
=======
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
import com.Ecostyle.CrimsonEyes.repository.ProductoRepository;
import com.Ecostyle.CrimsonEyes.repository.UsuarioRepository;
import com.Ecostyle.CrimsonEyes.repository.VentaRepository;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

<<<<<<< HEAD
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

=======
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
    public List<VentaDTO> listar() {
        return ventaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<VentaDTO> obtenerPorId(int id) {
        return ventaRepository.findByIdWithDetalles(id).map(this::convertToDTO);
    }

    public List<VentaDTO> obtenerPorUsuario(String email) {
        return ventaRepository.findByUsuarioEmail(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> crearVenta(VentaDTO ventaDTO) {
        try {
            // Obtener usuario
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(ventaDTO.getUsuarioEmail());
            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("estado", "Error", "mensaje", "Usuario no existe"));
            }

            // Crear venta
            Venta venta = new Venta();
            venta.setUsuario(usuarioOpt.get());
            venta.setFecha(LocalDateTime.now());
<<<<<<< HEAD
            venta.setEstado("FINALIZADO");
            venta.setMetodoPago(ventaDTO.getMetodoPago().trim());
=======
            venta.setEstado("PENDIENTE");
            venta.setMetodoPago(ventaDTO.getMetodoPago());
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
            venta.setTotal(ventaDTO.getTotal());

            Venta ventaGuardada = ventaRepository.save(venta);

            // Crear detalles de venta
            if (ventaDTO.getDetalles() != null && !ventaDTO.getDetalles().isEmpty()) {
                double total = 0;
                for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
                    Optional<Producto> productoOpt = productoRepository.findById(detalleDTO.getProductoId());
                    if (!productoOpt.isPresent()) {
                        return ResponseEntity.badRequest().body(java.util.Map.of("estado", "Error", "mensaje", "Producto con ID " + detalleDTO.getProductoId() + " no existe"));
                    }

<<<<<<< HEAD
                    // Obtener método de pago si está especificado en el detalle
                    MetodoPago metodoPago = null;
                    if (detalleDTO.getMetodoPagoId() != null && !detalleDTO.getMetodoPagoId().isEmpty()) {
                        Optional<MetodoPago> metodoPagoOpt = metodoPagoRepository.findById(detalleDTO.getMetodoPagoId());
                        if (metodoPagoOpt.isPresent()) {
                            metodoPago = metodoPagoOpt.get();
                        }
                    }

                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setVenta(ventaGuardada);
                    detalle.setProducto(productoOpt.get());
                    detalle.setMetodoPago(metodoPago);
=======
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setVenta(ventaGuardada);
                    detalle.setProducto(productoOpt.get());
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
                    detalle.setCantidad(detalleDTO.getCantidad());
                    detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                    detalle.setSubtotal(detalleDTO.getCantidad() * detalleDTO.getPrecioUnitario());
                    total += detalle.getSubtotal();
                    detalleVentaRepository.save(detalle);
                }
<<<<<<< HEAD
                // Actualizar total de la venta + 3000 por envío
                ventaGuardada.setTotal(total + 3000);
=======
                // Actualizar total de la venta
                ventaGuardada.setTotal(total);
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
                ventaRepository.save(ventaGuardada);

                // Recargar la venta desde la base de datos para obtener los detalles
                Optional<Venta> ventaRecargada = ventaRepository.findByIdWithDetalles(ventaGuardada.getId());
                if (ventaRecargada.isPresent()) {
                    ventaGuardada = ventaRecargada.get();
                }
            } else {
<<<<<<< HEAD
=======
                // Si no hay detalles, recargar igualmente para inicializar la lista
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
                Optional<Venta> ventaRecargada = ventaRepository.findByIdWithDetalles(ventaGuardada.getId());
                if (ventaRecargada.isPresent()) {
                    ventaGuardada = ventaRecargada.get();
                }
            }

            return ResponseEntity.ok(java.util.Map.of("estado", "OK", "mensaje", "Venta creada correctamente", "venta", convertToDTO(ventaGuardada)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("estado", "Error", "mensaje", e.getMessage()));
        }
    }

    public ResponseEntity<?> actualizarEstado(int ventaId, String nuevoEstado) {
        try {
            Optional<Venta> ventaOpt = ventaRepository.findByIdWithDetalles(ventaId);
            if (!ventaOpt.isPresent()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("estado", "Error", "mensaje", "Venta no existe"));
            }

<<<<<<< HEAD
            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("estado", "Error", "mensaje", "El estado no puede estar vacío"));
            }

            Venta venta = ventaOpt.get();
            String estadoAnterior = venta.getEstado();
            venta.setEstado(nuevoEstado.toUpperCase().trim());
            Venta ventaActualizada = ventaRepository.save(venta);

            return ResponseEntity.ok(java.util.Map.of(
                "estado", "OK",
                "mensaje", "Estado actualizado correctamente de " + estadoAnterior + " a " + nuevoEstado.toUpperCase(),
                "venta", convertToDTO(ventaActualizada)
            ));
=======
            Venta venta = ventaOpt.get();
            venta.setEstado(nuevoEstado);
            ventaRepository.save(venta);

            return ResponseEntity.ok(java.util.Map.of("estado", "OK", "mensaje", "Estado actualizado correctamente", "venta", convertToDTO(venta)));
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("estado", "Error", "mensaje", e.getMessage()));
        }
    }

    private VentaDTO convertToDTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setUsuarioEmail(venta.getUsuario().getEmail());
        dto.setFecha(venta.getFecha());
        dto.setTotal(venta.getTotal());
        dto.setEstado(venta.getEstado());
        dto.setMetodoPago(venta.getMetodoPago());

        // Siempre convertir detalles, incluso si es null o vacío
        List<DetalleVentaDTO> detallesDTO = new java.util.ArrayList<>();
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            detallesDTO = venta.getDetalles().stream()
                    .map(this::convertDetalleToDTO)
                    .collect(Collectors.toList());
        }
        dto.setDetalles(detallesDTO);

        return dto;
    }

    private DetalleVentaDTO convertDetalleToDTO(DetalleVenta detalle) {
        DetalleVentaDTO dto = new DetalleVentaDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProducto().getId());
        dto.setProductoNombre(detalle.getProducto().getNombre());
<<<<<<< HEAD
        if (detalle.getMetodoPago() != null) {
            dto.setMetodoPagoId(detalle.getMetodoPago().getId());
            dto.setMetodoPagoNombre(detalle.getMetodoPago().getNombre());
        }
=======
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}

