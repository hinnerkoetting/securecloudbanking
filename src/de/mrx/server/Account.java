package de.mrx.server;

import java.io.Serializable;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.Query;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Account extends GeneralAccount implements Serializable{
	
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	    
    @Persistent
	private double balance;//current money
    
    public Account(){
    	
    }
    
    public Account( String owner, String accountNr, double balance, Bank bank) {
		super(owner,accountNr, bank);
		this.balance = balance;		
	}
    
    	public double getBalance() {
		return balance;
	}

	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public static Account getOwnByAccountNr(String accountNr){
		Extent e=PMF.get().getPersistenceManager().getExtent(Account.class);
		Query query=PMF.get().getPersistenceManager().newQuery(e,"accountNr == accountNrParam");
		query.setFilter("accountNr == accountNrParam");
		query.declareParameters("java.lang.String accountNrParam");
		query.setUnique(true);
		Account result= (Account) query.execute(accountNr);
		
		return result;
	}
}
