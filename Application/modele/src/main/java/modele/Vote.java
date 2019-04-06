package modele;

import java.io.Serializable;

public class Vote implements Serializable {

    private int id;
    private String pseudoVotant;
    private Integer note;
    private String commentaire;

    private static int lastId = 0;

    public Vote() {}

    public Vote(String pseudoVotant, Integer note) {
        this(++lastId, pseudoVotant, note,null);
    }

    public Vote(String pseudoVotant, Integer note, String commentaire) {
        this(++lastId,pseudoVotant,note,commentaire);
    }
    private Vote(int id, String pseudoVotant, Integer note, String commentaire) {
        this.id = id;
        this.pseudoVotant = pseudoVotant;
        this.note = note;
        this.commentaire = commentaire;
    }

    public int getId() {
        return id;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getPseudoVotant() {
        return pseudoVotant;
    }

    public void setPseudoVotant(String pseudoVotant) {
        this.pseudoVotant = pseudoVotant;
    }

    @Override
    public String toString() {
        return "\t" + "Pseudo " + pseudoVotant + "\n" +
                "\t" + "note " + note + "\n" +
                "\t" + "commentaire " + commentaire +"\n";
    }
}