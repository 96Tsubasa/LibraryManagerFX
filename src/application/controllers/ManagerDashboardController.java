package application.controllers;

import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.LibrarySystem;
import application.User;

import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

public class ManagerDashboardController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;

    String current = "dashboard";
    
    private Image image;
    private Image addMemberImage;
    private Image addBookImage;

    private LibrarySystem librarySystem;
    
    private User editingUser;
    
    @FXML
    private AnchorPane addBook;

    @FXML
    private Button addBookBtn;

    @FXML
    private AnchorPane addMember;

    @FXML
    private Button addMemberBtn;

    @FXML
    private TextField addMemberEmail;

    @FXML
    private ImageView addMemberImageView;

    @FXML
    private TextField addMemberPassword;

    @FXML
    private ComboBox<?> addMemberRole;

    @FXML
    private TextField addMemberUsername;

    @FXML
    private AnchorPane bookList;

    @FXML
    private Button bookListBtn;

    @FXML
    private AnchorPane dashboard;

    @FXML
    private Button dashboardBtn;

    @FXML
    private AnchorPane deleteBook;

    @FXML
    private Button deleteBookBtn;

    @FXML
    private AnchorPane deleteMember;

    @FXML
    private Button deleteMemberBtn;

    @FXML
    private AnchorPane editBook;

    @FXML
    private Button editBookBtn;

    @FXML
    private AnchorPane editMember;

    @FXML
    private Button editMemberBtn;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane memberList;

    @FXML
    private Button memberListBtn;

    @FXML
    private Label welcomeUser;

    @FXML
    private TableView<User> memberListTable;

    @FXML
    private TableColumn<User, String> memberListEmail;

    @FXML
    private TableColumn<User, String> memberListID;

    @FXML
    private TableColumn<User, String> memberListPassword;

    @FXML
    private TableColumn<User, String> memberListRole;

    @FXML
    private TableColumn<User, String> memberListUsername;

    @FXML
    private AnchorPane manager_dashboard;

    @FXML
    private Label memberListEmailShow;

    @FXML
    private Label memberListIDShow;

    @FXML
    private Label memberListPasswordShow;

    @FXML
    private Label memberListRoleShow;

    @FXML
    private Label memberListUsernameShow;

    @FXML
    private ImageView memberListImageShow;

    @FXML
    private TextField deleteMemberID;

    @FXML
    private TextField editMemberEmail;

    @FXML
    private TextField editMemberID;

    @FXML
    private TextField editMemberPassword;

    @FXML
    private ComboBox<String> editMemberRole;

    @FXML
    private TextField editMemberUsername;

    @FXML
    private ImageView editMemberImage;

    @FXML
    private Label memberNumber;

    @FXML
    private Label bookNumber;

    @FXML
    private Label managerNumber;

    @FXML
    private TextField addBookAuthor;

    @FXML
    private Spinner<Integer> addBookCopiesAvailable;

    @FXML
    private TextArea addBookDescription;

    @FXML
    private TextField addBookGenre;

    @FXML
    private TextField addBookISBN;

    @FXML
    private ImageView addBookImageView;

    @FXML
    private TextField addBookPublicationYear;

    @FXML
    private TextField addBookPublisher;

    @FXML
    private TextField addBookTitle;

    @FXML
    private TextField memberListSearch;

    private String[] roleList = {"USER", "ADMIN"};
    public void userRoleList() {
        List<String> roleL = new ArrayList<>();
        for (String data : roleList) {
            roleL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(roleL);
        addMemberRole.setItems(listData);
        editMemberRole.setItems(listData);
    }

    private ObservableList<User> memberListData;
    private FilteredList<User> filteredData;
    public void memberListShowData() {
        memberListData = FXCollections.observableArrayList(librarySystem.getUsers());
        
        memberListID.setCellValueFactory(new PropertyValueFactory<>("userId"));
        memberListUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        memberListPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        memberListEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        memberListRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        memberListTable.setItems(memberListData);

        filteredData = new FilteredList<>(memberListData, b -> true);
        memberListTable.setItems(filteredData);
    }

    private void handleSearch() {
        memberListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
    
                //searchByUsername
                return user.getUsername().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    public void memberListSelectData() {
        User user = memberListTable.getSelectionModel().getSelectedItem();
        int num = memberListTable.getSelectionModel().getSelectedIndex();
        if (num < 0) return;

        memberListIDShow.setText("ID: " + user.getUserId());
        memberListUsernameShow.setText("Username: " + user.getUsername());
        memberListPasswordShow.setText("Password: " + user.getPassword());
        memberListEmailShow.setText("Email: " + user.getEmail());
        memberListRoleShow.setText("Role: " + user.getRole());
        if (user.getImageUser() != null) {
            memberListImageShow.setImage(convertBytesToImage(user.getImageUser()));
        } else {
            memberListImageShow.setImage(new Image(getClass().getResource("/resources/image/avatar.png").toExternalForm()));
        }
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
    
    private void clearAddMemberInput() {
        addMemberUsername.clear();
        addMemberPassword.clear();
        addMemberEmail.clear();
        addMemberRole.getSelectionModel().clearSelection();
        addMemberImage = new Image("/resources/image/avatar.png");
        addMemberImageView.setImage(addMemberImage);
    }

    @FXML
    private void addMember(ActionEvent event) throws IOException {
        try {
            String username = addMemberUsername.getText();
            String password = addMemberPassword.getText();
            String email = addMemberEmail.getText();
            String role = (String) addMemberRole.getValue();
            User newUser = librarySystem.addUser(username, email, password, role, convertImageToBytes(addMemberImage));
            if (username.equals("") || password.equals("") || email.equals("") || role.equals("")) {
                showAlert(AlertType.ERROR, "Error Message", "You must fill all information!");
            } else {
                memberListData.add(newUser);
                showAlert(AlertType.INFORMATION, "Add Member", "Add Member Successfully!");
                clearAddMemberInput();
            }
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error Message", e.getMessage());
        }
    }

    @FXML
    private void addMemberClearImage(ActionEvent e) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to clear the avatar?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            addMemberImage = new Image("/resources/image/avatar.png");
            addMemberImageView.setImage(addMemberImage);
        }
    }

    @FXML
    private void deleteMember(ActionEvent event) throws IOException {
        try {
            long userID = Long.parseLong(deleteMemberID.getText());
            librarySystem.deleteUserById(userID);
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error Message", e.getMessage());
        }
    }

    @FXML
    private void searchMember(ActionEvent event) {
        try {
            long userID = Long.parseLong(editMemberID.getText());
            editingUser = librarySystem.getUserById(userID);
            if (editingUser != null) {
                editMemberUsername.setText(editingUser.getUsername());
                editMemberPassword.setText(editingUser.getPassword());
                editMemberEmail.setText(editingUser.getEmail());
                if (editingUser.getImageUser() != null) {
                    image = convertBytesToImage(editingUser.getImageUser());
                } else {
                    image = new Image("/resources/image/avatar.png");
                }
                editMemberImage.setImage(image);
                String role = editingUser.getRole();
                editMemberRole.getSelectionModel().select(role);
            } else {
                showAlert(AlertType.ERROR, "Error Message", "That user doesn't exist!");
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error Message", "Invalid Member ID!");
        }
    }

    @FXML
    private void editMember(ActionEvent e) throws IllegalArgumentException {
        try {
            String username = editMemberUsername.getText();
            String email = editMemberEmail.getText();
            String password = editMemberPassword.getText();
            String role = editMemberRole.getSelectionModel().getSelectedItem();
            byte[] bytes = convertImageToBytes(image);
            librarySystem.editUserById(editingUser, username, email, password, role, bytes);
            memberListTable.refresh();
            showAlert(AlertType.INFORMATION, "Information Message", "You updated a member successfully!");
        } catch (IllegalArgumentException exception) {
            showAlert(AlertType.ERROR, "Error Message", exception.getMessage());
        }
    }

    @FXML
    private void editMemberClearImage(ActionEvent e) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to clear the avatar?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            image = new Image("/resources/image/avatar.png");
            editMemberImage.setImage(image);
        }
    }

    public void addMemberImportBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(manager_dashboard.getScene().getWindow());

        if (file != null) {
            addMemberImage = new Image(file.toURI().toString(), 200, 237, false, true);
            addMemberImageView.setImage(addMemberImage);
        }
    }

    public void editMemberImportBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(manager_dashboard.getScene().getWindow());

        if (file != null) {
            image = new Image(file.toURI().toString(), 200, 237, false, true);
            editMemberImage.setImage(image);
        }
    }

    public void addBookImportBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(manager_dashboard.getScene().getWindow());

        if (file != null) {
            addBookImage = new Image(file.toURI().toString(), 200, 237, false, true);
            addBookImageView.setImage(addBookImage);
        }
    }

    public void addBook() {
        try {
            String isbn = addBookISBN.getText();
            String[] authors = addBookAuthor.getText().split("\\s*,\\s*");
            String title = addBookTitle.getText();
            String[] genres = addBookGenre.getText().split("\\s*,\\s*");
            String publisher = addBookPublisher.getText();
            String description = addBookDescription.getText();
            
            int publicationYear;
            try {
                publicationYear = Integer.parseInt(addBookPublicationYear.getText());
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Error Message", "Publication year must be a valid number!");
                return;
            }
            
            int copiesAvailable = addBookCopiesAvailable.getValue();
            librarySystem.addBook(
                title, 
                authors, 
                publisher, 
                publicationYear, 
                genres, 
                copiesAvailable, 
                description, 
                convertImageToBytes(addBookImage), 
                isbn
            );
            showAlert(AlertType.INFORMATION, "Success", "Book added successfully!");
            clearAddBookInput();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void clearAddBookInput() {
        addBookISBN.clear();
        addBookAuthor.clear();
        addBookTitle.clear();
        addBookGenre.clear();
        addBookPublisher.clear();
        addBookDescription.clear();
        addBookPublicationYear.clear();
        addBookImage = null;
        addBookImageView.setImage(addBookImage);
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

    private Image convertBytesToImage(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new Image(byteArrayInputStream);
    }

    private void showDashboardInformation() {
        memberNumber.setText(String.valueOf(librarySystem.getCountUser()));
        bookNumber.setText(String.valueOf(librarySystem.getBooks().size()));
        managerNumber.setText(String.valueOf(librarySystem.getCountAdmin()));
    }

    private void initializeSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
            valueFactory.setValue(0);
            addBookCopiesAvailable.setValueFactory(valueFactory);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        userRoleList();
        librarySystem = LibrarySystem.getInstance();
        memberListShowData();
        showDashboardInformation();
        addMemberImage = new Image("/resources/image/avatar.png");
        handleSearch();
        initializeSpinner();
    }
}