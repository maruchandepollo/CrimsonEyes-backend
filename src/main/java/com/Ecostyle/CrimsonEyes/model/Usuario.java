package com.Ecostyle.CrimsonEyes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "usuarios")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email")
public class Usuario {

    @Id
    private String email;
    private String password;
    @OneToOne
    @JoinColumn(name = "persona_rut", referencedColumnName = "rut", foreignKey = @ForeignKey(name = "fk_usuario_persona"))
    private Persona persona;

    public Usuario() {
        this.email = "";
        this.password = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
