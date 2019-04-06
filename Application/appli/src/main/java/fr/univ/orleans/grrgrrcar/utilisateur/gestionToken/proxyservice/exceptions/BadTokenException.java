package fr.univ.orleans.grrgrrcar.utilisateur.gestionToken.proxyservice.exceptions;

public class BadTokenException extends Exception {
    String uri;

    public BadTokenException(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
