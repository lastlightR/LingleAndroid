package com.example.lingle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    //para recibir datos de Firebase
    FirebaseUser user;
    DocumentReference reference;
    FirebaseFirestore db;
    //ArrayList<ModeloStats> modeloStats; //ArrayList para las filas del RecyclerView
    TextView textViewUsername;
    TextView textViewNumPartidas;
    TextView textViewWinrate;
    String username;
    String uid;
    double numeroStat; //para recibir la cantidad de partidas por stat
    double totalpartidas;
    double totalDerrotas;
    double total1, total2, total3, total4, total5, total6;
    TextView tvTotal1, tvTotal2, tvTotal3, tvTotal4, tvTotal5, tvTotal6;
    //RecyclerView recyclerViewStats;
    //StatsAdaptador statsAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Button buttonVolver = (Button) findViewById(R.id.buttonVolverStats);
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //simplemente cerramos el activity
            }
        });

        textViewNumPartidas = (TextView) findViewById(R.id.textViewNumPartidas);
        textViewWinrate = (TextView) findViewById(R.id.textViewWinrate);

        db = FirebaseFirestore.getInstance();
        //modeloStats = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        Log.i("UID", uid);
        getUsername(uid);
        textViewUsername = (TextView) findViewById(R.id.textViewStatsDeUsername);
        getTotalPartidas(uid); getWinrate(uid); getStats(uid);

        tvTotal1 = findViewById(R.id.textViewTotal1); tvTotal2 = findViewById(R.id.textViewTotal2);
        tvTotal3 = findViewById(R.id.textViewTotal3); tvTotal4 = findViewById(R.id.textViewTotal4);
        tvTotal5 = findViewById(R.id.textViewTotal5); tvTotal6 = findViewById(R.id.textViewTotal6);

        //recyclerViewStats = (RecyclerView) findViewById(R.id.recyclerViewStats);

        //prepararModelosStats();
        //statsAdaptador = new StatsAdaptador(StatsActivity.this, modeloStats);
        //recyclerViewStats.setAdapter(statsAdaptador);
        //recyclerViewStats.setLayoutManager(new LinearLayoutManager(StatsActivity.this));
    }

    /**private void prepararModelosStats() {
        //array con los int de cada drawable
        int[] imagenesNumero = {R.drawable.number1, R.drawable.number2, R.drawable.number3,
            R.drawable.number4, R.drawable.number5, R.drawable.number6};
        double[] cantidadesStats = {total1, total2, total3, total4, total5, total6};

        //6 filas necesarias
        for (int i = 0; i < 6; i++) {
            modeloStats.add(new ModeloStats(imagenesNumero[i], cantidadesStats[i]));
        }
    }*/
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
                        textViewUsername.append(username);
                    } else {
                        //el documento no existe
                        Toast.makeText(StatsActivity.this, "El documento no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Firestore", "Error getting document", task.getException());
                }
            }
        });
    }
    //método para conseguir los stats y pasárselos al array de cantidadesStats
    private double getStats(String uid) {
        reference = db.collection("stats").document(uid); //conseguimos la referencia al documento de stats del usuario

        //recibimos el documento y realizamos operaciones sobre ello
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DecimalFormat df = new DecimalFormat("#");
                df.setRoundingMode(RoundingMode.CEILING);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //extraemos el valor del campo de la stat pasada por parámetro
                        tvTotal1.setText(String.valueOf(df.format(document.getDouble("onetry"))));
                        tvTotal2.setText(String.valueOf(df.format(document.getDouble("twotries"))));
                        tvTotal3.setText(String.valueOf(df.format(document.getDouble("threetries"))));
                        tvTotal4.setText(String.valueOf(df.format(document.getDouble("fourtries"))));
                        tvTotal5.setText(String.valueOf(df.format(document.getDouble("fivetries"))));
                        tvTotal6.setText(String.valueOf(df.format(document.getDouble("sixtries"))));
                    } else {
                        //el documento no existe
                        Toast.makeText(StatsActivity.this, "El documento no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Firestore", "Error getting document", task.getException());
                }
            }
        });
        return numeroStat;
    }

    private void getTotalPartidas(String uid) {
        reference = db.collection("stats").document(uid); //conseguimos la referencia al documento de stats del usuario

        //recibimos el documento y realizamos operaciones sobre ello
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DecimalFormat df = new DecimalFormat("#");
                    df.setRoundingMode(RoundingMode.CEILING);
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //extraemos el valor del campo de la stat pasada por parámetro
                        totalpartidas = document.getDouble("totalmatches");
                        textViewNumPartidas.setText(String.valueOf(df.format(totalpartidas)));
                        Log.i("numeroStat", totalpartidas+"");
                    } else {
                        //el documento no existe
                        Toast.makeText(StatsActivity.this, "El documento no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Firestore", "Error getting document", task.getException());
                }
            }
        });
    }

    private void getWinrate(String uid) {
        reference = db.collection("stats").document(uid); //conseguimos la referencia al documento de stats del usuario

        //recibimos el documento y realizamos operaciones sobre ello
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    df.setRoundingMode(RoundingMode.CEILING);
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //extraemos el valor del campo de la stat pasada por parámetro
                        totalpartidas = document.getDouble("totalmatches");
                        totalDerrotas = document.getDouble("losses");
                        textViewWinrate.setText(String.valueOf(df.format(((totalpartidas-totalDerrotas)/totalpartidas)*100)));
                        Log.i("numeroStat", totalDerrotas+"");
                    } else {
                        //el documento no existe
                        Toast.makeText(StatsActivity.this, "El documento no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Firestore", "Error getting document", task.getException());
                }
            }
        });
    }
}