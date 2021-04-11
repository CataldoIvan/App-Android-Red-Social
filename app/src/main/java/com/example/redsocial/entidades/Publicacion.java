package com.example.redsocial.entidades;

import android.graphics.Bitmap;

public class Publicacion {
    Integer id;
    String comentario;
    String usuario_id;
    private Bitmap img_Post;

    public Bitmap getImg_Post() {
        return img_Post;
    }

    public void setImg_Post(Bitmap img_Post) {
        this.img_Post = img_Post;
    }

    public Publicacion() {

    }

    public Publicacion( String comentario, String usuario_id) {

        this.comentario = comentario;
        this.usuario_id = usuario_id;
    }

    public Publicacion(String comentario, String usuario_id, Bitmap img_Post) {
        this.comentario = comentario;
        this.usuario_id = usuario_id;
        this.img_Post = img_Post;
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

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

  
}
