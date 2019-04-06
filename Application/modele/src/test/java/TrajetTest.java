import modele.Personne;
import modele.Trajet;
import modele.Vote;
import modele.reservation.Reservation;
import modele.reservation.types.ReservationAttente;
import modele.reservation.types.ReservationRefusee;
import modele.reservation.types.ReservationValidee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class TrajetTest {

    private Trajet trajet;
    private Collection<Reservation> reservations;
    private Reservation r1;
    private Reservation r2;
    private Reservation r3;

    @Before
    public void before(){
        trajet = new Trajet("orleans","paris",new Date(),2,25f,"bob");
        Collection<Reservation> reservations = new ArrayList<>();
        r1 = new ReservationAttente("alice");
        r2 = new ReservationValidee("charlie");
        r3 = new ReservationRefusee("bob");
        reservations.add(r1);
        reservations.add(r2);
        reservations.add(r3);
        trajet.setReservations(reservations);
    }

    @Test
    public void getReservationsAttentesOK(){
        Assert.assertTrue("Reservations en attente bien récupérées.", trajet.getReservationsAttentes().contains(r1));
        Assert.assertFalse("Reservations validées non récupérées.", trajet.getReservationsAttentes().contains(r2));
        Assert.assertFalse("Reservations refusées non récupérées.", trajet.getReservationsAttentes().contains(r3));
    }

    @Test
    public void getReservationsValideesOK(){
        Assert.assertFalse("Reservations en attente non récupérées.", trajet.getReservationsValidees().contains(r1));
        Assert.assertTrue("Reservations validées bien récupérées.", trajet.getReservationsValidees().contains(r2));
        Assert.assertFalse("Reservations refusées non récupérées.", trajet.getReservationsValidees().contains(r3));
    }

    @Test
    public void getReservationsRefuseesOK(){
        Assert.assertFalse("Reservations en attente non récupérées.", trajet.getReservationsRefusees().contains(r1));
        Assert.assertFalse("Reservations validées bien récupérées.", trajet.getReservationsRefusees().contains(r2));
        Assert.assertTrue("Reservations refusées non récupérées.", trajet.getReservationsRefusees().contains(r3));
    }




}
