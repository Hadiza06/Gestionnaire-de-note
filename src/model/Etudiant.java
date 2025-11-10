package model;

import java.util.ArrayList;

public class Etudiant {
    public String nom;
    public float moyenne;
    public String avis;
    private ArrayList<Notation> notations;

    public String getNom() {
        return nom;
    }

    public float getMoyenne() {
        return moyenne;
    }

    public String getAvis() {
        return avis;
    }

    public ArrayList<Notation> getNotations() {
        return notations;
    }

    public Etudiant(String nom) {
        this.nom = nom;
        this.moyenne = 0;
        this.notations = new ArrayList<Notation>();
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMoyenne(float moyenne) {
        this.moyenne = moyenne;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public float calculerMoyenne() {
        float sumCoef = 0;
        float sumCoefxNote = 0;

        for (Notation n : notations) {
            sumCoef += n.getCoef();
            sumCoefxNote += n.getCoef() * n.getNote();
        }

        // CORRECTION: la formule était inversée
        if (sumCoef != 0) {
            this.moyenne = sumCoefxNote / sumCoef;
        } else {
            this.moyenne = 0;
        }

        return this.moyenne;
    }

    public void genererAvis() {
        this.avis = moyenne >= 10 ? "Passe en classe supérieure" : "Autorisé à redoubler";
    }

    public void ajouterNotation(Notation n) {
        notations.add(n);
    }

    // Méthode pour afficher le bulletin
    public void afficherBulletin() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              BULLETIN DE NOTES - ANNÉE 2024/2025          ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Étudiant: %-48s ║%n", nom);
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Matière              │ Coefficient │ Note /20             ║");
        System.out.println("╠══════════════════════╪═════════════╪══════════════════════╣");

        for (Notation n : notations) {
            System.out.printf("║ %-20s │     %-7d │      %-15d║%n",
                    n.getMatiere(),
                    n.getCoef(),
                    n.getNote());
        }

        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║ MOYENNE GÉNÉRALE:                          %.2f / 20      ║%n", moyenne);
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║ AVIS: %-52s ║%n", avis);
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
}