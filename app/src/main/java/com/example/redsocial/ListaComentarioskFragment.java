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
import android.view.View.OnScrollChangeListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.redsocial.entidades.AdaptadorComentarioFrag;
import com.example.redsocial.entidades.Comentarios;
import com.example.redsocial.utilidades.Utilidades;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaComentarioskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaComentarioskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<Comentarios> listaComentarios;
    RecyclerView recycleComentario;
    ConexionSQLiteHelper conxDB;

    public ListaComentarioskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaComentarioskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaComentarioskFragment newInstance(String param1, String param2) {
        ListaComentarioskFragment fragment = new ListaComentarioskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

        llenarListaPrueba();

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

    private void llenarListaPrueba() {
        SQLiteDatabase db = conxDB.getReadableDatabase();
        Comentarios comentObj = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_COMENTARIOS + " ORDER BY id DESC;", null);

        while (cursor.moveToNext()) {
            comentObj = new Comentarios();
            comentObj.setId(cursor.getInt(0));
            comentObj.setComentario(cursor.getString(1));
            comentObj.setUser_id(cursor.getInt(2));
            comentObj.setPublicacion_id(cursor.getInt(3));
            comentObj.setFecha(cursor.getString(4));

            System.out.println("comentariooooooo ///////////**********" + comentObj.getComentario());
            listaComentarios.add(comentObj);

        }
    }
}