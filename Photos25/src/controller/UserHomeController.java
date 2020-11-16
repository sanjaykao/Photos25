package controller;

import java.util.ArrayList;
import java.util.Optional;

import javafx.event.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.User;
import model.Album;

//import app.Photos;


public class UserHomeController {
	@FXML
	private TilePane tilePane;
	
	private ArrayList<Album> albums;
	private User user;
	
	public void start(Stage mainStage) {
		
	}
	
	@FXML
	private void createAlbum(ActionEvent event) {
		TextInputDialog td = new TextInputDialog();
		td.setTitle("Add a user");
		td.setHeaderText("Enter a name");
		Optional<String> result = td.showAndWait();
		
		if(result.isPresent()) {
			String name = td.getEditor().getText();
			if(exists(name)) {
				setWarning("Cannot add user", "This name is already taken!");
			}
		}
	}
		
	private boolean exists(String name) {
		for(Album album : albums) {
			if(user.getUsername().equals(name)) {
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
