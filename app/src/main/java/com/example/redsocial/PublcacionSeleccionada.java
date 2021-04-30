package com.example.redsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.utilidades.Utilidades;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class PublcacionSeleccionada extends AppCompatActivity {
    TextView nombreUser;
    TextView Publicacion;
    ImageView imgPublicacion;
    ImageView fotoPerfil;
    Publicacion publi;
    ConexionSQLiteHelper conxDB;
    Button volver;

    //rediumensionar inmagen
    Publicacion pub;
    Bitmap fotoaredimen;
    ImageView rotar;
    TextView ancho;
    TextView alto;
    String ruta;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publcacion_seleccionada);
        nombreUser = findViewById(R.id.publSelNomUser);
        Publicacion = findViewById(R.id.publSelPublicacion);
        imgPublicacion = findViewById(R.id.publSelFoto);
        fotoPerfil = findViewById(R.id.publSelFotoPerfil);
        conxDB = new ConexionSQLiteHelper(getApplicationContext());
        volver = (Button) findViewById(R.id.button);


        ancho=(TextView)findViewById(R.id.tvlatitud);
        alto=(TextView)findViewById(R.id.tvlongitud);

        try {
            int intent = (Integer) getIntent().getSerializableExtra("postSelect");
             pub = conxDB.obtenerDatosPublicacionPorId(intent);

            System.out.println("ppppppppppppppppppppppppppp" + pub.getId());
            Publicacion.setText(pub.getComentario());
            nombreUser.setText("id:" + pub.getId() + " | " + pub.getUsuario_nombre());
            fotoPerfil.setImageBitmap(pub.getUsuario_img_perfil());
            if (pub.getImg_Post() != null) {
                ruta=pub.getImg_Post();
                File imagenfile = new File(pub.getImg_Post());
                Bitmap bitmap = BitmapFactory.decodeFile(imagenfile.getAbsolutePath());

                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(imagenfile.getAbsolutePath());
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

                imgPublicacion.setImageBitmap(rotatedBitmap);
                imgPublicacion.setVisibility(View.VISIBLE);
                fotoaredimen=bitmap;
            } else {
                imgPublicacion.setImageBitmap(null);
                imgPublicacion.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            System.out.println("ERRRROOOOOO" + e);
        }

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }



}