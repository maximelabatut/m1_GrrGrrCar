import modele.Personne;
import modele.Vote;
import modele.exceptions.authentification.DejaConnecteException;
import modele.exceptions.authentification.MotDePasseIncorrectException;
import modele.exceptions.authentification.PersonneIntrouvableException;
import modele.exceptions.authentification.PseudoDejaPrisException;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PersonneTest {

    private Personne personne;

    @Before
    public void before(){
        personne = new Personne("bob","bob");
    }

    @Test
    public void addVoteTestOK(){
        Vote vote = new Vote("alice",4);
        personne.addVote(vote);
        Assert.assertTrue("Vote bien inséré.", personne.getVotes().contains(vote));
    }

    /*
    @Test
    public void getNoteMoyenneTestOK(){
        Vote vote1 = new Vote("alice",7);
        Vote vote2 = new Vote("charlie",2);
        Vote vote3 = new Vote("daniel",1);
        Vote vote4 = new Vote("emilie",1);
        personne.addVote(vote1);
        personne.addVote(vote2);
        personne.addVote(vote3);
        personne.addVote(vote4);
        Assert.assertEquals("Moyenne des votes correcte.", personne.calculNoteMoyenne(),2.75,0);
    }
    @Test
    public void getNoteMoyenneTest0(){
        Assert.assertEquals("Moyenne des votes correcte.", personne.calculNoteMoyenne(),0,0);
    }
    */

    @Test
    public void addRoleTestOK(){
        String role = "admin";
        personne.addRole(role);
        Assert.assertTrue("Vote bien inséré.", personne.getRoles().contains(role));
    }


}
