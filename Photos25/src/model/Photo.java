package model;

import java.io.*;
import java.util.*;

/*
 * Photo model is to store all tags and other details of a Photo
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

public class Photo implements Serializable{

	private static final long serialVersionUID = 1L;
	public String photoName;
	public String caption;
	public Calendar date;
	public ArrayList<Tag> tags;
	public ArrayList<Album> albums;
		
	/*
	 * 3 Arg Constructor for Photo
	 * @param name Name of Photo
	 * @param date Calendar date of the Photo
	 */
	public Photo(String name, Calendar date) {
		this.photoName = name;
		this.caption = "";
		this.date = date;
		date.set(Calendar.MILLISECOND, 0);
		this.tags = new ArrayList<Tag>();
		this.albums = new ArrayList<Album>();
	}
		
	/*
	 * Gets the name of the Photo
	 * @return Name of Photo
	 */
	public String getPhotoName() {
		return photoName;
	}
	
	/*
	 * Gets the caption of the Photo
	 * @return Caption of Photo
	 */
	public String getCaption() {
		return caption;
	}
	
	/*
	 * Changes the caption of the Photo
	 * @param content New caption of the Photo
	 */
	public void setCaption(String content) {
		caption = content;
	}
	
	/*
	 * Gets the date of the Photo
	 * @return Date of the Photo
	 */
	public Calendar getDate() {
		return date;
	}
	
	/*
	 * Gets the list of Tags of the Photo
	 * @return Arraylist of Tags
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/*
	 * Gets the list of all the albums the Photo is a part of
	 * @return Arraylist of Albums
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}

}
