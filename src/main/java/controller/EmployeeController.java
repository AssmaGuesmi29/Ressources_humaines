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
        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Permet de sélectionner une seule ligne


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












}
