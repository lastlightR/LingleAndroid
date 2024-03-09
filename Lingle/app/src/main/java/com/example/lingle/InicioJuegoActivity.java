package com.example.lingle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lingle.databinding.ActivityInicioJuegoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

public class InicioJuegoActivity extends AppCompatActivity {
    FirebaseUser user;
    DocumentReference reference;
    FirebaseFirestore db;
    String username = ""; //recibirá el valor del nombre de usuario para mostrarlo en el Activity
    TextView textViewUsername;
    Button buttonJugar;
    Intent fromActivityAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_juego);
        fromActivityAnterior = getIntent();
        //solo se añade el nombre de usuario en pantalla si no se es invitado
        TextView textViewCerrarSesion = (TextView) findViewById(R.id.textViewCerrarSesion);
        textViewCerrarSesion.setVisibility(View.INVISIBLE);
        if (fromActivityAnterior.getStringExtra("esInvitado").equals("usuarionormal")) {
            db = FirebaseFirestore.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            Log.i("UID", uid);
            getUsername(uid);
            textViewUsername = (TextView) findViewById(R.id.textViewUsernameInicioJuego);
            textViewCerrarSesion.setVisibility(View.VISIBLE); //solo se ve para quien ha iniciado sesión
        }
        buttonJugar = (Button) findViewById(R.id.buttonJugarDesdeInicio);
        CheckBox checkBoxEspanol = (CheckBox) findViewById(R.id.checkBoxEspanol);
        CheckBox checkBoxIngles = (CheckBox) findViewById(R.id.checkBoxIngles);
        buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String esInvitado = fromActivityAnterior.getStringExtra("esInvitado");
                Intent haciaJugarActivity = new Intent(InicioJuegoActivity.this, JugarActivity.class);
                //si no hay nada marcado no inicia partida
                if (!checkBoxEspanol.isChecked() && !checkBoxIngles.isChecked()) {
                    Toast.makeText(InicioJuegoActivity.this, "Selecciona al menos un idioma", Toast.LENGTH_SHORT).show();
                }
                else if (checkBoxEspanol.isChecked() && checkBoxIngles.isChecked()) {
                    haciaJugarActivity.putExtra("modoJuego", "ambos");
                    haciaJugarActivity.putExtra("esInvitado", esInvitado);
                    startActivity(haciaJugarActivity);
                }
                else if (checkBoxEspanol.isChecked() && !checkBoxIngles.isChecked()) {
                    haciaJugarActivity.putExtra("modoJuego", "espanol");
                    haciaJugarActivity.putExtra("esInvitado", esInvitado);
                    startActivity(haciaJugarActivity);
                }
                else if (!checkBoxEspanol.isChecked() && checkBoxIngles.isChecked()) {
                    haciaJugarActivity.putExtra("modoJuego", "ingles");
                    haciaJugarActivity.putExtra("esInvitado", esInvitado);
                    startActivity(haciaJugarActivity);
                }
            }
        });
        TextView textViewReglas = (TextView) findViewById(R.id.textViewReglas);
        ImageView imageViewReglas = (ImageView) findViewById(R.id.imageViewReglasMain);
        textViewReglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioJuegoActivity.this, ReglasActivity.class));
            }
        });
        imageViewReglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioJuegoActivity.this, ReglasActivity.class));
            }
        });
        textViewCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //cuadro de dialógo con sí o no a cerrar sesión
                AlertDialog.Builder builder = new AlertDialog.Builder(InicioJuegoActivity.this);
                builder.setMessage("¿Seguro que quieres cerrar sesión?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut(); //cierra sesión de Firebase
                                //pone a false las preferencias
                                SharedPreferences sharedPref = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("Preferencias_logueado", false);
                                editor.apply();
                                //vuelve al MainActivity y cierra este
                                startActivity(new Intent(InicioJuegoActivity.this, MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //cierra el cuadro de diálogo
                                dialog.dismiss();
                            }
                        });
                //muestra el cuadro de diálogo con las opciones
                builder.create().show();
            }
        });
    }

    private void getUsername(String uid) {
        reference = db.collection("users").document(uid); //conseguimos la referencia al documento del usuario actual

        //recibimos el documento y realizamos operaciones sobre ello
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //extraemos el valor del campo username
                        username = document.getString("username");
                        Log.i("Username", username);
                        textViewUsername.setText("¡Bienvenido/a, " + username + "!");
                    } else {
                        //el documento no existe
                        Toast.makeText(InicioJuegoActivity.this, "El documento no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Firestore", "Error getting document", task.getException());
                }
            }
        });
    }

    //se llama a la notificación que recomienda buscar palabras en Google 5 minutos después de cerrar la aplicación
    @Override
    protected void onDestroy() {
        super.onDestroy();
        long cinco_minutos = 5 * 60 * 1000; //en milisegundos
        Intent notificacion = new Intent(this, RecibeNotificacion.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificacion,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + cinco_minutos, pendingIntent);
    }
}