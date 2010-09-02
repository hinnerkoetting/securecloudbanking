package de.mrx.server;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;

/**
 * represents an internal account of SCB 
 * Contains the balance and the owner email. 
 * @author IV#11C9
 *
 */
@PersistenceCapable
public class InternalSCBAccount extends GeneralAccount {
	
   

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

	

	public static InternalSCBAccount getOwnByAccountNr(PersistenceManager pm, String accountNr){
		
		Extent<InternalSCBAccount> e=pm.getExtent(InternalSCBAccount.class);
		Query query=pm.newQuery(e,"accountNr == accountNrParam");
		query.setFilter("accountNr == accountNrParam");
		query.declareParameters("java.lang.String accountNrParam");
		query.setUnique(true);
		InternalSCBAccount result= (InternalSCBAccount) query.execute(accountNr);
		
		return result;
	}
	
	
public static InternalSCBAccount getOwnByEmail(PersistenceManager pm, String email){
		
		Extent<InternalSCBAccount> e=pm.getExtent(InternalSCBAccount.class);
		Query query=pm.newQuery(e,"ownerEmail == emailParam");		
		query.declareParameters("java.lang.String emailParam");
		query.setUnique(true);
		InternalSCBAccount result= (InternalSCBAccount) query.execute(email);
		
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
	
	public InternalSCBAccount(){
    	
    }
	
	public InternalSCBAccount( String owner, String accountNr, double balance, Bank bank) {
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
