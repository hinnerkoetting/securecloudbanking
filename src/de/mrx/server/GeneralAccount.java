package de.mrx.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Inheritance;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import javax.jdo.annotations.InheritanceStrategy;
import de.mrx.client.AccountDTO;


@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class GeneralAccount {
	@Persistent
	private String accountNr;
	
//	@Persistent
	private Key bankID;

	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	private String owner;

	@Persistent
	List<Key> transfers = new ArrayList<Key>();

	public GeneralAccount(AccountDTO dto, Bank bank) {
		this.owner = dto.getOwner();
		this.accountNr = dto.getAccountNr();
		this.bankID=bank.getId();

	}

	public GeneralAccount(String owner, String accountNr,Bank bank){
		this.owner=owner;
		this.accountNr=accountNr;
		this.bankID=bank.getId();
		
	}

	public void addMoneyTransfer(MoneyTransfer transfer) {
		transfers.add(transfer.getId());
	}

	public String getAccountNr() {
		return accountNr;
	}

	public Bank getBank() {
		Bank bank=PMF.get().getPersistenceManager().getObjectById(Bank.class,getId());
		return bank;
	}
	public AccountDTO getDTO() {
		AccountDTO dto = new AccountDTO(getOwner(), getAccountNr());
		return dto;
	}

	public Key getId() {
		return id;
	}

	public String getOwner() {
		return owner;
	}

	public List<MoneyTransfer> getTransfers() {
		List<MoneyTransfer> result=new ArrayList<MoneyTransfer>();
		PersistenceManager pm=PMF.get().getPersistenceManager();
		for (Key k:transfers){
			result.add((MoneyTransfer) pm.getObjectById(k));
		}
		return result;
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
