package com.example.lingle;

import java.io.Serializable;

//clase para añadir documentos que enlacen los datos de nombre de usuario con su email de autenticación
public class User implements Serializable {
    private String username;
    private String email;

    //constructor por defecto necesario para Firestore
    public User() {
        //DataSnapshot.getValue(User.class)
    }

    //constructor con los dos valores
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    //getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
