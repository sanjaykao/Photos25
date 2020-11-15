package model;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class AdminUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String admin;
	public ArrayList<User> users;
	public User currentUser; //to keep track of which user is logged in
	
	public static final String storeDir = ".";
	public static final String storeFile = "users.dat";
	
	//idk about this
	public AdminUser() {
		admin = "admin";
		users = new ArrayList<User>();
	}
	
	public void addUser(String username) {
		//creates new user with specified username in the arraylist of users
		User user = new User(username);
		users.add(user);
		
	}
	
	public void removeUser(User user) {
		//removes user with specified username in the arraylist of users
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).equals(user)) {
				users.remove(i);
			}
		}
	}
	
	public boolean checkUser(String username) {
		String existingUser;
		for(User user : users) {
			existingUser = user.getUsername();
			if(existingUser.equals(username)) {
				currentUser = user;
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<User> getUsers(){
		return users;
	}
	
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
}
