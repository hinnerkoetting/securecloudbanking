package de.mrx.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;

/**
 * represents an account. Can either be an internal account (SCBAccount) or an external Account.
 * @author IV#11C9
 *
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class GeneralAccount {
	@Persistent
	private String accountNr;
	
	@Persistent
	private String blz;

	
	public String getBLZ() {
		return blz;
	}

	
//	public void setBankID(Key bankID) {
//		this.bankID = bankID;
//	}
	
	

	@PrimaryKey	
	private Key id;

	@Persistent
	private String owner;

	@Persistent
	List<MoneyTransfer> transfers;

	
	
	public void setTransfers(List<MoneyTransfer> transfers) {
		this.transfers = transfers;
	}

	public GeneralAccount(){
		 transfers=new ArrayList<MoneyTransfer>();
	}
	
	

	protected GeneralAccount(String owner, String accountNr,Bank bank, String accountType){
		this.owner=owner;
		this.accountNr=accountNr;
		this.blz = bank.getBlz();
		transfers= new ArrayList<MoneyTransfer>();
		id = KeyFactory.createKey(bank.getId(),
				accountType, accountNr);
		
	}
	
	protected GeneralAccount(AccountDTO dto, Bank bank, String accountType) {
		this(dto.getOwner(), dto.getAccountNr(), bank, accountType);
	}

	public void addMoneyTransfer(MoneyTransfer transfer) {
		transfers.add(transfer);
	}

	public String getAccountNr() {
		return accountNr;
	}


	public AccountDTO getDTO() {
		AccountDTO dto = new AccountDTO(getOwner(), getAccountNr());
		
		return dto;
	}
	
	
	public AccountDetailDTO getDetailedDTO(PersistenceManager pm) {
		AccountDetailDTO dto = new AccountDetailDTO(getOwner(), getAccountNr());
		for (MoneyTransfer m:getTransfers()){
			dto.addTranfer(m.getDTO(pm));
		}
		
		return dto;
	}
	

	public Key getId() {
		return id;
	}

	public String getOwner() {
		return owner;
	}

	public List<MoneyTransfer> getTransfers() {
		return transfers;
	}

	public void setAccountNr(String accountNr) {
		this.accountNr = accountNr;
	}

//	public void setBank(Bank bank) {
//		this.bankID = bank.getId();
//	}

//	public void setId(Key id) {
//		this.id = id;
//	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
    
    /**
     * retrieve stored account
     * @param pm
     * @param accountNr
     * @param blz
     * @return
     */
    public static GeneralAccount getAccount(PersistenceManager pm, String accountNr, String blz){
    	
    	if (blz.equals(Bank.SCB_BLZ)) { //internal account
    		return InternalSCBAccount.getOwnByAccountNr(pm, accountNr);
    	}
    	else {//external account 
    		return ExternalAccount.getExternalAccount(pm, accountNr, blz);
    	}
    	
    }

    public abstract void changeMoney(double amount);

	
}
