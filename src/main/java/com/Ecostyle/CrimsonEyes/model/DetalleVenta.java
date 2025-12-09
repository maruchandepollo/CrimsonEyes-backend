package com.Ecostyle.CrimsonEyes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_ventas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "venta_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_venta_detalle"))
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_producto_detalle"))
    private Producto producto;

<<<<<<< HEAD
    @ManyToOne
    @JoinColumn(name = "metodo_pago_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_metodo_pago_detalle"))
    private MetodoPago metodoPago;

=======
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    @Column(nullable = false)
    private double subtotal;

    public DetalleVenta() {
        this.cantidad = 0;
        this.precioUnitario = 0;
        this.subtotal = 0;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

<<<<<<< HEAD
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

=======
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}

