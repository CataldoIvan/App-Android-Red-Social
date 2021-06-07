package com.example.redsocial;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static java.lang.Integer.parseInt;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imagenInBytes;
    private  int PICK_IMG_REQUEST=100;
    Context context;

    public ConexionSQLiteHelper(@Nullable Context context) {
        super(context, Utilidades.NOMBRE_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    db.execSQL(Utilidades.CREAR_TABLA_PUBLICACION);
    db.execSQL(Utilidades.CREAR_TABLA_USUARIO);
    db.execSQL(Utilidades.CREAR_TABLA_COMENTARIOS);
    db.execSQL(Utilidades.CREAR_TABLA_HISTORIA);
    }
    //String SQLUpdateV2 = "ALTER TABLE publicaciones ADD COLUMN ubicacion TEXT";
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        /*if(versionAntigua == 5 && versionNueva == 6){
            db.execSQL(SQLUpdateV2);
        }*/
        db.execSQL("DROP TABLE IF EXISTS publicaciones");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS comentarios");
        db.execSQL("DROP TABLE IF EXISTS historias");

    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    public Usuario obtenerDatosUserLog(String usuario){
        SQLiteDatabase objSQLdb=this.getReadableDatabase();
        Usuario objUser=new Usuario();
        Cursor objCursor=objSQLdb.rawQuery("SELECT * FROM "+Utilidades.TABLA_USUARIOS+" WHERE "+Utilidades.CAMPO_USER_USUARIO +"='"+usuario+"'",null);
        if(objCursor!=null){
            while (objCursor.moveToNext()){

                objUser.setId(objCursor.getInt(objCursor.getColumnIndex("id")));
                objUser.setNombre(objCursor.getString(objCursor.getColumnIndex("nombre")));
                objUser.setApellido(objCursor.getString(objCursor.getColumnIndex("apellido")));
                objUser.setUsuario(objCursor.getString(objCursor.getColumnIndex("usuario")));
                objUser.setMail(objCursor.getString(objCursor.getColumnIndex("correo")));
                objUser.setContrasenia(objCursor.getString(objCursor.getColumnIndex("contrasenia")));
                //objUser.setComentario_id(objCursor.getInt(5));
                //convierto la imagen tipo Blob en ArraByte y luego en Bitmap para pasarlo al objeto
                try{
                    if (objCursor.getBlob(objCursor.getColumnIndex("img_perfil"))!=null){
                        byte[] imagenEnBites=objCursor.getBlob(objCursor.getColumnIndex("img_perfil"));
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(imagenEnBites);
                        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                        objUser.setImg_Post(theImage);
                     }else{

                     }
                }catch (Exception e){
                    System.out.println("///////////// error de carga foto perfil"+e);
                }

                System.out.println("////////////////////////se encontro el usuario: \n" +
                        objUser.getNombre()+"\n"
                        +objUser.getMail()+"\n");
            }

        }else {
            objUser.setId(null);
            objUser.setNombre(null);
            objUser.setApellido(null);
            objUser.setUsuario(null);
            objUser.setMail(null);
            objUser.setContrasenia(null);
            return objUser;
        }

        return objUser;
    }

    public Usuario obtenerDatosUserForId(Integer usuario){
        SQLiteDatabase objSQLdb=this.getReadableDatabase();
        Usuario objUser=new Usuario();
        Cursor objCursor=objSQLdb.rawQuery("SELECT * FROM "+Utilidades.TABLA_USUARIOS+" WHERE "+Utilidades.CAMPO_USER_ID+"="+usuario,null);
        if(objCursor!=null){
            while (objCursor.moveToNext()){

                objUser.setId(objCursor.getInt(objCursor.getColumnIndex("id")));
                objUser.setNombre(objCursor.getString(objCursor.getColumnIndex("nombre")));
                objUser.setApellido(objCursor.getString(objCursor.getColumnIndex("apellido")));
                objUser.setUsuario(objCursor.getString(objCursor.getColumnIndex("usuario")));
                objUser.setMail(objCursor.getString(objCursor.getColumnIndex("correo")));
                objUser.setContrasenia(objCursor.getString(objCursor.getColumnIndex("contrasenia")));
                //objUser.setComentario_id(objCursor.getInt(5));
                //convierto la imagen tipo Blob en ArraByte y luego en Bitmap para pasarlo al objeto
                byte[] imagenEnBites=objCursor.getBlob(objCursor.getColumnIndex("img_perfil"));
                if (imagenEnBites!=null){
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imagenEnBites);
                    Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                    objUser.setImg_Post(theImage);
                }else{
                    objUser.setImg_Post(null);

                }               /* System.out.println("////////////////////////se encontro el usuario: \n" +
                        objUser.getNombre()+"\n"
                        +objUser.getMail()+"\n");*/
           }

        }else{
            objUser=null;
        }
        return objUser;

    }
    public Boolean elUsuarioExiste(String usuario){
        SQLiteDatabase objSQLdb=this.getReadableDatabase();
        Boolean retorno=false;
        Cursor objCursor=objSQLdb.rawQuery("SELECT * FROM "+Utilidades.TABLA_USUARIOS+" WHERE "+Utilidades.CAMPO_USER_USUARIO+"='"+usuario+"'",null);
        if (objCursor.moveToNext()) { //Si el cursor tiene un registro, quiere decir que ya existe.
            retorno=true;
        } else {
            retorno=false;
        }
        objCursor.close(); //Cerramos nuestro cursor
        objSQLdb.close(); //Cerramos nuestro el db

        return retorno;
    }




    public static final String CAMPO_UBICACION_PUBLICACIONES="ubicacion";


    public Publicacion obtenerDatosPublicacionPorId(Integer publicacion){

        SQLiteDatabase objSQLdb=this.getReadableDatabase();
        Publicacion objPubli=null;
        Cursor objCursor=objSQLdb.rawQuery("SELECT * FROM publicaciones c INNER JOIN usuarios u on u.id=c.usuario_id WHERE c.id="+publicacion,null);
       if(objCursor!=null){
            while (objCursor.moveToNext()){
                objPubli= new Publicacion();
                objPubli.setId(objCursor.getInt(0));
                objPubli.setComentario(objCursor.getString(objCursor.getColumnIndex(Utilidades.CAMPO_PUBLICACION)));
                objPubli.setUsuario_id(objCursor.getInt(8));//id de usuario
                objPubli.setImg_Post(objCursor.getString(3));//imagen post
                objPubli.setUsuario_nombre(objCursor.getString(9));//NOMBRE USUAIRO
                objPubli.setFecha(objCursor.getString(objCursor.getColumnIndex(Utilidades.CAMPO_DATE)));
                objPubli.setCantComentarios(objCursor.getInt(objCursor.getColumnIndex(Utilidades.CAMPO_CANT_ME_GUSTAS_PUBLICACIONES)));
                objPubli.setCantMeGustas(objCursor.getInt(objCursor.getColumnIndex(Utilidades.CAMPO_USUARIOID)));
                objPubli.setUbicacion(objCursor.getString(objCursor.getColumnIndex(Utilidades.CAMPO_UBICACION_PUBLICACIONES)));

                byte[] imagenEnBites=objCursor.getBlob(objCursor.getColumnIndex("img_perfil"));
                if (imagenEnBites!=null){
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imagenEnBites);
                    Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                    objPubli.setUsuario_img_perfil(theImage);
                }
                }
            }
        return objPubli;
       }

    public void SumarCantComentariosEnPublicaion(Integer id) {
        SQLiteDatabase objSQLdb=this.getWritableDatabase();
        Publicacion objPubli=null;
        Cursor objCursor=objSQLdb.rawQuery("SELECT * FROM publicaciones WHERE id="+id,null);
        if(objCursor!=null){
            while (objCursor.moveToNext()) {
                objPubli = new Publicacion();
                objPubli.setCantComentarios(objCursor.getInt(objCursor.getColumnIndex("cant_comentarios")));
            }
        }
        Integer cant=0;
        cant=objPubli.getCantComentarios();
        System.out.println("/*//////////////////////************CANTIDADDDD "+cant);

        cant=cant+1;

        String strSQL = "UPDATE "+Utilidades.TABLA_PUBLICAIONES+" SET "+Utilidades.CAMPO_CANT_COMENTARIOS_PUBLICACIONES+" = "+cant+" WHERE id="+id;

        objSQLdb.execSQL(strSQL);



    }







    /*public void storageimage(PublicacionFrag objPublicacion, ContentValues objContent, @Nullable String nameDB){
        SQLiteDatabase objectSqLiteDB=this.getWritableDatabase();
        Bitmap imagenAAlmacenarBitmap=objPublicacion.getImg_Post();
        objectByteArrayOutputStream =new ByteArrayOutputStream();
        imagenAAlmacenarBitmap.compress(Bitmap.CompressFormat.JPEG,PICK_IMG_REQUEST,objectByteArrayOutputStream);
        imagenInBytes=objectByteArrayOutputStream.toByteArray();

        *//*objContent=new ContentValues();*//*
        objContent.put(Utilidades.CAMPO_IMAGEN,imagenInBytes);

        Long checkIfQueryRun=objectSqLiteDB.insert(Utilidades.TABLA_USUARIO,null,objContent);
        if(checkIfQueryRun!=-1) {
            Toast.makeText(context, "Se agrego a tu tabla", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(context, "NO SE PUDO AGREGAR", Toast.LENGTH_SHORT).show();
        }
       }
*/



}
