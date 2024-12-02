package application.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.logic.Book;
import application.logic.LibrarySystem;
import application.logic.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MemberDashboardController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    String currentPane = "browseBook";
    
    LibrarySystem librarySystem;

    @FXML
    private GridPane bookContainer;

    @FXML
    private AnchorPane browseBook;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane profile;

    @FXML
    private Label profileEmail;

    @FXML
    private Label profileID;

    @FXML
    private Label profileRole;

    @FXML
    private Label profileUsername;

    @FXML
    private ImageView profileImage;

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

    public void disablePane() {
        switch(currentPane) {
            case "browseBook":
                browseBook.setVisible(false);
                break;
            case "profile":
                profile.setVisible(false);
                break;
        }
    }

    public void switchToBrowseBook() {
        disablePane();
        currentPane = "browseBook";
        browseBook.setVisible(true);
    }

    public void switchToProfile() {
        disablePane();
        currentPane = "profile";
        profile.setVisible(true);
    }

    private void loadBookContainer() {
        int column = 0;
        int row = 1;
        try {
            for (Book book : librarySystem.getBooks()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("book.fxml"));
                VBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(book);

                if (column == 5) {
                    column = 0;
                    row++;
                }

                bookContainer.add(bookBox, column++, row);
                GridPane.setMargin(bookBox, new Insets(4));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProfileData() {
        User currentUser = LoginController.currentUser;
        profileID.setText("User ID: " + String.valueOf(currentUser.getUserId()));
        profileUsername.setText("Username: " + currentUser.getUsername());
        profileEmail.setText("Email: " + currentUser.getEmail());
        profileRole.setText("Role: " + currentUser.getRole());
        if (currentUser.getImageUser() != null) {
            profileImage.setImage(convertBytesToImage(currentUser.getImageUser()));
        } else {
            profileImage.setImage(new Image(getClass().getResource("/resources/image/avatar.png").toExternalForm()));
        }
    }

    private Image convertBytesToImage(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new Image(byteArrayInputStream);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        librarySystem = LibrarySystem.getInstance();
        loadBookContainer();
        loadProfileData();
    }
}
