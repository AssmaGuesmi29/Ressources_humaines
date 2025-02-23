package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Model.Formation;
import service.FormationService;

import java.io.IOException;
import java.math.BigDecimal;

public class FormationController {
    @FXML private TableView<Formation> formationTable;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    @FXML private TextField trainerField;
    @FXML private TextField dureeField;
    @FXML private TextField costField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;

    private final FormationService formationService = new FormationService();
    private final ObservableList<Formation> formationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        formationList.addAll(formationService.getAllFormations());
        formationTable.setItems(formationList);
        formationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn<Formation, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Formation, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Formation, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Formation, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Formation, String> trainerColumn = new TableColumn<>("Trainer");
        trainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));

        TableColumn<Formation, String> dureeColumn = new TableColumn<>("Duree");
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("duree"));

        TableColumn<Formation, BigDecimal> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        formationTable.getColumns().addAll(titleColumn, descriptionColumn, startDateColumn, endDateColumn, trainerColumn,dureeColumn, costColumn);
    }

    @FXML
    public void handleAddFormation() {
        BigDecimal cost;
        try {
            cost = new BigDecimal(costField.getText().trim());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid cost format. Please enter a valid number.");
            return;
        }

        Formation formation = new Formation(
                0,
                titleField.getText(),
                descriptionField.getText(),
                startDateField.getText(),
                endDateField.getText(),
                trainerField.getText(),
                dureeField.getText(),
                cost
        );

        formationService.addFormation(formation);
        formationList.add(formation);
        clearFields();
    }

    @FXML
    public void handleDeleteFormation() {
        Formation selectedFormation = formationTable.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            formationService.deleteFormation(selectedFormation.getId());
            formationList.remove(selectedFormation);
        } else {
            showErrorAlert("No formation selected.");
        }
    }

    @FXML
    public void handleEditFormation() {
        Formation selectedFormation = formationTable.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            titleField.setText(selectedFormation.getTitle());
            descriptionField.setText(selectedFormation.getDescription());
            startDateField.setText(selectedFormation.getStartDate());
            endDateField.setText(selectedFormation.getEndDate());
            trainerField.setText(selectedFormation.getTrainer());
            dureeField.setText(selectedFormation.getDuree());
            costField.setText(selectedFormation.getCost().toString());

            addButton.setText("Update");
            addButton.setOnAction(event -> handleUpdateFormation(selectedFormation));
        } else {
            showErrorAlert("No formation selected.");
        }
    }

    @FXML
    public void handleUpdateFormation(Formation formation) {
        formation.setTitle(titleField.getText());
        formation.setDescription(descriptionField.getText());
        formation.setStartDate(startDateField.getText());
        formation.setEndDate(endDateField.getText());
        formation.setTrainer(trainerField.getText());
        formation.setTrainer(dureeField.getText());

        try {
            BigDecimal cost = new BigDecimal(costField.getText());
            formation.setCost(cost);
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid cost format.");
            return;
        }

        formationService.updateFormation(formation);
        formationTable.refresh();

        addButton.setText("Add Formation");
        addButton.setOnAction(event -> handleAddFormation());
        clearFields();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        titleField.clear();
        descriptionField.clear();
        startDateField.clear();
        endDateField.clear();
        trainerField.clear();
        dureeField.clear();
        costField.clear();
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
