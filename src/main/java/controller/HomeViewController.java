package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeViewController {

    @FXML
    private Button gestionEmployesButton;

    @FXML
    private Button suiviCongesButton;

    @FXML
    private Button gestionPaieButton;

    @FXML
    private Button evaluationsPerformanceButton;

    @FXML
    private Button gestionFormationsButton;

    @FXML
    private Button rapportsStatistiquesButton;

    @FXML
    private void handleGestionEmployes() {
        System.out.println("Gestion des Employés clicked");
        // Add your navigation or logic here
    }

    @FXML
    private void handleSuiviConges() {
        System.out.println("Suivi des Congés et Absences clicked");
        // Add your navigation or logic here
    }

    @FXML
    private void handleGestionPaie() {
        System.out.println("Gestion de la Paie clicked");
        // Add your navigation or logic here
    }

    @FXML
    private void handleEvaluationsPerformance() {
        System.out.println("Évaluations de Performance clicked");
        // Add your navigation or logic here
    }

    @FXML
    private void handleGestionFormations() {
        System.out.println("Gestion des Formations clicked");
        // Add your navigation or logic here
    }

    @FXML
    private void handleRapportsStatistiques() {
        System.out.println("Rapports et Statistiques RH clicked");
        // Add your navigation or logic here
    }


    @FXML
    public void goToEmployeView(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de l'interface employe-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/employe-view.fxml"));
            Parent employeView = loader.load();

            // Créer une nouvelle scène pour afficher employe-view.fxml
            Scene employeScene = new Scene(employeView);

            // Obtenir la scène actuelle et la remplacer par la nouvelle
            Stage stage = (Stage) gestionEmployesButton.getScene().getWindow();
            stage.setScene(employeScene);
            stage.setTitle("Gestion des Employés");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
