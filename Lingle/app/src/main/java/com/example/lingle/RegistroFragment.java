package com.example.lingle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //elementos del fragment
    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextRepitePassword;
    Button buttonCrearCuenta;

    FirebaseAuth fba;

    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        fba = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View registroView = inflater.inflate(R.layout.fragment_registro, container, false);

        //inicialización de cajas de texto y botones, así como su evento onClickListener
        editTextUsername = (EditText) registroView.findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) registroView.findViewById(R.id.editTextEmailRegistro);
        editTextPassword = (EditText) registroView.findViewById(R.id.editTextPasswordRegistro);
        editTextRepitePassword = (EditText) registroView.findViewById(R.id.editTextRepitePassword);
        buttonCrearCuenta = (Button) registroView.findViewById(R.id.buttonCrearCuenta);

        buttonCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String repitePassword = editTextRepitePassword.getText().toString();

                //comprobación de todas las posibles introducciones erróneas de registro
                if (username.length() < 3 || username.length() > 20) //no acepta menores que 3 o mayores que 20
                    Toast.makeText(getContext(),
                            "El nombre de usuario debe tener entre 3 y 20 caracteres", Toast.LENGTH_SHORT).show();
                else if (email.isEmpty())
                    Toast.makeText(getContext(),
                            "El e-mail no puede estar vacío", Toast.LENGTH_SHORT).show();
                else if (password.isEmpty())
                    Toast.makeText(getContext(),
                            "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
                else if (!password.equals(repitePassword))
                    Toast.makeText(getContext(),
                            "La contraseña no coincide en ambos campos", Toast.LENGTH_SHORT).show();
                else {
                    registrarNuevoUsuario(email, password, username); //envío de los valores de los campos para crear un nuevo usuario
                }
            }
        });

        return registroView;
    }

    //recibe el e-mail, la contraseña y el username introducido en el formulario
    //crea un nuevo campo en Firebase Auth y le asocia un documento con su username
    private void registrarNuevoUsuario(String email, String password, String username) {
        //registro con Firebase Auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //si los valores de correo y contraseña son correctos para Firebase
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();

                            //añadir un documento con el username asociado a la cuenta
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            //con el uid, crea un documento de la colección stats
                            db.collection("stats").document(uid)
                                    .set(new Stats(0,0,0,0,0,0,0,0), SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //si se añade el documento con éxito, se registra y se hace login
                                            Log.i("Documento de stats creado", "en el registro");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //error al añadir el documento
                                            Log.e("Error al crear doc de stats", "Error writing document", e);
                                        }
                                    });
                            //recibe el user ID y le asocia un documento con el username
                            db.collection("users").document(uid)
                                    .set(new User(username, email), SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //si se añade el documento con éxito, se registra y se hace login
                                            Toast.makeText(getContext(),
                                                    "¡Bienvenido/a a Lingle!", Toast.LENGTH_SHORT).show();
                                            //haciendo login y cambiando preferencias de usuario
                                            fba.signInWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) { //si es correcta
                                                                //proceso de edición de preferencias
                                                                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedPref.edit();
                                                                editor.putString("Preferencias_email", email);
                                                                editor.putString("Preferencias_password", password);
                                                                editor.putBoolean("Preferencias_logueado", true);
                                                                editor.apply();
                                                                getActivity().finish();
                                                                MainActivity.mainActivity.finish();
                                                                Intent intent = new Intent(getContext(), InicioJuegoActivity.class);
                                                                intent.putExtra("esInvitado", "usuarionormal"); //devolvemos que no es invitado
                                                                startActivity(intent);
                                                            } else { //si es incorrecta

                                                            }
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //error al añadir el documento
                                            Log.e("Firestore", "Error writing document", e);
                                            Toast.makeText(getContext(),
                                                    "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(),
                                    task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}