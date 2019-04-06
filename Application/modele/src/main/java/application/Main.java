package application;

import modele.Trajet;
import modele.exceptions.authentification.*;
import modele.exceptions.gestionAppli.*;
import modele.facade.GestionGrrGrrCar;
import modele.facade.GestionGrrGrrCarImpl;

import java.util.Date;

public class Main {
    public static void main(String[] args) {

        GestionGrrGrrCar modele = new GestionGrrGrrCarImpl();
        try {
            // Inscription des utilisateurs
            modele.inscription("bob","bob");
            modele.inscription("alice","alice");
            modele.inscription("charlie","charlie");
            modele.inscription("daniel","daniel");

            // Connexion des utilisateurs
            modele.connexion("bob","bob");
            modele.connexion("alice","alice");
            modele.connexion("charlie","charlie");
            modele.connexion("daniel","daniel");

            // Alice peut completer son profil
            modele.modifierPersonne(modele.getPersonneByPseudo("alice").getId(),"alice","Dupont","45 rue des tours", "lyon", "69000","dupontalice@gmail.com", new Date());

            // Bob cree un trajet d'Orléans vers Paris de 2 places (bob non compris)
            int idTrajet = modele.creerTrajet("Orléans", "Paris", new Date(),2, 25, "bob");

            // Demandes de reservation des utilisateurs ( Demandes en attente )
            modele.nouvelleDemande(idTrajet, "alice");
            modele.nouvelleDemande(idTrajet,"charlie");
            modele.nouvelleDemande(idTrajet,"daniel");

            // Le fournisseur refuse la demande de Daniel ( Demande refusee )
            modele.refuserDemande(idTrajet, "daniel");

            // Le fournisseur valide les demandes d'Alice et de Charlie ( Demandes validees )
            modele.validerDemande(idTrajet,"alice");
            modele.validerDemande(idTrajet,"charlie");

            // Un des passagers du trajet le valide une fois termine
            modele.validerFinTrajet(idTrajet, "alice");

            // Les passagers peuvent voter pour le conducteur
            modele.passagerNoterTrajet(idTrajet, "alice", 4, "Tres grincheux comme je les aime !");
            modele.passagerNoterTrajet(idTrajet, "charlie", 1, "Pas assez grincheux a mon gout.");

            // Le conducteur peut voter pour les passagers
            modele.conducteurNoterPassager(idTrajet,"bob","alice",5, "Tres sympa");
            modele.conducteurNoterPassager(idTrajet,"bob","alice",2);

            // Deconnexion des utilisateurs
            modele.deconnexion("bob");
            modele.deconnexion("alice");
            modele.deconnexion("charlie");
            modele.deconnexion("daniel");

        } catch (TrajetIntrouvableException e) {
            e.printStackTrace();
        } catch (TrajetCompletException e) {
            e.printStackTrace();
        } catch (ReservationIntrouvableException e) {
            e.printStackTrace();
        } catch (PasPassagerTrajetException e) {
            e.printStackTrace();
        } catch (PseudoDejaPrisException e) {
            e.printStackTrace();
        } catch (MotDePasseIncorrectException e) {
            e.printStackTrace();
        } catch (DejaConnecteException e) {
            e.printStackTrace();
        } catch (PersonneIntrouvableException e) {
            e.printStackTrace();
        } catch (DejaDeconnecteException e) {
            e.printStackTrace();
        } catch (TrajetDejaDemandeException e) {
            e.printStackTrace();
        } catch (TrajetNonDemandableException e) {
            e.printStackTrace();
        } catch (ConducteurValideFinTrajetException e) {
            e.printStackTrace();
        }

    }
}
