package model;

import java.io.*;
import java.util.*;

public class Photo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String photoName; //not sure about this one
	public String caption;
	public Calendar date;
	public ArrayList<Tag> tags;
	public ArrayList<Album> albums;
		
	public Photo(String name, Calendar date) {
		this.photoName = name;
		this.caption = "";
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
