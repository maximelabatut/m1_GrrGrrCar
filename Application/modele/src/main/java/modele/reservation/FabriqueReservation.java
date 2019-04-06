package modele.reservation;

import modele.reservation.types.ReservationAttente;
import modele.reservation.types.ReservationRefusee;
import modele.reservation.types.ReservationValidee;

public class FabriqueReservation {

    /*
    Par convention pour le type :
    0 = ReservationAttente
    1 = ReservationValidee
    2 = ReservationRefusee
     */

    public Reservation creerReservation(String pseudoReservant,int type){
        switch (type){
            case 0 :
                return new ReservationAttente(pseudoReservant);
            case 1 :
                return new ReservationValidee(pseudoReservant);
            case 2 :
                return new ReservationRefusee(pseudoReservant);
            default:
                return null;
        }
    }
}
