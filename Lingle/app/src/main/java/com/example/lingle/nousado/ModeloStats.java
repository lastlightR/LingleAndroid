package com.example.lingle.nousado;

public class ModeloStats {
    int imagenNumero; //imagen de 1 a 6 para los stats
    double cantidadStats; //número de partidas con ese número de intentos

    public ModeloStats(int imagenNumero, double cantidadStats) {
        this.imagenNumero = imagenNumero;
        this.cantidadStats = cantidadStats;
    }

    public int getImagenNumero() {
        return imagenNumero;
    }

    public double getCantidadStats() {
        return cantidadStats;
    }
}
