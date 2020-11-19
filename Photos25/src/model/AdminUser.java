package model;

import java.io.*;
import java.util.*;

/*
 * AdminUser model is for the admin account
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

public class AdminUser implements Serializable{

	private static final long serialVersionUID = 1L;
	public ArrayList<User> users;
	public static final String storeDir = ".";
	public static final String storeFile = "users.dat";
	
	/*
	 * Zero Arg Constructor for AdminUser
	 */
	public AdminUser() {
		users = new ArrayList<User>();
	}
	
	/*
	 * Adds user to the arraylist of users with username string
	 * @param username Username of new user
	 * @return The instance of a new User
	 */
	public User addUser(String username) {
		//creates new user with specified username in the arraylist of users
		User user = new User(username);
		users.add(user);
		return user;
	}
	
	/*
	 * Adds User instance to the arraylist of users
	 * @param user User instance to be added
	 */
	public void addUser(User user) {
		users.add(user);
	}
	
	/*
	 * Removes user from the arraylist of users
	 * @param user User instance to be removed
	 */
	public void removeUser(User user) {
		//removes user with specified username in the arraylist of users
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).equals(user)) {
				users.remove(i);
			}
		}
	}
	
	/*
	 * Gets the list of existing users
	 * @return Arraylist of existing users
	 */
	public ArrayList<User> getUsers(){
		return users;
	}
	
	/*
	 * Writes a new file with an AdminUser instance
	 * @param admin AdminUser instance to be added to output file
	 */
	public static void write(AdminUser admin) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile, false));
			oos.writeObject(admin);
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
	 * Writes a new file for stock user
	 * @param user Stock User instance to be added to output file
	 * @param name Name of file to be created
	 */
	public static void writeStock(User user, String name) {
		String fileName = name + ".dat";
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + fileName, false));
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
	 * Reads the given file to get the AdminUser instance
	 * @return AdminUser instance
	 */
	public static AdminUser read() {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
			AdminUser admin = (AdminUser)ois.readObject();
			ois.close();
			return admin;
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
	
	/*
	 * Deletes the AdminUser file
	 */
	public static void delete() {
		File file = new File(storeDir + File.separator + storeFile);
		file.delete();
	}
	
	/*
	 * Deletes the given User file
	 * @param name Name of file to be deleted
	 */
	public void deleteUserFile(String name) {
		File file = new File(storeDir + File.separator + name + ".dat");
		file.delete();
	}
}
