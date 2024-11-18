package application.controllers;

import java.util.ResourceBundle;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;

public class ManagerDashboardController implements Initializable{
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

    private void displayUsername() {
        welcomeUser.setText("Welcome " + LoginController.currentUser.getUsername());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
    }
}