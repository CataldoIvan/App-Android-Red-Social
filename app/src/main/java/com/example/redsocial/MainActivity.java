package com.example.redsocial;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.biometric.BiometricPrompt;

import com.example.redsocial.entidades.Usuario;
import com.example.redsocial.utilidades.Utilidades;

import java.util.concurrent.Executor;

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

    TextView errorUsuario;
    TextView errorContrasena;
    Boolean sinErrorUsu =false;
    Boolean sinErrorCont =false;

    //huelladigital
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utilidades.USER_LOGUEADO=0;

        fragmentbtn=findViewById(R.id.irFragment);
        usuarioLog=(EditText)findViewById(R.id.usuarioLogin);
        contraseniaLog=(EditText)findViewById(R.id.contraseniaLogin);
        iniciarS=(Button)findViewById(R.id.iniSesion);
        registrarse=(Button)findViewById(R.id.registrarseBTN);
        findViewById(R.id.errorContrasena).setVisibility(View.GONE);
        findViewById(R.id.errorUsuario).setVisibility(View.GONE);
        mantenerSesion= (CheckBox) findViewById(R.id.guardarDatosChBox);
        errorContrasena=(TextView)findViewById(R.id.errorContrasena);
        errorUsuario=(TextView)findViewById(R.id.errorUsuario);


        this.conxDB=new ConexionSQLiteHelper(this);


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

                iniciarSesion(usuarioLog.getText().toString(),contraseniaLog.getText().toString());

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

               Intent intento=new Intent(getApplicationContext(), Feed.class);
                startActivity(intento);

            }
        });

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                validarUsuarioxPreferencias();
                Toast.makeText(MainActivity.this, "El sistema no cuenta sistema de Huella"+errString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(MainActivity.this, "Huella confirmada con exito...", Toast.LENGTH_LONG).show();
                validarUsuarioxPreferencias();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "La huella no es la correcta!", Toast.LENGTH_LONG).show();
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Ingresa con tu huella digital")
                //.setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("CANCELAR")
                .build();

        biometricPrompt.authenticate(promptInfo);

    }



    private void iniciarSesion(String userIngres,String contraIngres) {
        restaurarErrores();
        validarCamposUsuario(userIngres);
        validarCamposContrasena(userIngres);



        try {
            if (sinErrorUsu && sinErrorCont){
                ConexionSQLiteHelper conxDB=new ConexionSQLiteHelper(getApplicationContext());
                Usuario user=conxDB.obtenerDatosUserLog(usuarioLog.getText().toString());
                String value=contraseniaLog.getText().toString();
                if (user.getUsuario()!=null){
                    if (user.getContrasenia().equals(value)){

                        if(mantenerSesion.isChecked()){
                            guardarPreferencias(user.getUsuario(),user.getContrasenia());
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
                        errorContrasena.setText("La contrasenia es incorrecta");
                        errorContrasena.setVisibility(View.VISIBLE);

                    }

                }   else{
                    errorUsuario.setText("El usuario no registrado");
                    errorUsuario.setVisibility(View.VISIBLE);
                }
            }else {
                findViewById(R.id.errorUsuario).setVisibility(View.VISIBLE);
            }
        }catch(Exception e){
            Toast.makeText(MainActivity.this, "aca esta haciednolo"+e, Toast.LENGTH_LONG).show();
            System.out.println("********** error de "+e);
            //findViewById(R.id.errorPass2).setVisibility(View.VISIBLE);
            findViewById(R.id.errorContrasena).setVisibility(View.VISIBLE);
        }

                /*Intent intento=new Intent(MainActivity.this,Inicio.class);
                intento.putExtra("usu", usuarioLog.getText().toString());
                startActivity(intento);*/

    }

    private void restaurarErrores() {
        sinErrorCont=false;
        sinErrorUsu=false;
        errorContrasena.setVisibility(View.INVISIBLE);
        errorUsuario.setVisibility(View.INVISIBLE);
    }

    private void validarCamposContrasena(String contrasena) {
        if ( !contrasena.equals("")){
            sinErrorCont =true;
        }else {
            errorContrasena.setText("El campo no púede esta vacio");
            errorContrasena.setVisibility(View.VISIBLE);
            sinErrorCont =false;
        }
    }

    private void validarCamposUsuario(String user) {
        if (!user.equals("")){
            sinErrorUsu =true;
        }else {
            errorUsuario.setText("El campo no púede esta vacio");
            errorUsuario.setVisibility(View.VISIBLE);
            sinErrorUsu =false;
        }

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
            iniciarSesion(usuarioLog.getText().toString(),contraseniaLog.getText().toString());
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