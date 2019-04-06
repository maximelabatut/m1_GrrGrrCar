package gestionFacade;

import modele.Vote;
import modele.exceptions.authentification.DejaConnecteException;
import modele.exceptions.authentification.MotDePasseIncorrectException;
import modele.exceptions.authentification.PersonneIntrouvableException;
import modele.exceptions.authentification.PseudoDejaPrisException;
import modele.exceptions.gestionAppli.PasPassagerTrajetException;
import modele.exceptions.gestionAppli.TrajetIntrouvableException;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class NoterTrajetTest {

    private GestionGrrGrrCar gestionFacade;
    private int idTrajet;

    @Before
    public void before() throws PseudoDejaPrisException, PersonneIntrouvableException, MotDePasseIncorrectException, DejaConnecteException {
        gestionFacade = new GestionGrrGrrCarImpl();
        gestionFacade.inscription("bob","bob");
        gestionFacade.inscription("alice","alice");
        gestionFacade.connexion("bob","bob");
        gestionFacade.connexion("alice","alice");
        idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),4,10,"bob");
    }

    @Test
    public void passagerNoterTrajetSansCommentaireOK() throws Exception {
        gestionFacade.nouvelleDemande(idTrajet,"alice");
        gestionFacade.validerDemande(idTrajet,"alice");
        gestionFacade.passagerNoterTrajet(idTrajet,"alice",2);
        Vote v= (Vote)gestionFacade.getPersonneByPseudo("bob").getVotes().toArray()[0];
        Assert.assertEquals("le commentaire vide est bien ajouté.",null,v.getCommentaire());
    }

    @Test
    public void passagerNoterTrajetAvecCommentaireOK() throws Exception {
        gestionFacade.nouvelleDemande(idTrajet,"alice");
        gestionFacade.validerDemande(idTrajet,"alice");
        gestionFacade.passagerNoterTrajet(idTrajet,"alice",2,"blabla");
        Vote v= (Vote)gestionFacade.getPersonneByPseudo("bob").getVotes().toArray()[0];
        Assert.assertEquals("le commentaire est bien ajouté.","blabla",v.getCommentaire());
    }

    @Test(expected = TrajetIntrouvableException.class)
    public void passagerNoterTrajetAvecCommentaireKO1() throws Exception {
        gestionFacade.nouvelleDemande(idTrajet,"alice");
        gestionFacade.validerDemande(idTrajet,"alice");
        gestionFacade.passagerNoterTrajet(10000,"alice",2);
    }

    @Test(expected = PasPassagerTrajetException.class)
    public void passagerNoterTrajetAvecCommentaireKO2() throws Exception {
        gestionFacade.passagerNoterTrajet(idTrajet,"alice",2);
    }

    @Test
    public void conducteurNoterPassagerSansCommentaireOK() throws Exception{
        gestionFacade.nouvelleDemande(idTrajet,"alice");
        gestionFacade.validerDemande(idTrajet,"alice");
        gestionFacade.conducteurNoterPassager(idTrajet,"bob","alice",2);
        Vote v= (Vote)gestionFacade.getPersonneByPseudo("alice").getVotes().toArray()[0];
        Assert.assertEquals("le commentaire vide est bien ajouté.",null,v.getCommentaire());
    }

    @Test
    public void conducteurNoterTrajetAvecCommentaireOK() throws Exception {
        gestionFacade.nouvelleDemande(idTrajet,"alice");
        gestionFacade.validerDemande(idTrajet,"alice");
        gestionFacade.conducteurNoterPassager(idTrajet,"bob","alice",2,"blabla");
        Vote v= (Vote)gestionFacade.getPersonneByPseudo("alice").getVotes().toArray()[0];
        Assert.assertEquals("le commentaire est bien ajouté.","blabla",v.getCommentaire());
    }

    @Test(expected = PasPassagerTrajetException.class)
    public void conducteurNoterTrajetAvecCommentaireKO() throws Exception {
        gestionFacade.conducteurNoterPassager(idTrajet,"bob","alice",2);
    }
}
