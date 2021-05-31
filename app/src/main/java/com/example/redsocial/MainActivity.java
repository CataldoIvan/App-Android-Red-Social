package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    EditText usuarioLog;
    EditText contraseniaLog;
    Button iniciarS;
    Button registrarse;
    Button localizacion;
    ConexionSQLiteHelper conxDB;
    ConexionSQLiteHelper conxDB_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Utilidades.USER_LOGUEADO=0;
        usuarioLog=(EditText)findViewById(R.id.usuarioLogin);
        contraseniaLog=(EditText)findViewById(R.id.contraseniaLogin);
        iniciarS=(Button)findViewById(R.id.iniSesion);
        registrarse=(Button)findViewById(R.id.registrarseBTN);
        localizacion=(Button)findViewById(R.id.localizacionbtn);
        findViewById(R.id.errorPass).setVisibility(View.GONE);
       // findViewById(R.id.errorPass2localizaCION).setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO}, 0);
        }

        this.conxDB=new ConexionSQLiteHelper(this);
        this.conxDB_user=new ConexionSQLiteHelper(this);


        localizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento=new Intent(MainActivity.this,MainActivity2.class);

                startActivity(intento);
            }
        });

        iniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConexionSQLiteHelper conxDB=new ConexionSQLiteHelper(getApplicationContext());


                Usuario user=conxDB.obtenerDatosUserLog(usuarioLog.getText().toString());
                try {
                    if (user!=null){
                        String value=contraseniaLog.getText().toString();

                        if (user.getContrasenia().equals(value)){
                            Toast.makeText(MainActivity.this, "se encontro el usuario: \n" +
                                            ""+user.getNombre()+"\n"+
                                            ""+user.getId()+"\n"+
                                            ""+user.getContrasenia()+"\n"+
                                            ""+user.getMail()+"\n"
                                    , Toast.LENGTH_LONG).show();
                            Intent intento=new Intent(MainActivity.this,Inicio.class);
                            Utilidades.USER_LOGUEADO=user.getId();
                            startActivity(intento);
                        }else {
                            findViewById(R.id.errorPass).setVisibility(View.VISIBLE);
                        }
                    }else {
                        findViewById(R.id.errorPass2).setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_LONG).show();
                    //findViewById(R.id.errorPass2).setVisibility(View.VISIBLE);
                    findViewById(R.id.errorPass).setVisibility(View.VISIBLE);
                }

                /*Intent intento=new Intent(MainActivity.this,Inicio.class);
                intento.putExtra("usu", usuarioLog.getText().toString());
                startActivity(intento);*/

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