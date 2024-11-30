package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;

import application.logic.LibrarySystem;
import application.logic.User;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static User currentUser;

    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField passwordField;

    @FXML
    private void login(ActionEvent event) throws IOException {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.equals("") || password.equals("")) {
                showAlert(AlertType.ERROR, "Error Message", "You must enter username and password!");
                return;
            }
            currentUser = LibrarySystem.getInstance().handleLogin(username, password);
            if (currentUser != null) {
                //if user account is ADMIN
                if (currentUser.getRole().equals("ADMIN")) {
                    root = FXMLLoader.load(getClass().getResource("ManagerDashboard.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                /*
                 * code for normal user
                 */
            }
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error Message", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}