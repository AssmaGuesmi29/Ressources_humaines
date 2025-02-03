package controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Employee;
import javafx.scene.control.cell.PropertyValueFactory;
import service.EmployeeService;

import java.math.BigDecimal;

public class EmployeeController {
    @FXML private TableView<Employee> employeeTable;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField hireDateField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField positionField;
    @FXML private TextField departementField;
    @FXML private TextField statusField;
    @FXML private TextField baseSalaryField;

    @FXML
    private TextField searchField;


    @FXML private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;

    private final EmployeeService employeeService = new EmployeeService();
    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        employeeList.addAll(employeeService.getAllEmployees());
        employeeTable.setItems(employeeList);
        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        TableColumn<Employee, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Employee, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Employee, String> positionColumn = new TableColumn<>("Position");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<Employee, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        TableColumn<Employee, String> hireDateColumn = new TableColumn<>("Hire Date");
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        TableColumn<Employee, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Employee, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Employee, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Employee, BigDecimal> baseSalaryColumn = new TableColumn<>("baseSalary");
        baseSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));

        employeeTable.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, positionColumn,
                departmentColumn, hireDateColumn, phoneColumn, addressColumn, statusColumn,baseSalaryColumn);
    }



    @FXML
    public void handleAddEmployee() {

        BigDecimal salary;
        try {
            salary = new BigDecimal(baseSalaryField.getText().trim());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid salary format. Please enter a valid number.");
            return;
        }
            // Create new employee
            Employee employee = new Employee(
                    0, // Placeholder ID
                    firstNameField.getText(),
                    lastNameField.getText(),
                    positionField.getText(),
                    departementField.getText(),
                    hireDateField.getText(),
                    statusField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    salary
            );

            // Add employee via service
            employeeService.addEmployee(employee);

            // Update local list
            employeeList.add(employee);

            // Clear input fields
            clearFields();

    }
    @FXML
    public void handleDeleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            employeeService.deleteEmployee(selectedEmployee.getId());

            employeeList.remove(selectedEmployee);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection Error");
            alert.setHeaderText("No employee selected");
            alert.setContentText("Please select an employee to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleEditEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            firstNameField.setText(selectedEmployee.getFirstName());
            lastNameField.setText(selectedEmployee.getLastName());
            emailField.setText(selectedEmployee.getEmail());
            positionField.setText(selectedEmployee.getPosition());
            departementField.setText(selectedEmployee.getDepartment());
            hireDateField.setText(selectedEmployee.getHireDate());
            phoneField.setText(selectedEmployee.getPhone());
            addressField.setText(selectedEmployee.getAddress());
            statusField.setText(selectedEmployee.getStatus());
            baseSalaryField.setText(selectedEmployee.getBaseSalary().toString());


            addButton.setText("Update");
            addButton.setOnAction(event -> handleUpdateEmployee(selectedEmployee));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection Error");
            alert.setHeaderText("No employee selected");
            alert.setContentText("Please select an employee to edit.");
            alert.showAndWait();
        }
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void handleUpdateEmployee(Employee employee) {
        employee.setFirstName(firstNameField.getText());
        employee.setLastName(lastNameField.getText());
        employee.setEmail(emailField.getText());
        employee.setPosition(positionField.getText());
        employee.setDepartment(departementField.getText());
        employee.setHireDate(hireDateField.getText());
        employee.setPhone(phoneField.getText());
        employee.setAddress(addressField.getText());
        employee.setStatus(statusField.getText());

        try {
            BigDecimal baseSalary = new BigDecimal(baseSalaryField.getText()); // Conversion en BigDecimal
            employee.setBaseSalary(baseSalary);
        } catch (NumberFormatException e) {
            System.out.println("Invalid base salary format");
            // Gère l'erreur de format si nécessaire
        }


        employeeService.updateEmployee(employee);

        employeeTable.refresh();

        addButton.setText("Add Employee");
        addButton.setOnAction(event -> handleAddEmployee());

        clearFields();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        positionField.clear();
        departementField.clear();
        hireDateField.clear();
        phoneField.clear();
        addressField.clear();
        statusField.clear();
    }










}