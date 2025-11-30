package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.logic.LibrarySystem;
import application.logic.Rating;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RatingController implements Initializable {
    LibrarySystem librarySystem;
    Rating rating;

    @FXML
    private TextArea comment;

    @FXML
    private ImageView star;

    @FXML
    private Label username;

    public void setData(Rating rating) {
        this.rating = rating;
        username.setText(librarySystem.getUserById(rating.getUserId()).getUsername());
        comment.setText(rating.getComment());
        showRating();
    }

    private void showRating() {
        Image image = new Image(getClass().getResource("/resources/image/rating.png").toExternalForm());
        double percentage = rating.getStar() / 5.0;
        double width = image.getWidth() * percentage;
        double height = image.getHeight();
        Rectangle2D viewport = new Rectangle2D(0, 0, width, height);
        star.setImage(image);
        star.setViewport(viewport);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        librarySystem = LibrarySystem.getInstance();
    }
}