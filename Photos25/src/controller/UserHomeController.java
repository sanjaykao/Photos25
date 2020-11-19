package controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.*;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import model.Album;
import model.Photo;

/*
 * User Home Controller allows user to see and edit their existing albums
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

public class UserHomeController {
	@FXML private TilePane tilePane;
	
	private ArrayList<Album> albums;
	private String selectedAlbum;
	private User user;
	
	private Scene loginScene;
	private Scene albumScene;
	private Scene searchScene;
	
	public AlbumController albumController;
	public SearchPageController searchController;
	
	/*
	 * Initializes the controller when called in Photos.java
	 */
	@FXML
	private void initialize() {
		selectedAlbum = "";
	}
	
	/*
	 * Allows users to add album to their account
	 * @param event The event when clicked on
	 */
	@FXML
	private void createAlbum(ActionEvent event) {
		if(!selectedAlbum.equals("")) {
			selectedAlbum = "";
		}
		TextInputDialog td = new TextInputDialog();
		td.setTitle("Create new album");
		td.setHeaderText("Enter a name");
		Optional<String> result = td.showAndWait();
		
		if(result.isPresent()) {
			String name = td.getEditor().getText();
			if(exists(name)) {
				setWarning("Cannot create album", "This album already exists!");
			}else {
				user.createAlbum(name);
				User.write(user, user.getUsername());
				albums = user.getAlbums();
				System.out.println(albums.get(1));
				tilePane.getChildren().clear();
				displayAlbums();	
			}
		}
	}
	
	/*
	 * Allows user to delete existing albums
	 * @param event The event when clicked on
	 */
	@FXML
	private void deleteAlbum(ActionEvent event) {
		if(selectedAlbum.equals("")) {
			setWarning("No album selected", "Please select an album or add more albums.");
		}else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirm changes");
    		alert.setContentText("Are you sure you want to make these changes?");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			user.deleteAlbum(selectedAlbum);
    			User.write(user, user.getUsername());
    			albums = user.getAlbums();
    			tilePane.getChildren().clear();
    			if(albums.size() > 0) {
    				displayAlbums();
    			}
    		}
    		selectedAlbum = "";
		}
	}
	
	/*
	 * Allows users to rename their existing albums
	 * @param event The event when clicked on
	 */
	@FXML
	private void renameAlbum(ActionEvent event) {
		if(selectedAlbum.equals("")) {
			setWarning("No album selected", "Please select an album or add more albums.");
		}else {
			for(Album album : albums) {
				if(album.getAlbumName().equals(selectedAlbum)) {
					TextInputDialog td = new TextInputDialog();
					td.setTitle("Rename album");
					td.setHeaderText("Enter a new name");
					Optional<String> result = td.showAndWait();
					
					if(result.isPresent()) {
						String name = td.getEditor().getText();
						boolean nameTaken = false;
						//add check to see if new name is same as another album
						for(Album item : albums) {
							if(item.getAlbumName().equals(album.getAlbumName())) {
								continue;
							}
							if(item.getAlbumName().equals(name)) {
								nameTaken = true;
								break;
							}
						}
						if(nameTaken) {
							setWarning("Can't rename album", "There is already another album with this name!");
						}else {
							user.renameAlbum(album, name);
							User.write(user, user.getUsername());
							tilePane.getChildren().clear();
							displayAlbums();
						}
						break;
					}
					break;
				}
			}
			selectedAlbum = "";
		}
	}
	
	/*
	 * Allows users to open an album page to see the photos and other details
	 * @param event The event when clicked on
	 */
	@FXML
	private void openAlbum(ActionEvent event) {
		if(selectedAlbum.equals("")) {
			setWarning("No album selected", "Please select an album or add more albums.");
		}else {
			for(Album album : albums) {
				if(album.getAlbumName().equals(selectedAlbum)) {
					albumController.initCurrentUserAlbum(user, album);
					break;
				}
			}
			openAlbumScene(event);
		}
	}
	
	/*
	 * Allows users to switch over to the Search Page
	 * @param event The event when clicked on
	 */
	@FXML
	private void searchPhotos(ActionEvent event) {
		searchController.initCurrentUser(user);
		openSearchScene(event);
	}
	
	/*
	 * Logs out of the user session and return to login page
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
	 * Opens the Login Page
	 * @param event The event when clicked on
	 */
	public void openLoginScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("Login");
		primaryStage.setScene(loginScene);
	}
	
	/*
	 * Set the local Album scene field to same scene as that of Photos.java
	 * @param scene Album scene from main application
	 */
	public void setAlbumScene(Scene scene) {
		albumScene = scene;
	}
	
	/*
	 * Opens the Album details Page
	 * @param event The event when clicked on
	 */
	public void openAlbumScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("Album Details");
		primaryStage.setScene(albumScene);
	}
	
	/*
	 * Set the local Search scene field to same scene as that of Photos.java
	 * @param scene Search scene from main application
	 */
	public void setSearchScene(Scene scene) {
		searchScene = scene;
	}
	
	/*
	 * Opens the Search Page
	 * @param event The event when clicked on
	 */
	public void openSearchScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("Search");
		primaryStage.setScene(searchScene);
	}
	
	/*
	 * Transfers the album controller object in Photos.java to this file
	 * @param controller Album Controller from Photos.java
	 */
	public void setAlbumController(AlbumController controller) {
		albumController = controller;
	}
	
	/*
	 * Transfers the search controller object in Photos.java to this file
	 * @param controller Search Controller from Photos.java
	 */
	public void setSearchController(SearchPageController controller) {
		searchController = controller;
	}
	
	/*
	 * Receives current user from Login Page
	 * @param user Current user that logged in
	 */
	public void initCurrentUser(User user) {
		this.user = user;
		readSerial();
		tilePane.getChildren().clear();
		displayAlbums();
	}
	
	/*
	 * Receives current user from Album Details and Search Pages
	 * @param user Current user that logged in
	 */
	public void initCurrentUser2(User user) {
		this.user = user;
		albums = user.getAlbums();
		tilePane.getChildren().clear();
		displayAlbums();
	}
	
	/*
	 * Displays all albums in tile pane
	 */
	private void displayAlbums() {
		for(Album album : albums) {
			if(album.getNumOfPhotos() == 0) {
				File file = new File("./data/pic7.JPG");
				Image image = new Image(file.toURI().toString(), 150, 110, false, false);
				ImageView imageView = new ImageView(image);
				imageView.setUserData(album);
				Text details = new Text(album.getAlbumName() + "\n  " + album.getNumOfPhotos() + "\n");
				VBox vbox = new VBox();
				vbox.setAlignment(Pos.CENTER);
				vbox.getChildren().add(imageView);
				vbox.getChildren().add(details);
				tilePane.getChildren().add(vbox);
				imageView.setOnMouseClicked(e -> {
					selectedAlbum = album.getAlbumName();
				});
			}else {
				ArrayList<Photo> photos = album.getPhotos();
				//Image image = photos.get(photos.size() - 1).getImage();
				File file = new File(photos.get(photos.size() - 1).getPhotoName());
				Image image = new Image(file.toURI().toString(), 150, 110, false, false);
				ImageView imageView = new ImageView(image);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Text details = new Text("                    " + album.getAlbumName() + "\n                         " + album.getNumOfPhotos() + "\n" + dateFormat.format(album.getEarliestDate().getTime()) + " - " + dateFormat.format(album.getLatestDate().getTime()));
				VBox vbox = new VBox();
				vbox.setAlignment(Pos.CENTER);
				vbox.getChildren().add(imageView);
				vbox.getChildren().add(details);
				tilePane.getChildren().add(vbox);
				imageView.setOnMouseClicked(e -> {
					selectedAlbum = album.getAlbumName();
				});
			}
		}
	}
	
	/*
	 * Resets the admin and list of albums after every change to the list
	 */
	private void readSerial() {
		String name = user.getUsername();
		user = User.read(name);
		if(user != null) {
			albums = user.getAlbums();
		}else {
			user = new User(name);
			albums = user.getAlbums();
		}
	}
	
	/*
	 * Checks to see if the album name is already taken
	 * @param name Album name to be added
	 * @return True if album name already exists in the list of albums
	 */
	private boolean exists(String name) {
		for(Album album : albums) {
			if(album.getAlbumName().equals(name)) {
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
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}	
}
