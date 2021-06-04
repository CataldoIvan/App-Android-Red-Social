package com.example.redsocial.entidades;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

public class Comentarios {
    private Integer id;
    private String comentario;
    private Integer user_id;
    private Integer publicacion_id;
    private Object fecha;
    private String usuario_nombre;
    private Bitmap usuario_img_perfil;

    public Comentarios() {
    }

    public Comentarios(String comentario, Integer user_id, Integer publicacion_id) {

        this.comentario = comentario;
        this.user_id = user_id;
        this.publicacion_id = publicacion_id;
        this.fecha= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public String getUsuario_nombre() {
        return usuario_nombre;
    }

    public void setUsuario_nombre(String usuario_nombre) {
        this.usuario_nombre = usuario_nombre;
    }

    public Bitmap getUsuario_img_perfil() {
        return usuario_img_perfil;
    }

    public void setUsuario_img_perfil(Bitmap usuario_img_perfil) {
        this.usuario_img_perfil = usuario_img_perfil;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getPublicacion_id() {
        return publicacion_id;
    }

    public void setPublicacion_id(Integer publicacion_id) {
        this.publicacion_id = publicacion_id;
    }

    public Object getFecha() {
        return fecha;
    }

    public void setFecha(Object fecha) {
        this.fecha = fecha;
    }
}
