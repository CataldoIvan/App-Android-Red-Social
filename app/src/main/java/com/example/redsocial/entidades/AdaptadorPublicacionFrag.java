package com.example.redsocial.entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redsocial.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.redsocial.entidades.PublicacionAdaptador.rotateImage;

public class AdaptadorPublicacionFrag extends
        RecyclerView.Adapter<AdaptadorPublicacionFrag.PublicacionViewHolder> implements
        View.OnClickListener {

    ArrayList<Publicacion> listaPublicaciones;
    private View.OnClickListener listener;

    public AdaptadorPublicacionFrag(ArrayList<Publicacion> listaPublicaciones) {
        this.listaPublicaciones = listaPublicaciones;
    }

    public AdaptadorPublicacionFrag() {

    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @NotNull
    @Override
    public PublicacionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vistaAdevolver= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_comentarios,null,false);
        vistaAdevolver.setOnClickListener((this));
        return new PublicacionViewHolder(vistaAdevolver);
    }
    TextView nombreUser,textoPublicacion;
    ImageView imgPerfil,imgPublicacion;
    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptadorPublicacionFrag.PublicacionViewHolder holder, int position) {
        holder.nombreUser.setText(listaPublicaciones.get(position).getUsuario_nombre());
        holder.textoPublicacion.setText(listaPublicaciones.get(position).getComentario());


        if (listaPublicaciones.get(position).getUsuario_img_perfil()!=null){
            holder.imgPerfil.setImageBitmap(listaPublicaciones.get(position).getUsuario_img_perfil());
        }else {
            holder.imgPerfil.setImageResource(R.drawable.jeremy_full);
        }

        if (listaPublicaciones.get(position).getImg_Post() !=null) {
            File imagenfile = new File(listaPublicaciones.get(position).getImg_Post());
            Bitmap bitmap = BitmapFactory.decodeFile(imagenfile.getAbsolutePath());
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


            holder.imgPublicacion.setImageBitmap(rotatedBitmap);
            holder.imgPublicacion.setVisibility(View.VISIBLE);

        }else{
            holder.imgPublicacion.setImageBitmap(null);
            holder.imgPublicacion.setVisibility(View.GONE);
            holder.cardImagen.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    public class PublicacionViewHolder extends RecyclerView.ViewHolder {
        TextView nombreUser,textoPublicacion;
        ImageView imgPerfil,imgPublicacion;
        CardView cardImagen;
        public PublicacionViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nombreUser=(TextView)itemView.findViewById(R.id.publSelNomUser);
            textoPublicacion=(TextView)itemView.findViewById(R.id.publSelPublicacion);
            imgPerfil=(ImageView)itemView.findViewById(R.id.publSelFotoPerfil);
            imgPublicacion=(ImageView)itemView.findViewById(R.id.publSelFoto);
            cardImagen=(CardView) itemView.findViewById(R.id.cardImgPublicacion);
        }
    }


}
