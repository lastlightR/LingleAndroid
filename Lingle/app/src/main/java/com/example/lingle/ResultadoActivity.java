package com.example.lingle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ResultadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        Intent fromJugarActivity = getIntent(); //recibimos el Intent pasado por JugarActivity

        Button buttonSignificado = (Button) findViewById(R.id.buttonSignificado);
        buttonSignificado.append(fromJugarActivity.getStringExtra("correctWord_key")+" en Google"); //añadimos la palabra recibida del Intent
        Button buttonCompartir = (Button) findViewById(R.id.buttonCompartir);
        Button buttonVolver = (Button) findViewById(R.id.buttonVolverResultado);
        Button buttonNuevaPartida = (Button) findViewById(R.id.buttonNuevaPartidaResultado);

        TextView textViewPalabraCorrecta = (TextView) findViewById(R.id.textViewPalabraCorrecta);
        textViewPalabraCorrecta.setText(fromJugarActivity.getStringExtra("textView_key"));
        TextView textViewTuResultado2 = (TextView) findViewById(R.id.textViewTuResultado2);
        textViewTuResultado2.append(fromJugarActivity.getStringExtra("numTries_key") + "/6\n\n"
                + fromJugarActivity.getStringExtra("lingleResult_key")); //recibe el número de intentos y el conjunto de emojis como resultado
        TextView textViewResultado = (TextView) findViewById(R.id.textViewResultado);
        if (fromJugarActivity.getStringExtra("lossNotification_key").equals("¡Una pena!"))
            textViewResultado.setText("¡Una pena!");

        buttonSignificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //buscamos una url de búsqueda al pulsar el botón
                Uri uriGoogle = Uri.parse("https://www.google.com/search?q="
                        + fromJugarActivity.getStringExtra("wordSearch_key"));
                Intent uriSearch = new Intent(Intent.ACTION_VIEW, uriGoogle);
                startActivity(uriSearch);
            }
        });
        //este botón copia el resultado en el portapapeles
        buttonCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager portapapeles = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData copiado = ClipData.newPlainText("Resultado copiado", textViewTuResultado2.getText());
                portapapeles.setPrimaryClip(copiado);
                Toast.makeText(ResultadoActivity.this, "Copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }
        });
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //finaliza la actividad actual
            }
        });
        buttonNuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JugarActivity.jugarActivity.finish(); //finaliza la JugarActivity previa
                startActivity(new Intent(ResultadoActivity.this, JugarActivity.class));
                finish();
            }
        });
    }
}