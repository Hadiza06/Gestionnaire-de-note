package model;

public class Notation {
    private int coef;
    private int note;
    private String matiere;

    public Notation(int coef, int note, String matiere) {
        this.coef = coef;
        this.note = note;
        this.matiere = matiere;
    }

    public int getCoef() {
        return coef;
    }

    public int getNote() {
        return note;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setCoef(int coef) {
        this.coef = coef;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }


}