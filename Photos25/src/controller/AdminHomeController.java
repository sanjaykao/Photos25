package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import model.AdminUser;
import model.Album;
import model.Photo;
import model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

/*
 * Admin Controller controls the admin user's ability to add or delete regular users
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

public class AdminHomeController {
	@FXML
	ListView<String> listView;
	
	private ObservableList<String> obsList;
	private ArrayList<User> users;
	private AdminUser admin;
	private User stockUser;
	
	private Scene loginScene;
	
	/*
	 * When initialized, the admin controller initializes a stock account if this is the first session
	 */
	@FXML
	private void initialize() {		
		readSerial();

		//create the stock account upon startup
		stockUser = initStockUser();
		if(stockUser != null) {
			admin.addUser(stockUser);
			AdminUser.write(admin);
			User.write(stockUser, stockUser.getUsername());
			users = admin.getUsers();
		} 
		
		if(users.size() > 0) {
			obsList = FXCollections.observableArrayList();
	    	obsList = getList();

			listView.setItems(obsList); 

			// select the first item
			listView.getSelectionModel().select(0);

			// set listener for the items
			listView
			.getSelectionModel()
			.selectedIndexProperty()
			.addListener(
					(obs, oldVal, newVal) -> 
					getSelected());
		}
	}
	
	/*
	 * Add new user to the list of existing users
	 * @param event The event when clicked on
	 */
	@FXML
    private void addUser(ActionEvent event) {
		TextInputDialog td = new TextInputDialog();
		td.setTitle("Add a user");
		td.setHeaderText("Enter a name");
		Optional<String> result = td.showAndWait();
		
		if(result.isPresent()) {
			String name = td.getEditor().getText();
			if(exists(name)) {
				setWarning("Cannot add user", "This name is already taken!");
			}else {
				User newUser = admin.addUser(name);
				User.write(newUser, name);
				AdminUser.write(admin);
				users = admin.getUsers();
				
				obsList = getList();
				listView.setItems(obsList);
				int ind = getIndex(name);
				listView.getSelectionModel().select(ind);
			}
		}
	}
	
	/*
	 * Delete a user from the list of existing users
	 * @param event The event when clicked on
	 */
	@FXML
	private void deleteUser(ActionEvent event) {
		String item = listView.getSelectionModel().getSelectedItem();
		if(item != null) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirm changes");
    		alert.setContentText("Are you sure you want to make these changes?");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			int index = listView.getSelectionModel().getSelectedIndex();
    			if((index > 0) && (index == users.size() - 1)) {
    				index--;
    			}
    			
    			for(User user : users) {
    				if(user.getUsername().equals(item)) {
    					admin.deleteUserFile(user.getUsername());
    					admin.removeUser(user);
    					break;
    				}
    			}
    			
    			if(users.size() == 0) {
    				AdminUser.delete();
    				readSerial();
    			}else {
    				AdminUser.write(admin);
    				users = admin.getUsers();
    			}
    			
    			obsList = getList();
            	listView.setItems(obsList);
            	listView.getSelectionModel().select(index);
    		}
    	}else {
    		setWarning("No user selected", "Please select a user or add more users.");
    	}
	}
	
	/*
	 * Logs out of the admin session and return to login page
	 * @param event The event when clicked on
	 */
	@FXML
	private void logout(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm logout");
		alert.setContentText("Are you sure you want to logout?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			openLoginScene(event);
		}
	}
	
	/*
	 * Set the local login scene field to same scene as that of Photos.java
	 * @param scene Login scene from main application
	 */
	public void setLoginScene(Scene scene) {
		loginScene = scene;
	}
	
	/*
	 * Opens the login page
	 * @param event The event when clicked on
	 */
	public void openLoginScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("Login");
		primaryStage.setScene(loginScene);
	}
	
	/*
	 * Initialize the stock user and the stock album with already existing photos
	 * @return The new stock user
	 */
	public User initStockUser() {
		File datFile = new File("./users.dat");
		
		//create the stock account
		if(!datFile.exists() || !datFile.isFile() || !datFile.canRead()) {
			Album stockAlbum = new Album("stock");
			File stockPhotoFile;
			for(int i = 1; i <= 7; i++) {
				stockPhotoFile = new File("./data/pic" + Integer.toString(i) +".JPG");
				
				if(stockPhotoFile != null) {
					String picName = stockPhotoFile.getAbsolutePath(); 
					Calendar date = Calendar.getInstance();
					Photo newPic = new Photo(picName, date);
					stockAlbum.addPhotoToAlbum(newPic);
					stockAlbum.findEarliestDate();
					stockAlbum.findLatestDate();
				}
				
			}
			
			User stockUser = new User("stock");
			stockUser.addAlbum(stockAlbum);
			
			return stockUser;
		}
		
		return null;
	}
	
	/*
	 * Keeps track of the listener of which user is selected
	 */
	private void getSelected() {
		
	}
	
	/*
	 * Gets a user's index in the arraylist of users
	 * @param name Username of the user
	 * @return Index of the user in the arraylist
	 */
	private int getIndex(String name) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getUsername().equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	/*
	 * Resets the admin and list of users after every change to the list
	 */
	private void readSerial() {
		admin = AdminUser.read();
		if(admin != null) {
			users = admin.getUsers();
		}else {
			admin = new AdminUser();
			users = admin.getUsers();
		}
	}
	
	/*
	 * Gets the list of users from the users.dat file
	 * @return List of existing users
	 */
	private ObservableList<String> getList(){
		ObservableList<String> temp = FXCollections.observableArrayList();
		for(User user : users) {
			temp.add(user.getUsername());
		}
		return temp;
	}
	
	/*
	 * Checks to see if username already taken
	 * @return True if username already exists
	 */
	private boolean exists(String name) {
		for(User user : users) {
			if(user.getUsername().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Creates a popup warning whenever an invalid input occurs
	 * @param title Text for the header
	 * @param content Text for the main message
	 */
	private void setWarning(String title, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
