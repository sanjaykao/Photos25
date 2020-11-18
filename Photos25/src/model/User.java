package model;

import java.io.*;
import java.util.*;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String username;
	public ArrayList<Album> albums;
	public ArrayList<Tag> userTags;
	
	public static final String storeDir = ".";
	public static String storeFile = "";
	
	public User(String username) {
		this.username = username;
		this.albums = new ArrayList<Album>();
		this.userTags = new ArrayList<Tag>();
	}
	
	public String getUsername() {
		return username;
	}
	
	public ArrayList<Album> getAlbums(){
		return albums;
	}
	
	public void createAlbum(String name) {
		//create new album in the user home page then add to the arraylist of albums
		Album newAlbum = new Album(name);
		albums.add(newAlbum);
		
	}
	
	public void createAlbum(String name, ArrayList<Photo> pics) {
		//create new album from the search of photos and then add to the arrlist of albums
		Album newAlbum = new Album(name, pics);
		albums.add(newAlbum);
	}
	
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	public void deleteAlbum(String name) {
		//removes album by name from the albums arraylist
		String indexName;
		for(int i = 0; i < albums.size(); i++) {
			indexName = albums.get(i).getAlbumName();
			if(indexName.equals(name)) {
				albums.remove(i);
			}
		}
	}
	
	public void renameAlbum(Album album, String newName) {
		//sets new name of album 
		album.setAlbumName(newName);
	}

	public void addCaption(String content, Photo photo) {
		//adds or edits the caption to the photo
		photo.setCaption(content);
	}
	
	public void displayPhoto(Photo photo) {
		//not sure if this is supposed to be in the controller?
	}
	
	public void displayAlbum(Album album) {
		//also not sure about this one too
	}
	
	public void addTag(Photo photo, String name, String value) {
		//creates a new tag and then adds to the photo's tag arraylist
		ArrayList<Tag> tags = photo.getTags();
		Tag newTag = new Tag(name, value);
		tags.add(newTag);
		addAlbumTag(newTag);
	}
	
	public void addAlbumTag(Tag tag) {
		// adds tag to user tags arraylist if it doesn't already exist
		for(Tag item : userTags) {
			if(item.getName().equals(tag.getName()) && item.getValue().equals(tag.getValue())) {
				return;
			}
		}
		userTags.add(tag);
	}
	
	public void deleteTag(Photo photo, String name, String value) {
		//finds tag in the arraylist of tags and removes it
		ArrayList<Tag> tags = photo.getTags();
		for(int i = 0; i < tags.size(); i++) {
			if(tags.get(i).getName().equals(name) && tags.get(i).getValue().equals(value)) {
				deleteUserTag(tags.get(i));
				tags.remove(i);
				break;
			}
		}
		
	}
	
	public void deleteUserTag(Tag tag) {
		// deletes the tag from the user tags arraylist if no more photos have the tag
		for(Album album : albums) {
			ArrayList<Photo> photos = album.getPhotos();
			for(Photo photo : photos) {
				ArrayList<Tag> tags = photo.getTags();
				for(Tag item : tags) {
					if(item.getName().equals(tag.getName()) && item.getValue().equals(tag.getValue())) {
						return;
					}
				}
			}
		}
		userTags.remove(tag);
	}
	
	public ArrayList<Tag> getAlbumTags(){
		return userTags;
	}
	
	public void copyPhoto(Album dest, Photo photo) {
		//creates a copy of the photo and adds to the dest album's arraylist of photos
		//also update the photo's arraylist of albums
		dest.getPhotos().add(photo);
		photo.getAlbums().add(dest);
	}
	
	public void movePhoto(Album dest, Album source, Photo photo) {
		//removes photo from source and add to the dest album's arraylist of photos
		//also update the photo's arralist of albums
		dest.getPhotos().add(photo);
		photo.getAlbums().add(dest);
		photo.getAlbums().remove(source);
		source.getPhotos().remove(photo);
	}
	
	public static void write(User user, String name) {
		storeFile = name + ".dat";
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile, false));
			oos.writeObject(user);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public static User read(String name) {
		storeFile = name + ".dat";
		System.out.println(storeFile);
		//File file = new File(storeDir + File.separator + storeFile);
		//System.out.println(file.exists());
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
			User temp = (User)ois.readObject();
			ois.close();
			return temp;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
}
