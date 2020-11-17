package app;

import controller.*;
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
		
		LoginController loginController = loginLoader.getController();
		

        Scene loginScene = new Scene(loginRoot);
        
        // get loader and pane for admin
        FXMLLoader adminLoader = new FXMLLoader();
        adminLoader.setLocation(
				getClass().getResource("/view/AdminHome.fxml"));

        AnchorPane adminRoot = (AnchorPane)adminLoader.load();
		
		//might be needed for logging out
        AdminHomeController adminController = (AdminHomeController)adminLoader.getController();
        adminController.setLoginScene(loginScene);

        Scene adminScene = new Scene(adminRoot);
        
        //get loader and pane for user
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(
        		getClass().getResource("/view/UserHome.fxml"));
        
        AnchorPane userRoot = (AnchorPane)userLoader.load();
        
        //might be needed for logging out
        UserHomeController userController = userLoader.getController();
        userController.setLoginScene(loginScene);
        
        Scene userScene = new Scene(userRoot);
        
        // get loader and pane for search page
        //user home --> search page
        //search page --> user home OR login
        FXMLLoader searchLoader = new FXMLLoader();
        searchLoader.setLocation(
				getClass().getResource("/view/SearchPage.fxml"));
        AnchorPane searchRoot = (AnchorPane)searchLoader.load();
        Scene searchScene = new Scene(searchRoot);
        
        SearchPageController searchController = searchLoader.getController();
        searchController.setLoginScene(loginScene);
        searchController.setUserScene(userScene);
        userController.setSearchScene(searchScene);
        
        // get loader and pane for album details page
        //user home --> album details
        //album details --> user home OR login
        FXMLLoader albumLoader = new FXMLLoader();
        albumLoader.setLocation(
				getClass().getResource("/view/Album.fxml"));
        AnchorPane albumRoot = (AnchorPane)albumLoader.load();
        Scene albumScene = new Scene(albumRoot);
        
        AlbumController albumController = albumLoader.getController();
        albumController.setLoginScene(loginScene);
        albumController.setUserScene(userScene);
        userController.setSearchScene(albumScene);
        
        // send admin and user scenes to login controller
        loginController.setAdminScene(adminScene);
        loginController.setUserScene(userScene);
        
        //pass controller parameters to other controllers
        loginController.setUserController(userController);
        //loginController.setAdminController(adminController);
        userController.setAlbumController(albumController);
        userController.setSearchController(searchController);
        
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		launch(args);
	}

}
