package de.mrx.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceAware;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Inheritance;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import javax.jdo.annotations.InheritanceStrategy;
import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;


@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class GeneralAccount {
	@Persistent
	private String accountNr;
	
//	@Persistent
	private Key bankID;

	
	public Key getBankID() {
		return bankID;
	}

	public void setBankID(Key bankID) {
		this.bankID = bankID;
	}
	
	

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
	
	public GeneralAccount(AccountDTO dto, Bank bank) {
		this.owner = dto.getOwner();
		this.accountNr = dto.getAccountNr();
		this.bankID=bank.getId();
		transfers= new ArrayList<MoneyTransfer>();

	}

	public GeneralAccount(String owner, String accountNr,Bank bank){
		this.owner=owner;
		this.accountNr=accountNr;
		this.bankID=bank.getId();
		transfers= new ArrayList<MoneyTransfer>();
		
	}

	public void addMoneyTransfer(MoneyTransfer transfer) {
		transfers.add(transfer);
	}

	public String getAccountNr() {
		return accountNr;
	}

	public Bank getBank() {
		Bank bank=PMF.get().getPersistenceManager().getObjectById(Bank.class,getBankID());
		return bank;
	}
	public AccountDTO getDTO() {
		AccountDTO dto = new AccountDTO(getOwner(), getAccountNr());
		return dto;
	}
	
	
	public AccountDetailDTO getDetailedDTO() {
		AccountDetailDTO dto = new AccountDetailDTO(getOwner(), getAccountNr());
		for (MoneyTransfer m:getTransfers()){
			dto.addTranfer(m.getDTO());
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

	public void setBank(Bank bank) {
		this.bankID = bank.getId();
	}

	public void setId(Key id) {
		this.id = id;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	

	
}
