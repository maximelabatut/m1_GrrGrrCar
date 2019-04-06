package modele.reservation.types;

import modele.reservation.Reservation;

public class ReservationRefusee extends Reservation{

    private static final int ETAT = 2;

    public ReservationRefusee(String pseudoReservant) {
        super(pseudoReservant, ETAT);
    }
}
