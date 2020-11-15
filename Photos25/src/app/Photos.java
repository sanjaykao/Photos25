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
		FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		loginLoader.setLocation(
				getClass().getResource("/view/login.fxml"));
		AnchorPane loginRoot = (AnchorPane)loginLoader.load();
		
		LoginController loginController = 
				loginLoader.getController();
		

        Scene loginScene = new Scene(loginRoot);
        
        // get loader and pane for admin
        FXMLLoader adminLoader = new FXMLLoader();
        adminLoader.setLocation(
				getClass().getResource("/view/AdminHome.fxml"));

        AnchorPane adminRoot = (AnchorPane)adminLoader.load();
		
		//might be needed for logging out
        //AdminHomeController adminController = loginLoader.getController();

        Scene adminScene = new Scene(adminRoot);
        
        //get loader and pane for user
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(
        		getClass().getResource("/view/UserHome.fxml"));
        
        AnchorPane userRoot = (AnchorPane)userLoader.load();
        
        //might be needed for logging out
        //UserHomeController userController = loginLoader.getController();
        
        Scene userScene = new Scene(userRoot);
        
        // send admin and user scenes to login controller
        loginController.setAdminScene(adminScene);
        loginController.setUserScene(userScene);
        
        
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		launch(args);
	}

}
