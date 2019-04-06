package gestionFacade;

import modele.Personne;
import modele.Trajet;
import modele.exceptions.authentification.DejaConnecteException;
import modele.exceptions.authentification.MotDePasseIncorrectException;
import modele.exceptions.authentification.PersonneIntrouvableException;
import modele.exceptions.authentification.PseudoDejaPrisException;
import modele.exceptions.gestionAppli.ReservationIntrouvableException;
import modele.exceptions.gestionAppli.TrajetIntrouvableException;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class GetPersoTest {

    private GestionGrrGrrCar gestionFacade;
    private int idTrajet;

    @Before
    public void before() throws PseudoDejaPrisException, PersonneIntrouvableException, MotDePasseIncorrectException, DejaConnecteException {
        gestionFacade = new GestionGrrGrrCarImpl();
        gestionFacade.inscription("bob","bob");
        gestionFacade.inscription("alice","alice");
        gestionFacade.inscription("charlie","charlie");
        gestionFacade.connexion("bob","bob");
        gestionFacade.connexion("alice","alice");
        idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),4,10f,"bob");
}

    @Test
    public void getTrajetByIdOK() throws Exception {
        Collection<Trajet> t=gestionFacade.getLesTrajets();
        gestionFacade.getTrajetById(idTrajet);
        Assert.assertEquals("le trajet est bien trouver avec un id de 0",gestionFacade.getTrajetById(idTrajet),t.toArray()[0]);
    }

    @Test(expected = TrajetIntrouvableException.class)
    public void getTrajetByIdKO() throws TrajetIntrouvableException {
        gestionFacade.getTrajetById(10000);
    }

    @Test
    public void getReservationByIdOK() throws Exception {
        gestionFacade.nouvelleDemande(idTrajet,"alice");
        Assert.assertEquals("la reservation est faite et en attente",gestionFacade.getReservationByPseudo(idTrajet,"alice"),gestionFacade.getTrajetById(idTrajet).getReservations().toArray()[0]);
    }

    @Test(expected = ReservationIntrouvableException.class)
    public void getReservationByIdKO() throws ReservationIntrouvableException {
        gestionFacade.getReservationByPseudo(idTrajet,"charlie");
    }

    @Test
    public void isConnecteTestOK() throws Exception {
        Assert.assertTrue("Personne bien connectée.",gestionFacade.isConnecte("alice"));
        Assert.assertFalse("Personne bien deconnectée.",gestionFacade.isConnecte("charlie"));
    }

    @Test(expected = PersonneIntrouvableException.class)
    public void isConnecteTestKO() throws PersonneIntrouvableException {
        Assert.assertTrue("Personne bien connectée.",gestionFacade.isConnecte("didier"));
    }
}
