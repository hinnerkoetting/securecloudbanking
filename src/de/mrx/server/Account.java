package de.mrx.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.AccountDTO;

@PersistenceCapable
public class Account implements Serializable{
	
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
    
    @Persistent
    private String owner;

	@Persistent
	private String accountNr;
    
    @Persistent
	private double balance;//current money
    
    public Account( String owner, String accountNr, double balance) {
		super();		
		this.owner = owner;
		this.accountNr = accountNr;
		this.balance = balance;
	}
    
    public Account(AccountDTO dto){
    	this.owner=dto.getOwner();
    	this.accountNr=dto.getAccountNr();
    	this.balance=dto.getBalance();    	
    }

	public String getAccountNr() {
		return accountNr;
	}

	public double getBalance() {
		return balance;
	}

	public Key getId() {
		return id;
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

	public void setId(Key id) {
		this.id = id;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
    
    @Override
	public String toString() {
		return "Account [accountNr=" + accountNr + ", balance=" + balance
				+ ", " + (owner != null ? "owner=" + owner : "") + "]";
	}
    
    public AccountDTO getDTO(){
    	AccountDTO dto=new AccountDTO(getOwner(), getAccountNr(), getBalance());
    	return dto;
    }

}
