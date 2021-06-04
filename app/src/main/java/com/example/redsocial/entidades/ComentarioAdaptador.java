 package com.example.redsocial.entidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redsocial.ConexionSQLiteHelper;
import com.example.redsocial.R;

import java.util.List;

public class ComentarioAdaptador extends ArrayAdapter<Comentarios> {
  public List<Comentarios> comentariosList;
    public Context context;
    public int resourseLayaout;
    //TextView TVcomentario;

    public ComentarioAdaptador(@NonNull Context context, int resource, @NonNull List<Comentarios> objects) {
        super(context, resource, objects);
        this.context=context;
        this.comentariosList= objects;
        this.resourseLayaout=resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.fila_comentario,null);
        Comentarios comentObj=comentariosList.get(position);
        TextView TVcomentario=view.findViewById(R.id.TVcomentarioContenido);
        TextView TVnombre=view.findViewById(R.id.textView5);
        ImageView IVimgUser=view.findViewById(R.id.imgUsuComentarioIV);
        TVcomentario.setText(comentObj.getComentario());

        TVnombre.setText(comentObj.getComentario());
        IVimgUser.setImageBitmap(comentObj.getUsuario_img_perfil());


        return view;
    }
}
