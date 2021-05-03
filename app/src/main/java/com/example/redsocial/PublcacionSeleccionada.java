package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.redsocial.entidades.ComentarioAdaptador;
import com.example.redsocial.entidades.Comentarios;
import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.utilidades.Utilidades;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    String ruta;

    //Seccion Comentarios
    EditText comentarioText;
    Button agregarComen;

    //listado de publicacion prueba

    ListView mListView;
    List<Comentarios> mListComentarios=null;
    ListAdapter mAdapter;





    @SuppressLint("WrongViewCast")
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
        //comentario
        agregarComen = (Button) findViewById(R.id.btnComentar);
        comentarioText = (EditText) findViewById(R.id.ETcomentario);
        mListView=findViewById(R.id.listPublicacionesCom);





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

        agregarComen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarComentario();

            }
        });

        try {
            consultarBaseComentarios();
            if(!mListComentarios.isEmpty() && mListComentarios!=null){
                mAdapter=new ComentarioAdaptador(PublcacionSeleccionada.this,R.layout.fila_comentario,mListComentarios);
                mListView.setAdapter(mAdapter);
            }
        }catch (Exception e){
            System.out.println("EL ERROPR  DEL ADAPTER DE COMENTARIOS ES :"+e);
        }






    }

    private void agregarComentario() {
        SQLiteDatabase db=conxDB.getWritableDatabase();
        ContentValues valores=new ContentValues();
        valores.put(Utilidades.CAMPO_COMENTARIO,comentarioText.getText().toString());
        valores.put(Utilidades.CAMPO_COMEN_USERID,Utilidades.USER_LOGUEADO);

        valores.put(Utilidades.CAMPO_COMEN_PUBLIID,pub.getId());
        valores.put(Utilidades.CAMPO_COMEN_FECHA,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));

        Long idresultanteComent=db.insert(Utilidades.TABLA_COMENTARIOS,Utilidades.CAMPO_COMEN_ID,valores);
        Snackbar.make(findViewById(android.R.id.content),"Se ingreso :"+idresultanteComent,
                Snackbar.LENGTH_LONG).setDuration(5000).show();
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void consultarBaseComentarios() {

        SQLiteDatabase db=conxDB.getReadableDatabase();
        Comentarios comentObj=null;
        mListComentarios =new ArrayList<Comentarios>();

        Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_COMENTARIOS +" ORDER BY id DESC;",null);

        while(cursor.moveToNext()){
            comentObj=new Comentarios();
            comentObj.setId(cursor.getInt(0));
            comentObj.setComentario(cursor.getString(1));
            comentObj.setUser_id(cursor.getInt(2));
            comentObj.setPublicacion_id(cursor.getInt(3));
            comentObj.setFecha(cursor.getString(4));

            System.out.println("comentariooooooo ///////////**********"+comentObj.getComentario());


            mListComentarios.add(comentObj);

        }
    }



}