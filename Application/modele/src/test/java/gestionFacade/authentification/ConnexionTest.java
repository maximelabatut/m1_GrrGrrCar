package gestionFacade.authentification;

import modele.exceptions.authentification.DejaConnecteException;
import modele.exceptions.authentification.MotDePasseIncorrectException;
import modele.exceptions.authentification.PersonneIntrouvableException;
import modele.exceptions.authentification.PseudoDejaPrisException;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConnexionTest {

    private GestionGrrGrrCar gestionFacade;

    @Before
    public void before() throws PseudoDejaPrisException, PersonneIntrouvableException, MotDePasseIncorrectException, DejaConnecteException {
        gestionFacade = new GestionGrrGrrCarImpl();
    }

    @Test(expected = DejaConnecteException.class)
    public void testConnexionDejaConnecte() throws Exception {
        gestionFacade.inscription("bob","bob");
        gestionFacade.connexion("bob","bob");
        gestionFacade.connexion("bob","bob");
    }

    @Test(expected = MotDePasseIncorrectException.class)
    public void testConnexionMotDePasseIncorrect() throws Exception {
        gestionFacade.inscription("bob","bob");
        gestionFacade.connexion("bob","alice");
    }

    @Test
    public void testConnexionOK() throws Exception {
        gestionFacade.inscription("bob","bob");
        gestionFacade.connexion("bob","bob");
        Assert.assertTrue("Personne bien connectee.", gestionFacade.isConnecte("bob"));
    }
}
