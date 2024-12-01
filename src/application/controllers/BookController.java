package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import application.logic.Book;

public class BookController {
    Book book;

    @FXML
    private Label bookAuthor;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookTitle;

    public void setData(Book book) {
        this.book = book;
        Image image = convertBytesToImage(book.getCoverImage());
        bookCover.setImage(image);
        bookAuthor.setText(book.getAuthorsAsString());
        bookTitle.setText(book.getTitle());
    }

    private Image convertBytesToImage(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new Image(byteArrayInputStream);
    }

    @FXML
    public void onBookClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BookDetails.fxml"));
            Parent root = fxmlLoader.load();

            // Lấy controller của BookDetails để truyền dữ liệu
            BookDetailsController detailsController = fxmlLoader.getController();
            detailsController.setBookData(book);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Đặt kiểu modal
            stage.setTitle("Book Details");
            stage.showAndWait(); // Chờ đóng
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
