package modele.facade;

import modele.Personne;
import modele.Trajet;
import modele.exceptions.authentification.*;
import modele.exceptions.gestionAppli.*;
import modele.reservation.Reservation;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * The interface Gestion grr grr car.
 */
public interface GestionGrrGrrCar {

    // --------------------------------------------------
    //                    Getters
    // --------------------------------------------------

    /**
     * Gets les trajets.
     *
     * @return the les trajets
     */
    Collection<Trajet> getLesTrajets();

    /**
     * Gets personne by pseudo.
     *
     * @param pseudo the pseudo
     * @return the personne by pseudo
     * @throws PersonneIntrouvableException the personne introuvable exception
     */
    Personne getPersonneByPseudo(String pseudo) throws PersonneIntrouvableException;

    /**
     * Gets trajet by id.
     *
     * @param idTrajet the id trajet
     * @return the trajet by id
     * @throws TrajetIntrouvableException the trajet introuvable exception
     */
    Trajet getTrajetById(int idTrajet) throws TrajetIntrouvableException;

    /**
     * Gets reservation by id.
     *
     * @param idReservation the id reservation
     * @return the reservation by id
     * @throws ReservationIntrouvableException the reservation introuvable exception
     */
    Reservation getReservationById(int idReservation) throws ReservationIntrouvableException;

    /**
     * Gets reservation by pseudo.
     *
     * @param idTrajet the id trajet
     * @param pseudo   the pseudo
     * @return the reservation by pseudo
     * @throws ReservationIntrouvableException the reservation introuvable exception
     */
    Reservation getReservationByPseudo(int idTrajet, String pseudo) throws ReservationIntrouvableException;

    /**
     * Modifier trajet.
     *
     * @param idTrajet   the id trajet
     * @param depart     the depart
     * @param arrivee    the arrivee
     * @param dateDepart the date depart
     * @param nbPlaces   the nb places
     * @param tarif      the tarif
     */
    void modifierTrajet(int idTrajet, String depart, String arrivee, Date dateDepart, int nbPlaces, float tarif);

    /**
     * Is connecte boolean.
     *
     * @param pseudo the pseudo
     * @return the boolean
     * @throws PersonneIntrouvableException the personne introuvable exception
     */
    Boolean isConnecte(String pseudo) throws PersonneIntrouvableException;

    /**
     * Gets population.
     *
     * @return the population
     */
    Collection<Personne> getPopulation();


    // --------------------------------------------------
    //                 Authentification
    // --------------------------------------------------

    /**
     * Inscription.
     *
     * @param pseudo   the pseudo
     * @param password the password
     * @throws PseudoDejaPrisException the pseudo deja pris exception
     */
    void inscription(String pseudo, String password) throws PseudoDejaPrisException;

    /**
     * Connexion.
     *
     * @param pseudo   the pseudo
     * @param password the password
     * @throws PersonneIntrouvableException the personne introuvable exception
     * @throws DejaConnecteException        the deja connecte exception
     * @throws MotDePasseIncorrectException the mot de passe incorrect exception
     */
    void connexion(String pseudo, String password) throws PersonneIntrouvableException, DejaConnecteException, MotDePasseIncorrectException;

    /**
     * Deconnexion.
     *
     * @param pseudo the pseudo
     * @throws PersonneIntrouvableException the personne introuvable exception
     * @throws DejaDeconnecteException      the deja deconnecte exception
     */
    void deconnexion(String pseudo) throws PersonneIntrouvableException, DejaDeconnecteException;


    // --------------------------------------------------
    //                 Reservation
    // --------------------------------------------------

    /**
     * Gets reservations by pseudo.
     *
     * @param pseudo the pseudo
     * @return the reservations by pseudo
     * @throws ReservationIntrouvableException the reservation introuvable exception
     */
    Collection<Reservation> getReservationsByPseudo(String pseudo) throws ReservationIntrouvableException;

    /**
     * Nouvelle demande.
     *
     * @param idTrajet the id trajet
     * @param pseudo   the pseudo
     * @throws TrajetIntrouvableException the trajet introuvable exception
     * @throws TrajetCompletException     the trajet complet exception
     * @throws TrajetDejaDemandeException the trajet deja demande exception
     */
    void nouvelleDemande(int idTrajet, String pseudo) throws TrajetIntrouvableException, TrajetCompletException, TrajetDejaDemandeException, TrajetNonDemandableException;

    /**
     * Valider demande.
     *
     * @param idTrajet the id trajet
     * @param pseudo   the pseudo
     * @throws ReservationIntrouvableException the reservation introuvable exception
     * @throws TrajetIntrouvableException      the trajet introuvable exception
     * @throws TrajetCompletException          the trajet complet exception
     */
    void validerDemande(int idTrajet, String pseudo) throws ReservationIntrouvableException, TrajetIntrouvableException, TrajetCompletException;

    /**
     * Refuser demande.
     *
     * @param idTrajet the id trajet
     * @param pseudo   the pseudo
     * @throws ReservationIntrouvableException the reservation introuvable exception
     * @throws TrajetIntrouvableException      the trajet introuvable exception
     */
    void refuserDemande(int idTrajet, String pseudo) throws ReservationIntrouvableException, TrajetIntrouvableException;


    // --------------------------------------------------
    //                   Notation
    // --------------------------------------------------

    /**
     * Passager noter trajet.
     *
     * @param idTrajet    the id trajet
     * @param pseudo      the pseudo
     * @param note        the note
     * @param commentaire the commentaire
     * @throws TrajetIntrouvableException   the trajet introuvable exception
     * @throws PasPassagerTrajetException   the pas passager trajet exception
     * @throws PersonneIntrouvableException the personne introuvable exception
     */
    void passagerNoterTrajet(int idTrajet, String pseudo, Integer note, String commentaire) throws TrajetIntrouvableException, PasPassagerTrajetException, PersonneIntrouvableException;

    /**
     * Passager noter trajet.
     *
     * @param idTrajet the id trajet
     * @param pseudo   the pseudo
     * @param note     the note
     * @throws TrajetIntrouvableException   the trajet introuvable exception
     * @throws PasPassagerTrajetException   the pas passager trajet exception
     * @throws PersonneIntrouvableException the personne introuvable exception
     */
    void passagerNoterTrajet(int idTrajet, String pseudo, Integer note) throws TrajetIntrouvableException, PasPassagerTrajetException, PersonneIntrouvableException;

    /**
     * Conducteur noter passager.
     *
     * @param idTrajet         the id trajet
     * @param pseudoConducteur the pseudo conducteur
     * @param pseudo           the pseudo
     * @param note             the note
     * @param commentaire      the commentaire
     * @throws TrajetIntrouvableException   the trajet introuvable exception
     * @throws PasPassagerTrajetException   the pas passager trajet exception
     * @throws PersonneIntrouvableException the personne introuvable exception
     */
    void conducteurNoterPassager(int idTrajet, String pseudoConducteur, String pseudo, Integer note, String commentaire) throws TrajetIntrouvableException, PasPassagerTrajetException, PersonneIntrouvableException;

    /**
     * Conducteur noter passager.
     *
     * @param idTrajet         the id trajet
     * @param pseudoConducteur the pseudo conducteur
     * @param pseudo           the pseudo
     * @param note             the note
     * @throws TrajetIntrouvableException   the trajet introuvable exception
     * @throws PasPassagerTrajetException   the pas passager trajet exception
     * @throws PersonneIntrouvableException the personne introuvable exception
     */
    void conducteurNoterPassager(int idTrajet, String pseudoConducteur, String pseudo, Integer note) throws TrajetIntrouvableException, PasPassagerTrajetException, PersonneIntrouvableException;


    // --------------------------------------------------
    //                    Autres
    // --------------------------------------------------

    /**
     * Valider fin trajet.
     *
     * @param idTrajet the id trajet
     * @param pseudo   the pseudo
     * @throws TrajetIntrouvableException         the trajet introuvable exception
     * @throws ConducteurValideFinTrajetException the conducteur valide fin trajet exception
     */
    void validerFinTrajet(int idTrajet, String pseudo) throws TrajetIntrouvableException, ConducteurValideFinTrajetException;


    /**
     * Creer trajet int.
     *
     * @param depart     the depart
     * @param arrivee    the arrivee
     * @param dateDepart the date depart
     * @param nbPlaces   the nb places
     * @param tarif      the tarif
     * @param pseudo     the pseudo
     * @return the int
     */
    int creerTrajet(String depart, String arrivee, Date dateDepart, int nbPlaces, float tarif, String pseudo);

    /**
     * Delete all trajets.
     */
    void deleteAllTrajets();

    /**
     * Modifier personne.
     *
     * @param idPersonne    the id personne
     * @param motDePasse    the mot de passe
     * @param newMotDePasse the new mot de passe
     * @param nom           the nom
     * @param prenom        the prenom
     * @param adresse       the adresse
     * @param ville         the ville
     * @param codePostal    the code postal
     * @param email         the email
     * @param dateNaiss     the date naiss
     */
    void modifierPersonne(int idPersonne, String nom, String prenom, String adresse, String ville, String codePostal, String email, Date dateNaiss);

    /**
     * Recherche trajet map.
     *
     * @param villeDepart  the ville depart
     * @param villeArrivee the ville arrivee
     * @param dateDepart   the date depart
     * @return the map
     */
    Collection<Trajet> rechercheTrajet(String villeDepart, String villeArrivee, Date dateDepart);
}
