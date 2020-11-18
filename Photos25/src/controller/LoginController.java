package controller;

import java.io.*;
import java.util.ArrayList;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AdminUser;
import model.User;


public class LoginController {

	@FXML public Button loginButton;
	@FXML public TextField username;
	private ArrayList<User> users;
	private static final String storeFile = "users.dat";
	
	
	private Scene adminScene;	
	private Scene userScene;
	
	public UserHomeController userController;
	//public AdminHomeController adminController; 
	
	public void start(Stage mainStage) {

	 }
	
	@FXML 
	private void loginAction(ActionEvent event) throws IOException, ClassNotFoundException {
		
		String user = username.getText().trim();
		File datFile = new File(storeFile);
		
		//if admin, open adminScene
		if (user.equals("admin")) {
			openAdminScene(event);
		} else {
			if(datFile.exists()) {
				FileInputStream fis = new FileInputStream(datFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				AdminUser adminUser = (AdminUser) ois.readObject();
				users = adminUser.getUsers();
				ois.close();
				fis.close();
					
				String existingUsername;
				boolean userExists = false;
				for(User existingUser : users) {
					existingUsername = existingUser.getUsername();
					if(existingUsername.equals(user)) {
						userExists = true;
						userController.initCurrentUser(existingUser);
						openUserScene(event);
					}
				}	
				if(!userExists) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Error with logging in");
					alert.setContentText("Username does not exist");
					alert.showAndWait();
				}
			}else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Error with logging in");
				alert.setContentText("Username does not exist");
				alert.showAndWait();
			}
		} 
	}
	
	public void setAdminScene(Scene scene) {
		adminScene = scene;
	}
	
	public void openAdminScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("Admin User Home Page");
		primaryStage.setScene(adminScene);
	}
	
	public void setUserScene(Scene scene) {
		userScene = scene;
	}
	
	public void openUserScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("User Home Page");
		primaryStage.setScene(userScene);
	}
	
	public void setUserController(UserHomeController controller) {
		this.userController = controller;		
	}
	
	/*public void setAdminController(AdminHomeController controller) {
		this.adminController = controller;
	}*/
	
}
