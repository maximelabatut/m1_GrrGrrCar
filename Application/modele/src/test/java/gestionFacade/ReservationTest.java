package gestionFacade;

import modele.Trajet;
import modele.exceptions.authentification.DejaConnecteException;
import modele.exceptions.authentification.MotDePasseIncorrectException;
import modele.exceptions.authentification.PersonneIntrouvableException;
import modele.exceptions.authentification.PseudoDejaPrisException;
import modele.exceptions.gestionAppli.*;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import modele.reservation.Reservation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ReservationTest {

    private GestionGrrGrrCar gestionFacade;

    @Before
    public void before() throws PseudoDejaPrisException, PersonneIntrouvableException, MotDePasseIncorrectException, DejaConnecteException {
        gestionFacade = new GestionGrrGrrCarImpl();
        gestionFacade.inscription("bob","bob");
        gestionFacade.inscription("alice","alice");
        gestionFacade.inscription("charlie","charlie");
        gestionFacade.connexion("bob","bob");
        gestionFacade.connexion("alice","alice");
        gestionFacade.connexion("charlie","charlie");
    }

    @Test
    public void nouvelleDemandeOK() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),2,25f,"bob");
        gestionFacade.nouvelleDemande(idTrajet,"alice");
        Assert.assertEquals("Reservation bien ajoutee.",1,gestionFacade.getTrajetById(idTrajet).getReservations().size());
    }

    @Test(expected = TrajetIntrouvableException.class)
    public void nouvelleDemandeKO1() throws Exception {
        gestionFacade.nouvelleDemande(1,"alice");
    }

    @Test(expected = TrajetCompletException.class)
    public void nouvelleDemandeKO2() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.nouvelleDemande(idTrajet,"bob");
        gestionFacade.validerDemande(idTrajet,"bob");
        gestionFacade.nouvelleDemande(idTrajet,"charlie");
    }

    @Test(expected = TrajetNonDemandableException.class)
    public void nouvelleDemandeKO3() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.nouvelleDemande(idTrajet,"alice");
    }

    @Test(expected = TrajetDejaDemandeException.class)
    public void nouvelleDemandeKO4() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.nouvelleDemande(idTrajet,"bob");
        gestionFacade.nouvelleDemande(idTrajet,"bob");
    }

    @Test
    public void validerDemandeOK() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.nouvelleDemande(idTrajet,"bob");
        gestionFacade.validerDemande(idTrajet,"bob");
        int etat = gestionFacade.getReservationByPseudo(idTrajet,"bob").getEtat();
        Assert.assertEquals("Demande bien validee.",1, etat);
    }

    @Test(expected = ReservationIntrouvableException.class)
    public void validerDemandeKO1() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.validerDemande(idTrajet,"bob");
    }

    @Test(expected = TrajetCompletException.class)
    public void validerDemandeKO4() throws Exception {
        int idTrajet =  gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.nouvelleDemande(idTrajet,"bob");
        gestionFacade.nouvelleDemande(idTrajet,"charlie");
        gestionFacade.validerDemande(idTrajet,"bob");
        gestionFacade.validerDemande(idTrajet,"charlie");
    }

    @Test
    public void refuserDemandeOK() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.nouvelleDemande(idTrajet,"bob");
        gestionFacade.refuserDemande(idTrajet,"bob");
        int etat = gestionFacade.getReservationByPseudo(idTrajet,"bob").getEtat();
        Assert.assertEquals("Demande bien refusee.",2, etat);
    }

    @Test(expected = ReservationIntrouvableException.class)
    public void refuserDemandeKO1() throws Exception {
        int idTrajet =  gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.refuserDemande(idTrajet,"bob");
    }

    @Test(expected = TrajetIntrouvableException.class)
    public void refuserDemandeKO2() throws Exception {
        gestionFacade.refuserDemande(100000,"bob");
    }

    @Test
    public void getReservationsByPseudoOK() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.nouvelleDemande(idTrajet, "bob");
        Assert.assertEquals("Reservation bien prise en compte", gestionFacade.getReservationsByPseudo("bob"),gestionFacade.getTrajetById(idTrajet).getReservations());
    }

    @Test
    public void getReservationByIdOK() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),1,25f,"alice");
        gestionFacade.nouvelleDemande(idTrajet, "bob");
        Reservation reservation = gestionFacade.getReservationByPseudo(idTrajet,"bob");
        Assert.assertEquals("Reservation bien recuperee par son id", gestionFacade.getReservationById(reservation.getId()), reservation);
    }

    @Test(expected = ReservationIntrouvableException.class)
    public void getReservationByIdKO() throws Exception {
        gestionFacade.getReservationById(1000000);
    }

    @Test
    public void rechercheTrajetOK() throws Exception {
        Date date = new Date();
        int idTrajet1 =  gestionFacade.creerTrajet("orleans","paris",date,1,25f,"alice");
        int idTrajet2 =  gestionFacade.creerTrajet("marseille","paris",date,1,25f,"alice");
        int idTrajet3 =  gestionFacade.creerTrajet("lyon","paris",date,1,25f,"alice");
        int idTrajet4 =  gestionFacade.creerTrajet("orleans","paris",new Date(1000),1,25f,"alice");
        int idTrajet5 =  gestionFacade.creerTrajet("orleans","lille",date,1,25f,"alice");

        Collection<Trajet> trajets = gestionFacade.rechercheTrajet("orleans","paris",date);
        Collection<Trajet> trajets1 = new ArrayList<>();
        trajets1.add(gestionFacade.getTrajetById(idTrajet1));
        trajets1.add(gestionFacade.getTrajetById(idTrajet4));
        trajets1.add(gestionFacade.getTrajetById(idTrajet2));
        trajets1.add(gestionFacade.getTrajetById(idTrajet3));

        Assert.assertEquals("Trajets bien recuperes", trajets1, trajets);
    }
}
