package service;

import Model.Employee;
import Model.Payroll;
import Util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PayrollService {

    private final EmployeeService employeeService;

    public PayrollService() {
        this.employeeService = new EmployeeService();
        createPayrollTableIfNotExists();
    }

    private void createPayrollTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS payroll (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "employee_id INT, " +
                "month VARCHAR(50), " +
                "year INT, " +
                "base_salary DECIMAL(10, 2), " +
                "bonus DECIMAL(10, 2), " +
                "deductions DECIMAL(10, 2), " +
                "net_salary DECIMAL(10, 2), " +
                "payment_date DATE, " +
                "FOREIGN KEY (employee_id) REFERENCES employees(id)" +
                ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la table payroll.", e);
        }
    }

    public void calculateSalary(Employee employee, String month, int year) {
        if (employee == null || month == null || month.isEmpty() || year <= 0) {
            throw new IllegalArgumentException("Données invalides pour le calcul du salaire.");
        }

        BigDecimal baseSalary = employee.getBaseSalary();
        BigDecimal bonus = calculateBonus(employee);
        BigDecimal deductions = calculateDeductions(employee);

        Payroll payroll = new Payroll(0, employee, month, year, baseSalary, bonus, deductions, LocalDate.now());
        savePayrollToDatabase(payroll);
    }

    public List<Payroll> getAllPayrolls() {
        List<Payroll> payrollList = new ArrayList<>();
        String sql = "SELECT * FROM payroll";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                Employee employee = employeeService.getEmployeeById(employeeId);

                if (employee != null) {
                    Payroll payroll = new Payroll(
                            rs.getInt("id"),
                            employee,
                            rs.getString("month"),
                            rs.getInt("year"),
                            rs.getBigDecimal("base_salary"),
                            rs.getBigDecimal("bonus"),
                            rs.getBigDecimal("deductions"),
                            rs.getDate("payment_date").toLocalDate()
                    );
                    payrollList.add(payroll);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des bulletins de paie.", e);
        }
        return payrollList;
    }

    private BigDecimal calculateBonus(Employee employee) {
        return BigDecimal.valueOf(500);
    }

    private BigDecimal calculateDeductions(Employee employee) {
        return BigDecimal.valueOf(300);
    }

    private void savePayrollToDatabase(Payroll payroll) {
        String sql = "INSERT INTO payroll (employee_id, month, year, base_salary, bonus, deductions, net_salary, payment_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payroll.getEmployee().getId());
            stmt.setString(2, payroll.getMonth());
            stmt.setInt(3, payroll.getYear());
            stmt.setBigDecimal(4, payroll.getBaseSalary());
            stmt.setBigDecimal(5, payroll.getBonus());
            stmt.setBigDecimal(6, payroll.getDeductions());
            stmt.setBigDecimal(7, payroll.getNetSalary());
            stmt.setDate(8, Date.valueOf(payroll.getPaymentDate()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du bulletin de paie.", e);
        }
    }

    public void generatePayslip(Payroll payroll, String outputPath) {
        if (payroll == null || outputPath == null || outputPath.isEmpty()) {
            throw new IllegalArgumentException("Données invalides pour la génération du bulletin de paie.");
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Bulletin de Paie - " + payroll.getMonth() + " " + payroll.getYear());
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText("Nom: " + payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Poste: " + payroll.getEmployee().getPosition());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Salaire de base: " + payroll.getBaseSalary());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Prime: " + payroll.getBonus());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Retenues: " + payroll.getDeductions());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Salaire net: " + payroll.getNetSalary());
                contentStream.endText();
            }

            document.save(outputPath);
            System.out.println("Bulletin de paie généré : " + outputPath);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du bulletin de paie.", e);
        }
    }
}