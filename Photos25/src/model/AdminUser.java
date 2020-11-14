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
		
	}
	
	public void addUser(String username) {
		//creates new user with specified username in the arraylist of users
		
	}
	
	public void removeUser(String username) {
		//removes user with specified username in the arraylist of users
	}
}
