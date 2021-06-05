package com.example.redsocial;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.redsocial.entidades.AdaptadorComentarioFrag;
import com.example.redsocial.entidades.Comentarios;
import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;


public class ListaComentarioskFragment extends Fragment {

    Integer idComentSeleccionado=null;
    ArrayList<Comentarios> listaComentarios;
    RecyclerView recycleComentario;
    ConexionSQLiteHelper conxDB;


    public ListaComentarioskFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idComentSeleccionado=null;
        if (getArguments() != null) {
            idComentSeleccionado=getArguments().getInt("idComentarioPSelect");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        conxDB = new ConexionSQLiteHelper(getContext());
        View vista=inflater.inflate(R.layout.fragment_lista_comentariosk, container, false);
        listaComentarios=new ArrayList<>();
        recycleComentario=vista.findViewById(R.id.recyclerId);
        recycleComentario.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarLista();

        AdaptadorComentarioFrag adapter=new AdaptadorComentarioFrag(listaComentarios);
        recycleComentario.setAdapter(adapter);


        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), " es el comemntaior", Toast.LENGTH_LONG).show();
            }
        });

        return vista;
    }

    private void llenarLista() {


        SQLiteDatabase db = conxDB.getReadableDatabase();
        Comentarios comentObj = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_COMENTARIOS +" WHERE "+ Utilidades.CAMPO_COMEN_PUBLIID+" = "+idComentSeleccionado+ " ORDER BY id DESC;", null);
        while (cursor.moveToNext()) {
            comentObj = new Comentarios();
            comentObj.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_COMEN_ID)));
            comentObj.setComentario(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_COMENTARIO)));
            comentObj.setUser_id(cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_COMEN_USERID)));
            comentObj.setPublicacion_id(cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_COMEN_PUBLIID)));
            comentObj.setFecha(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_COMEN_FECHA)));

            ConexionSQLiteHelper conxObj=new ConexionSQLiteHelper(getContext());
            Usuario user=conxObj.obtenerDatosUserForId(comentObj.getUser_id());
            comentObj.setUsuario_nombre(user.getNombre()+" "+user.getApellido());
            comentObj.setUsuario_img_perfil(user.getImg_Post());

            // System.out.println("comentariooooooo ///////////**********" + comentObj.getComentario());
            listaComentarios.add(comentObj);

            }
        }





}