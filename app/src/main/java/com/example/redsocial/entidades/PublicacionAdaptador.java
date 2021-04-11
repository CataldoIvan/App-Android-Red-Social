package com.example.redsocial.entidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redsocial.R;

import java.io.File;
import java.util.List;

public class PublicacionAdaptador extends ArrayAdapter<Publicacion> {
    public List<Publicacion> publicacionList;
    public Context context;
    public int resourseLayout;
    public ImageView imgPost;


    public PublicacionAdaptador(@NonNull Context context, int resource, List<Publicacion> objects) {
        super(context, resource, objects);
        this.publicacionList=objects;
        this.context=context;
        this.resourseLayout=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_view_comentarios,null);

        Publicacion publicacion=publicacionList.get(position);

        ImageView imagen=view.findViewById(R.id.imagenUsuaioIV);
        imagen.setImageResource(R.drawable.jeremy_full);

        TextView txtUser=view.findViewById(R.id.nomUsuarioTV);
        txtUser.setText(publicacion.getId().toString()+" | "+publicacion.getUsuario_id());


        TextView txtcComent=view.findViewById(R.id.comentUserTV);
        txtcComent.setText(publicacion.getComentario());


        imgPost=view.findViewById(R.id.postImageV);

        if (publicacion.getImg_Post()!=null){
            imgPost.setImageBitmap(publicacion.getImg_Post());
            imgPost.setVisibility(View.VISIBLE);


            /* VIeja forma de cargar mostrar las fotos

            File imagenfile=new File(publicacion.getImg_Post());
             Bitmap bitmap = BitmapFactory.decodeFile(imagenfile.getAbsolutePath());
            // Bitmap bitmap = BitmapFactory.decodeFile(publicacion.getImg_Post());
            //Uri myUri = Uri.parse(publicacion.getImg_Post());
            imgPost.setImageBitmap(bitmap);
            imgPost.setVisibility(View.VISIBLE);*/
        }else{
            imgPost.setImageBitmap(null);
            imgPost.setVisibility(View.GONE);
        }


        //imgPost.setImageURI(Uri.fromFile(new File(publicacion.getImg_Post())));
        System.out.println("////////////////////////////////LA IMAGEN ES DE EEEEEE "+publicacion.getImg_Post());
       //Uri uri=imagenfile.toURI();
       // imgPost.setImageURI(imagenfile.getAbsolutePath());




        return view;
    }
}
