package application.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.logic.Book;
import application.logic.LibrarySystem;
import application.logic.User;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MemberDashboardController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    String currentPane = "browseBook";
    
    LibrarySystem librarySystem;

    Image profileImage;

    @FXML
    private GridPane bookContainer;

    @FXML
    private AnchorPane browseBook;

    @FXML
    private AnchorPane inventory;

    @FXML
    private GridPane inventoryContainer;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane profile;

    @FXML
    private TextField profileEmail;

    @FXML
    private TextField profileID;

    @FXML
    private TextField profilePassword;

    @FXML
    private TextField profileUsername;

    @FXML
    private ImageView profileImageView;

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
            case "inventory":
                inventory.setVisible(false);
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

    public void switchToInventory() {
        disablePane();
        currentPane = "inventory";
        inventory.setVisible(true);
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
                bookController.setMemberDashboardController(this);

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
        profileID.setText(String.valueOf(currentUser.getUserId()));
        profileUsername.setText(currentUser.getUsername());
        profileEmail.setText(currentUser.getEmail());
        profilePassword.setText(currentUser.getPassword());
        if (currentUser.getImageUser() != null) {
            profileImageView.setImage(convertBytesToImage(currentUser.getImageUser()));
        } else {
            profileImageView.setImage(new Image(getClass().getResource("/resources/image/avatar.png").toExternalForm()));
        }
    }

    public void profileImportBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(profile.getScene().getWindow());

        if (file != null) {
            profileImage = new Image(file.toURI().toString(), 200, 237, false, true);
            profileImageView.setImage(profileImage);
        }
    }

    public void profileClearBtn() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to clear avatar?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            profileImage = null;
        profileImageView.setImage(new Image(getClass().getResource("/resources/image/avatar.png").toExternalForm()));
        }
    }

    public void editProfile() {
        try {
            librarySystem.editUserById(LoginController.currentUser
            , profileUsername.getText()
            , profileEmail.getText()
            , profilePassword.getText()
            , "USER"
            , convertImageToBytes(profileImage));
            showAlert(AlertType.INFORMATION, "Success", "Edit profile successfully!");
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error Message", e.getMessage());
        }
    }

    public void loadInventory() {
        int column = 0;
        int row = 1;
        try {
            for (Book book : librarySystem.getBookListUserBorrowing(LoginController.currentUser.getUserId())) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("book.fxml"));
                VBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(book);

                if (column == 5) {
                    column = 0;
                    row++;
                }

                inventoryContainer.add(bookBox, column++, row);
                GridPane.setMargin(bookBox, new Insets(4));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Image convertBytesToImage(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new Image(byteArrayInputStream);
    }

    private byte[] convertImageToBytes(Image image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        librarySystem = LibrarySystem.getInstance();
        loadBookContainer();
        loadProfileData();
        loadInventory();
    }
}
