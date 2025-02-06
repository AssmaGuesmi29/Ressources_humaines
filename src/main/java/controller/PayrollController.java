package controller;

import Model.Payroll;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.PayrollService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class PayrollController {

    @FXML private TableView<Payroll> payrollTable;
    @FXML private TableColumn<Payroll, String> employeeNameColumn;
    @FXML private TableColumn<Payroll, BigDecimal> grossSalaryColumn;
    @FXML private TableColumn<Payroll, BigDecimal> netSalaryColumn;
    @FXML private TableColumn<Payroll, BigDecimal> bonusColumn;
    @FXML private TableColumn<Payroll, BigDecimal> deductionsColumn;

    private final PayrollService payrollService = new PayrollService();

    @FXML
    public void initialize() {
        employeeNameColumn.setCellValueFactory(cellData -> cellData.getValue().getEmployee().fullNameProperty());
        grossSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        netSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("netSalary"));
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        deductionsColumn.setCellValueFactory(new PropertyValueFactory<>("deductions"));

        loadPayrollData();
    }

    private void loadPayrollData() {
        List<Payroll> payrollList = payrollService.getAllPayrolls();
        payrollTable.setItems(FXCollections.observableArrayList(payrollList));
    }

    @FXML
    public void handleGeneratePayslip() {
        Payroll selectedPayroll = payrollTable.getSelectionModel().getSelectedItem();
        if (selectedPayroll != null) {
            payrollService.generatePayslip(selectedPayroll, "payslip_" + selectedPayroll.getId() + ".pdf");
            showAlert("Succès", "Le bulletin de paie a été généré avec succès.");
        } else {
            showAlert("Erreur", "Veuillez sélectionner un salaire pour générer un bulletin de paie.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleReturnHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home-view.fxml"));
            Parent homeView = loader.load();

            // Obtenir la scène en remontant via un composant existant
            Scene currentScene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) currentScene.getWindow();

            stage.setScene(new Scene(homeView));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page Home.");
            e.printStackTrace();
        }
    }
}