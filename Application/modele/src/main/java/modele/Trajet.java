package modele;

import modele.reservation.Reservation;

import java.io.Serializable;
import java.util.*;

public class Trajet implements Serializable {

    private int id;
    private String depart;
    private String arrivee;
    private Date dateDepart;
    private Integer nbPlaces;
    private Float tarif;
    private String createur;
    private Boolean isFini;
    private Collection<Reservation> reservations;

    private static int lastId = 0;

    public Trajet() {
        this.reservations = new ArrayList<>();
    }

    public Trajet(String depart, String arrivee, Date dateDepart, Integer nbPlaces, Float tarif, String createur){
        this(++lastId,depart,arrivee,dateDepart,nbPlaces,tarif,createur);
    }

    private Trajet(int id, String depart, String arrivee, Date dateDepart, Integer nbPlaces, Float tarif, String createur) {
        this.id = id;
        this.depart = depart;
        this.arrivee = arrivee;
        this.dateDepart = dateDepart;
        this.nbPlaces = nbPlaces;
        this.tarif = tarif;
        this.createur = createur;
        this.isFini = false;
        this.reservations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrivee() {
        return arrivee;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public Float getTarif() {
        return tarif;
    }

    public void setTarif(Float tarif) {
        this.tarif = tarif;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public Boolean getFini() {
        return isFini;
    }

    public void setFini(Boolean fini) {
        isFini = fini;
    }

    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Collection<Reservation> getReservationsAttentes() {
        Collection<Reservation> reserv = new ArrayList<>();
        for (Reservation r : reservations){
            if(r.getEtat() == 0){
                reserv.add(r);
            }
        }
        return reserv;
    }

    public Collection<Reservation> getReservationsValidees() {
        Collection<Reservation> reserv = new ArrayList<>();
        for (Reservation r : reservations){
            if(r.getEtat() == 1){
                reserv.add(r);
            }
        }
        return reserv;
    }

    public Collection<Reservation> getReservationsRefusees() {
        Collection<Reservation> reserv = new ArrayList<>();
        for (Reservation r : reservations){
            if(r.getEtat() == 2){
                reserv.add(r);
            }
        }
        return reserv;
    }

    @Override
    public String toString() {
        String res = "";
        res = res + ("Nombre de places maximal " + nbPlaces + "\n" +
                "Tarif " + tarif + "€ \n" +
                "Créateur du trajet " + createur + "\n" +
                "Trajet termine ? " + isFini + "\n");

       res += "Reservations en attente :"+"\n";
       for (Reservation r : this.getReservationsAttentes()){
           res = res + ("\t" + r.getPseudoReservant() + "\n");
       }

       res += "Reservations validees :"+"\n";
       for (Reservation r : this.getReservationsValidees()){
           res = res + ("\t" + r.getPseudoReservant() + "\n");
       }

       res += "Reservations refusees :"+"\n";
       for (Reservation r : this.getReservationsRefusees()){
           res = res + ("\t" + r.getPseudoReservant() + "\n");
       }
       return res;
    }
}