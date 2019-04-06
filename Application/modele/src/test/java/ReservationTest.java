import modele.Personne;
import modele.Vote;
import modele.reservation.FabriqueReservation;
import modele.reservation.Reservation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReservationTest {

    private FabriqueReservation fabriqueReservation;

    @Before
    public void before(){
        fabriqueReservation = new FabriqueReservation();
    }

    @Test
    public void creationReservationAttente(){
        Reservation r = fabriqueReservation.creerReservation("bob",0);
        Assert.assertEquals("Reservation bien de type Attente",0,r.getEtat());
    }

    @Test
    public void creationReservationValidee(){
        Reservation r = fabriqueReservation.creerReservation("bob",1);
        Assert.assertEquals("Reservation bien de type Validee",1,r.getEtat());
    }

    @Test
    public void creationReservationRefusee(){
        Reservation r = fabriqueReservation.creerReservation("bob",2);
        Assert.assertEquals("Reservation bien de type Refusee",2,r.getEtat());
    }

    @Test
    public void creationReservationKO(){
        Reservation r = fabriqueReservation.creerReservation("bob",3);
        Assert.assertEquals("Reservation bien de type Refusee",null,r);
    }

}
