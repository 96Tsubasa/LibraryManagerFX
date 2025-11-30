package application.controllers;

import java.io.ByteArrayInputStream;

import application.logic.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookCardController {
    @FXML
    private Label author;

    @FXML
    private Label copiesAvailable;

    @FXML
    private Label description;

    @FXML
    private Label genre;

    @FXML
    private Label isbn;

    @FXML
    private Label publicationYear;

    @FXML
    private Label publisher;

    @FXML
    private Label title;

    @FXML
    private ImageView image;

    public void setData(Book book) {
        author.setText(book.getAuthorsAsString());
        copiesAvailable.setText(String.valueOf(book.getCopiesAvailable()));
        description.setText(book.getDescription());
        genre.setText(book.getGenresAsString());
        isbn.setText(book.getIsbn());
        publicationYear.setText(String.valueOf(book.getPublicationYear()));
        publisher.setText(book.getPublisher());
        title.setText(book.getTitle());
        image.setImage(convertBytesToImage(book.getCoverImage()));
    }

    private Image convertBytesToImage(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new Image(byteArrayInputStream);
    }
}
