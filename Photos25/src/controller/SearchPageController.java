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

/*
 * Search Page Controller allows users to search for photos based on certain queries
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

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
	
	public UserHomeController userController;
	
	/*
	 * When initialized, combobox for compareType is filled in
	 */
	@FXML
	private void initialize() {
		compareType.getItems().setAll("SINGLE", "AND", "OR");
	}
	
	/*
	 * Processes the 2 DatePicker inputs given to perform the search
	 * @param event The event when clicked on
	 */
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
			Instant secondInstant = Instant.from(secondLocal.plusDays(1).atStartOfDay(ZoneId.systemDefault()));
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
	
	/*
	 * Processes the ComboBox inputs to perform the search
	 * @param event The event when clicked on
	 */
	@FXML
	private void searchByTagBtn(ActionEvent event) {
		if(albums.size() == 0) {
			setWarning("No pictures to search", "Please add pictures");
		} else if(compareType.getValue() == null) { 
			setWarning("No compare type selected", "Please select a compare type");
		} else if(tag1.getValue() == null) {
			setWarning("No valid tags to search", "Please select at least one tag");
		} else if(compareType.getValue().equals("SINGLE")) {
			picResults = searchByTag(tag1.getValue(), null, compareType.getValue());
			
			if(picResults.size() == 0) {
				setWarning("Empty search result", "There are no photos that fit the search criteria");
			} else {
				displayResults();
			}
		} else if(tag2.getValue() == null) {
			setWarning("Invalid input", "Please choose a second tag for AND/OR comparisons");

		} else {
			picResults = searchByTag(tag1.getValue(), tag2.getValue(), compareType.getValue());
			
			if(picResults.size() == 0) {
				setWarning("Empty search result", "There are no photos that fit the search criteria");
			} else {
				displayResults();
			}
		}
	}
	
	/*
	 * Takes the search results and creates a new album out of the list
	 * @param event The event when clicked on
	 */
	@FXML
	private void newSearchAlbum(ActionEvent event) {
		if(picResults == null || picResults.size() == 0) {
			setWarning("Cannot create album", "There are no pictures to create album with");
		} else {
			TextInputDialog td = new TextInputDialog();
			td.setTitle("Create new album from search results");
			td.setHeaderText("Enter a name");
			Optional<String> result = td.showAndWait();
			
			if(result.isPresent()) {
				String name = td.getEditor().getText();
				if(exists(name)) {
					setWarning("Cannot create album", "This album already exists!");
				}else {
					Album alb = new Album(name, picResults);
					user.addAlbum(alb);
					alb.findEarliestDate();
					alb.findLatestDate();
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
	}
	
	/*
	 * Returns user back to User Home Page
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
	 * Set the user home scene field to same scene as that of Photos.java
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
	 * Receives current user from User Home Page
	 * @param user Current user that logged in
	 */
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
	
	/*
	 * Fills in the options for the ComboBox of tags
	 */
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
	
	/*
	 * Displays the search results of pictures in the tile pane
	 */
	private void displayResults() {
		tilePane.getChildren().clear();
		for(Photo pic : picResults) {
			File file = new File(pic.getPhotoName());
			Image image = new Image(file.toURI().toString(), 150, 110, false, false);
			ImageView imageView = new ImageView(image);
			Text details = new Text(pic.getCaption());
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(imageView);
			vbox.getChildren().add(details);
			tilePane.getChildren().add(vbox);
		}
	}
	
	/*
	 * Finds all the pictures that were taken between the first and last dates (inclusive)
	 * @param first First date in the range
	 * @param last Last date in the range
	 * @return List of photos that fulfill the query
	 */
	public ArrayList<Photo> searchByDate(Date first, Date last) {
		//returns a new arraylist (copy of the photos) between first and last dates
		ArrayList<Photo> results = new ArrayList<Photo>();

		for(Album currAlbum : albums) {
			for(Photo currPic : currAlbum.getPhotos()) {
				Date picDate = currPic.getDate().getTime();
				if(picDate.after(first) && picDate.before(last)) {
					if(!results.contains(currPic)) {
						results.add(currPic);
					}
				}
			}
		}
		return results;
	}
	
	/*
	 * Finds all the pictures that fulfill the search query of tags
	 * @param first First tag in string form
	 * @param second Second tag in string form
	 * @param type Comparison used for the query: SINGLE, AND, OR
	 * @return List of photos that fulfill the query
	 */
	public ArrayList<Photo> searchByTag(String first, String second, String type) {
		//type : single, and, or
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
				
				//makes sure each resulting photo is unique
				if(!results.contains(currPic)) {
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
		}
		
		return results;
	}
	
	/*
	 * Checks to see if the album name is already taken
	 * @param name Name of album to be added
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
