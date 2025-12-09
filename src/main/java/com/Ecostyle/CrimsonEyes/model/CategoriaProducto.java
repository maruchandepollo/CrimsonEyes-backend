package com.Ecostyle.CrimsonEyes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CategoriaProducto {
    @Id
    private int id;
    private String nombre;
    private String descripcion;

    public CategoriaProducto() {
        this.id = 0;
        this.nombre = "";
        this.descripcion = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
