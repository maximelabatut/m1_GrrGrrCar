package modele.exceptions.authentification;

/**
 * The type Deja connecte exception.
 */
public class MotDePasseIncorrectException extends Exception {


    /**
     * Instantiates a new Reservation introuvable.
     */
    public MotDePasseIncorrectException() {
        super("Mot de passe incorrect.");
    }

}
