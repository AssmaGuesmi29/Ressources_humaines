package service;

import Model.Employee;
import Util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    public EmployeeService() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "firstName VARCHAR(255), " +
                "lastName VARCHAR(255), " +
                "position VARCHAR(255), " +
                "department VARCHAR(255), " +
                "hireDate DATE, " +
                "status VARCHAR(255), " +
                "email VARCHAR(255), " +
                "phone VARCHAR(255), " +
                "address TEXT, " +
                "baseSalary DECIMAL(10, 2) " +
                ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("✅ Table employees vérifiée/créée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la création de la table : " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (firstName, lastName, position, department, hireDate, status, email, phone, address, baseSalary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Correction ici

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getPosition());
            stmt.setString(4, employee.getDepartment());

            if (employee.getHireDate() == null || employee.getHireDate().isEmpty()) {
                stmt.setNull(5, Types.DATE);
            } else {
                try {
                    String formattedDate = formatDate(employee.getHireDate());
                    stmt.setString(5, formattedDate); // Correction ici : Utilisation du formattedDate
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format: " + employee.getHireDate());
                    stmt.setNull(5, Types.DATE);
                }
            }

            stmt.setString(6, employee.getStatus());
            stmt.setString(7, employee.getEmail());
            stmt.setString(8, employee.getPhone());
            stmt.setString(9, employee.getAddress());
            stmt.setBigDecimal(10, employee.getBaseSalary());

            int i = stmt.executeUpdate();

            // Récupération de la clé générée
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    employee.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String formatDate(String date) throws IllegalArgumentException {
        String[] parts = date.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid date format");
        }

        String day, month, year;

        // Check if the input is in yyyy-MM-dd format
        if (parts[0].length() == 4) {
            year = parts[0];
            month = parts[1];
            day = parts[2];
        }
        // Check if the input is in dd-MM-yyyy format
        else if (parts[2].length() == 4) {
            day = parts[0];
            month = parts[1];
            year = parts[2];
        } else {
            throw new IllegalArgumentException("Invalid date format");
        }

        if (day.length() != 2 || month.length() != 2 || year.length() != 4) {
            throw new IllegalArgumentException("Invalid date format");
        }

        return year + "-" + month + "-" + day;
    }


    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("position"),
                        rs.getString("department"),
                        rs.getString("hireDate"),
                        rs.getString("status"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBigDecimal("baseSalary")


                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;

    }

    public Employee getEmployeeById(int id) {
        Employee employee = null;
        String sql = "SELECT * FROM employees WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            stmt.setInt(1, id); // Paramétrer l'ID de l'employé à récupérer
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Si un employé avec cet ID est trouvé, créer un objet Employee
                employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("position"),
                        rs.getString("department"),
                        rs.getString("hireDate"),
                        rs.getString("status"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBigDecimal("baseSalary")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee; // Retourner l'objet Employee trouvé, ou null si aucun employé n'est trouvé
    }


    public void updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET firstName = ?, lastName = ?, position = ?, department = ?, hireDate = ?, status = ?, baseSalary = ?, email = ?, phone = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getPosition());
            stmt.setString(4, employee.getDepartment());
            stmt.setString(5, employee.getHireDate());
            stmt.setString(6, employee.getStatus());
            stmt.setBigDecimal(7, employee.getBaseSalary());  // Correction ici
            stmt.setString(8, employee.getEmail());
            stmt.setString(9, employee.getPhone());
            stmt.setString(10, employee.getAddress());
            stmt.setInt(11, employee.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteEmployee(int employeeId) {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}