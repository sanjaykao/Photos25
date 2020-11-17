package model;

import java.io.*;
import java.util.*;

public class Album implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String albumName;
	public Date earliestDate;
	public Date latestDate;
	public int numOfPhotos;
	public ArrayList<Photo> photos;
	
	public Album(String name) {
		albumName = name;
		photos = new ArrayList<Photo>();
		numOfPhotos = 0;
	}
	
	public Album(String name, ArrayList<Photo> pics) {
		albumName = name;
		photos = pics;
		numOfPhotos = pics.size();
		//need to add a function to find earliest and latest dates
	}
	
	public void addPhotoToAlbum(Photo pic) {
		photos.add(pic);
		numOfPhotos++;
	}
	
	public void deletePhoto(String photo) {
		for(Photo item : photos) {
			if(item.getPhotoName().equals(photo)) {
				photos.remove(item);
				numOfPhotos--;
			}
		}
	}
	
	public String getAlbumName() {
		return albumName;
	}
	
	public void setAlbumName(String newName) {
		this.albumName = newName;
	}
	
	public Date getEarliestDate() {
		return earliestDate;
	}
	
	public void setEarliestDate(Date newDate) {
		this.earliestDate = newDate;
	}
	
	public Date getLatestDate() {
		return latestDate;
	}
	
	public void setLatestDate(Date newDate) {
		this.latestDate = newDate;
	}
	
	public int getNumOfPhotos() {
		return numOfPhotos;
	}
	
	public void setNumOfPhotos(int newNum) {
		this.numOfPhotos = newNum;
	}
	
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
}
