package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import model.AdminUser;
import model.Album;
import model.Photo;
import model.User;

import java.io.File;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

//import app.Photos;


public class AdminHomeController {
	@FXML
	ListView<String> listView;
	
	private ObservableList<String> obsList;
	private ArrayList<User> users;
	private AdminUser admin;
	private User stockUser;
	
	private Scene loginScene;
	
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
		System.out.println(getList());
		
		if(users.size() > 0) {
			//System.out.println("arraylist is not empty");
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
				//File userFile = new File("." + File.separator + name + ".dat");
				User newUser = admin.addUser(name);
				User.write(newUser, name);
				AdminUser.write(admin);
				users = admin.getUsers();
				
				obsList = getList();
				listView.setItems(obsList);
				int ind = getIndex(name);
				listView.getSelectionModel().select(ind);
				System.out.println(getList());
			}
		}
	}
	
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
	
	public void setLoginScene(Scene scene) {
		loginScene = scene;
	}
	
	public void openLoginScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("Login");
		primaryStage.setScene(loginScene);
	}
	
	public User initStockUser() {
		File datFile = new File("./users.dat");
		
		//create the stock account
		if(!datFile.exists() || !datFile.isFile() || !datFile.canRead()) {
			System.out.println("enters stock init");
			Album stockAlbum = new Album("stock");
			File stockPhotoFile;
			for(int i = 1; i <= 7; i++) {
				stockPhotoFile = new File("./data/pic" + Integer.toString(i) +".JPG");
				
				if(stockPhotoFile != null) {
					Image pic = new Image(stockPhotoFile.toURI().toString());
					String picName = stockPhotoFile.getAbsolutePath(); 
					Calendar date = Calendar.getInstance();
					Photo newPic = new Photo(picName, pic, date);
					System.out.print(newPic.getPhotoName() + " ");
					stockAlbum.addPhotoToAlbum(newPic);
				}
				
			}
			
			for(int i = 0; i < stockAlbum.getNumOfPhotos(); i++) {
				System.out.print(stockAlbum.getPhotos().get(i) + " ");
			}
			
			System.out.println(stockAlbum.getNumOfPhotos());
			
			User stockUser = new User("stock");
			stockUser.addAlbum(stockAlbum);
			
			System.out.println(stockUser.getAlbums());
			return stockUser;
		}
		
		return null;
	}
	
	private void getSelected() {
		
	}
	
	private int getIndex(String name) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getUsername().equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	private void readSerial() {
		admin = AdminUser.read();
		if(admin != null) {
			users = admin.getUsers();
		}else {
			admin = new AdminUser();
			users = admin.getUsers();
		}
	}
	
	private ObservableList<String> getList(){
		ObservableList<String> temp = FXCollections.observableArrayList();
		for(User user : users) {
			temp.add(user.getUsername());
		}
		return temp;
	}
	
	private boolean exists(String name) {
		for(User user : users) {
			if(user.getUsername().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	private void setWarning(String title, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
