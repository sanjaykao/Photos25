package controller;

import java.io.*;
import java.util.ArrayList;

import javafx.event.*;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.AdminUser;
import model.User;


public class LoginController {

	@FXML public Button loginButton;
	
	@FXML public TextField username;
		
	private Scene adminScene;
	
	private Scene userScene;
	
	public static final String storeDir = ".";
	
	public static final String storeFile = "users.dat";
	
	public ArrayList<User> users;
	
	public UserHomeController userController;
	
	public AdminHomeController adminController; 
	
	public void start(Stage mainStage) {

	 }
	
	@FXML 
	private void loginAction(ActionEvent event) throws IOException, ClassNotFoundException {
		
		String user = username.getText().trim();
		File datFile = new File(storeFile);
		
		//if admin, open adminScene
		if (user.equals("admin")) {
			openAdminScene(event);
		} else if(datFile.exists()){
			FileInputStream fis = new FileInputStream(datFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			AdminUser adminUser = (AdminUser) ois.readObject();
			users = adminUser.getUsers();
			ois.close();
			fis.close();
				
			String existingUsername;
			for(User existingUser : users) {
				existingUsername = existingUser.getUsername();
				if(existingUsername.equals(user)) {
					userController.initCurrentUser(existingUser);
					openUserScene(event);
				}
			}	

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error with logging in");
			alert.setContentText("Username does not exist");
			alert.showAndWait();
		}
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
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setScene(userScene);
	}
	
	public void setUserController(UserHomeController controller) {
		this.userController = controller;		
	}
	
	public void setAdminController(AdminHomeController controller) {
		this.adminController = controller;
	}
	
}
