package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecostyle.CrimsonEyes.model.ItemCarrito;
import com.Ecostyle.CrimsonEyes.service.ItemCarritoService;

@RestController
@RequestMapping("/items-carrito")
public class ItemCarritoController {

    @Autowired
    private ItemCarritoService itemCarritoService;

    @GetMapping("/carrito/{carritoId}")
    public List<ItemCarrito> obtenerItemsDelCarrito(@PathVariable int carritoId) {
        return itemCarritoService.listarPorCarrito(carritoId);
    }

    @PostMapping("/agregar/{carritoId}")
    public ResponseEntity<?> agregarItemAlCarrito(
            @PathVariable int carritoId,
            @RequestBody ItemCarrito item) {
        return itemCarritoService.agregarItem(carritoId, item);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarItemDelCarrito(@PathVariable int id) {
        return itemCarritoService.eliminar(id);
    }

    @PutMapping("/actualizar-cantidad/{itemId}/{cantidad}")
    public ResponseEntity<?> actualizarCantidad(@PathVariable int itemId, @PathVariable int cantidad) {
        return itemCarritoService.actualizarCantidad(itemId, cantidad);
    }

    @DeleteMapping("/vaciar-carrito/{carritoId}")
    public ResponseEntity<?> vaciarCarrito(@PathVariable int carritoId) {
        return itemCarritoService.vaciarCarrito(carritoId);
    }

}
