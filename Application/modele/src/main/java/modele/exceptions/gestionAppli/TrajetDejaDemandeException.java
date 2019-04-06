package modele.exceptions.gestionAppli;

/**
 * The type Deja connecte exception.
 */
public class TrajetDejaDemandeException extends Exception {


    /**
     * Instantiates a new Reservation introuvable.
     */
    public TrajetDejaDemandeException() {
        super("Trajet deja demande.");
    }

}
