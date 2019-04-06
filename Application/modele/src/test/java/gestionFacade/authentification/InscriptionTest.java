package gestionFacade.authentification;

import modele.Personne;
import modele.exceptions.authentification.PseudoDejaPrisException;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InscriptionTest {

    private GestionGrrGrrCar gestionFacade;

    @Before
    public void before() {
        gestionFacade = new GestionGrrGrrCarImpl();
    }

    /**
     * Method: inscription(String pseudo, String password)
     *
     * @throws Exception the exception
     */
    @Test(expected = PseudoDejaPrisException.class)
    public void testInscriptionPseudoDejaPris() throws Exception {
        gestionFacade.inscription("bob","bob");
        gestionFacade.inscription("bob","bob");
    }

    /**
     * Test inscription ok.
     *
     * @throws Exception the exception
     */
    @Test
    public void testInscriptionOK() throws Exception {
        gestionFacade.inscription("bob","bob");
        Assert.assertTrue("Personne bien enregistree.", gestionFacade.getPopulation().contains(gestionFacade.getPersonneByPseudo("bob")));
    }
}
