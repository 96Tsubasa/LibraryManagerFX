package application.controllers;

import java.util.Optional;
import java.util.ResourceBundle;

import application.LibrarySystem;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

public class ManagerDashboardController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;
    String current = "dashboard";
    
    @FXML
    private AnchorPane addMember;

    @FXML
    private AnchorPane editMember;

    @FXML
    private AnchorPane deleteMember;

    @FXML
    private AnchorPane memberList;

    @FXML
    private AnchorPane addBook;

    @FXML
    private AnchorPane editBook;

    @FXML
    private AnchorPane deleteBook;

    @FXML
    private AnchorPane bookList;

    @FXML
    private AnchorPane dashboard;

    @FXML
    private Label welcomeUser;

    @FXML
    private TextField addMemberEmail;

    @FXML
    private TextField addMemberPassword;

    @FXML
    private ComboBox<?> addMemberRole;

    @FXML
    private TextField addMemberUsername;

    private String[] roleList = {"Manager", "Member"};
    public void userRoleList() {
        List<String> roleL = new ArrayList<>();
        for (String data : roleList) {
            roleL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(roleL);
        addMemberRole.setItems(listData);
    }

    private void disableNode() {
        if (current.equals("dashboard")) dashboard.setVisible(false);
        if (current.equals("addMember")) addMember.setVisible(false);
        if (current.equals("editMember")) editMember.setVisible(false);
        if (current.equals("deleteMember")) deleteMember.setVisible(false);
        if (current.equals("memberList")) memberList.setVisible(false);
        if (current.equals("addBook")) addBook.setVisible(false);
        if (current.equals("editBook")) editBook.setVisible(false);
        if (current.equals("deleteBook")) deleteBook.setVisible(false);
        if (current.equals("bookList")) bookList.setVisible(false);
    }

    @FXML
    private void switchToDashboard(ActionEvent event) {
        disableNode();
        current = "dashboard";
        dashboard.setVisible(true);
    }

    @FXML
    private void switchToAddMember(ActionEvent event) {
        disableNode();
        current = "addMember";
        addMember.setVisible(true);
    }

    @FXML
    private void switchToEditMember(ActionEvent event) {
        disableNode();
        current = "editMember";
        editMember.setVisible(true);
    }

    @FXML
    private void switchToDeleteMember(ActionEvent event) {
        disableNode();
        current = "deleteMember";
        deleteMember.setVisible(true);
    }

    @FXML
    private void switchToMemberList(ActionEvent event) {
        disableNode();
        current = "memberList";
        memberList.setVisible(true);
    }

    @FXML
    private void switchToAddBook(ActionEvent event) {
        disableNode();
        current = "addBook";
        addBook.setVisible(true);
    }

    @FXML
    private void switchToEditBook(ActionEvent event) {
        disableNode();
        current = "editBook";
        editBook.setVisible(true);
    }

    @FXML
    private void switchToDeleteBook(ActionEvent event) {
        disableNode();
        current = "deleteBook";
        deleteBook.setVisible(true);
    }

    @FXML
    private void switchToBookList(ActionEvent event) {
        disableNode();
        current = "bookList";
        bookList.setVisible(true);
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void addMember(ActionEvent event) throws IOException {
        try {
            String username = addMemberUsername.getText();
            String password = addMemberPassword.getText();
            String email = addMemberEmail.getText();
            String role = (String) addMemberRole.getValue();
            LibrarySystem libSys = LibrarySystem.getInstance();
            libSys.addUser(username, 10, email, password, role);
            showAlert(AlertType.INFORMATION, "Add Member", "Add Member Successfully!");
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error Message", "Username or Email is already registered");
        }

    }

    private void displayUsername() {
        welcomeUser.setText("Welcome " + LoginController.currentUser.getUsername());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        userRoleList();
    }
}