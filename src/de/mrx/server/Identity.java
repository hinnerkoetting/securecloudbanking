package de.mrx.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.IdentityDTO;

@PersistenceCapable
public class Identity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;


	@Persistent
	private String name;


	public Identity(String name) {
		super();
		this.name = name;
	}


	public Key getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		return "Identity [" + (name != null ? "name=" + name : "") + "]";
	}
	
	public Identity(IdentityDTO dto){
		this.name=dto.getName();		
	}
	
	
	public IdentityDTO getDTO(){
		IdentityDTO dto=new IdentityDTO(getName());
		return dto;
	}
	

}
