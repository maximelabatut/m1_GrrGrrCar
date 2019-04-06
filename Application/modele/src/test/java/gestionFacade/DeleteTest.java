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

public class DeleteTest {

    private GestionGrrGrrCar gestionFacade;

    @Before
    public void before() throws PseudoDejaPrisException, PersonneIntrouvableException, MotDePasseIncorrectException, DejaConnecteException {
        gestionFacade = new GestionGrrGrrCarImpl();
    }

    @Test
    public void deleteAllTrajetsOK() {
        gestionFacade.creerTrajet("orleans","paris",new Date(),2,25f,"alice");
        gestionFacade.creerTrajet("orleans","paris",new Date(),1,125f,"charlie");
        gestionFacade.creerTrajet("orleans","paris",new Date(),3,15f,"alice");
        gestionFacade.deleteAllTrajets();
        Assert.assertTrue("Trajets bien supprimés.", gestionFacade.getLesTrajets().isEmpty());
    }
}
