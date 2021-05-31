package com.example.redsocial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity2 extends AppCompatActivity {


    public int MAX_VALUE = 50;
    CountDownTimer cTimer = null;
    LayoutInflater inflaterHistoria;
    View historiaInflate;
    ImageView imagenHistoria;
    ProgressBar progressBar;
    Long tiempoRestante;
    ConstraintLayout layoutFeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        layoutFeed = findViewById(R.id.layoutFeed);
        inflaterHistoria = getLayoutInflater();
        historiaInflate = inflaterHistoria.inflate(R.layout.activity_historia, layoutFeed, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    public void abrirHistoria(View view) {
        layoutFeed.addView(historiaInflate);
        tiempoRestante = Long.valueOf(1);
        iniciarTimer(5000, 100);
        progressBar = findViewById(R.id.progresoHistoria);
        progressBar.setMax(MAX_VALUE);
        imagenHistoria = findViewById(R.id.imagenHistoria);
        imagenHistoria.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(final View v, final MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cTimer.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        iniciarTimer(5000 - (tiempoRestante*100), 100);
                        break;
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void iniciarTimer(long total, long intervalo) {
        cTimer = new CountDownTimer(total, intervalo) {
            int progreso = Math.toIntExact(tiempoRestante);
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progreso);
                progreso += (1);
                tiempoRestante = Long.valueOf(progreso);
            }
            @Override
            public void onFinish() {
                progressBar.setProgress(MAX_VALUE);
                layoutFeed.removeView(historiaInflate);
            }
        }.start();
    }
}