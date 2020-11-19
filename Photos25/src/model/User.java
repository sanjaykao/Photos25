package model;

import java.io.*;
import java.util.*;

/*
 * User model is for the instance of a user and the changes the user can make
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String username;
	public ArrayList<Album> albums;
	public ArrayList<Tag> userTags;
	
	public static final String storeDir = ".";
	public static String storeFile = "";
	
	/*
	 * 1 Arg Constructor for User
	 * @param username Username of the User
	 */
	public User(String username) {
		this.username = username;
		this.albums = new ArrayList<Album>();
		this.userTags = new ArrayList<Tag>();
	}
	
	/*
	 * Gets the username of the User
	 * @return Username of the User
	 */
	public String getUsername() {
		return username;
	}
	
	/*
	 * Gets the list of Albums of the User
	 * @return Arraylist of Albums that the User has
	 */
	public ArrayList<Album> getAlbums(){
		return albums;
	}
	
	/*
	 * Creates a new Album with a name
	 * @param name Name of the new Album
	 */
	public void createAlbum(String name) {
		Album newAlbum = new Album(name);
		albums.add(newAlbum);
		
	}
	
	/*
	 * Creates a new Album with a name and list of Photos
	 * @param name Name of the new Album
	 * @param pics Arraylist of Photos to add to the Album
	 */
	public void createAlbum(String name, ArrayList<Photo> pics) {
		Album newAlbum = new Album(name, pics);
		albums.add(newAlbum);
	}
	
	/*
	 * Add an Album instance to the User's list of Albums
	 * @param album Album to be added to the list
	 */
	public void addAlbum(Album album) {
		albums.add(album);
	}
	
	/*
	 * Delete an Album from the User's list of Albums
	 * @param name Name of the Album to be deleted
	 */
	public void deleteAlbum(String name) {
		String indexName;
		for(int i = 0; i < albums.size(); i++) {
			indexName = albums.get(i).getAlbumName();
			if(indexName.equals(name)) {
				albums.remove(i);
			}
		}
	}
	
	/*
	 * Renames the Album
	 * @param album Album to be renamed
	 * @param newName New name of the Album
	 */
	public void renameAlbum(Album album, String newName) {
		album.setAlbumName(newName);
	}

	/*
	 * Add a caption to a Photo
	 * @param content The new caption to be added
	 * @param photo The Photo to be changed
	 */
	public void addCaption(String content, Photo photo) {
		photo.setCaption(content);
	}
	
	/*
	 * Add Tag to a Photo's list of Tags
	 * @param photo Photo to be changed
	 * @param name Name of the new Tag
	 * @param value Value of the new Tag
	 */
	public void addTag(Photo photo, String name, String value) {
		ArrayList<Tag> tags = photo.getTags();
		Tag newTag = new Tag(name, value);
		tags.add(newTag);
		addAlbumTag(newTag);
	}
	
	/*
	 * Add Tag to the User's list of unique tags from all of their Photos
	 * @param tag New Tag to be added
	 */
	public void addAlbumTag(Tag tag) {
		for(Tag item : userTags) {
			if(item.getName().equals(tag.getName()) && item.getValue().equals(tag.getValue())) {
				return;
			}
		}
		userTags.add(tag);
	}
	
	/*
	 * Delete Tag from a Photo's list of Tags
	 * @param photo Photo to be changed
	 * @param name Name of the Tag to be removed
	 * @param value Value of the Tag to be removed
	 */
	public void deleteTag(Photo photo, String name, String value) {
		ArrayList<Tag> tags = photo.getTags();
		for(int i = 0; i < tags.size(); i++) {
			if(tags.get(i).getName().equals(name) && tags.get(i).getValue().equals(value)) {
				String temp = tags.get(i).getName() + ":" + tags.get(i).getValue();
				tags.remove(i);
				deleteUserTag(temp);
				break;
			}
		}
		
	}
	
	/*
	 * Remove Tag from the User's list of unique tags from all of their Photos
	 * @param tag Tag in String form to be removed
	 */
	public void deleteUserTag(String tag) {
		// deletes the tag from the user tags arraylist if no more photos have the tag
		String name = tag.substring(0, tag.indexOf(':'));
		String value = tag.substring(tag.indexOf(':') + 1);
		for(Album album : albums) {
			ArrayList<Photo> photos = album.getPhotos();
			for(Photo photo : photos) {
				ArrayList<Tag> tags = photo.getTags();
				for(Tag item : tags) {
					if(item.getName().equals(name) && item.getValue().equals(value)) {
						return;
					}
				}
			}
		}
		for(Tag uTag : userTags) {
			if(uTag.getName().equals(name) && uTag.getValue().equals(value)) {
				userTags.remove(uTag);
				break;
			}
		}
	}
	
	/*
	 * Gets the User's list of unique tags from all of their Photos
	 * @return Arraylist of unique Tags from all Photos
	 */
	public ArrayList<Tag> getAlbumTags(){
		return userTags;
	}
	
	/*
	 * Copy a Photo to another Album
	 * @param dest Album for the Photo to be added to
	 * @param photo Photo to be copied
	 */
	public void copyPhoto(Album dest, Photo photo) {
		dest.addPhotoToAlbum(photo);
		photo.getAlbums().add(dest);
	}
	
	/*
	 * Move a Photo to another Album
	 * @param dest Album for the Photo to be added to
	 * @param source Album for the Photo to be deleted from
	 * @param photo Photo to be moved
	 */
	public void movePhoto(Album dest, Album source, Photo photo) {
		dest.addPhotoToAlbum(photo);
		photo.getAlbums().add(dest);
		photo.getAlbums().remove(source);
		source.deletePhoto(photo.getPhotoName());
	}
	
	/*
	 * Writes a new file for User
	 * @param user User instance to be added to output file
	 * @param name Name of file to be created
	 */
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
	
	/*
	 * Reads the given file to get the User instance
	 * @return User instance
	 */
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
