package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.PublicacionAdaptador;
import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.redsocial.utilidades.Utilidades.USER_LOGUEADO;
import static java.lang.Integer.parseInt;

public class Inicio extends AppCompatActivity {

    TextView msj;
    Button nuevaPubli;
    ListView mListView;
    List<Publicacion> mListPublicacion;
    ArrayList<String> mListInformacion=new ArrayList<>();
    ListAdapter mAdapter;
    ConexionSQLiteHelper conxDB;
    ConexionSQLiteHelper conxDB_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO}, 0);
        }
        conxDB=new ConexionSQLiteHelper(this);
        nuevaPubli=(Button)findViewById(R.id.nuevaPubliBTN);
        mListView=findViewById(R.id.listadoPost);

        ConexionSQLiteHelper objConx=new ConexionSQLiteHelper(getApplicationContext());
        Usuario objUser=objConx.obtenerDatosUserForId(Utilidades.USER_LOGUEADO);
/*        Toast.makeText(this, ""+Utilidades.USER_LOGUEADO+"\n"+
                objUser.getNombre()+objUser.getMail(), Toast.LENGTH_LONG).show();*/
        setTitle("Bienvenido "+objUser.getNombre());



        try {
            consultarBase();
        }catch (Exception e){
            System.out.println("EL ERROR ES POR EEE"+e);
            Intent intento2=new Intent(getApplicationContext(),CrearPublicacion.class);
            startActivity(intento2);
        }

        mAdapter=new PublicacionAdaptador(Inicio.this,R.layout.card_view_comentarios,mListPublicacion);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Inicio.this, "Id:"+mListPublicacion.get(position).getId()+"\n"+
                                "Usuario Id:"+mListPublicacion.get(position).getUsuario_id()+"\n"+
                                "Comentario : "+mListPublicacion.get(position).getComentario()+"\n"+                             "Usuario Id:"+mListPublicacion.get(position).getUsuario_id()+
                                "path imagen:"+mListPublicacion.get(position).getImg_Post()

                        , Toast.LENGTH_LONG).show();
               /* Snackbar.make(findViewById(android.R.id.content), "Id:"+mListPublicacion.get(position).getId()+"\n"+
                                "Usuario Id:"+mListPublicacion.get(position).getUsuario_id()+"\n"+
                                "Comentario : "+mListPublicacion.get(position).getComentario()+"\n"+                             "Usuario Id:"+mListPublicacion.get(position).getUsuario_id()+
                                "path imagen:"+mListPublicacion.get(position).getImg_Post(),
                        Snackbar.LENGTH_LONG)
                        .setDuration(5000)
                        .show();*/
            }
        });


        nuevaPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento= new Intent(getApplicationContext(),CrearPublicacion.class);
                startActivity(intento);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
       /* Snackbar.make(findViewById(android.R.id.content), "Hello this is a snackbar!!!",
                Snackbar.LENGTH_LONG)
                .setBackgroundTint(1212)
                .setDuration(5000)
                .show();*/
    }

    private void consultarBase() {

        SQLiteDatabase db=conxDB.getReadableDatabase();
        Publicacion publicacion=null;
        mListPublicacion=new ArrayList<Publicacion>();

        Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_COMENTARIO+" ORDER BY id DESC;",null);

        while(cursor.moveToNext()){
            publicacion=new Publicacion();
            publicacion.setId(cursor.getInt(0));
            publicacion.setComentario(cursor.getString(1));
            publicacion.setUsuario_id(cursor.getString(2));
            if (cursor.getBlob(3)!=null){
                byte[] imagenEnBites=cursor.getBlob(3);
                ByteArrayInputStream imageStream = new ByteArrayInputStream(imagenEnBites);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                publicacion.setImg_Post(theImage);

            }else{
                publicacion.setImg_Post(null);
            }

            Usuario objUserPost=conxDB.obtenerDatosUserForId(parseInt(cursor.getString(2)));
            publicacion.setUsuario_nombre(objUserPost.getNombre());
            publicacion.setUsuario_img_perfil(objUserPost.getImg_Post());


            /* Forma Antigua de publicar la foto
            if (cursor.getString(3)!=null){
            publicacion.setImg_Post(cursor.getString(3));
            }else{
                publicacion.setImg_Post(null);
            }*/
           // System.out.println("el numero de usuario es> "+publicacion.getUsuario_id());
           // System.out.println("cursorr get count :"+cursor.getCount());
            //System.out.println("cursorr ir al ultimo :"+cursor.moveToLast());
            //System.out.println(cursor.getString(3));
            //Toast.makeText(this, ""+cursor.getString(3), Toast.LENGTH_SHORT).show();
            mListPublicacion.add(publicacion);
           // Collections.reverse(mListPublicacion);
        }
    }
}