package app;

import controller.*;
//import view.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Photos extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		// get loader and pane for login 
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		LoginController controller = 
				loader.getController();

        Scene scene = new Scene(root);
        
        // get loader and pane for admin
        FXMLLoader loader1 = new FXMLLoader();
        loader1.setLocation(
				getClass().getResource("/view/AdminHome.fxml"));
		AnchorPane root1 = (AnchorPane)loader.load();
		
		AdminHomeController controller1 = 
				loader.getController();

        Scene scene1 = new Scene(root1);
        
        // send admin scene to login controller
        
        
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		launch(args);
	}

}
