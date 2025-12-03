package com.Ecostyle.CrimsonEyes.dto;

import java.util.List;
import java.time.LocalDateTime;

public class VentaDTO {

    private List<DetalleVentaDTO> detalles;
    private String metodoPago;
    private String estado;
    private double total;
    private LocalDateTime fecha;
    private String usuarioEmail;
    private int id;

    public VentaDTO() {
    }

    // Constructor con todos los parámetros
    public VentaDTO(int id, String usuarioEmail, LocalDateTime fecha, double total, String estado, String metodoPago, List<DetalleVentaDTO> detalles) {
        this.id = id;
        this.usuarioEmail = usuarioEmail;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.detalles = detalles;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
    }
}
