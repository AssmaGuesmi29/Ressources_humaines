package controller;

import Model.Employees;
import Util.Createdb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Objects;

public class EmployeController {


    @FXML
    private TableView<Employees> employeTable;

    @FXML
    private TableColumn<?, Integer> id;

    @FXML
    private TableColumn<?, String> firstName;

    @FXML
    private TableColumn<?, String> lastName;

    @FXML
    private TableColumn<?, String> department;

    @FXML
    private TableColumn<?, String> email;

    @FXML
    private TableColumn<?, String> positionColumn;

    @FXML
    private TableColumn<Employees, Void> actionsColumn; // Nouvelle colonne pour les boutons

    @FXML
    private TextField searchField;

    private ObservableList<Employees> employeList;

    @FXML
    public void initialize() {
        // Configure the columns
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        department.setCellValueFactory(new PropertyValueFactory<>("department"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        // Configure the actions column
        addActionsToTable();

        // Load the employee data
        loadEmployees();

        // Set listener for search field to filter employees
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchEmploye());
    }

    private void loadEmployees() {
        employeList = FXCollections.observableArrayList(Createdb.getEmployes());
        employeTable.setItems(employeList);
    }
    public void addEmploye() {
        try {
            // Charger le fichier FXML de l'ajout d'employé
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ajout-employe.fxml"));
            // Charger la scène avec le fichier FXML
            Parent root = fxmlLoader.load();

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Ajouter un employé");

            // Créer la scène et l'affecter au stage
            Scene scene = new Scene(root, 800, 250);
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm()
            );

            // Afficher la fenêtre
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchEmploye() {
        String searchQuery = searchField.getText().toLowerCase();
        ObservableList<Employees> filteredList = FXCollections.observableArrayList();

        for (Employees employee : Createdb.getEmployes()) {
            if (employee.getFirstName().toLowerCase().contains(searchQuery) ||
                    employee.getPosition().toLowerCase().contains(searchQuery)) {
                filteredList.add(employee);
            }
        }
        employeTable.setItems(filteredList);
    }



    private void addActionsToTable() {

        Callback<TableColumn<Employees, Void>, TableCell<Employees, Void>> cellFactory = param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox actionBox = new HBox(editButton, deleteButton);

            {
                // Espacement entre les boutons
                actionBox.setSpacing(10);
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");


                // Action du bouton Modifier
                editButton.setOnAction(event -> {
                    Employees employee = getTableView().getItems().get(getIndex());
                    editEmployee(employee);
                });

                // Action du bouton Supprimer
                deleteButton.setOnAction(event -> {
                    Employees employee = getTableView().getItems().get(getIndex());
                    deleteEmployee(employee);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionBox);
                }
            }
        };

        // Appliquer le cellFactory à la colonne
        actionsColumn.setCellFactory(cellFactory);
    }

    private void editEmployee(Employees employee) {
        System.out.println("Modifier : " + employee);
        // Implémentez votre logique de modification ici (ex. ouvrir un formulaire de modification)
    }

    private void deleteEmployee(Employees employee) {
        System.out.println("Supprimer : " + employee);
        employeTable.getItems().remove(employee);
        // Implémentez votre logique de suppression ici (ex. supprimer de la base de données)
    }
}
