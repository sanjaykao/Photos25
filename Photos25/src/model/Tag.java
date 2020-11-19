package model;

import java.io.Serializable;

/*
 * Tag model is for tags in a Photo
 * 
 * @author Virginia Cheng
 * @author Sanjay Kao
 */

public class Tag implements Serializable{

	private static final long serialVersionUID = 1L;
	public String name;
	public String value;
	
	/*
	 * 2 Arg Constructor for Tag
	 * @param name Name of the Tag
	 * @param value Value of the Tag
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/*
	 * Gets the name of the Tag
	 * @return Name of Tag
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * Gets the value of the Tag
	 * @return Value of Tag
	 */
	public String getValue() {
		return value;
	}

}
