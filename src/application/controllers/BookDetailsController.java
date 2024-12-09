package application.controllers;

import java.io.ByteArrayInputStream;

import application.logic.Book;
import application.logic.LibrarySystem;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookDetailsController {
    private Book book;
    
    private MemberDashboardController memberDashboardController;

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
    private Button bookBtn;

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
    }

    public void borrowBook() {
        
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
}
