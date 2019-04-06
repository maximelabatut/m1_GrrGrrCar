package modele.reservation.types;

import modele.reservation.Reservation;

public class ReservationAttente extends Reservation{

    private static final int ETAT = 0;

    public ReservationAttente(String pseudoReservant) {
        super(pseudoReservant, ETAT);
    }
}
