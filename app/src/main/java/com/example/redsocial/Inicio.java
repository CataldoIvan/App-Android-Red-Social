package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        mListView=findViewById(R.id.listadoPost);

        ConexionSQLiteHelper objConx=new ConexionSQLiteHelper(getApplicationContext());
        //Usuario objUser=objConx.obtenerDatosUserForId(Utilidades.USER_LOGUEADO);
        Usuario objUser=objConx.obtenerDatosUserForId(Utilidades.USER_LOGUEADO);
        /*   Toast.makeText(this, ""+Utilidades.USER_LOGUEADO+"\n"+
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


                Intent objIntent=new Intent(Inicio.this,PublcacionSeleccionada.class);
                objIntent.putExtra("postSelect",mListPublicacion.get(position).getId());
                startActivity(objIntent);
              /*  Toast.makeText(Inicio.this, "Id:"+mListPublicacion.get(position).getId()+"\n"+
                                "Usuario Id:"+mListPublicacion.get(position).getUsuario_id()+"\n"+
                                "Comentario : "+mListPublicacion.get(position).getComentario()+"\n"+                             "Usuario Id:"+mListPublicacion.get(position).getUsuario_id()+
                                "path imagen:"+mListPublicacion.get(position).getImg_Post()
                        , Toast.LENGTH_LONG).show();*/

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

        Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_PUBLICAIONES +" ORDER BY id DESC;",null);

        while(cursor.moveToNext()){
            publicacion=new Publicacion();
            publicacion.setId(cursor.getInt(0));
            publicacion.setComentario(cursor.getString(1));
            publicacion.setUsuario_id(cursor.getString(2));
           // Forma Antigua de publicar la foto
            if (cursor.getString(3)!=null){
            publicacion.setImg_Post(cursor.getString(3));
            }else{
                publicacion.setImg_Post(null);
            }

            Usuario objUserPost=conxDB.obtenerDatosUserForId(parseInt(cursor.getString(2)));
            publicacion.setUsuario_nombre(objUserPost.getNombre());
            publicacion.setUsuario_img_perfil(objUserPost.getImg_Post());

            mListPublicacion.add(publicacion);
           // Collections.reverse(mListPublicacion);
        }
    }
}