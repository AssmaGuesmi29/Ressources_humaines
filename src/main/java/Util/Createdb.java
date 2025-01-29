package Util;

import Model.Employees;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Createdb {

    private static final String URL = "jdbc:mysql://localhost:3306/ressources_humaines?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static List<Employees> getEmployes() {
        List<Employees> employes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                boolean add = employes.add(new Employees(rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("position"),
                        rs.getString("department"),
                        rs.getString("hireDate"),
                        rs.getString("status"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    public static List<Employees> searchEmployes(String query) {
        // Implémentation similaire avec une requête SQL filtrée
        return new ArrayList<>();
    }
}
