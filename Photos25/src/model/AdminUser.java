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
}
