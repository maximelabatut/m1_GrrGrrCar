package gestionFacade;

import modele.Personne;
import modele.Trajet;
import modele.exceptions.authentification.DejaConnecteException;
import modele.exceptions.authentification.MotDePasseIncorrectException;
import modele.exceptions.authentification.PersonneIntrouvableException;
import modele.exceptions.authentification.PseudoDejaPrisException;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class ModifierTest {

    private GestionGrrGrrCar gestionFacade;
    private int idTrajet = 0;

    @Before
    public void before() throws PseudoDejaPrisException, PersonneIntrouvableException, MotDePasseIncorrectException, DejaConnecteException {
        gestionFacade = new GestionGrrGrrCarImpl();
        gestionFacade.inscription("bob","bob");
        gestionFacade.inscription("alice","alice");
        gestionFacade.inscription("charlie","charlie");
        gestionFacade.connexion("bob","bob");
        gestionFacade.connexion("alice","alice");
        gestionFacade.connexion("charlie","charlie");
        idTrajet = gestionFacade.creerTrajet("orleans","paris",new Date(),2,25f,"alice");

    }

    @Test
    public void modifierTrajetOK() throws Exception{
        Date date = new Date();
        gestionFacade.modifierTrajet(idTrajet,"montargis","lyon",date, 4, 15f);
        Trajet trajet = gestionFacade.getTrajetById(idTrajet);
        Assert.assertEquals("Ville depart bien modifiee", "montargis", trajet.getDepart());
        Assert.assertEquals("Ville arrivee bien modifiee", "lyon", trajet.getArrivee());
        Assert.assertEquals("Date bien modifiee", date, trajet.getDateDepart());
        Assert.assertEquals("Nombre de places bien modifie", 4, trajet.getNbPlaces(),0);
        Assert.assertEquals("Nombre de places bien modifie", 15f, trajet.getTarif(),0);
    }

    @Test
    public void modifierPersonneOK() throws Exception{
        Date date = new Date();
        Personne p = gestionFacade.getPersonneByPseudo("bob");
        gestionFacade.modifierPersonne(p.getId(),"dupont","thierry","40 rue blabla","lyon","69000","dupontthierry@gmail.com",date);
        Assert.assertEquals("Nom bien modifie", "dupont", p.getNom());
        Assert.assertEquals("prenom bien modifie", "thierry", p.getPrenom());
        Assert.assertEquals("prenom bien modifie", "thierry", p.getPrenom());
        Assert.assertEquals("prenom bien modifie", "lyon", p.getVille());
        Assert.assertEquals("prenom bien modifie", "69000", p.getCodePostal());
        Assert.assertEquals("prenom bien modifie", "dupontthierry@gmail.com", p.getEmail());
        Assert.assertEquals("prenom bien modifie", date, p.getDateNaiss());
    }
}
