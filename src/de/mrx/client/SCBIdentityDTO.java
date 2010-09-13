package de.mrx.client;

import java.io.Serializable;

/**
 * data transfer object for SCBIdentity. 
 * @see de.mrx.server.SCBIdentity
 *
 */
public class SCBIdentityDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4920238689942058591L;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private boolean activated=false;
	
	private String city;

	private String email;

	private String houseNr;
	
	private String language;
	
	private boolean admin;
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isAdmin() {
		return admin;
	}
	
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	
	private String nickName;
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	private String name;
	private String firstName;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	private String phone;
	  private String plz;
	  private String street;
	
	  public void setAdmin(boolean isAdmin) {
		 this.admin = isAdmin;
	  }
	
	public SCBIdentityDTO(){
		
	}

	public SCBIdentityDTO(String name, String email) {
		super();
		this.name = name;
		this.email=email;
	}

	public String getCity() {
		return city;
	}

	public String getEmail() {
		return email;
	}

	public String getHouseNr() {
		return houseNr;
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
		return "SCBIdentityDTO [activated=" + activated + ", "
				+ (city != null ? "city=" + city + ", " : "")
				+ (email != null ? "email=" + email + ", " : "")
				+ (firstName != null ? "firstName=" + firstName + ", " : "")
				+ (houseNr != null ? "houseNr=" + houseNr + ", " : "")
				+ "loggedIn=" + loggedIn + ", "
				+ (loginUrl != null ? "loginUrl=" + loginUrl + ", " : "")
				+ (logoutUrl != null ? "logoutUrl=" + logoutUrl + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (nickName != null ? "nickName=" + nickName + ", " : "")
				+ (phone != null ? "phone=" + phone + ", " : "")
				+ (plz != null ? "plz=" + plz + ", " : "")
				+ (street != null ? "street=" + street : "") + "]";
	}
}
