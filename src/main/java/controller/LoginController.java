package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    // Méthode appelée lors du clic sur "Se connecter"
    @FXML
    private void handleLogin() {
        // Exemple de validation de connexion
        String email = emailField.getText();
        String password = passwordField.getText();

        if (isLoginSuccessful(email, password)) {
            loadHomePage();
        } else {
            System.out.println("Échec de la connexion : Email ou mot de passe incorrect.");
        }
    }

    // Méthode simulée pour vérifier si la connexion est réussie
    private boolean isLoginSuccessful(String email, String password) {
        // Remplacez cette logique par une vérification réelle
        return "admin@test.com".equals(email) && "password".equals(password);
    }

    // Redirection vers la page "employe-view.fxml"

    private void loadHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.setTitle("Page d'accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'accueil.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
