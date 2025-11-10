package Test;

import model.Etudiant;
import model.Notation;

import java.util.Scanner;

public class TestEtudiant {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== SYSTÈME DE GESTION DES NOTES ===\n");
        System.out.print("Nom de l'étudiant: ");
        String nom = sc.nextLine();

        Etudiant etudiant = new Etudiant(nom);

        // Ajout des notations
        etudiant.ajouterNotation(new Notation(4, 12, "Mathématiques"));
        etudiant.ajouterNotation(new Notation(5, 15, "Physique-Chimie"));
        etudiant.ajouterNotation(new Notation(1, 18, "Histoire-Géographie"));
        etudiant.ajouterNotation(new Notation(3, 14, "Français"));
        etudiant.ajouterNotation(new Notation(2, 16, "Anglais"));

        // Calculs
        etudiant.calculerMoyenne();
        etudiant.genererAvis();

        // Affichage du bulletin complet
        etudiant.afficherBulletin();

        sc.close();
    }
}