package com.example.redsocial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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


public class PublcacionSeleccionada extends AppCompatActivity {
    TextView nombreUser;
    TextView Publicacion;
    ImageView imgPublicacion;
    ImageView fotoPerfil;
    ConexionSQLiteHelper conxDB;
    int intent;
    //rediumensionar inmagen
    Publicacion pub;
    Bitmap fotoaredimen;

    String ruta;
    //Seccion Comentarios
    EditText comentarioText;
    Button agregarComen;

    //listado de publicacion prueba

    ListView mListView;
    List<Comentarios> mListComentarios=null;
    ListAdapter mAdapter;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    ListaComentarioskFragment listaComentarioskFragment;
    //variables para visualizar foto, como historias
    public int MAX_VALUE = 50;
    CountDownTimer cTimer = null;
    LayoutInflater inflaterHistoria;
    View historiaInflate;
    ImageView imagenHistoria;
    ProgressBar progressBar;
    Long tiempoRestante;
    LinearLayout layoutFeed;

    @SuppressLint({"WrongViewCast", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publcacion_seleccionada);
        nombreUser = findViewById(R.id.publSelNomUser);
        Publicacion = findViewById(R.id.publSelPublicacion);
        imgPublicacion = findViewById(R.id.publSelFoto);
        fotoPerfil = findViewById(R.id.publSelFotoPerfil);
        conxDB = new ConexionSQLiteHelper(getApplicationContext());



        //comentario
        agregarComen = (Button) findViewById(R.id.btnComentar);
        comentarioText = (EditText) findViewById(R.id.ETcomentario);
        //mListView=findViewById(R.id.listPublicacionesCom);


        try {
             intent = (Integer) getIntent().getSerializableExtra("postSelect");

             pub = conxDB.obtenerDatosPublicacionPorId(intent);

            //System.out.println("ppppppppppppppppppppppppppp" + pub.getId());
            Publicacion.setText(pub.getComentario());
            nombreUser.setText("id:" + pub.getId().toString() + " - " + pub.getUsuario_nombre());
            if (pub.getUsuario_img_perfil()!=null){
                fotoPerfil.setImageBitmap(pub.getUsuario_img_perfil());
            }else{
                fotoPerfil.setImageResource(R.drawable.jeremy_full);
            }

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
            System.out.println("ERRRROOOOOO:" + e);
        }
        try {
            //Manjeo de Fragment
            listaComentarioskFragment = new ListaComentarioskFragment();
            Bundle datosIdComent = new Bundle();
            datosIdComent.putInt("idComentarioPSelect", intent);
            listaComentarioskFragment.setArguments(datosIdComent);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView4, listaComentarioskFragment).commit();
        }catch (Exception e){
            System.out.println("error en crear frament"+e);
        }

        imgPublicacion.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //visualizacion de imagen
                layoutFeed = findViewById(R.id.linear2);
                inflaterHistoria = getLayoutInflater();
                historiaInflate = inflaterHistoria.inflate(R.layout.cardview_historias, layoutFeed, false);
                dialogBuilder = new AlertDialog.Builder(PublcacionSeleccionada.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.cardview_historias, layoutFeed, false);
                ImageView foto=view1.findViewById(R.id.imagenHistoria);
                ruta=pub.getImg_Post();

                File imagenfile=new File(pub.getImg_Post());
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

                foto.setImageBitmap(rotatedBitmap);
                dialogBuilder.setView(view1);
                dialog=dialogBuilder.create();
                dialog.show();
                //fin de visualizacion de imagen como historia,
                /*dialogBuilder = new AlertDialog.Builder(getApplicationContext());
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.cardview_historias, layoutFeed, false);
                
                dialogBuilder.setView(view1);
                dialog=dialogBuilder.create();
                dialog.show();*/
            }
        });


      agregarComen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                agregarComentario(pub.getId());
               // ventanaEmergenteComentarios();
            }
        });

       /*   try {
            consultarBaseComentarios();
            if(!mListComentarios.isEmpty() && mListComentarios!=null){
                mAdapter=new ComentarioAdaptador(PublcacionSeleccionada.this,R.layout.fila_comentario,mListComentarios);

                mListView.setAdapter(mAdapter);

            }
        }catch (Exception e){
            System.out.println("EL ERROPR  DEL ADAPTER DE COMENTARIOS ES :"+e);
        }
*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void agregarComentario(Integer idPublicacion) {
        SQLiteDatabase db=conxDB.getWritableDatabase();
        ContentValues valores=new ContentValues();
        valores.put(Utilidades.CAMPO_COMENTARIO,comentarioText.getText().toString());
        valores.put(Utilidades.CAMPO_COMEN_USERID,Utilidades.USER_LOGUEADO);

        valores.put(Utilidades.CAMPO_COMEN_PUBLIID,pub.getId());

        valores.put(Utilidades.CAMPO_COMEN_FECHA,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        conxDB.SumarCantComentariosEnPublicaion(idPublicacion);

        Long idresultanteComent=db.insert(Utilidades.TABLA_COMENTARIOS,Utilidades.CAMPO_COMEN_ID,valores);



        Snackbar.make(findViewById(android.R.id.content),"Se ingreso :"+idresultanteComent,
                Snackbar.LENGTH_LONG).setDuration(5000).show();
        finishAfterTransition();

        Intent objIntent=new Intent(PublcacionSeleccionada.this,PublcacionSeleccionada.class);
        objIntent.putExtra("postSelect",intent);
        startActivity(objIntent);
        onBackPressed();
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

            //System.out.println("comentariooooooo *"+comentObj.getComentario());


            mListComentarios.add(comentObj);

        }
    }

    private void ventanaEmergenteComentarios() {
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        for (int i=0;i<5;i++){
            View view1 = inflater.inflate(R.layout.listado,null );
            TextView nombre=(TextView) view1.findViewById(R.id.textView6);
            TextView comentario=(TextView) view1.findViewById(R.id.TVcomentarioContenido);
            nombre.setText("puto"+i);

            dialogBuilder.setView(view1);

        }
        dialog=dialogBuilder.create();
        dialog.show();

        /*Toast toas = new Toast(this);
        toas.setView(view1);
        toas.setDuration(Toast.LENGTH_LONG);
        toas.show();
*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    public void abrirHistoria(View view) {

        layoutFeed.addView(historiaInflate);
        tiempoRestante = Long.valueOf(1);
        iniciarTimer(5000, 100);
        progressBar = findViewById(R.id.progresoHistoria);
        progressBar.setMax(MAX_VALUE);
        imagenHistoria = findViewById(R.id.imagenHistoria);
        imagenHistoria.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(final View v, final MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cTimer.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        iniciarTimer(5000 - (tiempoRestante*100), 100);
                        break;
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void iniciarTimer(long total, long intervalo) {
        cTimer = new CountDownTimer(total, intervalo) {
            int progreso = Math.toIntExact(tiempoRestante);
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progreso);
                progreso += (1);
                tiempoRestante = Long.valueOf(progreso);
            }
            @Override
            public void onFinish() {
                progressBar.setProgress(MAX_VALUE);
                layoutFeed.removeView(historiaInflate);
            }
        }.start();
    }



}