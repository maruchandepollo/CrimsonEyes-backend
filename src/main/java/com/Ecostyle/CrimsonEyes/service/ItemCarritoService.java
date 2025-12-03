package com.Ecostyle.CrimsonEyes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Ecostyle.CrimsonEyes.model.Carrito;
import com.Ecostyle.CrimsonEyes.model.ItemCarrito;
import com.Ecostyle.CrimsonEyes.model.Producto;
import com.Ecostyle.CrimsonEyes.repository.CarritoRepository;
import com.Ecostyle.CrimsonEyes.repository.ItemCarritoRepository;
import com.Ecostyle.CrimsonEyes.repository.ProductoRepository;
import com.Ecostyle.CrimsonEyes.util.Util;

@Service
public class ItemCarritoService {

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private Util util = new Util();

    public List<ItemCarrito> listarPorCarrito(int carritoId) {
        return itemCarritoRepository.findByCarritoId(carritoId);
    }

    @Transactional
    public ResponseEntity<?> agregarItem(int carritoId, ItemCarrito item) {
        try {
            System.out.println("=== AGREGANDO ITEM AL CARRITO ===");
            System.out.println("CarritoId: " + carritoId);
            System.out.println("Item recibido: " + item);
            System.out.println("Producto: " + (item.getProducto() != null ? item.getProducto().getId() : "null"));

            // Validar que el producto existe
            if (item.getProducto() == null || item.getProducto().getId() == 0) {
                System.out.println("ERROR: Producto inválido");
                return ResponseEntity.ok(java.util.Map.of(
                        "estado", "Error",
                        "mensaje", "Producto inválido o faltante"
                ));
            }

            int productoId = item.getProducto().getId();
            System.out.println("ProductoId: " + productoId);

            // Validar que el carrito existe
            Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);
            if (!carritoOpt.isPresent()) {
                System.out.println("ERROR: Carrito no encontrado - ID: " + carritoId);
                return ResponseEntity.ok(java.util.Map.of(
                        "estado", "Error",
                        "mensaje", "Carrito no encontrado"
                ));
            }
            System.out.println("✓ Carrito encontrado");

            // Validar que el producto existe en la BD
            Optional<Producto> productoOpt = productoRepository.findById(productoId);
            if (!productoOpt.isPresent()) {
                System.out.println("ERROR: Producto no encontrado en BD - ID: " + productoId);
                return ResponseEntity.ok(java.util.Map.of(
                        "estado", "Error",
                        "mensaje", "Producto no encontrado en la base de datos"
                ));
            }
            System.out.println("✓ Producto encontrado: " + productoOpt.get().getNombre());

            Carrito carrito = carritoOpt.get();
            Producto producto = productoOpt.get();

            // Verificar si ya existe el item en el carrito
            System.out.println("Buscando si ya existe el item...");
            Optional<ItemCarrito> existente = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId);

            if (existente.isPresent()) {
                System.out.println("Item ya existe, aumentando cantidad");
                // Si existe, aumentar cantidad
                ItemCarrito ic = existente.get();
                ic.setCantidad(ic.getCantidad() + item.getCantidad());
                itemCarritoRepository.save(ic);

                System.out.println("✓ Cantidad actualizada exitosamente");
                return ResponseEntity.ok(java.util.Map.of(
                        "estado", "OK",
                        "mensaje", "Cantidad actualizada",
                        "item", ic
                ));
            }

            System.out.println("Creando nuevo item...");
            // Si no existe, crear nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();

            // Generar ID
            if (item.getId() == 0) {
                String idStr = util.generarID();
                try {
                    nuevoItem.setId(Integer.parseInt(idStr.substring(0, 8)));
                } catch (NumberFormatException e) {
                    nuevoItem.setId(Math.abs(idStr.hashCode() % 1_000_000));
                }
            } else {
                nuevoItem.setId(item.getId());
            }
            System.out.println("Nuevo ItemId: " + nuevoItem.getId());

            // Establecer relaciones
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(item.getCantidad() > 0 ? item.getCantidad() : 1);

            System.out.println("Guardando item en la BD...");
            // Guardar
            itemCarritoRepository.save(nuevoItem);

            System.out.println("✓✓✓ ITEM AGREGADO EXITOSAMENTE ✓✓✓");
            return ResponseEntity.ok(java.util.Map.of(
                    "estado", "OK",
                    "mensaje", "Item agregado correctamente",
                    "item", nuevoItem
            ));

        } catch (Exception e) {
            System.out.println("❌❌❌ EXCEPCIÓN AL AGREGAR ITEM ❌❌❌");
            e.printStackTrace();
            return ResponseEntity.ok(java.util.Map.of(
                    "estado", "Error",
                    "mensaje", "Error al agregar item: " + e.getMessage()
            ));
        }
    }

    @Transactional
    public ResponseEntity<?> eliminar(int id) {
        if (!itemCarritoRepository.existsById(id)) {
            return ResponseEntity.ok(java.util.Map.of(
                    "estado", "Error",
                    "mensaje", "Item no encontrado"
            ));
        }
        itemCarritoRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of(
                "estado", "OK",
                "mensaje", "Item eliminado"
        ));
    }

    @Transactional
    public ResponseEntity<?> actualizarCantidad(int itemId, int nuevaCantidad) {
        if (!itemCarritoRepository.existsById(itemId)) {
            return ResponseEntity.ok(java.util.Map.of(
                    "estado", "Error",
                    "mensaje", "Item no encontrado"
            ));
        }

        if (nuevaCantidad <= 0) {
            itemCarritoRepository.deleteById(itemId);
            return ResponseEntity.ok(java.util.Map.of(
                    "estado", "OK",
                    "mensaje", "Item eliminado"
            ));
        }

        ItemCarrito item = itemCarritoRepository.findById(itemId).get();
        item.setCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);

        return ResponseEntity.ok(java.util.Map.of(
                "estado", "OK",
                "item", item
        ));
    }

    @Transactional
    public ResponseEntity<?> vaciarCarrito(int carritoId) {
        List<ItemCarrito> items = itemCarritoRepository.findByCarritoId(carritoId);

        if (items.isEmpty()) {
            return ResponseEntity.ok(java.util.Map.of(
                    "estado", "OK",
                    "mensaje", "Carrito ya estaba vacío"
            ));
        }

        itemCarritoRepository.deleteAll(items);

        return ResponseEntity.ok(java.util.Map.of(
                "estado", "OK",
                "mensaje", "Carrito vaciado correctamente"
        ));
    }
}