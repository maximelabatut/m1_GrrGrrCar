package modele.exceptions.gestionAppli;

/**
 * The type Deja connecte exception.
 */
public class TrajetNonDemandableException extends Exception {


    /**
     * Instantiates a new Reservation introuvable.
     */
    public TrajetNonDemandableException() {
        super("Trajet non demandable.");
    }

}
