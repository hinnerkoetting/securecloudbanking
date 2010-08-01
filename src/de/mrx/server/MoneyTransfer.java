package de.mrx.server;

import java.io.Serializable;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.MoneyTransferDTO;

@PersistenceCapable
public class MoneyTransfer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	
	@Persistent
	private Key sender;
	
	@Persistent
	private Key receiver;
	
	@Persistent
	private double amount;
	
//	@Persistent
//	private String senderAccountNr;
	
//	public String getSenderAccountNr() {
//		return senderAccountNr;
//	}



//	public void setSenderAccountNr(String senderAccountNr) {
//		this.senderAccountNr = senderAccountNr;
//	}
//	@Persistent
//	private String receiverAccountNr;
//	
//	@Persistent
//	private String receiverBankNr;

	

	public MoneyTransfer( GeneralAccount sender, GeneralAccount receiver, double amount) {
		super();
		if (sender==null){
			throw new RuntimeException("Sender may not be null");
		}
		if (receiver==null){
			throw new RuntimeException("Receiver may not be null");
		}
		this.amount = amount;
		this.sender=sender.getId();
		this.receiver=receiver.getId();
	}



	public double getAmount() {
		return amount;
	}

	public Key getId() {
		return id;
	}

	
	

	public void setAmount(double amount) {
		this.amount = amount;
	}

	
	
	public MoneyTransferDTO getDTO(){
		if (sender==null){
			throw new RuntimeException("getDTO: Sender may not be null");
		}
		if (receiver==null){
			throw new RuntimeException("getDTO: Receiver may not be null");
		}
		PersistenceManager pm= PMF.get().getPersistenceManager();
		Account s=(Account)pm.getObjectById(sender);
		Account r=(Account)pm.getObjectById(receiver);
		MoneyTransferDTO dto=new MoneyTransferDTO(s.getBank().getBlz(),s.getAccountNr(),r.getBank().getBlz(),r.getAccountNr(),getAmount());
		
		return dto;
	}
	

}
