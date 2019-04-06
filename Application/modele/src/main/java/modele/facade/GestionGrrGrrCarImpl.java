package modele.facade;
import modele.Trajet;
import modele.Personne;
import modele.Vote;
import modele.exceptions.authentification.*;
import modele.exceptions.gestionAppli.*;
import modele.reservation.FabriqueReservation;
import modele.reservation.Reservation;

import java.util.*;

public class GestionGrrGrrCarImpl implements GestionGrrGrrCar {

    private Map<Personne,Boolean> lesPersonnes;
    private Collection<Trajet> lesTrajets;
    private FabriqueReservation fabriqueReservation;

    public GestionGrrGrrCarImpl() {
        this.fabriqueReservation = new FabriqueReservation();
        this.lesTrajets = new ArrayList<>();
        this.lesPersonnes = new HashMap<>();
    }

    @Override
    public Collection<Trajet> getLesTrajets() {
        return lesTrajets;
    }

    @Override
    public void inscription(String pseudo, String password) throws PseudoDejaPrisException {
        Personne personne = new Personne(pseudo,password);
        for (Personne p : lesPersonnes.keySet()){
            if(p.getPseudo().equals(pseudo)){
                throw new PseudoDejaPrisException();
            }
        }
        this.lesPersonnes.put(personne,false);
    }

    @Override
    public void connexion(String pseudo, String password) throws PersonneIntrouvableException, DejaConnecteException, MotDePasseIncorrectException {
        if(lesPersonnes.containsKey(getPersonneByPseudo(pseudo))){
            if(getPersonneByPseudo(pseudo).getMotDePasse().equals(password)){
                if(!lesPersonnes.get(getPersonneByPseudo(pseudo))) {
                    lesPersonnes.put(getPersonneByPseudo(pseudo),true);
                }else {
                    throw new DejaConnecteException();
                }
            }else {
                throw new MotDePasseIncorrectException();
            }
        }
    }

    @Override
    public void deconnexion(String pseudo) throws PersonneIntrouvableException, DejaDeconnecteException {
        if(lesPersonnes.containsKey(getPersonneByPseudo(pseudo))) {
            if(lesPersonnes.get(getPersonneByPseudo(pseudo))) {
                lesPersonnes.put(getPersonneByPseudo(pseudo),false);
            }else {
                throw new DejaDeconnecteException();
            }
        }
    }

    @Override
    public Personne getPersonneByPseudo(String pseudo) throws PersonneIntrouvableException {
        for(Personne p : lesPersonnes.keySet()){
            if(p.getPseudo().equals(pseudo)){
                return p;
            }
        }
        throw new PersonneIntrouvableException();
    }

    private Reservation isDemande(int idTrajet,String pseudo) throws ReservationIntrouvableException, TrajetIntrouvableException {
        // Récupération des reservations du trajet passe en parametre
        Collection<Reservation> reservations = getTrajetById(idTrajet).getReservations();
        // Parcours de ces reservations
        for (Reservation r : reservations) {
            // Si le pseudo passe en parametre correspond au pseudo du reservant
            if (r.getPseudoReservant().equals(pseudo)) {
                // On retourne la reservation courante
                return r;
            }
        }
        // Aucune reservation n'a ete trouvee
        throw new ReservationIntrouvableException();
    }

    @Override
    public Trajet getTrajetById(int idTrajet) throws TrajetIntrouvableException{
        // Parcours des trajets
        for(Trajet t : lesTrajets){
            // Si l'id passe en parametre correspond a l'id du trajet courant
            if(t.getId() == idTrajet){
                // On retourne le trajet courant
                return t;
            }
        }
        // Aucun trajet n'a ete trouve
        throw new TrajetIntrouvableException();
    }


    @Override
    public Reservation getReservationById(int idReservation) throws ReservationIntrouvableException {
        for (Trajet trajet : lesTrajets){
            for (Reservation reservation : trajet.getReservations()){
                if(reservation.getId() == idReservation){
                    return reservation;
                }
            }
        }
        throw new ReservationIntrouvableException();
    }

    @Override
    public Reservation getReservationByPseudo(int idTrajet, String pseudo) throws ReservationIntrouvableException {
        // Parcours des trajets
        for(Trajet t : lesTrajets){
            // Parcours des reservations du trajet courant
            for (Reservation r : t.getReservations()){
                // Si l'id de la reservation courante du trajet courant correspond a l'id passe en parametre
                if(r.getPseudoReservant().equals(pseudo) && t.getId()==idTrajet){
                    // On retourne la reservation courante
                    return r;
                }
            }
        }
        // Aucune reservation n'a ete trouvee
        throw new ReservationIntrouvableException();
    }

    @Override
    public Collection<Reservation> getReservationsByPseudo(String pseudo) {
        Collection<Reservation> reservations = new ArrayList<>();
        // Parcours des trajets
        for(Trajet t : lesTrajets){
            // Parcours des reservations du trajet courant
            for (Reservation r : t.getReservations()){
                // Si l'id de la reservation courante du trajet courant correspond a l'id passe en parametre
                if(r.getPseudoReservant().equals(pseudo)){
                    // On retourne la reservation courante
                    reservations.add(r);
                }
            }
        }
        return reservations;
    }

    @Override
    public void nouvelleDemande(int idTrajet, String pseudo) throws TrajetIntrouvableException, TrajetCompletException, TrajetDejaDemandeException, TrajetNonDemandableException {
        // Si le nombre de place du trajet passe en parametre est strictement superieur au nombre de resevations validees de ce meme trajet
        if(this.getTrajetById(idTrajet).getNbPlaces() > this.getTrajetById(idTrajet).getReservationsValidees().size()) {
            // On teste que le demandeur n'est pas le fournisseur
            if(this.getTrajetById(idTrajet).getCreateur().equals(pseudo)){
                throw new TrajetNonDemandableException();
            }else {
                for (Reservation reservation : this.getTrajetById(idTrajet).getReservations()) {
                    // On teste si le reservant a deja reserve
                    if (reservation.getPseudoReservant().equals(pseudo)) {
                        throw new TrajetDejaDemandeException();
                    }
                }
                // Instanciation d'une nouvelle reservation
                // On note le type 0 pour signifier qu'il s'agit d'une reservation en attente
                Reservation reservation = fabriqueReservation.creerReservation(pseudo, 0);
                // Insertion de la reservation dans le trajet
                this.getTrajetById(idTrajet).getReservations().add(reservation);
            }
        }else {
            // Le trajet est complet
            throw new TrajetCompletException();
        }
    }

    @Override
    public void validerDemande(int idTrajet, String pseudo) throws ReservationIntrouvableException, TrajetIntrouvableException, TrajetCompletException {
        // Si le nombre de place du trajet passe en parametre est strictement superieur au nombre de resevations validees de ce meme trajet
        // Une personne ne peut donc pas demander une reservation sur un trajet plein
        if(this.getTrajetById(idTrajet).getNbPlaces() > this.getTrajetById(idTrajet).getReservationsValidees().size()) {
            // Si le trajet possede une reservation en attente pour la personne passee en parametre
            if (this.isDemande(idTrajet, pseudo).getEtat() == 0) {
                // On change l'etat de la reservation en question
                // On passe de 0 (attente) a 1(validee)
                this.isDemande(idTrajet, pseudo).setEtat(1);
            }
        }else {
            // Le trajet est plein
            throw new TrajetCompletException();
        }
    }

    @Override
    public void refuserDemande(int idTrajet, String pseudo) throws ReservationIntrouvableException, TrajetIntrouvableException {
        // Si la personne correspondant au pseudo passe en parametre possede une reservation sur le trajet passe en parametre
        if(this.isDemande(idTrajet,pseudo).getEtat() == 0){
            // On change l'etat de la reservation en question
            // On passe de 0 (attente) a 2(refusee)
            this.isDemande(idTrajet,pseudo).setEtat(2);
        }
    }

    @Override
    public void passagerNoterTrajet(int idTrajet, String pseudo, Integer note, String commentaire) throws TrajetIntrouvableException, PasPassagerTrajetException, PersonneIntrouvableException {
        // Initialisation d'un compteur
        int cpt =0;
        // Parcours des reservations validees du trajet dont l'id est passe en parametre
        for(Reservation r : this.getTrajetById(idTrajet).getReservationsValidees()){
            // Si le pseudo de la reservation validee courante correspond au pseudo passe en parametre
            if(r.getPseudoReservant().equals(pseudo)){
                // On incremente le compteur
                cpt++;
            }
        }
        // On entre dans la condition si la reservation de la personne passee en parametre est une reservation validee
        if(cpt>0) {
            // Instanciation d'un vote
            Vote vote;
            // Test du contenu du commentaire
            // Choix du bon constructeur de vote
            if (!commentaire.equals("")) {
                vote = new Vote(pseudo,note, commentaire);
            } else {
                vote = new Vote(pseudo,note);
            }
            // On ajoute le vote au trajet
            this.getPersonneByPseudo(this.getTrajetById(idTrajet).getCreateur()).addVote(vote);
        }else {
            // La personne voulant voter ne correspond pas aux criteres pour voter
            throw new PasPassagerTrajetException();
        }
    }

    @Override
    public void passagerNoterTrajet(int idTrajet, String pseudo, Integer note) throws TrajetIntrouvableException, PasPassagerTrajetException, PersonneIntrouvableException {
        // Vote sans commentaire
        this.passagerNoterTrajet(idTrajet,pseudo,note,"");
    }

    @Override
    public void conducteurNoterPassager(int idTrajet, String pseudoConducteur, String pseudo, Integer note, String commentaire) throws TrajetIntrouvableException, PasPassagerTrajetException, PersonneIntrouvableException {
        // Initialisation d'un compteur
        int cpt =0;
        // Parcours des reservations validees du trajet dont l'id est passe en parametre
        for(Reservation r : this.getTrajetById(idTrajet).getReservationsValidees()){
            // Si le pseudo de la reservation validee courante correspond au pseudo passe en parametre
            if(r.getPseudoReservant().equals(pseudo)){
                // On incremente le compteur
                cpt++;
            }
        }
        if(cpt>0 && this.getTrajetById(idTrajet).getCreateur().equals(pseudoConducteur)){
            // Instanciation d'un vote
            Vote vote;
            // Test du contenu du commentaire
            // Choix du bon constructeur de vote
            if (!commentaire.equals("")) {
                vote = new Vote(pseudo,note, commentaire);
            } else {
                vote = new Vote(pseudo,note);
            }
            // On ajoute le vote au trajet
            this.getPersonneByPseudo(pseudo).addVote(vote);
        }else {
            // La personne voulant voter ne correspond pas aux criteres pour voter
            throw new PasPassagerTrajetException();
        }
    }

    @Override
    public void conducteurNoterPassager(int idTrajet, String pseudoConducteur, String pseudo, Integer note) throws TrajetIntrouvableException, PasPassagerTrajetException, PersonneIntrouvableException {
        // Vote sans commentaire
        this.conducteurNoterPassager(idTrajet,pseudoConducteur,pseudo,note,"");
    }

    @Override
    public void validerFinTrajet(int idTrajet, String pseudo) throws TrajetIntrouvableException, ConducteurValideFinTrajetException {
        // On teste que la personne voulant valider la fin du trajet n'est pas le conducteur de ce meme trajet
        if(getTrajetById(idTrajet).getCreateur().equals(pseudo)){
            // La personne est le conducteur
            throw new ConducteurValideFinTrajetException();
        }else {
            // Parcours des reservation du trajet dont l'id est passe en parametre
            for (Reservation r : getTrajetById(idTrajet).getReservationsValidees()) {
                // Si le pseudo en parametre correspond au pseudo de la reservation
                if (r.getPseudoReservant().equals(pseudo)) {
                    // On change l'etat du trajet
                    this.getTrajetById(idTrajet).setFini(true);
                }
            }
        }
    }

    @Override
    public int creerTrajet(String depart, String arrivee, Date dateDepart, int nbPlaces, float tarif, String pseudo){
        Trajet trajet = new Trajet(depart,arrivee,dateDepart,nbPlaces,tarif,pseudo);
        this.lesTrajets.add(trajet);
        return trajet.getId();
    }

    @Override
    public void modifierTrajet(int idTrajet, String depart, String arrivee, Date dateDepart, int nbPlaces, float tarif){
        for (Trajet t : lesTrajets){
            if(t.getId() == idTrajet){
                t.setDepart(depart);
                t.setArrivee(arrivee);
                t.setDateDepart(dateDepart);
                t.setNbPlaces(nbPlaces);
                t.setTarif(tarif);
            }
        }
    }

    @Override
    public Boolean isConnecte(String pseudo) throws PersonneIntrouvableException {
        for (Personne p : lesPersonnes.keySet()) {
            if (p.getPseudo().equals(pseudo)) {
                return lesPersonnes.get(p);
            }
        }
        throw new PersonneIntrouvableException();
    }

    @Override
    public Collection<Personne> getPopulation(){
        return new ArrayList<>(lesPersonnes.keySet());
    }

    @Override
    public void deleteAllTrajets(){
        this.lesTrajets = new ArrayList<>();
    }

    @Override
    public void modifierPersonne(int idPersonne, String nom, String prenom, String adresse, String ville, String codePostal, String email, Date dateNaiss){
        for (Personne p : lesPersonnes.keySet()) {
            p.setNom(nom);
            p.setPrenom(prenom);
            p.setAdresse(adresse);
            p.setVille(ville);
            p.setCodePostal(codePostal);
            p.setEmail(email);
            p.setDateNaiss(dateNaiss);
        }
    }

    @Override
    public Collection<Trajet> rechercheTrajet(String villeDepart, String villeArrivee, Date dateDepart){
        // Instanciation d'une nouvelle map
        Collection<Trajet> trajets0 = new ArrayList<>();
        Collection<Trajet> trajets1 = new ArrayList<>();
        Collection<Trajet> trajets2 = new ArrayList<>();
        // Parcours des trajets
        for (Trajet trajet : lesTrajets){
            // Stockage des variables du trajet
            String from = trajet.getDepart();
            String to = trajet.getArrivee();
            Date date = trajet.getDateDepart();
            // Le trajet correspond parfaitement
            if(from.equals(villeDepart) && to.equals(villeArrivee) && date.equals(dateDepart)){
                trajets0.add(trajet);
            }else {
                if(from.equals(villeDepart) && to.equals(villeArrivee)){
                    trajets1.add(trajet);
                }else {
                    if(to.equals(villeArrivee) && date.equals(dateDepart)){
                        trajets2.add(trajet);
                    }
                }
            }
        }

        Collection<Trajet> trajets = new ArrayList<>();
        trajets.addAll(trajets0);
        trajets.addAll(trajets1);
        trajets.addAll(trajets2);

        return trajets;
    }

}
