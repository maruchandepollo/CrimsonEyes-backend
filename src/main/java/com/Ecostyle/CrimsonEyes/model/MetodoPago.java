package com.Ecostyle.CrimsonEyes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MetodoPago {
    @Id
    private String id;
    private String nombre;

    public MetodoPago() {
        this.id = "";
        this.nombre = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
