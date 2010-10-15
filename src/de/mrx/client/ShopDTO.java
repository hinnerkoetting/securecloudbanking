package de.mrx.client;

import java.io.Serializable;

public class ShopDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String blz;
	private String accountNr;
	private String url;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBlz() {
		return blz;
	}
	public void setBlz(String blz) {
		this.blz = blz;
	}
	public String getAccountNr() {
		return accountNr;
	}
	public void setAccountNr(String accountNr) {
		this.accountNr = accountNr;
	}
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ShopDTO() {
		
	}
	
	public ShopDTO(String name, String blz, String url, String accountNr) {
		this.name = name;
		this.blz = blz;
		this.url = url;
		this.accountNr = accountNr;
	}
}
