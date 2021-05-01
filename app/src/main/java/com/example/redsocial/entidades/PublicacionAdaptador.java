package com.example.redsocial.entidades;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redsocial.Inicio;
import com.example.redsocial.Perfil;
import com.example.redsocial.PublcacionSeleccionada;
import com.example.redsocial.R;
import com.example.redsocial.utilidades.Utilidades;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
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

        ImageView imagen=view.findViewById(R.id.publSelFotoPerfil);
        if (publicacion.getUsuario_img_perfil() !=null){
            imagen.setImageBitmap(publicacion.getUsuario_img_perfil());
        }else{
            imagen.setImageResource(R.drawable.jeremy_full);
        }
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(getContext(), Perfil.class);
                objIntent.putExtra("usuarioNro",publicacion.getUsuario_id());
                context.startActivity(objIntent);
            }
        });

        TextView txtUser=view.findViewById(R.id.publSelNomUser);
        txtUser.setText("id:"+publicacion.getId()+" | "+publicacion.getUsuario_nombre());
        txtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(getContext(), Perfil.class);
                objIntent.putExtra("usuarioNro",publicacion.getUsuario_id());
                context.startActivity(objIntent);
            }
        });
        TextView txtcComent=view.findViewById(R.id.publSelPublicacion);
        txtcComent.setText(publicacion.getComentario());

        imgPost=view.findViewById(R.id.publSelFoto);

        if (publicacion.getImg_Post()!=null){
           // VIeja forma de cargar mostrar las fotos
            File imagenfile=new File(publicacion.getImg_Post());
             Bitmap bitmap = BitmapFactory.decodeFile(imagenfile.getAbsolutePath());
            // Bitmap bitmap = BitmapFactory.decodeFile(publicacion.getImg_Post());
            //Uri myUri = Uri.parse(publicacion.getImg_Post());

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imagenfile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap rotatedBitmap = null;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }

            imgPost.setImageBitmap(rotatedBitmap);
            imgPost.setVisibility(View.VISIBLE);
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
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
