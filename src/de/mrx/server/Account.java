package de.mrx.server;

import java.io.Serializable;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;

@PersistenceCapable
public class Account extends GeneralAccount implements Serializable{
	
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@Persistent
	private String ownerEmail;
	
	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	

	public static Account getOwnByAccountNr(PersistenceManager pm, String accountNr){
		
		Extent e=pm.getExtent(Account.class);
		Query query=pm.newQuery(e,"accountNr == accountNrParam");
		query.setFilter("accountNr == accountNrParam");
		query.declareParameters("java.lang.String accountNrParam");
		query.setUnique(true);
		Account result= (Account) query.execute(accountNr);
		
		return result;
	}
	
	
public static Account getOwnByEmail(PersistenceManager pm, String email){
		
		Extent<Account> e=pm.getExtent(Account.class);
		Query query=pm.newQuery(e,"ownerEmail == emailParam");		
		query.declareParameters("java.lang.String emailParam");
		query.setUnique(true);
		Account result= (Account) query.execute(email);
		
		return result;
	}
	
	@Persistent
	private String accountDescription;

	@Persistent
	private int wrongTANCounter=0;

	public int getWrongTANCounter() {
		return wrongTANCounter;
	}
	@Persistent
	private int accountType;
	
	
	@Persistent
	private MoneyTransferPending pendingTransaction;
	
	

	public MoneyTransferPending getPendingTransaction() {
		return pendingTransaction;
	}

	public void setPendingTransaction(MoneyTransferPending pendingTransaction) {
		this.pendingTransaction = pendingTransaction;
	}
	@Persistent
	private double balance;//current money

	@Persistent
	TANList tans;
	
	public Account(){
    	
    }
	
	public Account( String owner, String accountNr, double balance, Bank bank) {
		super(owner,accountNr, bank);
		this.balance = balance;		
		tans=new TANList();
		tans.generatedTANs();
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
    
    
    	public String getTan(int pos){
			return tans.getTan().get(pos);
		}

	
    	public TANList getTans() {
			return tans;
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
	
	public void increaseWrongTANCounter(){
		wrongTANCounter++;
	}
	
	public void resetWrongTANCounter(){
		wrongTANCounter=0;
	}

}
