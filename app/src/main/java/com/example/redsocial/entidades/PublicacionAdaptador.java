package com.example.redsocial.entidades;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.redsocial.ConexionSQLiteHelper;
import com.example.redsocial.Inicio;
import com.example.redsocial.Perfil;
import com.example.redsocial.PublcacionSeleccionada;
import com.example.redsocial.R;
import com.example.redsocial.utilidades.Utilidades;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.lang.Integer.parseInt;


public class PublicacionAdaptador extends ArrayAdapter<Publicacion> {
    public List<Publicacion> publicacionList;
    public Context context;
    public int resourseLayout;
    public ImageView imgPost;
    public CardView cardImagen;

    ConexionSQLiteHelper conxDB;
    Integer contadorPublicaciones=0;


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
        consultarBasePublicaciones(publicacion.getId());
        TextView contadorPubli=view.findViewById(R.id.cantComentPublic);
        contadorPubli.setText(contadorPublicaciones.toString());



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
        cardImagen=view.findViewById(R.id.cardImgPublicacion);

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
            cardImagen.setVisibility(View.GONE);
        }

        ImageView compartir=view.findViewById(R.id.IVshare);
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContenido(publicacion);
                }
        });


        //imgPost.setImageURI(Uri.fromFile(new File(publicacion.getImg_Post())));
        //System.out.println("////////////////////////////////LA IMAGEN ES DE EEEEEE "+publicacion.getImg_Post());
       //Uri uri=imagenfile.toURI();
       // imgPost.setImageURI(imagenfile.getAbsolutePath());

        return view;


    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void shareContenido(Publicacion datos){
        try {
            if (datos.getImg_Post()!=null){
                /*Bitmap bitmap= BitmapFactory.decodeFile(datos.getImg_Post());
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
                OutputStream out = null;
                File file=new File(path);
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path=file.getPath();
                Uri bmpUri = Uri.parse("file://"+path);*/
                Uri pictureUri = Uri.parse(datos.getImg_Post());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Usuario : "+datos.getUsuario_nombre()+"\n Comentario de la publicacion: "+datos.getComentario());
                shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(shareIntent, "Compartir publicacion con"));

            }else{
                Intent intentoObj=new Intent();
                intentoObj.setAction(Intent.ACTION_SEND);
                intentoObj.putExtra(Intent.EXTRA_TEXT,"Usuario : "+datos.getUsuario_nombre()+"\n Comentario de la publicacion: "+datos.getComentario());
                intentoObj.setType("text/plain");
                Intent shareInt=Intent.createChooser(intentoObj,null);
                context.startActivity(shareInt);
            }




           /* ************* INTENTO DE MANDAR IMAGEN ************
      //   datos.getImg_Post() = /storage/emulated/0/dcim/camera/img_20210502_013359.jpg
           Bitmap urlAbitmap = BitmapFactory.decodeFile(datos.getImg_Post());

            File file = new File(datos.getImg_Post(),"null");
            FileOutputStream fOut = new FileOutputStream(file);
            urlAbitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, datos.getImg_Post());
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpg");
            context.startActivity(Intent.createChooser(intent, "Share image via"));*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void consultarBasePublicaciones(Integer idPublicacion) {
        contadorPublicaciones=0;
        conxDB=new ConexionSQLiteHelper(getContext());
        SQLiteDatabase db=conxDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_COMENTARIOS +" WHERE "+ Utilidades.CAMPO_COMEN_PUBLIID+" = "+idPublicacion+ " ;", null);

        while(cursor.moveToNext()){
            contadorPublicaciones+=1;

        }
    }

}
