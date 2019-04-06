package modele.exceptions.authentification;

/**
 * The type Deja connecte exception.
 */
public class PseudoDejaPrisException extends Exception {


    /**
     * Instantiates a new Reservation introuvable.
     */
    public PseudoDejaPrisException() {
        super("Pseudo deja pris.");
    }

}
