package gestionFacade;

import modele.Trajet;
import modele.exceptions.authentification.DejaConnecteException;
import modele.exceptions.authentification.MotDePasseIncorrectException;
import modele.exceptions.authentification.PersonneIntrouvableException;
import modele.exceptions.authentification.PseudoDejaPrisException;
import modele.exceptions.gestionAppli.ConducteurValideFinTrajetException;
import modele.exceptions.gestionAppli.TrajetIntrouvableException;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class ValiderFinTrajetTest {

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
    public void validerFintrajetOK() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),2,25f,"alice");

        //Nouvelle reservation
        gestionFacade.nouvelleDemande(idTrajet, "charlie");
        gestionFacade.nouvelleDemande(idTrajet, "bob");

        //ACceptation par le créateur
        gestionFacade.validerDemande(idTrajet, "charlie");
        gestionFacade.validerDemande(idTrajet, "bob");

        //Validation de la fin du trajet (ce ne sont pas les conducteurs)
        gestionFacade.validerFinTrajet(idTrajet, "charlie");

        //Test de la fin du trajet
        Trajet monTrajet = gestionFacade.getTrajetById(idTrajet);
        Boolean isFini = monTrajet.getFini();
        Assert.assertEquals("Le trajet est terminé", isFini, true);
    }

    @Test(expected = TrajetIntrouvableException.class)
    public void validerFintrajetTrajetIntrouvable() throws Exception {
        //Nouvelle reservation
        gestionFacade.nouvelleDemande(1, "charlie");
    }

    @Test(expected = ConducteurValideFinTrajetException.class)
    public void validerFintrajetConducteur() throws Exception {
        int idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),2,25f,"alice");

        //Nouvelle reservation
        gestionFacade.nouvelleDemande(idTrajet, "charlie");
        gestionFacade.nouvelleDemande(idTrajet, "bob");

        //ACceptation par le créateur
        gestionFacade.validerDemande(idTrajet, "charlie");
        gestionFacade.validerDemande(idTrajet, "bob");

        //Validation de la fin du trajet (ce ne sont pas les conducteurs)
        gestionFacade.validerFinTrajet(idTrajet, "alice");
    }

}
