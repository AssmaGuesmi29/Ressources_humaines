package controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Employee;
import javafx.scene.control.cell.PropertyValueFactory;
import service.EmployeeService;

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

        employeeTable.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, positionColumn,
                departmentColumn, hireDateColumn, phoneColumn, addressColumn, statusColumn);
    }



    @FXML
    public void handleAddEmployee() {
        Employee employee = new Employee(0, firstNameField.getText(), lastNameField.getText(), positionField.getText(), departementField.getText(), hireDateField.getText(), statusField.getText(), emailField.getText(), phoneField.getText(), addressField.getText());
        employeeService.addEmployee(employee);
        employeeList.add(employee);
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
            // Remplir les champs de texte avec les informations de l'employé sélectionné
            firstNameField.setText(selectedEmployee.getFirstName());
            lastNameField.setText(selectedEmployee.getLastName());
            emailField.setText(selectedEmployee.getEmail());
            positionField.setText(selectedEmployee.getPosition());
            departementField.setText(selectedEmployee.getDepartment());
            hireDateField.setText(selectedEmployee.getHireDate());
            phoneField.setText(selectedEmployee.getPhone());
            addressField.setText(selectedEmployee.getAddress());
            statusField.setText(selectedEmployee.getStatus());

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
