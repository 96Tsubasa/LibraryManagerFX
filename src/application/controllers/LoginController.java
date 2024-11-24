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

import application.LibrarySystem;
import application.User;

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
        String username = usernameField.getText();
        String password = passwordField.getText();
        currentUser = LibrarySystem.getInstance().handleLogin(username, password);
        //if login successfully
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
        } else {
            if (username.equals("") || password.equals("")) {
                showAlert(AlertType.ERROR, "Login Failed", "You must enter username and password!");
            } else {
                showAlert(AlertType.ERROR, "Login Failed", "Wrong username and password!");
            }
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