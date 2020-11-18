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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;

import app.Photos;


public class AlbumController {
	@FXML
	private TilePane tilePane;
	
	@FXML
	private AnchorPane anchorPane;
	
	private ArrayList<Photo> photos;
	private String selectedPhoto;
	private User user;
	private Album album;

	private Scene userScene;
	private Scene loginScene;
	
	public UserHomeController userController;
	
	@FXML
	private void initialize() {
		selectedPhoto = "";
	}
	
	public void start(Stage mainStage) {
		
	}
	
	@FXML
	private void addPhoto(ActionEvent event) {
		Stage stage = (Stage)anchorPane.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		File selected = fileChooser.showOpenDialog(stage);
		if(selected != null) {
			String path = selected.getName();
			Image image = new Image(selected.toURI().toString(), 100, 0, false, false);
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
				Photo temp = new Photo(path, image, date);
				album.addPhotoToAlbum(temp);
				User.write(user, user.getUsername());
				photos = album.getPhotos();
				tilePane.getChildren().clear();
				displayPhotos();
			}
		}
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
		}
	}
	
	@FXML
	private void addTag(ActionEvent event) {
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
		}
	}
	
	@FXML
	private void deleteTag(ActionEvent event) {
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
						if(photos.size() > 0) {
							displayPhotos();
						}
						break;
					}
				}
			}
		} 
	}
	
	@FXML
	private void copyPhoto(ActionEvent event) {
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
						ArrayList<Photo> destPhotos = item2.getPhotos();
						for(Photo pic : destPhotos) {
							if(pic.getPhotoName().equals(selectedPhoto)) {
								setWarning("Can't copy photo", "Photo already exists in destination album!");
								break;
							}
						}
						for(Photo photo : photos) {
							if(photo.getPhotoName().equals(selectedPhoto)) {
								user.copyPhoto(item2, photo);
								User.write(user, user.getUsername());
								setWarning("Success!", "Photo copied successfully");
								break;
							}
						}
						break;
					}
				}
			}
		}
	}
	
	@FXML
	private void movePhoto(ActionEvent event) {
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
						ArrayList<Photo> destPhotos = item2.getPhotos();
						for(Photo pic : destPhotos) {
							if(pic.getPhotoName().equals(selectedPhoto)) {
								setWarning("Can't move photo", "Photo already exists in destination album!");
								break;
							}
						}
						for(Photo photo : photos) {
							if(photo.getPhotoName().equals(selectedPhoto)) {
								user.movePhoto(item2, album, photo);
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
						break;
					}
				}
			}
		}
	}
	
	@FXML
	private void preview(ActionEvent event) {
		if(selectedPhoto.equals("")) {
			setWarning("No photo selected", "Please select a photo or add more photos.");
		}else {
			for(Photo photo : photos) {
				if(photo.getPhotoName().equals(selectedPhoto)) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle(selectedPhoto);
					ImageView imageView = new ImageView(photo.getImage());
					alert.setGraphic(imageView);
					Calendar date = photo.getDate();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
					ArrayList<Tag> tags = photo.getTags();
					alert.setContentText(photo.getCaption() + "\n" + dateFormat.format(date) + "\n");
					alert.showAndWait();
				}
			}
		}
	}
	
	@FXML
	private void slideshow(ActionEvent event) {
		
	}
	
	@FXML
	private void homePage(ActionEvent event) {
		userController.initCurrentUser(user);
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
	
	public void setUserController(UserHomeController controller) {
		userController = controller;
	}
	
	public void initCurrentUserAlbum(User user, Album album) {
		User temp = user;
		Album temp1 = album;
		setCurrentUserAlbum(temp, temp1);
	}
	
	private void setCurrentUserAlbum(User user, Album album) {
		this.user = user;
		this.album = album;
		displayPhotos();
	}
	
	private void displayPhotos() {
		for(Photo photo : photos) {
			Image image = photo.getImage();
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
