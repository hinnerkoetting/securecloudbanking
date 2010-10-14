package de.mrx.client;

import java.io.Serializable;

/**
 * 
 * @author Hinni
 * securybyscb transaction dto
 */

public class TransactionDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String shopName;
	
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

	Double amount;

	public TransactionDTO(String shopName, Double amount) {
		this.shopName = shopName;
		this.amount = amount;
	}
	
	public TransactionDTO() {
		
	}
}
