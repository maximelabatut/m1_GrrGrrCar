package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Personne implements Serializable{

    private int id;
    private String nom;
    private String prenom;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pseudo;
    private String motDePasse;
    private String email;
    private Date dateNaiss;
    private List<String> roles;
    private Collection<Vote> votes;
    private Date dateInscription;

    private static int lastId = 0;

    public Personne() {
        this.roles = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.dateInscription = new Date();
    }

    public Personne(String pseudo, String motDePasse) {
        this(++lastId,pseudo,motDePasse);
    }

    private Personne(int id,String pseudo, String motDePasse) {
        this.id = id;
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.roles = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.dateInscription = new Date();
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Collection<Vote> getVotes() {
        return votes;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public void addVote(Vote vote){
        this.votes.add(vote);
    }

    public void addRole(String role){
        if(!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }

    public float calculNoteMoyenne(){
        float moy = 0;
        for(Vote v : votes){
            moy += v.getNote();
        }
        if(moy == 0){
            return 0;
        }else {
            return moy / votes.size();
        }
    }

    @Override
    public String toString() {
        return "nom='" + nom + '\n' +
                "prenom='" + prenom + '\n' +
                "dateInscription=" + dateInscription;
    }
}
