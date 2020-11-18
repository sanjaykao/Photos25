package model;

import java.io.*;
import java.util.*;

public class Album implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String albumName;
	public Calendar earliestDate;
	public Calendar latestDate;
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
				break;
			}
		}
	}
	
	public String getAlbumName() {
		return albumName;
	}
	
	public void setAlbumName(String newName) {
		this.albumName = newName;
	}
	
	public Calendar getEarliestDate() {
		return earliestDate;
	}
	
	public void setEarliestDate(Calendar newDate) {
		this.earliestDate = newDate;
	}
	
	public Calendar getLatestDate() {
		return latestDate;
	}
	
	public void setLatestDate(Calendar newDate) {
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
