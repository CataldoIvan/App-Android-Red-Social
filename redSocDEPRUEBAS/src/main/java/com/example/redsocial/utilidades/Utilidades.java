package com.example.redsocial.utilidades;

public class Utilidades {

    public static final String TABLA_COMENTARIO="comentarios";
    public static final  String CAMPO_ID="id";
    public static final  String CAMPO_COMENTARIO="comentario";
    public static final  String CAMPO_IMG_POST="imagenPost";
    public static final String CAMPO_USUARIOID="usuario_id";
    public static final String CREAR_TABLA_COMENTARIO="CREATE TABLE "+TABLA_COMENTARIO+" " +
            "("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CAMPO_COMENTARIO+" TEXT,"+CAMPO_USUARIOID+" INTEGER,"+CAMPO_IMG_POST+" TEXT)";
    public static String RUTA_IMAGEN;
    public static  String USER_LOGUEADO;


    ////////// SE CREA LA TABLA USUARIO
    public static final String TABLA_USUARIOS="usuarios";
    public static final  String CAMPO_USER_ID="id";
    public static final  String CAMPO_USER_NOMBRE="nombre";
    public static final String CAMPO_USER_APELLIDO="apellido";
    public static final String CAMPO_USER_MAIL="correo";
    public static final String CAMPO_USER_CONTRAS="contrasenia";
    public static final  String CAMPO_COMENT_ID="comentario_id";
    public static final String CREAR_TABLA_USUARIO="CREATE TABLE "+TABLA_USUARIOS+" " +
            "("+CAMPO_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CAMPO_USER_NOMBRE+" TEXT,"
            +CAMPO_USER_APELLIDO+" TEXT,"+CAMPO_USER_MAIL+" TEXT,"+CAMPO_USER_CONTRAS+" TEXT,"
        +CAMPO_COMENT_ID+" INTEGER )";


}
