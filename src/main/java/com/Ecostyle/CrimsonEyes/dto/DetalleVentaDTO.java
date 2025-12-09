package com.Ecostyle.CrimsonEyes.dto;

public class DetalleVentaDTO {
    private int id;
    private int productoId;
    private String productoNombre;
    private String metodoPagoId;
    private String metodoPagoNombre;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleVentaDTO() {
    }

    public DetalleVentaDTO(int id, int productoId, String productoNombre, String metodoPagoId, String metodoPagoNombre, int cantidad, double precioUnitario, double subtotal) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.metodoPagoId = metodoPagoId;
        this.metodoPagoNombre = metodoPagoNombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getMetodoPagoId() {
        return metodoPagoId;
    }

    public void setMetodoPagoId(String metodoPagoId) {
        this.metodoPagoId = metodoPagoId;
    }

    public String getMetodoPagoNombre() {
        return metodoPagoNombre;
    }

    public void setMetodoPagoNombre(String metodoPagoNombre) {
        this.metodoPagoNombre = metodoPagoNombre;
    }

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

