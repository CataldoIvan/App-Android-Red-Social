package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.redsocial.utilidades.Utilidades;

public class MainActivity extends AppCompatActivity {

    EditText usuarioLog;
    Button iniciarS;
    Button registrarse;
    ConexionSQLiteHelper conxDB;
    ConexionSQLiteHelper conxDB_user;
    ImageView img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuarioLog=(EditText)findViewById(R.id.usuarioLogin);
        iniciarS=(Button)findViewById(R.id.iniSesion);
        registrarse=(Button)findViewById(R.id.registrarseBTN);
        img3=(ImageView)findViewById(R.id.imageView3);

        this.conxDB=new ConexionSQLiteHelper(this,"db_comentarios",null,1);
        this.conxDB_user=new ConexionSQLiteHelper(this,"db_usuarios",null,1);



        iniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConexionSQLiteHelper conxDB=new ConexionSQLiteHelper(getApplicationContext(),"db_comentarios",null,1);

                Intent intento=new Intent(MainActivity.this,Inicio.class);
                intento.putExtra("usu", usuarioLog.getText().toString());
                startActivity(intento);

            }
        });
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ConexionSQLiteHelper conxDB=new ConexionSQLiteHelper(getApplicationContext(),"db_comentarios",null,1);

                Intent intento=new Intent(getApplicationContext(),Registrarse.class);
                startActivity(intento);

            }
        });
    }
}