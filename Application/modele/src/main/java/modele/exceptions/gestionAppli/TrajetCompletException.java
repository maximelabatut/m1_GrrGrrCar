package modele.exceptions.gestionAppli;

/**
 * The type Deja connecte exception.
 */
public class TrajetCompletException extends Exception {


    /**
     * Instantiates a new Reservation introuvable.
     */
    public TrajetCompletException() {
        super("Trajet complet.");
    }

}
