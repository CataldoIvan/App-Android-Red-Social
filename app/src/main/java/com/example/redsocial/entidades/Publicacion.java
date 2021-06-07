package com.example.redsocial.entidades;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Publicacion implements Serializable {
    Integer id;
    String comentario;
    Integer usuario_id;
    private String img_Post;
    private Integer cantComentarios;
    private Integer cantMeGustas;
    private String ubicacion;
    private String fecha;
    //Estas variables no se encuentran en la Base de datos, se  utilizan para mostra datos en el Inicio
    private String usuario_nombre;
    private Bitmap usuario_img_perfil;



    public Publicacion() {

    }
    public Publicacion( String comentario, Integer usuario_id) {

        this.comentario = comentario;
        this.usuario_id = usuario_id;
    }

    public Publicacion(String comentario, Integer usuario_id, String img_Post) {
        this.comentario = comentario;
        this.usuario_id = usuario_id;
        this.img_Post = img_Post;
    }

    public Publicacion(Publicacion obtenerDatosPublicacionPorId) {
        this.id = obtenerDatosPublicacionPorId.getId();
        this.comentario = obtenerDatosPublicacionPorId.getComentario();
        this.usuario_id = obtenerDatosPublicacionPorId.getUsuario_id();
        this.img_Post = obtenerDatosPublicacionPorId.getImg_Post();
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getCantComentarios() {
        return cantComentarios;
    }

    public void setCantComentarios(Integer cantComentarios) {
        this.cantComentarios = cantComentarios;
    }

    public Integer getCantMeGustas() {
        return cantMeGustas;
    }

    public void setCantMeGustas(Integer cantMeGustas) {
        this.cantMeGustas = cantMeGustas;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
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

    public String getImg_Post() {
        return img_Post;
    }

    public void setImg_Post(String img_Post) {
        this.img_Post = img_Post;
    }
}
