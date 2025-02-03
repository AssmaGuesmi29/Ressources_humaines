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
                "address TEXT" +
                ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (firstName, lastName, position, department, hireDate, status, email, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getPosition());
            stmt.setString(4, employee.getDepartment());

            if (employee.getHireDate() == null || employee.getHireDate().isEmpty()) {
                stmt.setNull(5, Types.DATE);
            } else {
                try {
                    String formattedDate = formatDate(employee.getHireDate());
                    stmt.setString(5, employee.getHireDate());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format: " + employee.getHireDate());
                    stmt.setNull(5, Types.DATE);
                }
            }

            stmt.setString(6, employee.getStatus());
            stmt.setString(7, employee.getEmail());
            stmt.setString(8, employee.getPhone());
            stmt.setString(9, employee.getAddress());

            int i = stmt.executeUpdate();

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
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;

    }

    public void updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET firstName = ?, lastName = ?, position = ?, department = ?, hireDate = ?, status = ?, email = ?, phone = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getPosition());
            stmt.setString(4, employee.getDepartment());
            stmt.setString(5, employee.getHireDate());
            stmt.setString(6, employee.getStatus());
            stmt.setString(7, employee.getEmail());
            stmt.setString(8, employee.getPhone());
            stmt.setString(9, employee.getAddress());
            stmt.setInt(10, employee.getId());

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
