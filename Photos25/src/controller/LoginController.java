package controller;

import java.io.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import model.Album;
import app.Photos;


public class LoginController {

	@FXML public Button loginButton;
	
	@FXML public TextField username;
	
	private Scene adminScene;
	
	private Scene userScene;
	
	private final String path = "users.dat";
	
	public void start(Stage mainStage) {
		
	 }
	
	@FXML 
	private void loginAction(ActionEvent event) {
		
		String user = username.getText().trim();
		File existingFile = new File(path);
		
		//create the stock account
		if(!existingFile.exists()) {
			try {
				existingFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Album stockAlbum = new Album("stock");
			String stockPath = "data";
			File stockPhotoFile;
			
			
		}
		
		//if admin, open adminScene
		/*if(user.equals("admin")) {
			openAdminScene(event);
		} */
		//if user, open userScene
	}
	
	public void setAdminScene(Scene scene) {
		adminScene = scene;
	}
	
	public void openAdminScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setScene(adminScene);
	}
	
	public void setUserScene(Scene scene) {
		userScene = scene;
	}
	
	public void openUserScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Stage) (event.getSource())).getScene().getWindow();
		primaryStage.setScene(userScene);
	}
	
}
