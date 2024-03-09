package com.example.lingle;

import java.io.Serializable;

public class Stats implements Serializable {
    private int onetry = 0, twotries = 0, threetries = 0, fourtries = 0, fivetries = 0, sixtries = 0,
        totalmatches = 0, losses = 0;

    //constructor por defecto necesario para Firestore
    public Stats() {
        //DataSnapshot.getValue(User.class)
    }

    //constructor que se inicializará a 0 en el registro de un usuario
    public Stats(int onetry, int twotries, int threetries, int fourtries, int fivetries, int sixtries, int totalmatches, int losses) {
        this.onetry = onetry;
        this.twotries = twotries;
        this.threetries = threetries;
        this.fourtries = fourtries;
        this.fivetries = fivetries;
        this.sixtries = sixtries;
        this.totalmatches = totalmatches;
        this.losses = losses;
    }

    //getters
    public int getOnetry() {
        return onetry;
    }

    public int getTwotries() {
        return twotries;
    }

    public int getThreetries() {
        return threetries;
    }

    public int getFourtries() {
        return fourtries;
    }

    public int getFivetries() {
        return fivetries;
    }

    public int getSixtries() {
        return sixtries;
    }

    public int getTotalmatches() {
        return totalmatches;
    }

    public int getLosses() {
        return losses;
    }
}
