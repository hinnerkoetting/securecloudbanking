package de.mrx.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.AccountDTO;
import de.mrx.client.MoneyTransferDTO;

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
	List<MoneyTransfer> transfers=new ArrayList<MoneyTransfer>();
    
    public List<MoneyTransfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<MoneyTransfer> transfers) {
		this.transfers = transfers;
	}

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
		MoneyTransferDTO dto=new MoneyTransferDTO();
		dto.setAmount(20);
		dto.setReceiverBankNr("1212");
		transfers.add(new MoneyTransfer(dto));
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
				+ ", " + (owner != null ? "owner=" + owner : "") + "Transfers: "+getTransfers().size()+ "]";
	}
    
    public AccountDTO getDTO(){
    	AccountDTO dto=new AccountDTO(getOwner(), getAccountNr(), getBalance());
    	return dto;
    }
    
    public void addMoneyTransfer(MoneyTransfer transfer){
    	transfers.add(transfer);
    }

}
