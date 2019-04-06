package fr.univ.orleans.grrgrrcar.utilisateur.gestionToken;

import fr.univ.orleans.grrgrrcar.utilisateur.controller.GrrGrrCarController;

import java.util.Scanner;

public class DemandeToken implements View {

    GrrGrrCarController grrGrrCarController;

    public DemandeToken(GrrGrrCarController grrGrrCarController) {
        this.grrGrrCarController = grrGrrCarController;
    }

    public void afficherToken(String token) {
        System.out.println("---------------------------------------------");
        System.out.println("Token récupéré :");
        System.out.println(token);
        System.out.println("---------------------------------------------");
    }

    public void erreur() {
        System.out.println("---------------------------------------------");
        System.out.println("le couple login/password est incorrecte");
        System.out.println("---------------------------------------------");
    }

    public void erreurServeur() {
        System.out.println("---------------------------------------------");
        System.out.println("le serveur d'authentification n'est pas fonctionnel");
        System.out.println("---------------------------------------------");
    }

    public void help() {
        System.out.println("---------------------------------------------");
        System.out.println("Lancer une requête sur le serveur de sondage pour connaître le serveur d'authentification");
        System.out.println("---------------------------------------------");
    }

    @Override
    public void afficher() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---------------------------------------------");
        System.out.println(" login ?");
        String login = scanner.next();
        System.out.println(" password ?");
        String password = scanner.next();

        grrGrrCarController.demandeToken(login,password);
    }
}
