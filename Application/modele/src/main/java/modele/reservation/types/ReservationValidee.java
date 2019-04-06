package modele.reservation.types;

import modele.reservation.Reservation;

public class ReservationValidee extends Reservation{

    private static final int ETAT = 1;

    public ReservationValidee(String pseudoReservant) {
        super(pseudoReservant, ETAT);
    }
}
