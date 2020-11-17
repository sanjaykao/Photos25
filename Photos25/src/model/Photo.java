package model;

import java.io.*;
import java.util.*;

import javafx.scene.image.Image;

public class Photo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String photoName; //not sure about this one
	public String caption;
	public transient Image image;
	public Calendar date;
	public ArrayList<Tag> tags;
	public ArrayList<Album> albums;
	
	//NEED TO FIGURE OUT HOW TO IMPORT AN IMAGE
	
	public Photo(String name, Image image, Calendar date) {
		this.photoName = name;
		this.caption = "";
		this.image = image;
		this.date = date;
		date.set(Calendar.MILLISECOND, 0);
		this.tags = new ArrayList<Tag>();
		this.albums = new ArrayList<Album>();
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
	
	public Image getImage() {
		return image;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	public ArrayList<Album> getAlbums() {
		return albums;
	}

}
