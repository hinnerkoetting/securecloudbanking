package de.mrx.client;

import java.io.Serializable;



public class AccountDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3547407345134187104L;
	private String accountNr;
	private double balance;//current money
	private String owner;
	
	public AccountDTO(){
		
	}
	
	public AccountDTO(String owner, String accountNr) {
		super();
		this.owner = owner;
		this.accountNr = accountNr;
	
	}
	public String getAccountNr() {
		return accountNr;
	}
	public double getBalance() {
		return balance;
	}
	public String getOwner() {
		return owner;
	}
	public void setAccountNr(String accountNr) {
		this.accountNr = accountNr;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	@Override
	public String toString() {
		return "AccountDTO ["
				+ (accountNr != null ? "accountNr=" + accountNr + ", " : "")
				+ "balance=" + balance + ", "
				+ (owner != null ? "owner=" + owner : "") + "]";
	}
}
