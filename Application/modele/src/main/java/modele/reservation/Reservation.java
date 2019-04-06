package modele.reservation;

import java.io.Serializable;

public abstract class Reservation implements Serializable {

    private int id;
    private String pseudoReservant;
    private int etat;

    private static int lastId = 0;

    public Reservation() {
        this.id = ++lastId;
    }

    public Reservation(String pseudoReservant, int etat) {
        this();
        this.pseudoReservant = pseudoReservant;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public String getPseudoReservant() {
        return pseudoReservant;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}
