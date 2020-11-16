package controller;

import java.util.ArrayList;
import java.util.Optional;

import javafx.event.*;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
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
	
	@FXML
	private void initialize() {
		readSerial();
		selectedAlbum = "";
		tilePane = new TilePane();
	}
	
	public void start(Stage mainStage) {
		if(albums.size() > 0) {
			displayAlbums();
		}
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
				User.write(user);
				readSerial();
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
    			User.write(user);
    			readSerial();
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
						//add check to see if new name is same as another album
						user.renameAlbum(album, name);
						User.write(user);
						readSerial();
						displayAlbums();
						break;
					}
					break;
				}
			}
		}
	}
	
	@FXML
	private void openAlbum(ActionEvent event) {
		openAlbumScene(event);
	}
	
	@FXML
	private void searchPhotos(ActionEvent event) {
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
		primaryStage.setScene(loginScene);
	}
	
	public void setAlbumScene(Scene scene) {
		albumScene = scene;
	}
	
	public void openAlbumScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setScene(albumScene);
	}
	
	public void setSearchScene(Scene scene) {
		searchScene = scene;
	}
	
	public void openSearchScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setScene(searchScene);
	}
	
	private void displayAlbums() {
		for(Album album : albums) {
			if(album.getNumOfPhotos() == 0) {
				Image image = new Image("/data/shrek.jpeg", 60, 0, false, false);
				ImageView imageView = new ImageView(image);
				Text details = new Text(album.getAlbumName() + "\n" + album.getNumOfPhotos());
				StackPane pane = new StackPane();
				pane.getChildren().add(imageView);
				pane.getChildren().add(details);
				tilePane.getChildren().add(pane);
				imageView.setOnMouseClicked(e -> {
					selectedAlbum = album.getAlbumName();
				});
			}else {
				ArrayList<Photo> photos = album.getPhotos();
				Image image = photos.get(photos.size() - 1).image;
				ImageView imageView = new ImageView(image);
				Text details = new Text(album.getAlbumName() + "\n" + album.getNumOfPhotos() + "\n" + album.getEarliestDate() + " - " + album.getLatestDate());
				StackPane pane = new StackPane();
				pane.getChildren().add(imageView);
				pane.getChildren().add(details);
				tilePane.getChildren().add(pane);
				imageView.setOnMouseClicked(e -> {
					selectedAlbum = album.getAlbumName();
				});
			}
		}
	}
	
	private void readSerial() {
		user = User.read(user);
		albums = user.getAlbums();
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
