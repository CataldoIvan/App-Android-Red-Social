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
    Integer posicion;
    ImageView imagen;
    ImageView imagen_post;
    String direccUriImg;
    Integer SELEC_IMAGEN=10;
    Button sacarfoto;

    // VARIABLES PARA ALMACENAR FOTOS
    private  int PICK_IMG_REQUEST=100;
    private Uri imgFilePath;
    private Bitmap imgToStorage;
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imagenInBytes;
    String RUTA_IMAGEN=null;
    private Uri imagenUri;
    Boolean useCam=false;
    Boolean useGallery=false;


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
        useCam=true;
        useGallery=false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagenArchivo = null;
        try {
            imagenArchivo = CrearImagen();
        } catch (IOException ex) {
            Log.e("Error", ex.toString());
        }



        if (imagenArchivo != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "MyPicture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
            imagenUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            /*
            imagenUri = FileProvider.getUriForFile(this, "com.xample.redSocial.fileprovider", imagenArchivo);
           */ intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
            startActivityForResult(intent, 10);
        }
    }
    private File CrearImagen() throws IOException {
        String nombreImagen = "Foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);
        RUTA_IMAGEN = imagen.getAbsolutePath();
        return imagen;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (useGallery){
                if (requestCode== PICK_IMG_REQUEST && resultCode==RESULT_OK && data !=null &&
                        data.getData()!=null) {
                    imgFilePath = data.getData();
                    imgToStorage = MediaStore.Images.Media.getBitmap(getContentResolver(), imgFilePath);
                    imagen_post.setImageBitmap(imgToStorage);
                }
            }
            if (useCam){
                if (requestCode == 10 && resultCode == RESULT_OK) {
                    try {
                        imgToStorage = MediaStore.Images.Media.getBitmap(getContentResolver(), imagenUri);

                        imagen_post.setImageBitmap(imgToStorage);
                    } catch (FileNotFoundException e) {
                        System.out.println("ERROR FILE /////////////////////"+e);
                    } catch (IOException e) {
                        System.out.println("****************"+e);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
/*
         //*************LA VIEJA FORMA DE CARGAR IMAGENES*********
        if (resultCode == RESULT_OK && requestCode == SELEC_IMAGEN && data != null) {
            Uri imagen = data.getData();
            String pathImagen = getRealPathFromURI(imagen).toLowerCase();
            System.out.println(pathImagen);
            Utilidades.RUTA_IMAGEN = pathImagen;
            imagen_post.setImageURI(imagen);
        }*/
    }



    private void registraPublicacion() {

        ConexionSQLiteHelper conxDB=new ConexionSQLiteHelper(this);

        SQLiteDatabase db=conxDB.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_COMENTARIO,textoET.getText().toString());
        values.put(Utilidades.CAMPO_USUARIOID,Utilidades.USER_LOGUEADO);
        // forma anterior de cargar foto
       // values.put(Utilidades.CAMPO_IMG_POST,Utilidades.RUTA_IMAGEN);

      /*  File imagenFile = new File(getIntent().getExtras().getString("imageView"));

        if (!getIntent().getExtras().getString("imageView").equals("NULL") && imagenFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getExtras().getString("imageView"));
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(getIntent().getExtras().getString("imageView"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap rotatedBitmap = null;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            imageView.setImageBitmap(rotatedBitmap);
        }*/
        //Nueva forma de almacenar imagenes
        if (imgToStorage!=null){
            Bitmap imagenAAlmacenarBitmap=imgToStorage;
            objectByteArrayOutputStream =new ByteArrayOutputStream();
            imagenAAlmacenarBitmap.compress(Bitmap.CompressFormat.JPEG,PICK_IMG_REQUEST,objectByteArrayOutputStream);
            imagenInBytes=objectByteArrayOutputStream.toByteArray();
            values.put(Utilidades.CAMPO_IMG_POST,imagenInBytes);

        }

        Long idresultante=db.insert(Utilidades.TABLA_COMENTARIO,Utilidades.CAMPO_ID,values);

        Toast.makeText(this, "Se ingreso :"+idresultante, Toast.LENGTH_LONG).show();

        imgToStorage=null;
        Intent intento=new Intent(getApplicationContext(),Inicio.class);
        startActivity(intento);
        onBackPressed();
    }

    private byte[] imagemTratada(byte[] imagem_img){

        while (imagem_img.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;

    }


}
