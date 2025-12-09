package com.Ecostyle.CrimsonEyes.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carritos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Carrito {
    @Id
    private int id;

    private String fecha;

    private String estado;

    @OneToOne
    @JoinColumn(name = "usuario_email", referencedColumnName = "email")
    @JsonBackReference
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito")
<<<<<<< HEAD
    @JsonIgnore
=======
    @JsonIgnore // IMPORTANTE: Ignorar la lista de items para evitar ciclos
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
    private List<ItemCarrito> items;

    public Carrito() {
        this.id = 0;
        this.fecha = "";
        this.estado = "";
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }
}