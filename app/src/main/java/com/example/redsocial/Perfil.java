package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.redsocial.entidades.Publicacion;
import com.example.redsocial.entidades.Usuario;

import static java.lang.Integer.parseInt;

public class Perfil extends AppCompatActivity {

    ImageView imgPerfil,imgPortada;
    TextView correo,nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        correo=(TextView) findViewById(R.id.correoUserPerfilTV);
        nombre=(TextView) findViewById(R.id.nombreUserPerfil);
        imgPerfil=(ImageView) findViewById(R.id.imgPerfilenPerfilIV);
        imgPortada=(ImageView) findViewById(R.id.portadaIV);



        ConexionSQLiteHelper conx=new ConexionSQLiteHelper(getApplicationContext());
        Usuario user=conx.obtenerDatosUserForId(parseInt(getIntent().getStringExtra("usuarioNro")));
        imgPerfil.setImageBitmap(user.getImg_Post());
        imgPortada.setImageResource(R.drawable.jeremy_full);
        correo.setText(user.getMail());
        nombre.setText(user.getNombre()+" "+user.getApellido());

    }
}