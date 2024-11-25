package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			stage.setTitle("Library Manager");
			stage.getIcons().add(new Image("/resources/image/icon.png"));
			
			showLoginScreen(stage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void showLoginScreen(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/controllers/LoginScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
