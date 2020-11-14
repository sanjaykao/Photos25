package model;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String username;
	public ArrayList<Album> albums;
	
	public User(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void createAlbum(String name) {
		//create new album in the user home page
		
	}
	
	public void createAlbum(ArrayList<Photo> pics) {
		//create new album from the search of photocs
		
	}
	
	public void deleteAlbum(String name) {
		//removes album by name from the albums arraylist
		
	}
	
	public void renameAlbum(String newName) {
		//sets new name of album 
		
	}

	public void addCaption(String content, Photo photo) {
		//adds or edits the caption to the photo
		
	}
	
	public void displayPhoto(Photo photo) {
		//not sure if this is supposed to be in the controller?
	}
	
	public void displayAlbum(Album album) {
		//also not sure about this one too
	}
	
	public void addTag(Photo photo, String name, String value) {
		//creates a new tag and then adds to the photo's tag arraylist
		
	}
	
	public void deleteTag(Photo photo, Tag tag) {
		//finds tag in the arraylist of tags and removes it
	}
	
	public void copyPhoto(Album dest, Photo photo) {
		//creates a copy of the photo and adds to the dest album's arraylist of photos
		//also update the photo's arraylist of albums
	}
	
	public void movePhoto(Album dest, Album source, Photo photo) {
		//removes photo from source and add to the dest album's arraylist of photos
		//also update the photo's arralist of albums
	}
	
	public ArrayList<Photo> searchByDate(Date first, Date last) {
		//returns a new arraylist (copy of the photos) between first and last dates
		return null;
	}
	
	public ArrayList<Photo> searchByTag(Tag first, Tag second, String type) {
		//type : single, and, or
		//returns a new arraylist(copy of the photos)
		return null;
	}
	
	//idk about logout function
}
