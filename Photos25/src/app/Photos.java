package app;

import controller.*;
import view.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Photos extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub

		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		LoginController controller = 
				loader.getController();
		controller.start(stage);

        Scene scene = new Scene(root);

	}

	public void main(String[] args) {
		// TODO Auto-generated method stub

		launch(args);
	}

}
