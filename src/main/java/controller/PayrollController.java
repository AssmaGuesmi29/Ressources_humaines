package controller;
import Model.Payroll;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import service.PayrollService;
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
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        grossSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("grossSalary"));
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
//            payrollService.generatePayslip(selectedPayroll);
        } else {
            showAlert("Sélectionnez un salaire", "Veuillez sélectionner un salaire pour générer un bulletin de paie.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}