package com.example.redsocial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imagenInBytes;
    private  int PICK_IMG_REQUEST=100;

    public ConexionSQLiteHelper(@Nullable Context context) {
        super(context, Utilidades.NOMBRE_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    db.execSQL(Utilidades.CREAR_TABLA_COMENTARIO);
    db.execSQL(Utilidades.CREAR_TABLA_USUARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS comentarios");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
    //onCreate(db);
    }

    public Usuario obtenerDatosUserLog(String usuario){
        SQLiteDatabase objSQLdb=this.getReadableDatabase();
        Usuario objUser=new Usuario();
        Cursor objCursor=objSQLdb.rawQuery("SELECT * FROM "+Utilidades.TABLA_USUARIOS+" WHERE "+Utilidades.CAMPO_USER_MAIL+"='"+usuario+"'",null);
        if(objCursor!=null){
            while (objCursor.moveToNext()){

                objUser.setId(objCursor.getInt(objCursor.getColumnIndex("id")));
                objUser.setNombre(objCursor.getString(objCursor.getColumnIndex("nombre")));
                objUser.setApellido(objCursor.getString(objCursor.getColumnIndex("apellido")));
                objUser.setMail(objCursor.getString(objCursor.getColumnIndex("correo")));
                objUser.setContrasenia(objCursor.getInt(objCursor.getColumnIndex("contrasenia")));
                //objUser.setComentario_id(objCursor.getInt(5));
                //convierto la imagen tipo Blob en ArraByte y luego en Bitmap para pasarlo al objeto
                try{if (objCursor.getBlob(objCursor.getColumnIndex("img_perfil"))!=null){
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
                objUser.setMail(objCursor.getString(objCursor.getColumnIndex("correo")));
                objUser.setContrasenia(objCursor.getInt(objCursor.getColumnIndex("contrasenia")));
                //objUser.setComentario_id(objCursor.getInt(5));
                //convierto la imagen tipo Blob en ArraByte y luego en Bitmap para pasarlo al objeto
                byte[] imagenEnBites=objCursor.getBlob(objCursor.getColumnIndex("img_perfil"));
                ByteArrayInputStream imageStream = new ByteArrayInputStream(imagenEnBites);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                objUser.setImg_Post(theImage);

                System.out.println("////////////////////////se encontro el usuario: \n" +
                        objUser.getNombre()+"\n"
                        +objUser.getMail()+"\n");
            }

        }

        return objUser;
    }





    /*public void storageimage(Publicacion objPublicacion, ContentValues objContent, @Nullable String nameDB){
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
