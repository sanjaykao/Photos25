package model;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Photo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String photoName; //not sure about this one
	public String caption;
	public Date date;
	public ArrayList<Tag> tags;
	public ArrayList<Album> albums;
	
	//NEED TO FIGURE OUT HOW TO IMPORT AN IMAGE
	
	public Photo(String name) {
		this.photoName = name;
	}
		
	public String getPhotoName() {
		return photoName;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String content) {
		caption = content;
	}
	
	public Date getDate() {
		return date;
	}
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	public ArrayList<Album> getAlbums() {
		return albums;
	}

}
