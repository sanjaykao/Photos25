package controller;

import java.io.File;
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

//import app.Photos;


public class UserHomeController {
	@FXML
	private TilePane tilePane;
	
	private ArrayList<Album> albums;
	private String selectedAlbum;
	private User user;
	
	private Scene loginScene;
	private Scene albumScene;
	private Scene searchScene;
	
	public AlbumController albumController;
	public SearchPageController searchController;
	
	@FXML
	private void initialize() {
		selectedAlbum = "";
	}
	
	public void start(Stage mainStage) {
		
	}
	
	@FXML
	private void createAlbum(ActionEvent event) {
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
				tilePane.getChildren().clear();
				displayAlbums();	
			}
		}
	}
	
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
    			//readSerial();
    			tilePane.getChildren().clear();
    			if(albums.size() > 0) {
    				displayAlbums();
    			}
    		}
		}
	}
	
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
		}
	}
	
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
	
	@FXML
	private void searchPhotos(ActionEvent event) {
		searchController.initCurrentUser(user);
		openSearchScene(event);
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
	
	public void setAlbumScene(Scene scene) {
		albumScene = scene;
	}
	
	public void openAlbumScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("Album Details");
		primaryStage.setScene(albumScene);
	}
	
	public void setSearchScene(Scene scene) {
		searchScene = scene;
	}
	
	public void openSearchScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("Search");
		primaryStage.setScene(searchScene);
	}
	
	public void setAlbumController(AlbumController controller) {
		albumController = controller;
	}
	
	public void setSearchController(SearchPageController controller) {
		searchController = controller;
	}
	
	public void initCurrentUser(User user) {
		User temp = user;
		setCurrentUser(temp);
	}
	
	private void setCurrentUser(User user) {
		this.user = user;
		readSerial();
		displayAlbums();
	}
	
	private void displayAlbums() {
		for(Album album : albums) {
			if(album.getNumOfPhotos() == 0) {
				File file = new File("./data/pic7.JPG");
				Image image = new Image(file.toURI().toString(), 100, 110, false, false);
				ImageView imageView = new ImageView(image);
				imageView.setUserData(album);
				Text details = new Text(album.getAlbumName() + "\n" + album.getNumOfPhotos());
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
				Image image = photos.get(photos.size() - 1).getImage();
				ImageView imageView = new ImageView(image);
				Text details = new Text(album.getAlbumName() + "\n" + album.getNumOfPhotos() + "\n" + album.getEarliestDate() + " - " + album.getLatestDate());
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
		
	private boolean exists(String name) {
		for(Album album : albums) {
			if(album.getAlbumName().equals(name)) {
				return true;
			}
		}
		return false;
	}
		
	private void setWarning(String title, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}	
}
