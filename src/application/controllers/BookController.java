package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

import application.logic.Book;

public class BookController {
    @FXML
    private Label bookAuthor;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookTitle;

    public void setData(Book book) {
        Image image = convertBytesToImage(book.getCoverImage());
        bookCover.setImage(image);
        bookAuthor.setText(book.getAuthorsAsString());
        bookTitle.setText(book.getTitle());
    }

    private Image convertBytesToImage(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new Image(byteArrayInputStream);
    }
}
