package com.example.redsocial.entidades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redsocial.ConexionSQLiteHelper;
import com.example.redsocial.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdaptadorComentarioFrag extends
        RecyclerView.Adapter<AdaptadorComentarioFrag.ComentarioViewHolder> implements
View.OnClickListener{


    ArrayList<Comentarios> listaComentario;
    private View.OnClickListener listener;

    public AdaptadorComentarioFrag(ArrayList<Comentarios> listaComentario) {
        this.listaComentario = listaComentario;
    }
    @NonNull
    @NotNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vistaAdevolver= LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_comentario,null,false);
        vistaAdevolver.setOnClickListener((this));
        return new ComentarioViewHolder(vistaAdevolver);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ComentarioViewHolder holder, int position) {

        holder.txtNombre.setText(listaComentario.get(position).getUsuario_nombre());
        holder.txtComentario.setText(listaComentario.get(position).getComentario());
        holder.IVimgUser.setImageBitmap(listaComentario.get(position).getUsuario_img_perfil());
    }

    @Override
    public int getItemCount() {
        return listaComentario.size();
    }



    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class  ComentarioViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre,txtComentario;
        ImageView IVimgUser;
        public ComentarioViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtNombre=(TextView)itemView.findViewById(R.id.textView5);
            txtComentario=(TextView)itemView.findViewById(R.id.TVcomentarioContenido);

             IVimgUser=(ImageView)itemView.findViewById(R.id.imgUsuComentarioIV);
        }
    }
}
