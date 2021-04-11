package com.example.redsocial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.utilidades.Utilidades;

import java.util.Collections;

public class CrearPublicacion extends AppCompatActivity {

    Button crearPubli;
    Button cargar_img;
    TextView userPubli;
    EditText textoET;
    Integer posicion;
    ImageView imagen;
    ImageView imagen_post;
    String direccUriImg;
    Integer SELEC_IMAGEN=10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_publicacion);
        crearPubli=(Button)findViewById(R.id.crearPubliBTN);
        cargar_img=(Button)findViewById(R.id.cargarImgBTN);
        userPubli=(TextView) findViewById(R.id.userPubliTV);
        textoET=(EditText)findViewById(R.id.textPubliET);
        imagen=(ImageView)findViewById(R.id.imageView);
        imagen_post=(ImageView)findViewById(R.id.imageView2);


        imagen.setImageResource(R.drawable.jeremy_full);

        //asigno a USERLOGUEADO el nombre del campo puesto en inicio
        Intent intento=getIntent();
        Utilidades.USER_LOGUEADO=intento.getStringExtra("usu");
        userPubli.setText("Usuario: "+Utilidades.USER_LOGUEADO);

        // creo ola funcion para cargar imagen
        cargar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargar_imagen_post();
            }
        });
        crearPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               registraPublicacion();
            }
        });

    }

    private void cargar_imagen_post() {
        Intent intento= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intento.setType("image/");
        startActivityForResult(intento,SELEC_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELEC_IMAGEN && data != null) {
            Uri imagen = data.getData();
            String pathImagen = getRealPathFromURI(imagen).toLowerCase();
            System.out.println(pathImagen);
            Utilidades.RUTA_IMAGEN = pathImagen;
            imagen_post.setImageURI(imagen);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation") Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void registraPublicacion() {

        ConexionSQLiteHelper conxDB=new ConexionSQLiteHelper(this,"db_comentarios",null,1);

        SQLiteDatabase db=conxDB.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_COMENTARIO,textoET.getText().toString());
        values.put(Utilidades.CAMPO_USUARIOID,Utilidades.USER_LOGUEADO);
        values.put(Utilidades.CAMPO_IMG_POST,Utilidades.RUTA_IMAGEN);

        Long idresultante=db.insert(Utilidades.TABLA_COMENTARIO,Utilidades.CAMPO_ID,values);
        Toast.makeText(this, "Se ingreso :"+idresultante, Toast.LENGTH_LONG).show();

        Utilidades.RUTA_IMAGEN=null;
        Intent intento=new Intent(getApplicationContext(),Inicio.class);
        intento.putExtra("usu",Utilidades.USER_LOGUEADO);
        startActivity(intento);
        onBackPressed();
    }
}
