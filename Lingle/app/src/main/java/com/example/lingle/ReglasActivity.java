package com.example.lingle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReglasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglas);

        //declaración de TextViews a animar
        TextView green = (TextView) findViewById(R.id.textViewEj1_1);
        TextView yellow = (TextView) findViewById(R.id.textViewEj2_2);
        TextView black = (TextView) findViewById(R.id.textViewEj3_4);

        //animación para dar la vuelta a casillas decorativa
        Animation animationFlip = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_in); //animación inicial
        Animation animationFlipOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_out); //animación que revela la casilla de nuevo

        animationFlip.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                green.setBackgroundColor(ContextCompat.getColor(ReglasActivity.this, R.color.greenTile));
                yellow.setBackgroundColor(ContextCompat.getColor(ReglasActivity.this, R.color.yellowTile));
                black.setBackgroundColor(ContextCompat.getColor(ReglasActivity.this, R.color.blackTile));
                green.setTextColor(getResources().getColor(R.color.white));
                yellow.setTextColor(getResources().getColor(R.color.white));
                black.setTextColor(getResources().getColor(R.color.white));
                green.startAnimation(animationFlipOut);
                yellow.startAnimation(animationFlipOut);
                black.startAnimation(animationFlipOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        green.startAnimation(animationFlip); yellow.startAnimation(animationFlip);
        black.startAnimation(animationFlip);

        //botón para volver
        Button buttonVolver = (Button) findViewById(R.id.buttonVolverReglas);

        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //cierra la ReglasActivity y muestra de nuevo el menú o JugarActivity
            }
        });
    }
}