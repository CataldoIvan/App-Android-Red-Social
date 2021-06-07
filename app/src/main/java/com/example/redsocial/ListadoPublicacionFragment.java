package com.example.redsocial;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.redsocial.entidades.AdaptadorComentarioFrag;
import com.example.redsocial.entidades.AdaptadorPublicacionFrag;
import com.example.redsocial.entidades.Comentarios;
import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListadoPublicacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListadoPublicacionFragment extends Fragment {
    Integer idUsuarioEnPerfil=null;
    ArrayList<Publicacion> mListPublicacion;
    RecyclerView recyclePublicaciones;
    ConexionSQLiteHelper conxDB;


    public ListadoPublicacionFragment() {
        // Required empty public constructor
    }

    /*// TODO: Rename and change types and number of parameters
    public static ListadoPublicacionFragment newInstance(Integer idUserPerf, String param2) {
        ListadoPublicacionFragment fragment = new ListadoPublicacionFragment();
        Bundle args = new Bundle();
        args.putInt("idUserPerfil", idUserPerf);

        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            idUsuarioEnPerfil=getArguments().getInt("idUserPerfil");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        conxDB = new ConexionSQLiteHelper(getContext());
        View vista=inflater.inflate(R.layout.fragment_lista_publicacion, container, false);
        mListPublicacion=new ArrayList<>();
        recyclePublicaciones=vista.findViewById(R.id.recyclerIdPublicaciones);
        recyclePublicaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarLista();
        AdaptadorPublicacionFrag adapter=new AdaptadorPublicacionFrag(mListPublicacion);
        recyclePublicaciones.setAdapter(adapter);

        return vista;
    }

    private void llenarLista() {


        SQLiteDatabase db = conxDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_PUBLICAIONES +" WHERE "+ Utilidades.CAMPO_USUARIOID+" = "+idUsuarioEnPerfil+ " ORDER BY id DESC;", null);
        Publicacion publicacion=null;
        mListPublicacion=new ArrayList<Publicacion>();
        while(cursor.moveToNext()){
            publicacion=new Publicacion();
            publicacion.setId(cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID)));
            publicacion.setComentario(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_PUBLICACION)));
            publicacion.setUsuario_id(cursor.getInt(2));
            // Forma Antigua de publicar la foto
            if (cursor.getString(3)!=null){
                publicacion.setImg_Post(cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_IMG_POST)));
            }else{
                publicacion.setImg_Post(null);
            }

            Usuario objUserPost=conxDB.obtenerDatosUserForId(cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_USUARIOID)));
            publicacion.setUsuario_nombre(objUserPost.getNombre());
            publicacion.setUsuario_img_perfil(objUserPost.getImg_Post());

            mListPublicacion.add(publicacion);

        }
    }
}