package service;

import Model.Formation;
import Util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FormationService {
    public FormationService() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS formations (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "title VARCHAR(255), " +
                "startDate DATE, " +
                "endDate DATE, " +
                "description TEXT, " +
                "duree VARCHAR(255), " +
                "trainer VARCHAR(255), " +
                "cost DECIMAL(10,2) " +
                ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("✅ Table formations vérifiée/créée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la création de la table : " + e.getMessage());
        }
    }

    public void addFormation(Formation formation) {
        if (!isValidDate(formation.getStartDate()) || !isValidDate(formation.getEndDate())) {
            throw new IllegalArgumentException("❌ Format de date invalide. Utilisez YYYY-MM-DD.");
        }

        String sql = "INSERT INTO formations (title, startDate, endDate, description, duree, trainer, cost) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, formation.getTitle());
            stmt.setDate(2, java.sql.Date.valueOf(formation.getStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(formation.getEndDate()));
            stmt.setString(4, formation.getDescription());
            stmt.setString(5, formation.getDuree());
            stmt.setString(6, formation.getTrainer());
            stmt.setBigDecimal(7, formation.getCost());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    formation.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la formation : " + e.getMessage());
        }
    }

    public List<Formation> getAllFormations() {
        List<Formation> formations = new ArrayList<>();
        String sql = "SELECT * FROM formations";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                formations.add(new Formation(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDate("startDate").toString(),
                        rs.getDate("endDate").toString(),
                        rs.getString("description"),
                        rs.getString("duree"),
                        rs.getString("trainer"),
                        rs.getBigDecimal("cost")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des formations : " + e.getMessage());
        }
        return formations;
    }

    public Formation getFormationById(int id) {
        Formation formation = null;
        String sql = "SELECT * FROM formations WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    formation = new Formation(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getDate("startDate").toString(),
                            rs.getDate("endDate").toString(),
                            rs.getString("description"),
                            rs.getString("duree"),
                            rs.getString("trainer"),
                            rs.getBigDecimal("cost")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de la formation : " + e.getMessage());
        }
        return formation;
    }

    public void updateFormation(Formation formation) {
        if (!isValidDate(formation.getStartDate()) || !isValidDate(formation.getEndDate())) {
            throw new IllegalArgumentException("❌ Format de date invalide. Utilisez YYYY-MM-DD.");
        }

        String sql = "UPDATE formations SET title = ?, startDate = ?, endDate = ?, description = ?, duree = ?, trainer = ?, cost = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, formation.getTitle());
            stmt.setDate(2, java.sql.Date.valueOf(formation.getStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(formation.getEndDate()));
            stmt.setString(4, formation.getDescription());
            stmt.setString(5, formation.getDuree());
            stmt.setString(6, formation.getTrainer());
            stmt.setBigDecimal(7, formation.getCost());
            stmt.setInt(8, formation.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de la formation : " + e.getMessage());
        }
    }

    public void deleteFormation(int id) {
        String sql = "DELETE FROM formations WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de la formation : " + e.getMessage());
        }
    }

    private static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
