package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import application.logic.Book;
import application.logic.LibrarySystem;

public class BookController {
    private Book book;
    private LibrarySystem librarySystem;
    private final int maxRating = 5;

    private MemberDashboardController memberDashboardController;

    @FXML
    private Label bookAuthor;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookTitle;

    @FXML
    private ImageView rating;

    public void setMemberDashboardController(MemberDashboardController controller) {
        this.memberDashboardController = controller;
    }

    public void setData(Book book) {
        this.book = book;
        librarySystem = LibrarySystem.getInstance();
        Image image = convertBytesToImage(book.getCoverImage());
        bookCover.setImage(image);
        bookAuthor.setText(book.getAuthorsAsString());
        bookTitle.setText(book.getTitle());
        showRating();
    }

    private void showRating() {
        if (librarySystem.getRatingForBookId(book.getBookId()).isEmpty()) {
            rating.setImage(new Image("/resources/image/norating.png"));
            return;
        }

        Image image = new Image(getClass().getResource("/resources/image/rating.png").toExternalForm());
        double percentage = librarySystem.getAvgBookRating(book.getBookId()) / maxRating;
        double width = image.getWidth() * percentage;
        double height = image.getHeight();
        Rectangle2D viewport = new Rectangle2D(0, 0, width, height);
        rating.setImage(image);
        rating.setViewport(viewport);
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
            detailsController.setMemberDashboardController(this.memberDashboardController);

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
