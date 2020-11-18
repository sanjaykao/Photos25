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
import model.Tag;
import model.User;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import app.Photos;


public class SearchPageController {

	@FXML private TilePane tilePane;
	@FXML private DatePicker firstDate;
	@FXML private DatePicker secondDate;
	@FXML private ComboBox<String> tag1;
	@FXML private ComboBox<String> compareType;
	@FXML private ComboBox<String> tag2;

	private User user;
	private ArrayList<Album> albums;
	private ArrayList<Photo> picResults;
	private ArrayList<Tag> tags;
	
	private Scene userScene;
	private Scene loginScene;
	
	@FXML
	private void initialize() {
		compareType.getItems().setAll("SINGLE", "AND", "OR");
	}
	
	@FXML
	private void searchByDateBtn(ActionEvent event) {
		if(firstDate.getValue() == null || secondDate.getValue() == null) {
			setWarning("Invalid input", "Please input both dates");
		} else if(albums.size() == 0) {
			setWarning("No pictures to search", "Please add pictures");
		} else {
			LocalDate firstLocal = firstDate.getValue();
			Instant firstInstant = Instant.from(firstLocal.atStartOfDay(ZoneId.systemDefault()));
			Date date1 = Date.from(firstInstant);
			
			LocalDate secondLocal = secondDate.getValue();
			Instant secondInstant = Instant.from(secondLocal.atStartOfDay(ZoneId.systemDefault()));
			Date date2 = Date.from(secondInstant);
			
			picResults = searchByDate(date1, date2);
			for(int i = 0; i < picResults.size(); i++) {
				System.out.println(picResults.get(i).photoName);
			}
			
			if(picResults.size() == 0) {
				setWarning("Empty search result", "There are no photos that fit the search criteria");
			} else {
				displayResults();
			}
		}
	}
	
	@FXML
	private void searchByTagBtn(ActionEvent event) {
		//Tag tag1;
		//Tag tag2;
		
		if(albums.size() == 0) {
			setWarning("No pictures to search", "Please add pictures");
		} else if(tag1.getValue() == null || tag2.getValue() == null) {
			setWarning("No valid tags to search", "Please add tags");
		} else if(compareType.getValue().equals("SINGLE")) {
			//tag1 = new Tag(name1.getValue(), value1.getText().trim());
			picResults = searchByTag(tag1.getValue(), null, compareType.getValue());
			
			if(picResults.size() == 0) {
				setWarning("Empty search result", "There are no photos that fit the search criteria");
			} else {
				displayResults();
			}
		} else {
			//tag1 = new Tag(name1.getValue(), value1.getText().trim());
			//tag2 = new Tag(name2.getValue(), value2.getText().trim());
			picResults = searchByTag(tag1.getValue(), tag2.getValue(), compareType.getValue());
			
			if(picResults.size() == 0) {
				setWarning("Empty search result", "There are no photos that fit the search criteria");
			} else {
				displayResults();
			}
		}
	}
	@FXML
	private void newSearchAlbum(ActionEvent event) {
		TextInputDialog td = new TextInputDialog();
		td.setTitle("Create new album from search results");
		td.setHeaderText("Enter a name");
		Optional<String> result = td.showAndWait();
		
		if(result.isPresent()) {
			String name = td.getEditor().getText();
			if(exists(name)) {
				setWarning("Cannot create album", "This album already exists!");
			}else {
				user.createAlbum(name, picResults);
				User.write(user, user.getUsername());
				albums = user.getAlbums();
				tilePane.getChildren().clear();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success!");
				alert.setContentText("Creating an album from search results was successful");
				alert.showAndWait();
			}
		}
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
	
	public void initCurrentUser(User user) {
		this.user = user;
		albums = user.getAlbums();
		tags = user.getAlbumTags();
		
		if(tags == null) {
			tags = new ArrayList<Tag>();
		} else if(albums == null) {
			albums = new ArrayList<Album>();
		}
		
		setTagOptions();
	}
	
	private void setTagOptions() {
		if(tags.size() > 0) {
			ArrayList<String> tagString = new ArrayList<String>();
			//creates a set of unique names and values
			for(Tag currTag : tags) {
				tagString.add(currTag.getName() + " = " + currTag.getValue());
			}
			tag1.getItems().setAll(tagString);
			tag2.getItems().setAll(tagString);
		}
	}
	
	private void displayResults() {
		for(Photo pic : picResults) {
			Image image = pic.getImage();
			ImageView imageView = new ImageView(image);
			Text details = new Text(pic.getCaption());
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(imageView);
			vbox.getChildren().add(details);
			tilePane.getChildren().add(vbox);
		}
	}
	
	
	public ArrayList<Photo> searchByDate(Date first, Date last) {
		//returns a new arraylist (copy of the photos) between first and last dates
		ArrayList<Photo> results = new ArrayList<Photo>();

		for(Album currAlbum : albums) {
			for(Photo currPic : currAlbum.getPhotos()) {
				Date picDate = currPic.getDate().getTime();
				//System.out.println(picDate);
				if(picDate.after(first) && picDate.before(last)) {
					results.add(currPic);
				}
			}
		}
		//System.out.println(results.size());
		return results;
	}
	
	public ArrayList<Photo> searchByTag(String first, String second, String type) {
		//type : single, and, or
		//returns a new arraylist(copy of the photos)
		ArrayList<Photo> results = new ArrayList<Photo>();
		
		for(Album currAlbum : albums) {
			for(Photo currPic : currAlbum.getPhotos()) {
				boolean firstTag = false;
				boolean secondTag = false;
				//iterate through to see if tags are found 
				for(Tag currTag : currPic.getTags()) {
					String toStringTag = currTag.getName() + " = " + currTag.getValue();
					if(type.equals("SINGLE")) {
						if(toStringTag.equals(first)) {
							firstTag = true;
							break;
						}
					} else {
						if(toStringTag.equals(first)) {
							firstTag = true;
						} 
						if(toStringTag.equals(second)) {
							secondTag = true;
						}
					}
				}
				
				//add photo if conditions are true
				if(type.equals("SINGLE")) {
					if(firstTag) {
						results.add(currPic);
					}
				} else if(type.equals("AND")) {
					if(firstTag && secondTag) {
						results.add(currPic);
					}
				} else if(type.equals("OR")) {
					if(firstTag || secondTag) {
						results.add(currPic);
					}
				}
			}
		}
		
		return results;
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
