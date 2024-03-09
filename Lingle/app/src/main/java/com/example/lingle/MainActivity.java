package com.example.lingle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 * @author Robyn
 */
public class MainActivity extends AppCompatActivity {

    //elementos para inicialización de Firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    AlertDialog alertDialog; //dialog para el login

    //para finalizar la Activity desde otras
    public static Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializa Firestore DB y su autenticación
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mainActivity = this;

        //declaramos las preferencias en el inicio por si existen, para pasar directamente al activity de inicio de juego
        SharedPreferences sharedPref = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        boolean isLogin = sharedPref.getBoolean("Preferencias_logueado", false);

        Log.i("Firestore Email", sharedPref.getString("Preferencias_email", "DEFAULT"));

        //código a procesar si existen las preferencias de login
        if (isLogin) { //recibe las preferencias guardadas de email y contraseña, y no las pierde hasta que se cierre sesión
            firebaseAuth.signInWithEmailAndPassword(sharedPref.getString("Preferencias_email", "DEFAULT"), sharedPref.getString("Preferencias_password", "DEFAULT"))
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { //si es correcta
                                Toast.makeText(MainActivity.this, "¡Bienvenido/a a Lingle!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, InicioJuegoActivity.class);
                                intent.putExtra("esInvitado", "usuarionormal"); //devolvemos que no es invitado
                                startActivity(intent);
                                finish();

                            } else { //si es incorrecta
                                Toast.makeText(MainActivity.this, "El usuario o contraseña son incorrectos",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogLogin();
            }
        });
        Button buttonInvitado = (Button) findViewById(R.id.buttonInvitado);
        buttonInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InicioJuegoActivity.class);
                intent.putExtra("esInvitado", "invitado"); //devolvemos que es invitado
                startActivity(intent);
                //no hacemos finish() por si se quiere hacer login
            }
        });
        TextView textViewReglas = (TextView) findViewById(R.id.textViewReglas);
        textViewReglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReglasActivity.class));
            }
        });
        ImageView imageReglas = (ImageView) findViewById(R.id.imageViewReglasMain);
        imageReglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReglasActivity.class));
            }
        });
    }

    //método para mostrar el AlertDialog customizado para el inicio de sesión o registro
    public void mostrarDialogLogin() {
        //creación del AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //para inflar el layout con el XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(dialogView);

        //final EditText editTextUsername = (EditText) dialogView.findViewById(R.id.editTextUsername);
        final EditText editTextPassword = (EditText) dialogView.findViewById(R.id.editTextPassword);
        final EditText editTextEmail = (EditText) dialogView.findViewById(R.id.editTextEmail);
        Button btnLogin = (Button) dialogView.findViewById(R.id.btnLogin);
        Button btnVolver = (Button) dialogView.findViewById(R.id.btnVolver);
        Button btnRegistrarse = (Button) dialogView.findViewById(R.id.btnRegistrarse);

        alertDialog = builder.create();

        //OnClickListeners para los botones
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String email = editTextEmail.getText().toString();
                Log.i("Pepito", password);
                //comprobación de correo y contraseña
                if (email.isEmpty())
                Toast.makeText(MainActivity.this,
                        "El e-mail no puede estar vacío", Toast.LENGTH_SHORT).show();
                else if (password.isEmpty())
                    Toast.makeText(MainActivity.this,
                            "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
                else { //inicia la autenticación
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) { //si es correcta
                                        Toast.makeText(MainActivity.this, "¡Bienvenido/a a Lingle!",
                                                Toast.LENGTH_SHORT).show();
                                        //proceso de edición de preferencias
                                        SharedPreferences sharedPref = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("Preferencias_email", email);
                                        editor.putString("Preferencias_password", password);
                                        editor.putBoolean("Preferencias_logueado", true);
                                        editor.apply();
                                        //iniciamos el activity de inicio de juego
                                        Intent intent = new Intent(MainActivity.this, InicioJuegoActivity.class);
                                        intent.putExtra("esInvitado", "usuarionormal"); //devolvemos que no es invitado
                                        startActivity(intent);
                                        finish();

                                    } else { //si es incorrecta
                                        Toast.makeText(MainActivity.this, "El usuario o contraseña son incorrectos",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cierra el dialog
                alertDialog.dismiss();
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }
        });

        //muestra finalmente el alertDialog
        alertDialog.show();
    }
}