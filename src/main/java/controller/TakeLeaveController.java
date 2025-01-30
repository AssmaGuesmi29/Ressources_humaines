package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.LeaveRequest;
import Model.Employee;
import service.LeaveService;
import service.EmployeeService;

import java.time.LocalDate;
import java.util.List;

public class TakeLeaveController {
    @FXML private ComboBox<Employee> employeeComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private CheckBox halfDayStart;
    @FXML private CheckBox halfDayEnd;

    private LeaveService leaveService = new LeaveService();
    private EmployeeService employeeService = new EmployeeService();
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        List<Employee> employees = employeeService.getAllEmployees();
        employeeComboBox.getItems().addAll(employees);

        employeeComboBox.setCellFactory(param -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });

        employeeComboBox.setButtonCell(new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
    }

    @FXML
    public void handleAddLeave() {
        Employee selectedEmployee = employeeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (selectedEmployee == null || startDate == null || endDate == null) {
            showAlert("Input Error", "Veuillez sélectionner un employé et les dates de début et de fin.");
            return;
        }

        int duration = calculateDuration(startDate, endDate, halfDayStart.isSelected(), halfDayEnd.isSelected());

        LeaveRequest leaveRequest = new LeaveRequest(
                0,
                selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName(),
                startDate,
                endDate,
                duration,
                "Approbateur",
                "En attente"
        );

        leaveService.addLeaveRequest(leaveRequest);
        stage.close();
    }

    @FXML
    public void handleCancel() {
        stage.close();
    }

    private int calculateDuration(LocalDate startDate, LocalDate endDate, boolean halfDayStart, boolean halfDayEnd) {
        int days = (int) startDate.until(endDate).getDays() + 1;
        if (halfDayStart) days -= 0.5;
        if (halfDayEnd) days -= 0.5;
        return days;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}