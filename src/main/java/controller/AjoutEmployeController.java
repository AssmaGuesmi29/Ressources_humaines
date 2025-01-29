package controller;

import Model.Employees;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AjoutEmployeController {
    @FXML
    private DatePicker hireDateField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField departmentField;

    @FXML
    private TextField positionField;
    @FXML
    private TextField statusField;
    @FXML
    private TextField addressField;

    private ObservableList<Employees> employeList = FXCollections.observableArrayList();

    @FXML
    private TextField phoneField;


    @FXML

    public void addEmploye() {
        // S'assurer que la liste est initialisée
        if (employeList == null) {
            employeList = FXCollections.observableArrayList();
        }

        // Récupérer les données saisies
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String department = departmentField.getText().trim();
        String position = positionField.getText().trim();
        String status = statusField.getText().trim();
        LocalDate hireDate = hireDateField.getValue();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validation des champs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || department.isEmpty() || position.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }
        if (hireDate == null) {
            System.out.println("Format de date incorrect !");
            return;
        }


        // Créer un nouvel employé
        Employees newEmployee = new Employees(
                employeList.size() + 1, // L'identifiant est basé sur la taille actuelle de la liste
                firstName,
                lastName,
                email,
                department,
                position,
                status,
                hireDate.toString(), // Utiliser la méthode toString pour la date
                address,
                phone
        );

        // Ajouter l'employé à la liste observable
        employeList.add(newEmployee);

        // Vider les champs du formulaire
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        departmentField.clear();
        positionField.clear();
        statusField.clear();
        hireDateField.setValue(null);
        addressField.clear();
        phoneField.clear();
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
