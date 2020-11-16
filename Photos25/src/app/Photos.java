package app;

import java.io.File;
import java.util.Calendar;

import controller.*;
//import view.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

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
        AdminHomeController adminController = (AdminHomeController)adminLoader.getController();
        adminController.setLoginScene(loginScene);

        Scene adminScene = new Scene(adminRoot);
        
        //intialize stock account
        User stockUser = initStock();
        if(stockUser != null) {
        	adminController.initAdmin(stockUser);
        }
        
        //get loader and pane for user
       /*FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(
        		getClass().getResource("/view/UserHome.fxml"));
        
        AnchorPane userRoot = (AnchorPane)userLoader.load();*/
        
        //might be needed for logging out
        //UserHomeController userController = loginLoader.getController();
        
       //Scene userScene = new Scene(userRoot);
        
        // send admin and user scenes to login controller
        loginController.setAdminScene(adminScene);
        //loginController.setUserScene(userScene);
        
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
	}
	
	public User initStock() {
		File existingFile = new File("users.dat");
		
		//create the stock account
		if(!existingFile.exists()) {
			Album stockAlbum = new Album("stock");
			File stockPhotoFile;
			for(int i = 1; i <= 7; i++) {
				stockPhotoFile = new File("data/pic" + Integer.toString(i) +".JPG");
				
				if(stockPhotoFile != null) {
					Image pic = new Image(stockPhotoFile.toURI().toString());
					String picName = stockPhotoFile.getName(); 
					Calendar date = Calendar.getInstance();
					Photo newPic = new Photo(picName, pic, date);
					stockAlbum.getPhotos().add(newPic);
				}
			}
			
			User stockUser = new User("stock");
			stockUser.addAlbum(stockAlbum);
			return stockUser;
		}
		
		return null;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		launch(args);
	}

}
