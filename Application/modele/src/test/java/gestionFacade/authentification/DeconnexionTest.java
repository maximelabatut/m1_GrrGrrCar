package gestionFacade.authentification;

import modele.exceptions.authentification.DejaDeconnecteException;
import modele.exceptions.authentification.PersonneIntrouvableException;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeconnexionTest {

    private GestionGrrGrrCar gestionFacade;

    @Before
    public void before() {
        gestionFacade = new GestionGrrGrrCarImpl();
    }

    /**
     * Method: deconnexion(String pseudo)
     *
     * @throws Exception the exception
     */
    @Test(expected = PersonneIntrouvableException.class)
    public void testDeconnexionPersonneIntrouvable() throws Exception {
        gestionFacade.deconnexion("bob");
    }

    /**
     * Test deconnexion deja connecte.
     *
     * @throws Exception the exception
     */
    @Test(expected = DejaDeconnecteException.class)
    public void testDeconnexionDejaConnecte() throws Exception {
        gestionFacade.inscription("bob","bob");
        gestionFacade.connexion("bob","bob");
        gestionFacade.deconnexion("bob");
        gestionFacade.deconnexion("bob");
    }

    /**
     * Test deconnexion ok.
     *
     * @throws Exception the exception
     */
    @Test
    public void testDeconnexionOK() throws Exception {
        gestionFacade.inscription("bob","bob");
        gestionFacade.connexion("bob","bob");
        gestionFacade.deconnexion("bob");
        Assert.assertFalse("Personne bien deconnectee.", gestionFacade.isConnecte("bob"));
    }
}
