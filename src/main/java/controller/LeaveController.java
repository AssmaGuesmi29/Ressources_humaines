package controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.LeaveRequest;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.LeaveService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class LeaveController {
    @FXML private TableView<LeaveRequest> leaveTable;
    @FXML private TableColumn<LeaveRequest, String> employeeNameColumn;
    @FXML private TableColumn<LeaveRequest, LocalDate> startDateColumn;
    @FXML private TableColumn<LeaveRequest, LocalDate> endDateColumn;
    @FXML private TableColumn<LeaveRequest, Integer> durationColumn;
    @FXML private TableColumn<LeaveRequest, String> statusColumn;
    @FXML private TableColumn<LeaveRequest, Integer> remainingLeaveColumn;

    @FXML private Button submitButton;
    @FXML private Button approveButton;

    private final LeaveService leaveService = new LeaveService();
    private final ObservableList<LeaveRequest> leaveRequests = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        remainingLeaveColumn.setCellValueFactory(param -> {
            LeaveRequest leaveRequest = param.getValue();
            int totalLeave = 30;
            int usedLeave = leaveRequest.getDuration();
            int remainingLeave = totalLeave - usedLeave;

            return new SimpleIntegerProperty(remainingLeave).asObject();
        });
        leaveRequests.setAll(leaveService.getAllLeaveRequests());
        leaveTable.setItems(leaveRequests);
        refreshLeaveRequests();
    }

    @FXML
    public void handleSubmitLeaveRequest() {

    }

    @FXML
    public void handleApproveLeaveRequest() {
        LeaveRequest selectedLeaveRequest = leaveTable.getSelectionModel().getSelectedItem();
        if (selectedLeaveRequest != null) {
            leaveService.updateLeaveRequestStatus(selectedLeaveRequest.getId(), "Approuvé");
            leaveTable.refresh();
        } else {
            showAlert("Selection Error", "Please select a leave request to approve.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void handleTakeLeave() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/take-leave.fxml"));
            Parent root = loader.load();

            TakeLeaveController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Prendre un congé");
            stage.setScene(new Scene(root));
            controller.setStage(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshLeaveRequests() {
        List<LeaveRequest> newLeaveRequests = leaveService.getAllLeaveRequests();
        leaveRequests.setAll(newLeaveRequests);
        leaveTable.setItems(leaveRequests);
        leaveTable.refresh();
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