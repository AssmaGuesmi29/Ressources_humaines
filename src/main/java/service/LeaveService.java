package service;

import Model.LeaveRequest;
import Util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LeaveService {
    public LeaveService() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS leave_requests (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "employee_name VARCHAR(255), " +
                "start_date DATE, " +
                "end_date DATE, " +
                "duration INT, " +
                "approver VARCHAR(255), " +
                "status VARCHAR(255)" +
                ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLeaveRequest(LeaveRequest leaveRequest) {
        String sql = "INSERT INTO leave_requests (employee_name, start_date, end_date, duration, approver, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, leaveRequest.getEmployeeName());
            stmt.setDate(2, Date.valueOf(leaveRequest.getStartDate()));
            stmt.setDate(3, Date.valueOf(leaveRequest.getEndDate()));
            stmt.setInt(4, leaveRequest.getDuration());
            stmt.setString(5, leaveRequest.getApprover());
            stmt.setString(6, leaveRequest.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                leaveRequests.add(new LeaveRequest(
                        rs.getInt("id"),
                        rs.getString("employee_name"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("duration"),
                        rs.getString("approver"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveRequests;
    }

    public void updateLeaveRequestStatus(int id, String status) {
        String sql = "UPDATE leave_requests SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}