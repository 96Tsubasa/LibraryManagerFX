package application.controllers;

import java.io.ByteArrayInputStream;

import application.logic.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MemberCardController {
    @FXML
    private Label email;

    @FXML
    private ImageView image;

    @FXML
    private Label role;

    @FXML
    private Label username;

    public void setData(User user) {
        email.setText(user.getEmail());
        role.setText(user.getRole());
        username.setText(user.getUsername());
        if (user.getImageUser() == null) {
            image.setImage(new Image("/resources/image/avatar.png"));
        } else {
            image.setImage(convertBytesToImage(user.getImageUser()));
        }
    }

    private Image convertBytesToImage(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new Image(byteArrayInputStream);
    }
}
