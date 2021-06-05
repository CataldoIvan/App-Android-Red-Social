package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.redsocial.entidades.Comentarios;
import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Perfil extends AppCompatActivity {

    ImageView imgPerfil,imgPortada;
    TextView correo,nombre,usuario,contadorPubli;
    Integer contadorPublicaciones=0;

    //listado de publicacion prueba
    ConexionSQLiteHelper conxDB;
    ListView mListView;
    List<Comentarios> mListComentarios=null;
    ListAdapter mAdapter;

    //manejo Fragmen
    ListadoPublicacionFragment listadoPublicacionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        correo=(TextView) findViewById(R.id.correoUserPerfilTV);
        nombre=(TextView) findViewById(R.id.nombreUserPerfil);
        usuario=(TextView) findViewById(R.id.usuarioUserPerfil);
        contadorPubli=(TextView) findViewById(R.id.contPubliTV);
        imgPerfil=(ImageView) findViewById(R.id.imgPerfilenPerfilIV);
        imgPortada=(ImageView) findViewById(R.id.portadaIV);
        //manejo de List View
        conxDB = new ConexionSQLiteHelper(getApplicationContext());
        //mListView=findViewById(R.id.listPublicacionesCom);
        consultarBasePublicaciones();



        ConexionSQLiteHelper conx=new ConexionSQLiteHelper(getApplicationContext());
        Usuario user=conx.obtenerDatosUserForId(parseInt(getIntent().getStringExtra("usuarioNro")));
        imgPerfil.setImageBitmap(user.getImg_Post());
        imgPortada.setImageResource(R.drawable.fondo_negro);

        correo.setText(user.getMail());
        usuario.setText("@"+user.getUsuario());
        nombre.setText(user.getNombre()+" "+user.getApellido());
        contadorPubli.setText(contadorPublicaciones.toString());


        try {
            //Manjeo de Fragment
            listadoPublicacionFragment = new ListadoPublicacionFragment();
            Bundle datosIdUsuarioPerfil = new Bundle();
            datosIdUsuarioPerfil.putInt("idUserPerfil", parseInt(getIntent().getStringExtra("usuarioNro")));
            listadoPublicacionFragment.setArguments(datosIdUsuarioPerfil);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView5, listadoPublicacionFragment).commit();
        }catch (Exception e){
            System.out.println("error en crear frament"+e);
        }


         /*  try {
            consultarBaseComentarios();
            if(!mListComentarios.isEmpty() && mListComentarios!=null){
                mAdapter=new ComentarioAdaptador(Perfil.this,R.layout.fila_comentario,mListComentarios);

                mListView.setAdapter(mAdapter);

            }
        }catch (Exception e){
            System.out.println("EL ERROPR  DEL ADAPTER DE COMENTARIOS ES :"+e);
        }*/

    }

    private void consultarBasePublicaciones() {

        SQLiteDatabase db=conxDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_PUBLICAIONES +" WHERE "+ Utilidades.CAMPO_USUARIOID+" = "+parseInt(getIntent().getStringExtra("usuarioNro"))+ " ORDER BY id DESC;", null);

        while(cursor.moveToNext()){
            contadorPublicaciones+=1;

        }
    }
}