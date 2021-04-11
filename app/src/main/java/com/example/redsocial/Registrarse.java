package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocial.utilidades.Utilidades;

public class Registrarse extends AppCompatActivity {
    TextView nombre;
    TextView apellido;
    TextView correo;
    TextView contrasenia;
    Button guardar;
    Button cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        nombre=findViewById(R.id.nombreTV);
        apellido=findViewById(R.id.apellidoTV);
        correo=findViewById(R.id.correoTV);
        contrasenia=findViewById(R.id.contraseniaTV);
        guardar=findViewById(R.id.guardarRegBTN);
        cancelar=findViewById(R.id.cancelarRegBTN);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNuevoUsuario();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void guardarNuevoUsuario() {
        ConexionSQLiteHelper conxDB_user=new ConexionSQLiteHelper(this,"db_comentarios",null,1);
        SQLiteDatabase db_user=conxDB_user.getWritableDatabase();
        ContentValues values_user=new ContentValues();
        values_user.put(Utilidades.CAMPO_USER_NOMBRE,nombre.getText().toString());
        values_user.put(Utilidades.CAMPO_USER_APELLIDO,apellido.getText().toString());
        values_user.put(Utilidades.CAMPO_USER_MAIL,correo.getText().toString());
        values_user.put(Utilidades.CAMPO_USER_CONTRAS,contrasenia.getText().toString());
        //values_user.put(Utilidades.CAMPO_COMENT_ID,"AUN NO SE COMO TRAELO");

        Long idresultante_user=db_user.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USER_ID,values_user);
        Toast.makeText(this, "USUARIO REGISTRADO :"+idresultante_user, Toast.LENGTH_LONG).show();
        finish();
    }
}