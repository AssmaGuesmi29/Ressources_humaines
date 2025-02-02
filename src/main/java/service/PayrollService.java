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

    private final EmployeeService employeeService; // Utilisation d'un service pour gérer les employés

    public PayrollService() {
        this.employeeService = new EmployeeService();
    }

    /**
     * Calcule le salaire d'un employé pour un mois et une année donnés.
     *
     * @param employee L'employé concerné.
     * @param month    Le mois (ex: "Janvier").
     * @param year     L'année (ex: 2023).
     */
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

    /**
     * Récupère tous les bulletins de paie de la base de données.
     *
     * @return Une liste de bulletins de paie.
     */
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

    /**
     * Calcule la prime d'un employé.
     *
     * @param employee L'employé concerné.
     * @return La prime calculée.
     */
    private BigDecimal calculateBonus(Employee employee) {
        // Exemple de calcul de prime (à adapter selon les besoins)
        return BigDecimal.valueOf(500);
    }

    /**
     * Calcule les retenues (impôts, sécurité sociale, etc.) pour un employé.
     *
     * @param employee L'employé concerné.
     * @return Les retenues calculées.
     */
    private BigDecimal calculateDeductions(Employee employee) {
        // Exemple de calcul de retenues (à adapter selon les besoins)
        return BigDecimal.valueOf(300);
    }

    /**
     * Sauvegarde un bulletin de paie dans la base de données.
     *
     * @param payroll Le bulletin de paie à sauvegarder.
     */
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

    /**
     * Génère un bulletin de paie au format PDF.
     *
     * @param payroll Le bulletin de paie à générer.
     * @param outputPath Le chemin où enregistrer le fichier PDF.
     */
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