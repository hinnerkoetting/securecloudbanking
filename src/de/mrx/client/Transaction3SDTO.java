package de.mrx.client;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Hinni
 * securybyscb transaction dto
 */

public class Transaction3SDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String shopName;
	
	Double amount;

	Date date;
	
	String accountNr;
	
	int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	
	
	public String getAccountNr() {
		return accountNr;
	}

	public void setAccountNr(String accountNr) {
		this.accountNr = accountNr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Transaction3SDTO(String shopName, Double amount, Date date, String accountNr, int id) {
		this.shopName = shopName;
		this.amount = amount;
		this.date = date;
		this.accountNr = accountNr;
		this.id = id;
	}
	
	public Transaction3SDTO() {
		
	}
}
