package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocial.utilidades.Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Registrarse extends AppCompatActivity {
    TextView nombre;
    TextView apellido;
    TextView correo;
    TextView contrasenia;
    Button guardar;
    Button cancelar;
    //Variables manejo de foto
    ImageView fotoPerfil;
    private Integer PICK_IMG_REQUEST=100;
    private Uri imgFilePath;
    private Bitmap imgToStorage;
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imagenInBytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        nombre=findViewById(R.id.nombreTV);
        apellido=findViewById(R.id.apellidoTV);
        correo=findViewById(R.id.correoTV);
        contrasenia=findViewById(R.id.contraseniaTV);
        guardar=findViewById(R.id.guardarRegBTN);
        cancelar=findViewById(R.id.cancelarRegBTN);
        fotoPerfil=(ImageView)findViewById(R.id.imgPerfilIV);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNuevoUsuario();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarImgPerfil();
            }
        });

    }

    private void agregarImgPerfil() {
        Intent intento= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intento.setType("image/");
        startActivityForResult(intento,PICK_IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
                if (requestCode== PICK_IMG_REQUEST && resultCode==RESULT_OK && data !=null &&
                        data.getData()!=null) {
                    imgFilePath = data.getData();
                    imgToStorage = MediaStore.Images.Media.getBitmap(getContentResolver(), imgFilePath);
                    fotoPerfil.setImageBitmap(imgToStorage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void guardarNuevoUsuario() {
        ConexionSQLiteHelper conxDB_user=new ConexionSQLiteHelper(this);
        SQLiteDatabase db_user=conxDB_user.getWritableDatabase();
        ContentValues values_user=new ContentValues();
        values_user.put(Utilidades.CAMPO_USER_NOMBRE,nombre.getText().toString());
        values_user.put(Utilidades.CAMPO_USER_APELLIDO,apellido.getText().toString());
        values_user.put(Utilidades.CAMPO_USER_MAIL,correo.getText().toString());
        values_user.put(Utilidades.CAMPO_USER_CONTRAS,contrasenia.getText().toString());
        //values_user.put(Utilidades.CAMPO_COMENT_ID,"AUN NO SE COMO TRAELO");

        //Nueva forma de almacenar imagenes
        try {
            if (imgToStorage!=null){
            Bitmap imagenAAlmacenarBitmap=imgToStorage;
            objectByteArrayOutputStream =new ByteArrayOutputStream();
            imagenAAlmacenarBitmap.compress(Bitmap.CompressFormat.JPEG,PICK_IMG_REQUEST,objectByteArrayOutputStream);
            imagenInBytes=objectByteArrayOutputStream.toByteArray();
            values_user.put(Utilidades.CAMPO_IMG_PERFIL,imagenInBytes);
            }


        }catch(Exception e){
            Toast.makeText(this, "HUBO UN ERROR CON LA IMAGEN", Toast.LENGTH_LONG).show();
        }

        Long idresultante_user=db_user.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USER_ID,values_user);
        Toast.makeText(this, "USUARIO REGISTRADO :"+idresultante_user, Toast.LENGTH_LONG).show();
        finish();
    }
}