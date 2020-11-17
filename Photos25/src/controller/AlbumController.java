package controller;

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
import model.Album;
import model.Photo;
import model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import app.Photos;


public class AlbumController {
	@FXML
	private TilePane tilePane;
	
	private ArrayList<Photo> photos;
	private String selectedPhoto;
	private User user;
	private Album album;

	private Scene userScene;
	private Scene loginScene;
	
	@FXML
	private void initialize() {
		selectedPhoto = "";
	}
	
	public void start(Stage mainStage) {
		
	}
	
	@FXML
	private void addPhoto(ActionEvent event) {
		
	}
	
	@FXML
	private void deletePhoto(ActionEvent event) {
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirm changes");
    		alert.setContentText("Are you sure you want to make these changes?");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			album.deletePhoto(selectedPhoto);
    			User.write(user, user.getUsername());
    			photos = album.getPhotos();
    			tilePane.getChildren().clear();
    			if(photos.size() > 0) {
    				displayPhotos();
    			}
    		}
		}
	}
	
	@FXML
	private void addCap(ActionEvent event) {
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirm changes");
    		alert.setContentText("Are you sure you want to make these changes?");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			TextInputDialog td = new TextInputDialog();
    			td.setTitle("Add/edit caption");
    			td.setHeaderText("Enter caption");
    			Optional<String> cap = td.showAndWait();
    			if(cap.isPresent()) {
    				String caption = td.getEditor().getText();
    				for(Photo photo : photos) {
    					if(photo.getPhotoName().equals(selectedPhoto)) {
    						user.addCaption(caption, photo);
    						break;
    					}
    				}
    				User.write(user, user.getUsername());
    				photos = album.getPhotos();
    				tilePane.getChildren().clear();
    				displayPhotos();
    			}
    		}
		}
	}
	
	@FXML
	private void addTag(ActionEvent event) {
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			
		}
	}
	
	@FXML
	private void deleteTag(ActionEvent event) {
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			
		}
	}
	
	@FXML
	private void copyPhoto(ActionEvent event) {
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			
		}
	}
	
	@FXML
	private void movePhoto(ActionEvent event) {
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			
		}
	}
	
	@FXML
	private void preview(ActionEvent event) {
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			
		}
	}
	
	@FXML
	private void slideshow(ActionEvent event) {
		
	}
	
	@FXML
	private void homePage(ActionEvent event) {
		openUserScene(event);
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
	
	public void setUserScene(Scene scene) {
		userScene = scene;
	}
	
	public void openUserScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("User Home Page");
		primaryStage.setScene(userScene);
	}
	
	public void initCurrentUserAlbum(User user, Album album) {
		User temp = user;
		Album temp1 = album;
		setCurrentUserAlbum(temp, temp1);
	}
	
	private void setCurrentUserAlbum(User user, Album album) {
		this.user = user;
		this.album = album;
		//readSerial();
		displayPhotos();
	}
	
	private void displayPhotos() {
		for(Photo photo : photos) {
			File file = new File("./data/pic7.JPG");
			Image image = new Image(file.toURI().toString(), 100, 110, false, false);
			ImageView imageView = new ImageView(image);
			imageView.setUserData(photo);
			Text details = new Text(photo.getCaption());
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(imageView);
			vbox.getChildren().add(details);
			tilePane.getChildren().add(vbox);
			imageView.setOnMouseClicked(e -> {
				selectedPhoto = photo.getPhotoName();
			});
		}
	}
	
	private void setWarning(String title, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
