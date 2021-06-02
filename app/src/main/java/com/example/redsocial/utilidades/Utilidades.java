package com.example.redsocial.utilidades;

import androidx.appcompat.app.AppCompatActivity;

public class Utilidades extends AppCompatActivity {

    public static  Integer LOGUEADO;

    public static final String NOMBRE_DB="db_RedSocial";
    public static Integer USER_LOGUEADO;

    public static final String TABLA_PUBLICAIONES ="publicaciones";
    public static final  String CAMPO_ID="id";
    public static final  String CAMPO_PUBLICACION ="publicacion";
    public static final  String CAMPO_IMG_POST="imagenPost";
    public static final String CAMPO_USUARIOID="usuario_id";
    public static final String CAMPO_DATE="date";
    public static final String CREAR_TABLA_PUBLICACION ="CREATE TABLE "+ TABLA_PUBLICAIONES +" " +
            "("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ CAMPO_PUBLICACION +" TEXT,"+
            CAMPO_USUARIOID+" INTEGER,"+CAMPO_IMG_POST+" TEXT," +CAMPO_DATE+
            " TEXT)";
    public static String RUTA_IMAGEN;



    ////////// SE CREA LA TABLA USUARIO
    public static final String TABLA_USUARIOS="usuarios";
    public static final  String CAMPO_USER_ID="id";
    public static final  String CAMPO_USER_NOMBRE="nombre";
    public static final String CAMPO_USER_APELLIDO="apellido";
    public static final String CAMPO_USER_USUARIO ="usuario";
    public static final String CAMPO_CORREO_USUARIO ="correo";
    public static final String CAMPO_USER_CONTRAS="contrasenia";
    public static final  String CAMPO_COMENT_ID="comentario_id";
    public static final  String CAMPO_IMG_PERFIL="img_perfil";
    public static final String CREAR_TABLA_USUARIO="CREATE TABLE "+TABLA_USUARIOS+" " +
            "("+CAMPO_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CAMPO_USER_NOMBRE+" TEXT,"
            +CAMPO_USER_APELLIDO+" TEXT,"+ CAMPO_USER_USUARIO +" TEXT,"+ CAMPO_CORREO_USUARIO +" TEXT,"+CAMPO_USER_CONTRAS+" TEXT,"
        +CAMPO_COMENT_ID+" INTEGER,"+CAMPO_IMG_PERFIL+" BLOB)";


    ////////// SE CREA LA TABLA COMENTARIOS
    public static final String TABLA_COMENTARIOS="comentarios";
    public static final  String CAMPO_COMEN_ID="id";
    public static final  String CAMPO_COMENTARIO="comentario";
    public static final  String CAMPO_COMEN_USERID="user_id";
    public static final String CAMPO_COMEN_PUBLIID="publicacion_id";
    public static final String CAMPO_COMEN_FECHA="fecha";

    public static final String CREAR_TABLA_COMENTARIOS="CREATE TABLE "+TABLA_COMENTARIOS+" " +
            "("+CAMPO_COMEN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CAMPO_COMENTARIO+" TEXT,"
            +CAMPO_COMEN_USERID+" INTEGER,"+CAMPO_COMEN_PUBLIID+" INTEGER,"+CAMPO_COMEN_FECHA+" TEXT)";

    ////////// SE CREA LA TABLA HISTORIAS

    public static final String TABLA_HISTORIAS="historias";
    public static final  String CAMPO_HIST_ID="id";
    public static final  String CAMPO_HIST_USERID="user_id";
    public static final  String CAMPO_HIST_IMG="imgHist";
    public static final String CAMPO_HIST_FECHA="fecha";


    public static final String CREAR_TABLA_HISTORIA="CREATE TABLE "+TABLA_HISTORIAS+" " +
            "("+CAMPO_HIST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CAMPO_HIST_USERID+" INTEGER,"
            +CAMPO_HIST_IMG+" TEXT,"+CAMPO_HIST_FECHA+" TEXT)";



}


