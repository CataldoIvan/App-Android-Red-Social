package com.example.redsocial.entidades;

import java.io.Serializable;

public class Usuario implements Serializable {
    String nombre,apellido,mail;
    Integer id,contrasenia,comentario_id;

    public Usuario() {

    }

    public Usuario(String nombre, String apellido, String mail, Integer contrasenia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComentario_id() {
        return comentario_id;
    }

    public void setComentario_id(Integer comentario_id) {
        this.comentario_id = comentario_id;
    }
}
