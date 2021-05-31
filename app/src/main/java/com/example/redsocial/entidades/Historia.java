package com.example.redsocial.entidades;

public class Historia {
    Integer id;
    private String usuario_id;
    private String imgHist;
    private Object fecha;

    public Historia() {
    }

    public Historia(Integer id, String usuario_id, String img_Post) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.imgHist = img_Post;
    }

    public Object getFecha() {
        return fecha;
    }

    public void setFecha(Object fecha) {
        this.fecha = fecha;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getImgHist() {
        return imgHist;
    }

    public void setImgHist(String imgHist) {
        this.imgHist = imgHist;
    }
}
