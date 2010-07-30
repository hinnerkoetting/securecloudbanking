package de.mrx.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * represents a bank institute
 */

@PersistenceCapable
public class Institute implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	private String blz;

	@Persistent
	private String name;

	public Institute( String blz, String name) {
		super();		
		this.blz = blz;
		this.name = name;
	}

	public String getBlz() {
		return blz;
	}

	public Key getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setBlz(String blz) {
		this.blz = blz;
	}

	public void setId(Key id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Institute [blz=" + blz + ", "
				+ (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name : "") + "]";
	}
	
	

}
