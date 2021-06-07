package com.example.redsocial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    //Variables de GPS
    TextView textView1, textView2;
    double latitud;
    double longitud;
    String direccion;
    CheckBox agregarUbicacion;
    @RequiresApi(api = Build.VERSION_CODES.M)
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

        //Comienzo GPS
        textView1 = findViewById(R.id.textView11);
        textView2 = findViewById(R.id.ubicacionTV);
        agregarUbicacion = findViewById(R.id.checkBoxUbicacion);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        //FIN gps


        //imagen.setImageResource(R.drawable.jeremy_full);

        //asigno a USERLOGUEADO el nombre del campo puesto en inicio
        ConexionSQLiteHelper objConx=new ConexionSQLiteHelper(getApplicationContext());
        Usuario objUser=objConx.obtenerDatosUserForId(Utilidades.USER_LOGUEADO);
        /*Toast.makeText(this, ""+Utilidades.USER_LOGUEADO+"\n"+
                objUser.getNombre()+objUser.getMail(), Toast.LENGTH_LONG).show();*/
        userPubli.setText(objUser.getNombre()+" "+objUser.getApellido());

        if (objUser.getImg_Post()!=null){
            imagen.setImageBitmap(objUser.getImg_Post());
        }else {
            imagen.setImageResource(R.drawable.jeremy_full);
        }


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
                    imagen_post.setImageURI(imagenUri);
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
        //conxDB.onUpgrade(db,5,6);



        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_PUBLICACION,textoET.getText().toString());
        values.put(Utilidades.CAMPO_USUARIOID,Utilidades.USER_LOGUEADO);
        // forma anterior de cargar foto
        values.put(Utilidades.CAMPO_IMG_POST,Utilidades.RUTA_IMAGEN);
        values.put(Utilidades.CAMPO_DATE,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        values.put(Utilidades.CAMPO_CANT_COMENTARIOS_PUBLICACIONES,0);
        values.put(Utilidades.CAMPO_CANT_ME_GUSTAS_PUBLICACIONES,0);
        if (agregarUbicacion.isChecked()){
            values.put(Utilidades.CAMPO_UBICACION_PUBLICACIONES,direccion);
        }

        Long idresultante=db.insert(Utilidades.TABLA_PUBLICAIONES,Utilidades.CAMPO_ID,values);

        imgToStorage=null;
        Utilidades.RUTA_IMAGEN=null;
        useGallery=false;
        useCam=false;
        Intent intento=new Intent(getApplicationContext(),Inicio.class);
        startActivity(intento);
        onBackPressed();
    }


    //******Comienzo del manejo de GPS
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setCrearPublicaion(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
//        textView1.setText("Localización agregada");
        textView2.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                latitud = loc.getLatitude();
                longitud = loc.getLongitude();

                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    textView2.setText("Mi direccion es: \n"
                            +DirCalle.getAddressLine(0));
                    direccion = DirCalle.getAdminArea()+","+DirCalle.getCountryName();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void abrirMaps(View view,String direcc){
        String geo = "http://maps.google.co.in/maps?q=" + direcc;
        //String geo = "http://maps.google.com/maps?q=loc:" + latitud + "," + longitud + "(Buenos Aires)";
        Uri intentUri = Uri.parse(geo);
        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
        startActivity(intent);
    }

    //******FIN del manejo de GPS



}
