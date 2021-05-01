package com.example.redsocial.entidades;

public class Historia {
    Integer id;
    private String usuario_id;
    private String img_Post;


    public Historia() {

    }

    public Historia(Integer id, String usuario_id, String img_Post) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.img_Post = img_Post;
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



    public String getImg_Post() {
        return img_Post;
    }

    public void setImg_Post(String img_Post) {
        this.img_Post = img_Post;
    }
}
