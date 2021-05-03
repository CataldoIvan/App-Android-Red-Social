package com.example.redsocial.entidades;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

public class Comentarios {
    private Integer id;
    private String comentario;
    private Integer user_id;
    private Integer publicacion_id;
    private Object fecha;

    public Comentarios() {
    }

    public Comentarios(String comentario, Integer user_id, Integer publicacion_id) {

        this.comentario = comentario;
        this.user_id = user_id;
        this.publicacion_id = publicacion_id;
        this.fecha= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
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
