package de.mrx.client;

import java.io.Serializable;

public class IdentityDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4920238689942058591L;
	private String name;

	public IdentityDTO(){
		
	}
	
	public IdentityDTO(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "IdentityDTO [" + (name != null ? "name=" + name : "") + "]";
	}
}
