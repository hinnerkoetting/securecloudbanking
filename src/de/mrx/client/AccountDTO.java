package de.mrx.client;

import java.io.Serializable;


/**
 * Data transfer object for an account
 * @see de.mrx.server.GeneralAccount
 */
public class AccountDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3547407345134187104L;
	private String accountNr;
	private double balance;//current money
	private String owner;
	
	
	public final static int SAVING_ACCOUNT=1;
	public final static int CREDITCARD_ACCOUNT=2;
	
	public final static String SAVING_ACCOUNT_DES="Savings";
	public final static String CREDITCARD_ACCOUNT_DES="Credit Card";
	
	
	private String accountDescription;


	public String getAccountDescription() {
		return accountDescription;
	}

	public void setAccountDescription(String accountBeschreibung) {
		this.accountDescription = accountBeschreibung;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	private int accountType;

	
	
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
