package Test;

import model.Etudiant;
import model.Notation;
import java.sql.SQLException;
import java.util.Scanner;

public class TestEtudiant {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== SYSTÈME DE GESTION DES NOTES ===\n");
        System.out.print("Nom de l'étudiant: ");
        String nom = sc.nextLine();

        try {
            System.out.println("\n--- CRÉATION D'UN NOUVEL ÉTUDIANT ---");

            Etudiant etudiant = new Etudiant(nom);

            etudiant.ajouterNotation(new Notation(4, 12.0f, "Mathématiques"));
            etudiant.ajouterNotation(new Notation(5, 15.0f, "Physique-Chimie"));
            etudiant.ajouterNotation(new Notation(1, 18.0f, "Histoire-Géographie"));
            etudiant.ajouterNotation(new Notation(3, 14.0f, "Français"));
            etudiant.ajouterNotation(new Notation(2, 16.0f, "Anglais"));

            etudiant.calculerMoyenne();
            etudiant.genererAvis();

            etudiant.sauvegarder();
            int idSauvegarde = etudiant.getId();

            for (Notation notation : etudiant.getNotations()) {
                notation.setEtudiantId(etudiant.getId());
                notation.sauvegarder();
            }

            System.out.println("\n✅ Étudiant et notations sauvegardés avec succès !");

            System.out.println("\n--- RÉCUPÉRATION DEPUIS LA BASE DE DONNÉES ---");

            Etudiant etudiantRecharge = Etudiant.chargerDepuisBase(idSauvegarde);

            if (etudiantRecharge != null) {
                etudiantRecharge.chargerNotations();

                System.out.println("\n📋 BULLETIN RÉCUPÉRÉ DEPUIS LA BASE :");
                etudiantRecharge.afficherBulletin();
            } else {
                System.out.println("❌ Erreur lors du chargement de l'étudiant");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors des opérations sur la base : " + e.getMessage());
            e.printStackTrace();
        }

        sc.close();
    }
}