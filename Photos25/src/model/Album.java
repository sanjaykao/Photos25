package model;

import java.io.*;
import java.util.*;

/*
 * Album model is to store all of the Photos and other details of the album
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

public class Album implements Serializable{

	private static final long serialVersionUID = 1L;
	public String albumName;
	public Calendar earliestDate;
	public Calendar latestDate;
	public int numOfPhotos;
	public ArrayList<Photo> photos;
	
	/*
	 * 1 Arg Constructor for Album
	 * @param name Name of the album
	 */
	public Album(String name) {
		albumName = name;
		photos = new ArrayList<Photo>();
		numOfPhotos = 0;
	}
	
	/*
	 * 2 Arg Constructor for Album
	 * @param name Name of the Album
	 * @param pics Arraylist of photos to be added to album
	 */
	public Album(String name, ArrayList<Photo> pics) {
		albumName = name;
		photos = pics;
		numOfPhotos = pics.size();
	}
	
	/*
	 * Adds a Photo instance to the Album
	 * @param pic Photo to be added
	 */
	public void addPhotoToAlbum(Photo pic) {
		photos.add(pic);
		numOfPhotos++;
	}
	
	/*
	 * Deletes a Photo from the Album
	 * @param photo Name of the photo to be deleted
	 */
	public void deletePhoto(String photo) {
		for(Photo item : photos) {
			if(item.getPhotoName().equals(photo)) {
				photos.remove(item);
				numOfPhotos--;
				break;
			}
		}
	}
	
	/*
	 * Returns name of the Album
	 * @return Name of Album
	 */
	public String getAlbumName() {
		return albumName;
	}
	
	/*
	 * Changes the Album's name
	 * @param newName Album's new name
	 */
	public void setAlbumName(String newName) {
		this.albumName = newName;
	}
	
	/*
	 * Returns the date of the earliest Photo in the Album
	 * @return Calendar instance of the earliest date
	 */
	public Calendar getEarliestDate() {
		return earliestDate;
	}
	
	/*
	 * Changes the date of the earliest Photo in the Album
	 * @param newDate Calendar date of the earliest date in the Album
	 */
	public void setEarliestDate(Calendar newDate) {
		this.earliestDate = newDate;
	}
	
	/*
	 * Returns the date of the latest Photo in the Album
	 * @return Calendar instance of the latest date
	 */
	public Calendar getLatestDate() {
		return latestDate;
	}
	
	/*
	 * Changes the date of the latest Photo in the Album
	 * @param newDate Calendar date of the latest date in the Album
	 */
	public void setLatestDate(Calendar newDate) {
		this.latestDate = newDate;
	}
	
	/*
	 * Gets the number of Photos in the Album
	 * @return Number of Photos in the Album
	 */
	public int getNumOfPhotos() {
		return numOfPhotos;
	}
	
	/*
	 * Changes the number of Photos in the Album
	 * @param newNum New number of Photos in the Album
	 */
	public void setNumOfPhotos(int newNum) {
		this.numOfPhotos = newNum;
	}
	
	/*
	 * Gets the list of Photos in the Album
	 * @return Arraylist of Photos in the Album
	 */
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	
	/*
	 * Finds the date of the earliest Photo in the Album
	 */
	public void findEarliestDate() {
		if(photos.size() == 0) {
			return;
		}
		Calendar earliest = photos.get(0).getDate();
		if(photos.size() == 1) {
			earliestDate = earliest;
		}else {
			for(int i = 1; i < photos.size(); i++) {
				if(photos.get(i).getDate().before(earliest)) {
					earliest = photos.get(i).getDate();
				}
			}
			earliestDate = earliest;
		}
	}
	
	/*
	 * Finds the date of the latest Photo in the Album
	 */
	public void findLatestDate() {
		if(photos.size() == 0) {
			return;
		}
		Calendar latest = photos.get(0).getDate();
		if(photos.size() == 1) {
			latestDate = latest;
		}else {
			for(int i = 1; i < photos.size(); i++) {
				if(photos.get(i).getDate().after(latest)) {
					latest = photos.get(i).getDate();
				}
			}
			latestDate = latest;
		}
	}
}
