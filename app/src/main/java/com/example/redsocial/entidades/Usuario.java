package com.example.redsocial.entidades;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre,apellido,mail,contrasenia;
    private Integer id,comentario_id;
    private Bitmap img_Post;

    public Usuario() {

    }

    public Usuario(String nombre, String apellido, String mail, String contrasenia) {
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Integer getComentario_id() {
        return comentario_id;
    }

    public void setComentario_id(Integer comentario_id) {
        this.comentario_id = comentario_id;
    }

    public Bitmap getImg_Post() {
        return img_Post;
    }

    public void setImg_Post(Bitmap img_Post) {
        this.img_Post = img_Post;
    }

    

}
