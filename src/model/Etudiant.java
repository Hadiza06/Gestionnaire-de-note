package model;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import database.DatabaseManager;

public class Etudiant {
    public String nom;
    public float moyenne;
    public String avis;
    private ArrayList<Notation> notations;
    private int id = -1;

    public String getNom() {
        return nom;
    }
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id=id;
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
        this.id=-1;
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

    public void sauvegarder() throws SQLException {
        Connection conn = DatabaseManager.getConnection();

        String sql = "INSERT INTO etudiants (nom, moyenne, avis) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, this.nom);
        stmt.setFloat(2, this.moyenne);
        stmt.setString(3, this.avis);

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            this.id = rs.getInt(1);  // Stocker l'ID
        }

        rs.close();
        stmt.close();
        DatabaseManager.closeConnection(conn);

        System.out.println("Étudiant sauvegardé avec l'ID : " + this.id);
    }

    public void mettreAJour() throws SQLException {
        if (this.id <= 0) {
            System.out.println("Erreur : l'étudiant n'existe pas en base. Utilisez sauvegarder() d'abord.");
            return;
        }

        Connection conn = DatabaseManager.getConnection();

        String sql = "UPDATE etudiants SET moyenne = ?, avis = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);  // ← Pas de RETURN_GENERATED_KEYS

        stmt.setFloat(1, this.moyenne);
        stmt.setString(2, this.avis);
        stmt.setInt(3, this.id);

        stmt.executeUpdate();

        stmt.close();
        DatabaseManager.closeConnection(conn);

        System.out.println("Étudiant mis à jour (ID: " + this.id + ")");
    }

    public static Etudiant chargerDepuisBase(int id) throws SQLException {
        Connection conn = DatabaseManager.getConnection();

        String sql = "SELECT id, nom, moyenne, avis FROM etudiants WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Créer un nouvel étudiant avec les données de la base
            Etudiant etudiant = new Etudiant(rs.getString("nom"));
            etudiant.setId(rs.getInt("id"));
            etudiant.setMoyenne(rs.getFloat("moyenne"));
            etudiant.setAvis(rs.getString("avis"));

            rs.close();
            stmt.close();
            DatabaseManager.closeConnection(conn);

            System.out.println("Étudiant chargé depuis la base (ID: " + id + ")");
            return etudiant;
        } else {
            rs.close();
            stmt.close();
            DatabaseManager.closeConnection(conn);

            System.out.println("Aucun étudiant trouvé avec l'ID : " + id);
            return null;
        }
    }

    public void chargerNotations() throws SQLException {
        Connection conn = DatabaseManager.getConnection();

        String sql = "SELECT id, coef, note, matiere, etudiant_id FROM notation WHERE etudiant_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, this.id);

        ResultSet rs = stmt.executeQuery();

        this.notations.clear();

        while (rs.next()) {
            Notation notation = new Notation(
                    rs.getInt("coef"),
                    rs.getFloat("note"),  // ou rs.getFloat("note") selon ton type
                    rs.getString("matiere")
            );
            notation.setId(rs.getInt("id"));
            notation.setEtudiantId(rs.getInt("etudiant_id"));

            this.notations.add(notation);
        }

        rs.close();
        stmt.close();
        DatabaseManager.closeConnection(conn);

        System.out.println(notations.size() + " notation(s) chargée(s) pour l'étudiant " + this.nom);
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
            System.out.printf("║ %-20s │     %-7d │      %-15.2f║%n",
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