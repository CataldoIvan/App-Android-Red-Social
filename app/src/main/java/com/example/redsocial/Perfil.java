package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.Usuario;

import static java.lang.Integer.parseInt;

public class Perfil extends AppCompatActivity {

    ImageView imgPerfil;
    TextView hola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        hola=(TextView) findViewById(R.id.textView4);
        imgPerfil=(ImageView) findViewById(R.id.imageView3);

        hola.setText("Hola usuario>"+getIntent().getStringExtra("usuarioNro"));

        ConexionSQLiteHelper conx=new ConexionSQLiteHelper(getApplicationContext());
        Publicacion user=conx.obtenerDatosPublicacionPorId(parseInt(getIntent().getStringExtra("usuarioNro")));
        imgPerfil.setImageBitmap(user.getUsuario_img_perfil());
    }
}