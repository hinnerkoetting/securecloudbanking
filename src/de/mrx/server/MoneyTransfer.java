package de.mrx.server;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.MoneyTransferDTO;


@PersistenceCapable
public class MoneyTransfer implements Serializable{

	private final static Logger logger=Logger.getLogger(MoneyTransfer.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getReceiverAccountNr() {
		return receiverAccountNr;
	}


	public void setReceiverAccountNr(String receiverAccountNr) {
		this.receiverAccountNr = receiverAccountNr;
	}


	public String getReceiverBLZ() {
		return receiverBLZ;
	}


	public void setReceiverBLZ(String receiverBLZ) {
		this.receiverBLZ = receiverBLZ;
	}


	public String getSenderAccountNr() {
		return senderAccountNr;
	}


	public void setSenderAccountNr(String senderAccountNr) {
		this.senderAccountNr = senderAccountNr;
	}


	public String getSenderBLZ() {
		return senderBLZ;
	}


	public void setSenderBLZ(String senderBLZ) {
		this.senderBLZ = senderBLZ;
	}



	@Persistent
	private double amount;

	
	@PrimaryKey
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	public void setId(Key id) {
		this.id = id;
	}



	@Persistent
	private String receiverAccountNr;

	@Persistent
	private String receiverBLZ;


	@Persistent
	private String senderAccountNr;
	
	@Persistent
	private String senderBLZ;
	
	public MoneyTransfer(){
		
	}


	public MoneyTransfer( GeneralAccount sender, GeneralAccount receiver, double amount) {
		super();
		if (sender==null){
			throw new RuntimeException("Sender may not be null");
		}
		if (receiver==null){
			throw new RuntimeException("Receiver may not be null");
		}
		this.amount = amount;
		senderAccountNr=sender.getAccountNr();
		senderBLZ=sender.getBank().getBlz();
		receiverAccountNr=receiver.getAccountNr();
		receiverBLZ=receiver.getBank().getBlz();		
	}



	public double getAmount() {
		return amount;
	}



	public MoneyTransferDTO getDTO(){
		
		PersistenceManager pm= PMF.get().getPersistenceManager();
		
		Account s=(Account)Account.getOwnByAccountNr(senderAccountNr);
		

		Bank b=Bank.getByBLZ(receiverBLZ);
		ExternalAccount r=(ExternalAccount)ExternalAccount.getAccountByBLZAndAccountNr(b,receiverAccountNr);
		if (r!=null){
		MoneyTransferDTO dto=new MoneyTransferDTO(s.getBank().getBlz(),s.getAccountNr(),r.getBank().getBlz(),r.getAccountNr(),getAmount());
		
			return dto;
		}
		else{
			MoneyTransferDTO dto=new MoneyTransferDTO();
			dto.setAmount(getAmount());
			dto.setSenderAccountNr(s.getAccountNr());
			dto.setSenderBankNr(s.getBank().getBlz());
			dto.setReceiverAccountNr("Unbekannt");
			dto.setReceiverBankNr("Unbekannt");
			return dto;
			
		}
	}
	
	public Key getId() {
		return id;
	}
	




	
	public void setAmount(double amount) {
		this.amount = amount;
	}

	
	
	

}
