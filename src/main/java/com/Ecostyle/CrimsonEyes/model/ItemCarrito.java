package com.Ecostyle.CrimsonEyes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "item_carritos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemCarrito {
    @Id
    private int id;

    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "carrito_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_carrito_item_carrito"))
    @JsonBackReference // Evita referencia circular
    private Carrito carrito;

    @OneToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_producto_carrito"))
    private Producto producto;

    // Campo transient para facilitar la serialización
    @Transient
    private Integer carritoId;

    public ItemCarrito() {
        this.id = 0;
        this.cantidad = 0;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCarritoId() {
        if (carritoId != null) {
            return carritoId;
        }
        return carrito != null ? carrito.getId() : null;
    }

    public void setCarritoId(Integer carritoId) {
        this.carritoId = carritoId;
    }
}