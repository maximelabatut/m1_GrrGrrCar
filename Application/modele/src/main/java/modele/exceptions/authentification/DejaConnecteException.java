package modele.exceptions.authentification;

/**
 * The type Deja connecte exception.
 */
public class DejaConnecteException extends Exception {


    /**
     * Instantiates a new Reservation introuvable.
     */
    public DejaConnecteException() {
        super("Personne deja connectee.");
    }

}
