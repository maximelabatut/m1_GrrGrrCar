package modele.exceptions.authentification;

/**
 * The type Deja connecte exception.
 */
public class DejaDeconnecteException extends Exception {


    /**
     * Instantiates a new Reservation introuvable.
     */
    public DejaDeconnecteException() {
        super("Personne deja deconnectee.");
    }

}
