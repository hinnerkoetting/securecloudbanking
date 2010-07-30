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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Persistent
	private String city;
	
	@Persistent
	private String houseNr;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;


	@Persistent
	private String name;


	@Persistent
	private String phone;


	@Persistent
	private String plz;


	@Persistent
	private String street;

	
	@Persistent
	private String email;

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Identity(IdentityDTO dto){
		this.name=dto.getName();	
		setCity(dto.getCity());
		setHouseNr(dto.getHouseNr());
		setName(dto.getName());
		setPhone(dto.getPhone());
		setPlz(dto.getPlz());
		setStreet(dto.getStreet());
		setEmail(dto.getEmail());
	}


	public Identity(String name) {
		super();
		this.name = name;
	}


	public String getCity() {
		return city;
	}


	public IdentityDTO getDTO(){
		IdentityDTO dto=new IdentityDTO(getName());
		dto.setCity(getCity());
		dto.setHouseNr(getHouseNr());
		dto.setName(getName());
		dto.setPlz(getPlz());
		dto.setPhone(getPhone());
		dto.setStreet(getStreet());
		dto.setEmail(getEmail());
		return dto;
	}


	public String getHouseNr() {
		return houseNr;
	}


	public Key getId() {
		return id;
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
	
	public String getStreet() {
		return street;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	


	public void setHouseNr(String houseNr) {
		this.houseNr = houseNr;
	}


	public void setId(Key id) {
		this.id = id;
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
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	
	@Override
	public String toString() {
		return "Identity [city=" + city + ", houseNr=" + houseNr + ", id=" + id
				+ ", name=" + name + ", phone=" + phone + ", plz=" + plz
				+ ", street=" + street + ", email=" + email + "]";
	}
	

}
