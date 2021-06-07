package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.PublicacionAdaptador;
import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;


import static java.lang.Integer.parseInt;

public class Inicio extends AppCompatActivity {

    TextView msj;
    Button nuevaPubli;
    Button cerrarSesion;
    Button irAPerfil;
    ListView mListView;
    List<Publicacion> mListPublicacion;
    ArrayList<String> mListInformacion=new ArrayList<>();
    ListAdapter mAdapter;
    ConexionSQLiteHelper conxDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        conxDB=new ConexionSQLiteHelper(this);
        nuevaPubli=(Button)findViewById(R.id.nuevaPubliBTN);
        irAPerfil=(Button)findViewById(R.id.userBtn);
        cerrarSesion=(Button)findViewById(R.id.cerrarSesionBtn);
        mListView=findViewById(R.id.listadoPost);

        ConexionSQLiteHelper objConx=new ConexionSQLiteHelper(getApplicationContext());
        //Usuario objUser=objConx.obtenerDatosUserForId(Utilidades.USER_LOGUEADO);
        Usuario objUser=objConx.obtenerDatosUserForId(Utilidades.USER_LOGUEADO);

        setTitle("Bienvenido "+objUser.getNombre());

        try {
            consultarBase();
        }catch (Exception e){

            Intent intento2=new Intent(getApplicationContext(),CrearPublicacion.class);
            startActivity(intento2);
        }

        mAdapter=new PublicacionAdaptador(Inicio.this,R.layout.card_view_comentarios,mListPublicacion);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent objIntent=new Intent(Inicio.this,PublcacionSeleccionada.class);
                objIntent.putExtra("postSelect",mListPublicacion.get(position).getId());
                startActivity(objIntent);
                /*Toast.makeText(Inicio.this, "Id:"+mListPublicacion.get(position).getId()+"\n"+
                                "Usuario Id:"+mListPublicacion.get(position).getUsuario_id()+"\n"+
                                "Comentario : "+mListPublicacion.get(position).getComentario()+"\n"+  "Usuario Id:"+mListPublicacion.get(position).getUsuario_id()+
                                "path imagen:"+mListPublicacion.get(position).getImg_Post()
                        , Toast.LENGTH_LONG).show();*/

            }
        });
        irAPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento= new Intent(getApplicationContext(),Perfil.class);
                intento.putExtra("usuarioNro",Utilidades.USER_LOGUEADO);
                startActivity(intento);
            }
        });



        nuevaPubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento= new Intent(getApplicationContext(),CrearPublicacion.class);
                startActivity(intento);
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesionConDatos();
            }


        });

    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
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

        Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_PUBLICAIONES +" ORDER BY id DESC;",null);

        while(cursor.moveToNext()){
            publicacion=new Publicacion();
            publicacion.setId(cursor.getInt(0));
            publicacion.setComentario(cursor.getString(1));
            publicacion.setUsuario_id(cursor.getInt(2));
           // Forma Antigua de publicar la foto
            if (cursor.getString(3)!=null){
            publicacion.setImg_Post(cursor.getString(3));
            }else{
                publicacion.setImg_Post(null);
            }
            publicacion.setFecha(cursor.getString(4));
            publicacion.setCantComentarios(cursor.getInt(5));
            publicacion.setCantMeGustas(cursor.getInt(6));
            publicacion.setCantMeGustas(cursor.getInt(6));
            if (cursor.getString(7)!=null){
                publicacion.setUbicacion(cursor.getString(7));
            }else{
                publicacion.setUbicacion("");
            }

            Usuario objUserPost=conxDB.obtenerDatosUserForId(parseInt(cursor.getString(2)));
            publicacion.setUsuario_nombre(objUserPost.getNombre());
            publicacion.setUsuario_img_perfil(objUserPost.getImg_Post());

            mListPublicacion.add(publicacion);
           // Collections.reverse(mListPublicacion);
        }
    }
    private void cerrarSesionConDatos() {

        SharedPreferences preferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(Utilidades.CAMPO_USER_USUARIO,null);
        editor.putString(Utilidades.CAMPO_USER_CONTRAS,null);
        Utilidades.USER_LOGUEADO=null;
        editor.commit();
        Intent intento2=new Intent(this,MainActivity.class);
        startActivity(intento2);
        finish();
        //Toast.makeText(MainActivity.this, "Inicio exitoso", Toast.LENGTH_LONG).show();



    }
}