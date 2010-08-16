package de.mrx.server;

import java.io.Serializable;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;

@PersistenceCapable
public class Account extends GeneralAccount implements Serializable{
	
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public static Account getOwnByAccountNr(PersistenceManager pm, String accountNr){
		
		Extent e=pm.getExtent(Account.class);
		Query query=pm.newQuery(e,"accountNr == accountNrParam");
		query.setFilter("accountNr == accountNrParam");
		query.declareParameters("java.lang.String accountNrParam");
		query.setUnique(true);
		Account result= (Account) query.execute(accountNr);
		
		return result;
	}
	@Persistent
	private String accountDescription;


	@Persistent
	private int accountType;


	@Persistent
	private double balance;//current money


	public Account(){
    	
    }

	public Account( String owner, String accountNr, double balance, Bank bank) {
		super(owner,accountNr, bank);
		this.balance = balance;		
	}

	    
    public String getAccounDescriptiont() {
		return accountDescription;
	}
    
    public int getAccountType() {
		return accountType;
	}
    
    public double getBalance() {
	return balance;
}
    
    
    	public AccountDetailDTO getDetailedDTO(PersistenceManager pm) {
    		AccountDetailDTO dto=super.getDetailedDTO(pm);
    		dto.setBalance(getBalance());    		
    		return dto;
    	}

	
    	public AccountDTO getDTO() {
    		AccountDTO dto = super.getDTO();
    		dto.setBalance(getBalance());
    		dto.setAccountDescription(getAccounDescriptiont());
    		dto.setAccountNr(getAccountNr());
    		return dto;
    	}
    	
    	
    	public void setAccountDescription(String accountDesc) {
			this.accountDescription = accountDesc;
		}
    	
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
}
