package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    EditText usuarioLog;
    EditText contraseniaLog;
    Button iniciarS;
    Button registrarse;
    ConexionSQLiteHelper conxDB;
    ConexionSQLiteHelper conxDB_user;
    Button fragmentbtn;
    CheckBox mantenerSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentbtn=findViewById(R.id.irFragment);
        usuarioLog=(EditText)findViewById(R.id.usuarioLogin);
        contraseniaLog=(EditText)findViewById(R.id.contraseniaLogin);
        iniciarS=(Button)findViewById(R.id.iniSesion);
        registrarse=(Button)findViewById(R.id.registrarseBTN);
        findViewById(R.id.errorPass).setVisibility(View.GONE);
        findViewById(R.id.errorPass2).setVisibility(View.GONE);
        mantenerSesion= (CheckBox) findViewById(R.id.guardarDatosChBox);

        this.conxDB=new ConexionSQLiteHelper(this);
        validarUsuarioxPreferencias();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO}, 0);
        }

        iniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();

            }
        });
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento=new Intent(getApplicationContext(),Registrarse.class);
                startActivity(intento);
            }
        });
        fragmentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intento=new Intent(getApplicationContext(), PublicacionSeleccionadaScrolling.class);
                startActivity(intento);

            }
        });
    }



    private void iniciarSesion() {
        ConexionSQLiteHelper conxDB=new ConexionSQLiteHelper(getApplicationContext());
        Usuario user=conxDB.obtenerDatosUserLog(usuarioLog.getText().toString());

        try {
            if (user!=null){
                String value=contraseniaLog.getText().toString();

                if (user.getContrasenia().equals(value)){

                    if(mantenerSesion.isChecked()){
                        guardarPreferencias(user.getMail(),user.getContrasenia());
                    }

                    /*Toast.makeText(MainActivity.this, "se encontro el usuario: \n" +
                                    ""+user.getNombre()+"\n"+
                                    ""+user.getId()+"\n"+
                                    ""+user.getContrasenia()+"\n"+
                                    ""+user.getMail()+"\n"+
                            "Es chequeado esta :"+mantenerSesion.isChecked()+"\n"
                            , Toast.LENGTH_LONG).show();*/
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

    private void validarUsuarioxPreferencias() {
        usuarioLog=(EditText)findViewById(R.id.usuarioLogin);
        contraseniaLog=(EditText)findViewById(R.id.contraseniaLogin);
        SharedPreferences preferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String user=preferences.getString(Utilidades.CAMPO_USER_USUARIO,null);
        String pass=preferences.getString(Utilidades.CAMPO_USER_CONTRAS,null);
        if ( user!=null && pass!=null ){
            usuarioLog.setText(user);
            contraseniaLog.setText(pass);
            iniciarSesion();
        }



    }

    private void guardarPreferencias(String usuario,String contra) {
        SharedPreferences preferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(Utilidades.CAMPO_USER_USUARIO,usuario);
        editor.putString(Utilidades.CAMPO_USER_CONTRAS,contra);
        editor.commit();

        Toast.makeText(MainActivity.this, "se encontro el usuario: \n" +
                                    "Usuario :"+usuario+"\n"+
                                    "contrasenia: "+contra+"\n"+

                            "Es chequeado esta :"+mantenerSesion.isChecked()+"\n"
                            , Toast.LENGTH_LONG).show();

    }
}