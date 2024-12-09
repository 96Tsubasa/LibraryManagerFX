package application.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import application.logic.Book;
import application.logic.LibrarySystem;
import application.logic.Rating;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class BookDetailsController implements Initializable {
    private Book book;
    
    private MemberDashboardController memberDashboardController;

    LibrarySystem librarySystem;

    @FXML
    private Label author;

    @FXML
    private Label copiesAvailable;

    @FXML
    private Label description;

    @FXML
    private Label genre;

    @FXML
    private Label id;

    @FXML
    private ImageView image;

    @FXML
    private Label isbn;

    @FXML
    private Label publicationYear;

    @FXML
    private Label publisher;

    @FXML
    private Label title;

    @FXML
    private VBox ratingContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Spinner<Integer> star;

    @FXML
    private TextArea comment;

    public void setMemberDashboardController(MemberDashboardController controller) {
        this.memberDashboardController = controller;
    }

    public void setBookData(Book book) {
        this.book = book;
        title.setText(book.getTitle());
        author.setText(book.getAuthorsAsString());
        isbn.setText("ISBN:\n" + book.getIsbn());
        publicationYear.setText("Publication Year:\n" + book.getPublicationYear());
        publisher.setText("Publisher:\n" + book.getPublisher());
        description.setText("Description:\n" + book.getDescription());
        id.setText("ID:\n" + book.getBookId());
        copiesAvailable.setText("Copies Available:\n" + book.getCopiesAvailable());
        genre.setText("Genre:\n" + book.getGenresAsString());
        image.setImage(convertBytesToImage(book.getCoverImage()));
        displayRatings();
    }

    public void rate() {
        try {
            librarySystem.addRating(LoginController.currentUser.getUserId(), 
            book.getBookId(), 
            star.getValue(),
            LocalDate.now(),
            comment.getText());
            showAlert(AlertType.INFORMATION, "Success", "Rate book successfully!");
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error Message", e.getMessage());
        }
    }

    public void displayRatings() {
    try {
        for (Rating rating : librarySystem.getRatingForBookId(book.getBookId())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rating.fxml"));
            Parent ratingItem = loader.load();

            RatingController controller = loader.getController();
            controller.setData(rating);

            ratingContainer.getChildren().add(ratingItem);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private Image convertBytesToImage(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new Image(byteArrayInputStream);
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
        librarySystem = LibrarySystem.getInstance();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1); // min, max, initial
        star.setValueFactory(valueFactory);
    }
}
