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
	private boolean activated=false;
	
	@Persistent
	private String city;
	
	@Persistent
	private String email;


	@Persistent
	private String houseNr;
	
	@Persistent
	private String nickName;


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;


	private boolean loggedIn = false;


	private String loginUrl;

	
	private String logoutUrl;
	
	@Persistent
	private String name;
	
	@Persistent
	private String phone;


	@Persistent
	private String plz;


	@Persistent
	private String street;


	public Identity(IdentityDTO dto){
		this.name=dto.getName();	
		setCity(dto.getCity());
		setHouseNr(dto.getHouseNr());
		setName(dto.getName());
		setPhone(dto.getPhone());
		setPlz(dto.getPlz());
		setStreet(dto.getStreet());
		setEmail(dto.getEmail());
		setActivated(dto.isActivated());
		setNickName(dto.getNickName());
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
		dto.setActivated(isActivated());
		dto.setNickName(getNickName());
		return dto;
	}
	  public String getEmail() {
		return email;
	}
	  public String getHouseNr() {
		return houseNr;
	}
	  

	public Key getId() {
		return id;
	}


	public String getLoginUrl() {
		return loginUrl;
	}


	public String getLogoutUrl() {
		return logoutUrl;
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


	public boolean isActivated() {
		return activated;
	}


	public boolean isLoggedIn() {
		return loggedIn;
	}


	public void setActivated(boolean activated) {
		this.activated = activated;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setHouseNr(String houseNr) {
		this.houseNr = houseNr;
	}
	
	public void setId(Key id) {
		this.id = id;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	


	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}


	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
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
		return "Identity [activated=" + activated + ", "
				+ (city != null ? "city=" + city + ", " : "")
				+ (email != null ? "email=" + email + ", " : "")
				+ (houseNr != null ? "houseNr=" + houseNr + ", " : "")
				+ (nickName != null ? "nickName=" + nickName + ", " : "")
				+ "loggedIn=" + loggedIn + ", "
				+ (name != null ? "name=" + name + ", " : "")
				+ (phone != null ? "phone=" + phone + ", " : "")
				+ (plz != null ? "plz=" + plz + ", " : "")
				+ (street != null ? "street=" + street : "") + "]";
	}
	

}
