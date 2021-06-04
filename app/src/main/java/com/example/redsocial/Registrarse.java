package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocial.utilidades.Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.regex.Pattern;

public class Registrarse extends AppCompatActivity {
    TextView nombre;
    TextView apellido;
    TextView correo;
    TextView usuario;
    TextView contrasenia;
    Button guardar;
    Button cancelar;
    //Variables manejo de foto
    ImageView fotoPerfil;
    private Integer PICK_IMG_REQUEST=100;
    private Uri imgFilePath;
    private Bitmap imgToStorage;
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imagenInBytes;

    //variables de errores
    TextView errorNombre;
    TextView errorApellido;
    TextView errorCorreo;
    TextView errorContrasenia;
    TextView errorUsuario;
    Boolean ERROR=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        nombre=findViewById(R.id.nombreTV);
        apellido=findViewById(R.id.apellidoTV);
        correo=findViewById(R.id.correoTV);
        usuario=findViewById(R.id.userTV);
        contrasenia=findViewById(R.id.contraseniaTV);
        guardar=findViewById(R.id.guardarRegBTN);
        cancelar=findViewById(R.id.cancelarRegBTN);
        fotoPerfil=(ImageView)findViewById(R.id.imgPerfilIV);
        errorNombre=(TextView) findViewById(R.id.errorNomTV);
        errorApellido=(TextView) findViewById(R.id.errorApeTV);
        errorCorreo=(TextView) findViewById(R.id.errorCorreoTV);
        errorContrasenia=(TextView) findViewById(R.id.errorContraTV);
        errorUsuario=(TextView) findViewById(R.id.errorUserTV);



        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    limpiarCamposErrores();
                    guardarNuevoUsuario();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarImgPerfil();
            }
        });

    }

    private void agregarImgPerfil() {
        Intent intento= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intento.setType("image/");
        startActivityForResult(intento,PICK_IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
                if (requestCode== PICK_IMG_REQUEST && resultCode==RESULT_OK && data !=null &&
                        data.getData()!=null) {
                    imgFilePath = data.getData();
                    imgToStorage = MediaStore.Images.Media.getBitmap(getContentResolver(), imgFilePath);
                    fotoPerfil.setImageBitmap(imgToStorage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void limpiarCamposErrores(){
        errorNombre.setVisibility(View.INVISIBLE);
        errorApellido.setVisibility(View.INVISIBLE);
        errorCorreo.setVisibility(View.INVISIBLE);
        errorContrasenia.setVisibility(View.INVISIBLE);

    }

    private boolean validarNombre(String nombre) {

        if (!nombre.equals("") && !nombre.equals(" ")){
            return true;

        }else {

            errorNombre.setText("El campo no puede estar vacio");
            errorNombre.setVisibility(View.VISIBLE);
            return false;
        }

    }

    private boolean validarApellido(String apellido) {

        if (!apellido.equals("") && !apellido.equals(null)&& !apellido.equals(" ")){
            return true;

        }else {

            errorApellido.setText("El campo no puede estar vacio");
            errorApellido.setVisibility(View.VISIBLE);
            return false;
        }

    }
    private boolean validarUsuario(String user) {

        if (!user.equals("") && !user.equals(null)&& !user.equals(" ")){

            if (user.length()>3){
                ConexionSQLiteHelper conxDB_user=new ConexionSQLiteHelper(this);
                if (!conxDB_user.elUsuarioExiste(user)){
                    return true;
                }else {
                    errorUsuario.setText("El usuario ingresado ya existe");
                    errorUsuario.setVisibility(View.VISIBLE);
                    return false;
                }


                /*try {
                    ConexionSQLiteHelper conxDB_user=new ConexionSQLiteHelper(this);
                    conxDB_user.elUsuarioExiste(user);
                    SQLiteDatabase objSQLdb=conxDB_user.getReadableDatabase();
                    Cursor objCursor=objSQLdb.rawQuery("SELECT * FROM "+Utilidades.TABLA_USUARIOS+" WHERE "+Utilidades.CAMPO_USER_USUARIO+"="+user+"",null);
                    if(objCursor==null){
                        return true;
                    }else{
                        errorUsuario.setText("El usuario ingresado ya existe");
                        errorUsuario.setVisibility(View.VISIBLE);
                        return false;

                    }
                }catch (Exception e){
                    System.out.println("error al consultar es "+e);
                    errorUsuario.setText("El usuario ingresado ya existe");
                    errorUsuario.setVisibility(View.VISIBLE);
                    return false;
                }*/

            }else {
                errorUsuario.setText("El usuario debe contener al menos 4 digitos");
                errorUsuario.setVisibility(View.VISIBLE);
                return false;
            }

        }else {

            errorUsuario.setText("El campo no puede estar vacio");
            errorUsuario.setVisibility(View.VISIBLE);
            return false;
        }

    }





    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!email.equals("") && !email.equals(null)&& !email.equals(" ")){

            if (pattern.matcher(email).matches()){
                return true;


            }else{
                errorCorreo.setText("El campo debe contener @ y terminar en .com");
                errorCorreo.setVisibility(View.VISIBLE);
                return false;
            }

        }else {

            errorCorreo.setText("El campo no puede estar vacio");
            errorCorreo.setVisibility(View.VISIBLE);
            return false;
        }

    }

    public boolean validarContrasena(String contrasena) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$");
        if (!contrasena.equals("") && !contrasena.equals(null) && !contrasena.equals(null)){

            if (contrasena.matches(String.valueOf(pattern))){

                return true;

            }else{
                errorContrasenia.setText("Contrasenia debil, incompleta y/o Incorrecta.\n Debe contener al menos 6 caracteres con Mayus,Minusc y al menos un Numero");
                errorContrasenia.setVisibility(View.VISIBLE);
                return false;
            }

        }else {

            errorContrasenia.setText("El campo no puede estar vacio");
            errorContrasenia.setVisibility(View.VISIBLE);
            return false;
        }

    }


    private void guardarNuevoUsuario() {
        ERROR=validarNombre(nombre.getText().toString());
        ERROR=validarApellido(apellido.getText().toString());
        ERROR=validarEmail(correo.getText().toString());
        ERROR=validarContrasena(contrasenia.getText().toString());
        ERROR=validarUsuario(usuario.getText().toString());

        if (validarNombre(nombre.getText().toString()) &&
                validarApellido(apellido.getText().toString())&&validarEmail(correo.getText().toString()) &&
                validarContrasena(contrasenia.getText().toString())&&validarUsuario(usuario.getText().toString()))        {
            ConexionSQLiteHelper conxDB_user=new ConexionSQLiteHelper(this);
            SQLiteDatabase db_user=conxDB_user.getWritableDatabase();
            ContentValues values_user=new ContentValues();
            values_user.put(Utilidades.CAMPO_USER_NOMBRE,nombre.getText().toString());
            values_user.put(Utilidades.CAMPO_USER_APELLIDO,apellido.getText().toString());
            values_user.put(Utilidades.CAMPO_USER_USUARIO,usuario.getText().toString());
            values_user.put(Utilidades.CAMPO_CORREO_USUARIO,correo.getText().toString());
            values_user.put(Utilidades.CAMPO_USER_CONTRAS,contrasenia.getText().toString());


            //Nueva forma de almacenar imagenes
            try {
                if (imgToStorage!=null){
                    Bitmap imagenAAlmacenarBitmap=imgToStorage;
                    objectByteArrayOutputStream =new ByteArrayOutputStream();
                    imagenAAlmacenarBitmap.compress(Bitmap.CompressFormat.JPEG,PICK_IMG_REQUEST,objectByteArrayOutputStream);
                    imagenInBytes=objectByteArrayOutputStream.toByteArray();
                    values_user.put(Utilidades.CAMPO_IMG_PERFIL,imagenInBytes);
                }


            }catch(Exception e){
                Toast.makeText(this, "HUBO UN ERROR CON LA IMAGEN", Toast.LENGTH_LONG).show();
            }

            Long idresultante_user=db_user.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USER_ID,values_user);
            Toast.makeText(this, "USUARIO REGISTRADO :"+idresultante_user, Toast.LENGTH_LONG).show();
            finish();

        }

    }


}