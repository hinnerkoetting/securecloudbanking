package de.mrx.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

public class IdentityDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4920238689942058591L;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String city;
	
	private String houseNr;

	private String name;

	private String phone;
	
	private String plz;
	private String street;
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public IdentityDTO(){
		
	}	

	public IdentityDTO(String name) {
		super();
		this.name = name;
	}


	public String getCity() {
		return city;
	}

	public String getHouseNr() {
		return houseNr;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getPlz() {
		return plz;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setHouseNr(String houseNr) {
		this.houseNr = houseNr;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	@Override
	public String toString() {
		return "IdentityDTO [name=" + name + ", city=" + city + ", houseNr="
				+ houseNr + ", phone=" + phone + ", plz=" + plz + "]";
	}
}
