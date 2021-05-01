package com.example.redsocial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

public class CrearPublicacion extends AppCompatActivity {

    Button crearPubli;
    Button cargar_img;
    TextView userPubli;
    EditText textoET;
    ImageView imagen;
    ImageView imagen_post;
    Integer SELEC_IMAGEN=10;
    Button sacarfoto;

    // VARIABLES PARA ALMACENAR FOTOS
    private  int PICK_IMG_REQUEST=200;
    private Uri imgFilePath;
    private Bitmap imgToStorage;
    String pathImagen;
    private Uri imagenUri;
    Boolean useCam=false;
    Boolean useGallery=false;
    //variables tomas
    String NOMBREIMAGEN;
    int TOMAR_FOTO=100;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_publicacion);
        useCam=false;
        useGallery=false;

        crearPubli=(Button)findViewById(R.id.crearPubliBTN);
        cargar_img=(Button)findViewById(R.id.cargarImgBTN);
        userPubli=(TextView) findViewById(R.id.userPubliTV);
        textoET=(EditText)findViewById(R.id.textPubliET);
        imagen=(ImageView)findViewById(R.id.imageView);
        imagen_post=(ImageView)findViewById(R.id.imageView2);
        sacarfoto=(Button)findViewById(R.id.tomarFotoBNT);


        //imagen.setImageResource(R.drawable.jeremy_full);

        //asigno a USERLOGUEADO el nombre del campo puesto en inicio
        ConexionSQLiteHelper objConx=new ConexionSQLiteHelper(getApplicationContext());
        Usuario objUser=objConx.obtenerDatosUserForId(Utilidades.USER_LOGUEADO);
        /*Toast.makeText(this, ""+Utilidades.USER_LOGUEADO+"\n"+
                objUser.getNombre()+objUser.getMail(), Toast.LENGTH_LONG).show();*/
        userPubli.setText(objUser.getNombre()+" "+objUser.getApellido());


        imagen.setImageBitmap(objUser.getImg_Post());

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
        sacarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirCamara();
            }
        });

    }

    private void cargar_imagen_post() {

        useGallery=true;useCam=false;
        Intent intento= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intento.setType("image/");
        startActivityForResult(intento,PICK_IMG_REQUEST);
    }

    public void AbrirCamara() {
        useCam = true;
        useGallery = false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagenArchivo = null;
        try {
            imagenArchivo = CrearImagen();
        } catch (IOException ex) {
            Log.e("Error", ex.toString());
        }
        if (imagenArchivo != null) {
            imagenUri = FileProvider.getUriForFile(this, "com.example.redsocial.fileprovider", imagenArchivo);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
            startActivityForResult(intent, TOMAR_FOTO);

        }
    }
    private File CrearImagen() throws IOException {
        String nombreImagen = "Foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);
        Utilidades.RUTA_IMAGEN = imagen.getAbsolutePath();
        return imagen;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (useGallery){
            if (requestCode== PICK_IMG_REQUEST && resultCode==RESULT_OK && data !=null &&
                    data.getData()!=null) {
                imgFilePath = data.getData();
                Utilidades.RUTA_IMAGEN=getRealPathFromURI(imgFilePath).toLowerCase();
                imagen_post.setImageURI(imgFilePath);
            }
        }
        if (useCam) {
            if (resultCode == RESULT_OK && data != null) {
                File imagenfile=new File(Utilidades.RUTA_IMAGEN);
                imgFilePath = data.getData();

                if (imagenfile.exists()) {
                    imgToStorage=BitmapFactory.decodeFile(imagenfile.getAbsolutePath());

                    imagen_post.setImageBitmap(imgToStorage);
                }
               // almacenamiento sin girar la imagen
                //imgToStorage=BitmapFactory.decodeFile(imagenfile.getAbsolutePath());
                //imagen_post.setImageBitmap(imgToStorage);
            }
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

        ConexionSQLiteHelper conxDB=new ConexionSQLiteHelper(this);

        SQLiteDatabase db=conxDB.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_COMENTARIO,textoET.getText().toString());
        values.put(Utilidades.CAMPO_USUARIOID,Utilidades.USER_LOGUEADO);
        // forma anterior de cargar foto
        values.put(Utilidades.CAMPO_IMG_POST,Utilidades.RUTA_IMAGEN);

        Long idresultante=db.insert(Utilidades.TABLA_COMENTARIO,Utilidades.CAMPO_ID,values);

        Snackbar.make(findViewById(android.R.id.content),"Se ingreso :"+idresultante,
                Snackbar.LENGTH_LONG).setDuration(5000).show();

        imgToStorage=null;
        Utilidades.RUTA_IMAGEN=null;
        useGallery=false;
        useCam=false;
        Intent intento=new Intent(getApplicationContext(),Inicio.class);
        startActivity(intento);
        onBackPressed();
    }




}
