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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Optional;

/*
 * Album Controller allows users to manipulate the photos inside each individual album
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

public class AlbumController {
	@FXML private TilePane tilePane;
	@FXML private AnchorPane anchorPane;
	@FXML private AnchorPane anchorPane2;
	@FXML private TextArea details;
	
	private ArrayList<Photo> photos;
	private String selectedPhoto;
	private User user;
	private Album album;
	private boolean slideClicked;
	private int index;

	private Scene userScene;
	private Scene loginScene;
	
	public UserHomeController userController;
	
	/*
	 * Initializes the controller when called in Photos.java
	 */
	@FXML
	private void initialize() {
		selectedPhoto = "";
		slideClicked = false;
		index = 0;
	}
	
	/*
	 * Adds a photo to the current album
	 * @param event The event when clicked on
	 */
	@FXML
	private void addPhoto(ActionEvent event) {
		if(!selectedPhoto.equals("")) {
			selectedPhoto = "";
		}
		if(slideClicked) {
			slideClicked = false;
		}
		Stage stage = (Stage)anchorPane.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		File selected = fileChooser.showOpenDialog(stage);
		if(selected != null) {
			String path = selected.getAbsolutePath();
			String ext = path.substring(path.lastIndexOf('.') + 1).toLowerCase();
			if(!ext.equals("jpg") && !ext.equals("jpeg") && !ext.equals("gif") && !ext.equals("bmp") && !ext.equals("png")) {
				setWarning("Can't add photo", "The file you selected is not the right format");
			}else {
				Calendar date = Calendar.getInstance();
				boolean photoExists = false;
				for(Photo photo : photos) {
					if(photo.getPhotoName().equals(path)) {
						photoExists = true;
						break;
					}
				}
				if(photoExists) {
					setWarning("Can't add photo", "Photo already exists in this album!");
				}else {
					Photo temp = new Photo(path, date);
					album.addPhotoToAlbum(temp);
					album.findEarliestDate();
					album.findLatestDate();
					User.write(user, user.getUsername());
					photos = album.getPhotos();
					tilePane.getChildren().clear();
					displayPhotos();
				}
			}
		}
	}
	
	/*
	 * Deletes a photo from the current album
	 * @param event The event when clicked on
	 */
	@FXML
	private void deletePhoto(ActionEvent event) {
		if(slideClicked) {
			slideClicked = false;
		}
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirm changes");
    		alert.setContentText("Are you sure you want to make these changes?");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if(result.get() == ButtonType.OK) {
    			album.deletePhoto(selectedPhoto);
    			album.findEarliestDate();
    			album.findLatestDate();
    			User.write(user, user.getUsername());
    			photos = album.getPhotos();
    			tilePane.getChildren().clear();
    			if(photos.size() > 0) {
    				displayPhotos();
    			}
    		}
    		selectedPhoto = "";
		}
	}
	
	/*
	 * Adds a caption to the selected photo
	 * @param event The event when clicked on
	 */
	@FXML
	private void addCap(ActionEvent event) {
		if(slideClicked) {
			slideClicked = false;
		}
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
    			for(Photo photo : photos) {
    				if(photo.getPhotoName().equals(selectedPhoto)) {
    					td.setHeaderText("Enter caption for " + photo.getPhotoName());
    					td.setContentText(photo.getCaption());
    					break;
    				}
    			}
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
    				if(photos.size() > 0) {
    					displayPhotos();
    				}
    			}
    		}
    		selectedPhoto = "";
		}
	}
	
	/*
	 * Adds a tag to the selected photo
	 * @param event The event when clicked on
	 */
	@FXML
	private void addTag(ActionEvent event) {
		if(slideClicked) {
			slideClicked = false;
		}
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			ArrayList<Tag> temp = new ArrayList<Tag>();
			for(Photo photo : photos) {
				if(photo.getPhotoName().equals(selectedPhoto)) {
					ArrayList<Tag> temp2 = photo.getTags();
					ArrayList<Tag> temp3 = user.getAlbumTags();
					for(Tag item1 : temp3) {
						boolean tagExists = false;
						for(Tag item2 : temp2) {
							if(item1.getName().equals(item2.getName()) && item1.getValue().equals(item2.getValue())) {
								tagExists = true;
								break;
							}
						}
						if(!tagExists) {
							temp.add(item1);
						}
					}
					break;
				}
			}
			ArrayList<String> tags = new ArrayList<String>();
			for(Tag tag : temp) {
				tags.add(tag.getName() + ":" + tag.getValue());
			}
			tags.add("Custom");
			ChoiceDialog<String> cd = new ChoiceDialog<String>(tags.get(0), tags);
			cd.setHeaderText("Add a tag to photo: " + selectedPhoto);
			cd.setContentText("Select an existing tag or select Custom to add a new one: ");
			Optional<String> result = cd.showAndWait();
			if(result.isPresent()) {
				String selected = cd.getSelectedItem();
				if(selected.equals("Custom")) {
					TextInputDialog td = new TextInputDialog();
					td.setTitle("Add a tag to photo: " + selectedPhoto);
					td.setHeaderText("Please enter a new tag in the format name:value");
					Optional<String> result2 = td.showAndWait();
					if(result2.isPresent()) {
						String newTag = td.getEditor().getText();
						int ind = newTag.indexOf(':');
						if(ind < 0) {
							setWarning("Can't add tag", "Incorrect format, please try again");
						}else {
							String name = newTag.substring(0, ind);
							String value = newTag.substring(ind + 1);
							for(Photo photo : photos) {
								if(photo.getPhotoName().equals(selectedPhoto)) {
									ArrayList<Tag> temp4 = photo.getTags();
									boolean tagUsed = false;
									for(Tag item : temp4) {
										if(item.getName().equals(name) && item.getValue().equals(value)) {
											tagUsed = true;
											break;
										}
									}
									if(tagUsed) {
										setWarning("Can't add tag", "The photo already has this tag");
									}else {
										user.addTag(photo, name, value);
										User.write(user, user.getUsername());
										photos = album.getPhotos();
										tilePane.getChildren().clear();
										displayPhotos();
									}
									break;
								}
							}
						}
					}
				}else {
					String name1 = selected.substring(0, selected.indexOf(':'));
					String value1 = selected.substring(selected.indexOf(':') + 1);
					for(Photo photo : photos) {
						if(photo.getPhotoName().equals(selectedPhoto)) {
							user.addTag(photo, name1, value1);
							User.write(user, user.getUsername());
							photos = album.getPhotos();
							tilePane.getChildren().clear();
							displayPhotos();
							break;
						}
					}
				}
			}
			selectedPhoto = "";
		}
	}
	
	/*
	 * Deletes a tag from the selected photo
	 * @param event The event when clicked on
	 */
	@FXML
	private void deleteTag(ActionEvent event) {
		if(slideClicked) {
			slideClicked = false;
		}
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			ArrayList<Tag> temp = new ArrayList<Tag>();
			for(Photo photo : photos) {
				if(photo.getPhotoName().equals(selectedPhoto)) {
					temp = photo.getTags();
					break;
				}
			}
			if(temp.size() == 0) {
				setWarning("Can't delete tag", "This photo has no tags");
			}else {
				ArrayList<String> tags = new ArrayList<String>();
				for(Tag item : temp) {
					tags.add(item.getName() + ":" + item.getValue());
				}
				ChoiceDialog<String> cd = new ChoiceDialog<String>(tags.get(0), tags);
				cd.setHeaderText("Delete a tag from photo: " + selectedPhoto);
				cd.setContentText("Select a tag: ");
				Optional<String> result = cd.showAndWait();
				if(result.isPresent()) {
					String selected = cd.getSelectedItem();
					String name = selected.substring(0, selected.indexOf(':'));
					String value = selected.substring(selected.indexOf(':') + 1);
					for(Photo photo : photos) {
						if(photo.getPhotoName().equals(selectedPhoto)) {
							user.deleteTag(photo, name, value);
							User.write(user, user.getUsername());
							photos = album.getPhotos();
							tilePane.getChildren().clear();
							if(photos.size() > 0) {
								displayPhotos();
							}
							break;
						}
					}
				}
			}
			selectedPhoto = "";
		} 
	}
	
	/*
	 * Copies a photo from one album to another
	 * @param event The event when clicked on
	 */
	@FXML
	private void copyPhoto(ActionEvent event) {
		if(slideClicked) {
			slideClicked = false;
		}
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else if(user.getAlbums().size() == 1){
			setWarning("Can't copy photo, only have 1 album", "Please add more albums");
		}else {
			ArrayList<Album> temp = user.getAlbums();
			ArrayList<String> albums = new ArrayList<String>();
			for(Album item : temp) {
				if(item.getAlbumName().equals(album.getAlbumName())) {
					continue;
				}else {
					albums.add(item.getAlbumName());
				}
			}
			ChoiceDialog<String> cd = new ChoiceDialog<String>(albums.get(0), albums);
			cd.setHeaderText("Copy photo: " + selectedPhoto);
			cd.setContentText("Copy to: ");
			Optional<String> result = cd.showAndWait();
			if(result.isPresent()) {
				String name = cd.getSelectedItem();
				for(Album item2 : temp) {
					if(item2.getAlbumName().equals(name)) {
						boolean exists = false;
						ArrayList<Photo> destPhotos = item2.getPhotos();
						for(Photo pic : destPhotos) {
							if(pic.getPhotoName().equals(selectedPhoto)) {
								exists = true;
								break;
							}
						}
						if(exists) {
							setWarning("Can't copy photo", "Photo already exists in destination album!");
						}else {
							for(Photo photo : photos) {
								if(photo.getPhotoName().equals(selectedPhoto)) {
									user.copyPhoto(item2, photo);
									item2.findEarliestDate();
									item2.findLatestDate();
									User.write(user, user.getUsername());
									setWarning("Success!", "Photo copied successfully");
									break;
								}
							}
						}
						break;
					}
				}
			}
			selectedPhoto = "";
		}
	}
	
	/*
	 * Moves a photo from one album to another
	 * @param event The event when clicked on
	 */
	@FXML
	private void movePhoto(ActionEvent event) {
		if(slideClicked) {
			slideClicked = false;
		}
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else if(user.getAlbums().size() == 1){
			setWarning("Can't move photo, only have 1 album", "Please add more albums");
		}else {
			ArrayList<Album> temp = user.getAlbums();
			ArrayList<String> albums = new ArrayList<String>();
			for(Album item : temp) {
				if(item.getAlbumName().equals(album.getAlbumName())) {
					continue;
				}else {
					albums.add(item.getAlbumName());
				}
			}
			ChoiceDialog<String> cd = new ChoiceDialog<String>(albums.get(0), albums);
			cd.setHeaderText("Copy photo: " + selectedPhoto);
			cd.setContentText("Copy to: ");
			Optional<String> result = cd.showAndWait();
			if(result.isPresent()) {
				String name = cd.getSelectedItem();
				for(Album item2 : temp) {
					if(item2.getAlbumName().equals(name)) {
						boolean exists = false;
						ArrayList<Photo> destPhotos = item2.getPhotos();
						for(Photo pic : destPhotos) {
							if(pic.getPhotoName().equals(selectedPhoto)) {
								exists = true;
								break;
							}
						}
						if(exists) {
							setWarning("Can't move photo", "Photo already exists in destination album!");
						}else {
							for(Photo photo : photos) {
								if(photo.getPhotoName().equals(selectedPhoto)) {
									user.movePhoto(item2, album, photo);
									album.findEarliestDate();
									album.findLatestDate();
									item2.findEarliestDate();
									item2.findLatestDate();
									User.write(user, user.getUsername());
									photos = album.getPhotos();
									tilePane.getChildren().clear();
									if(photos.size() > 0) {
										displayPhotos();
									}
									setWarning("Success!", "Photo moved successfully");
									break;
								}
							}
						}
						break;
					}
				}
			}
			selectedPhoto = "";
		}
	}
	
	/*
	 * Allows users to preview each picture with its details
	 * @param event The event when clicked on
	 */
	@FXML
	private void preview(ActionEvent event) {
		if(slideClicked) {
			slideClicked = false;
		}
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			details.clear();
			anchorPane2.getChildren().clear();
			for(Photo photo : photos) {
				if(photo.getPhotoName().equals(selectedPhoto)) {
					File file = new File(photo.getPhotoName());
					Image image = new Image(file.toURI().toString(), 430, 324, false, false);
					ImageView imageView = new ImageView(image);
					anchorPane2.getChildren().add(imageView);
					Calendar date = photo.getDate();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
					ArrayList<Tag> temp = photo.getTags();
					String tags = "";
					for(int i = 0; i < temp.size(); i++) {
						if(i == temp.size() - 1) {
							tags += temp.get(i).getName() + ":" + temp.get(i).getValue();
						}else {
							tags += temp.get(i).getName() + ":" + temp.get(i).getValue() + ", ";
						}
					}
					String content = "Caption: " + photo.getCaption() + "\n" + "Date added: " + dateFormat.format(date.getTime()) + "\n" + "Tags: " + tags;
					details.setText(content);
					selectedPhoto = "";
					break;
				}
			}
			selectedPhoto = "";
		}
	}
	
	/*
	 * Allows users to view each picture in an album in a slideshow
	 * @param event The event when clicked on
	 */
	@FXML
	private void slideshow(ActionEvent event) {
		if(!selectedPhoto.equals("")) {
			selectedPhoto = "";
		}
		if(slideClicked) {
			index = 0;
		}else {
			slideClicked = true;
		}
		if(photos.size() == 0) {
			setWarning("Can't open slideshow", "No photos to display, please add more photos");
		}else {
			details.clear();
			anchorPane2.getChildren().clear();
			File file = new File(photos.get(index).getPhotoName());
			Image image = new Image(file.toURI().toString(), 430, 324, false, false);
			ImageView imageView = new ImageView(image);
			anchorPane2.getChildren().add(imageView);
			Calendar date = photos.get(index).getDate();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
			ArrayList<Tag> temp = photos.get(index).getTags();
			String tags = "";
			for(int i = 0; i < temp.size(); i++) {
				if(i == temp.size() - 1) {
					tags += temp.get(i).getName() + ":" + temp.get(i).getValue();
				}else {
					tags += temp.get(i).getName() + ":" + temp.get(i).getValue() + ", ";
				}
			}
			String content = "Caption: " + photos.get(index).getCaption() + "\n" + "Date added: " + dateFormat.format(date.getTime()) + "\n" + "Tags: " + tags;
			details.setText(content);
		}
	}
	
	/*
	 * Returns the user to the User Home Page
	 * @param event The event when clicked on
	 */
	@FXML
	private void homePage(ActionEvent event) {
		userController.initCurrentUser2(user);
		openUserScene(event);
	}
	
	/*
	 * Logs out of the user session and return to login page
	 * @param event The event when clicked on
	 */
	@FXML
	private void logout(ActionEvent event) {
		if(!selectedPhoto.equals("")) {
			selectedPhoto = "";
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm logout");
		alert.setContentText("Are you sure you want to logout?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			openLoginScene(event);
		}
	}
	
	/*
	 * Allows users to back to a previous photo in a slideshow
	 * @param event The event when clicked on
	 */
	@FXML
	private void previousPhoto(ActionEvent event) {
		if(!selectedPhoto.equals("")) {
			selectedPhoto = "";
		}
		if(!slideClicked) {
			setWarning("Warning", "Slideshow hasn't been open yet");
		}else {
			details.clear();
			anchorPane2.getChildren().clear();
			if(index == 0) {
				index = photos.size() - 1;
			}else {
				index--;
			}
			File file = new File(photos.get(index).getPhotoName());
			Image image = new Image(file.toURI().toString(), 430, 324, false, false);
			ImageView imageView = new ImageView(image);
			anchorPane2.getChildren().add(imageView);
			Calendar date = photos.get(index).getDate();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
			ArrayList<Tag> temp = photos.get(index).getTags();
			String tags = "";
			for(int i = 0; i < temp.size(); i++) {
				if(i == temp.size() - 1) {
					tags += temp.get(i).getName() + ":" + temp.get(i).getValue();
				}else {
					tags += temp.get(i).getName() + ":" + temp.get(i).getValue() + ", ";
				}
			}
			String content = "Caption: " + photos.get(index).getCaption() + "\n" + "Date added: " + dateFormat.format(date.getTime()) + "\n" + "Tags: " + tags;
			details.setText(content);
		}
	}
	
	/*
	 * Allows users to the next photo in a slideshow
	 * @param event The event when clicked on
	 */
	@FXML
	private void nextPhoto(ActionEvent event) {
		if(!selectedPhoto.equals("")) {
			selectedPhoto = "";
		}
		if(!slideClicked) {
			setWarning("Warning", "Slideshow hasn't been open yet");
		}else {
			details.clear();
			anchorPane2.getChildren().clear();
			if(index == photos.size() - 1) {
				index = 0;
			}else {
				index++;
			}
			File file = new File(photos.get(index).getPhotoName());
			Image image = new Image(file.toURI().toString(), 430, 324, false, false);
			ImageView imageView = new ImageView(image);
			anchorPane2.getChildren().add(imageView);
			Calendar date = photos.get(index).getDate();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
			ArrayList<Tag> temp = photos.get(index).getTags();
			String tags = "";
			for(int i = 0; i < temp.size(); i++) {
				if(i == temp.size() - 1) {
					tags += temp.get(i).getName() + ":" + temp.get(i).getValue();
				}else {
					tags += temp.get(i).getName() + ":" + temp.get(i).getValue() + ", ";
				}
			}
			String content = "Caption: " + photos.get(index).getCaption() + "\n" + "Date added: " + dateFormat.format(date.getTime()) + "\n" + "Tags: " + tags;
			details.setText(content);
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
	 * Set the local user home scene field to same scene as that of Photos.java
	 * @param scene User Home scene from main application
	 */
	public void setUserScene(Scene scene) {
		userScene = scene;
	}
	
	/*
	 * Opens the User Home Page
	 * @param event The event when clicked on
	 */
	public void openUserScene(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setTitle("User Home Page");
		primaryStage.setScene(userScene);
	}
	
	/*
	 * Transfers the user controller object in Photos.java to this file
	 * @param controller User Home Controller from Photos.java
	 */
	public void setUserController(UserHomeController controller) {
		userController = controller;
	}
	
	/*
	 * Receives current user and album from User Home Page
	 * @param user Current user that logged in
	 * @param album Current album that's being opened
	 */
	public void initCurrentUserAlbum(User user, Album album) {
		this.user = user;
		this.album = album;
		photos = album.getPhotos();
		tilePane.getChildren().clear();
		anchorPane2.getChildren().clear();
		details.clear();
		displayPhotos();
	}
	
	/*
	 * Displays the photos in a tile pane
	 */
	private void displayPhotos() {
		for(Photo photo : photos) {
			//Image image = photo.getImage();
			File file = new File(photo.getPhotoName());
			Image image = new Image(file.toURI().toString(), 150, 110, false, false);
			ImageView imageView = new ImageView(image);
			imageView.setUserData(photo);
			Text cap = new Text(photo.getCaption());
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(imageView);
			vbox.getChildren().add(cap);
			tilePane.getChildren().add(vbox);
			imageView.setOnMouseClicked(e -> {
				selectedPhoto = photo.getPhotoName();
			});
		}
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
