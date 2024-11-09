package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

import application.LibrarySystem;
import application.User;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField passwordField;

    @FXML
    private void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        //LibrarySystem libsys = new LibrarySystem();
        //User currentUser = libsys.handleLogin(username, password);
    }

    public void switchToManagerDashboard(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("ManagerDashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
