package model;

import java.io.*;
import java.util.*;

public class AdminUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//public String admin;
	public ArrayList<User> users;
	
	public static final String storeDir = ".";
	public static final String storeFile = "users.dat";
	
	//idk about this
	public AdminUser() {
		//admin = "admin";
		users = new ArrayList<User>();
	}
	
	public void addUser(String username) {
		//creates new user with specified username in the arraylist of users
		User user = new User(username);
		users.add(user);
		
	}
	
	public void addUser(User user) {
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
	
	public static void delete() {
		File file = new File(storeDir + File.separator + storeFile);
		file.delete();
	}
	
	public void deleteUserFile(String name) {
		File file = new File(storeDir + File.separator + name + ".dat");
		file.delete();
	}
}
