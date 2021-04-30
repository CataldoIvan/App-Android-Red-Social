package com.example.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Perfil extends AppCompatActivity {

    ImageView imgPerfil;
    TextView hola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        hola=(TextView) findViewById(R.id.textView4);

        hola.setText("Hola usuario>"+getIntent().getExtras().toString());
    }
}