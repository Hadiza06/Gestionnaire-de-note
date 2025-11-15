package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import database.DatabaseManager;

public class Notation {
    private int coef;
    private float note;
    private String matiere;
    private int id = -1;
    private int etudiantId;

    public Notation(int coef, float note, String matiere) {
        this.coef = coef;
        this.note = note;
        this.matiere = matiere;
        this.id = -1;
        this.etudiantId = -1;
    }

    public int getCoef() {
        return coef;
    }

    public float getNote() {
        return note;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setCoef(int coef) {
        this.coef = coef;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(int etudiantId) {
        this.etudiantId = etudiantId;
    }

    public void sauvegarder() throws SQLException {
        if (this.etudiantId <= 0) {
            System.out.println("Erreur : etudiantId non défini !");
            return;
        }

        Connection conn = DatabaseManager.getConnection();

        String sql = "INSERT INTO notation (coef, note, matiere, etudiant_id) VALUES (?, ?, ?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stmt.setInt(1, this.coef);
        stmt.setFloat(2, this.note);
        stmt.setString(3, this.matiere);
        stmt.setInt(4, this.etudiantId);

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            this.id = rs.getInt(1);  // Stocker l'ID
        }
        rs.close();
        stmt.close();
        DatabaseManager.closeConnection(conn);

        System.out.println("Notation sauvegardée avec l'ID : " + this.id);

    }
}

