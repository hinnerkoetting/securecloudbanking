package de.mrx.server;

import java.io.Serializable;
import java.util.Date;
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
	
	@Persistent	
	private double amount;
	
	@PrimaryKey
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
 
	@Persistent
	private boolean pending;
	
	@Persistent
	private int requiredTan;
	

	public int getRequiredTan() {
		return requiredTan;
	}


	public void setRequiredTan(int requiredTan) {
		this.requiredTan = requiredTan;
	}


	public boolean isPending() {
		return pending;
	}


	public void setPending(boolean pending) {
		this.pending = pending;
	}






	@Persistent
	private String receiverAccountNr;


	@Persistent
	private String receiverBLZ;

	@Persistent
	private String remark;


	@Persistent
	private String senderAccountNr;
	
	@Persistent
	private String receiverName;


	

	public String getReceiverName() {
		return receiverName;
	}


	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}






	@Persistent
	private String senderBLZ;

	@Persistent
	private Date timestamp;


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
		timestamp=new Date();
		
	}


	public double getAmount() {
		return amount;
	}


	public MoneyTransferDTO getDTO(PersistenceManager pm){
		
		
		
		Account s=(Account)Account.getOwnByAccountNr(pm,senderAccountNr);
		

		Bank b=Bank.getByBLZ(pm,receiverBLZ);
		ExternalAccount r=(ExternalAccount)ExternalAccount.getAccountByBLZAndAccountNr(pm,b,receiverAccountNr);
		if (r!=null){
		MoneyTransferDTO dto=new MoneyTransferDTO(s.getBank().getBlz(),s.getAccountNr(),r.getBank().getBlz(),r.getAccountNr(),getAmount());
		dto.setTimestamp(getTimestamp());
		dto.setRemark(getRemark());
		dto.setReceiverName(getReceiverName());
		dto.setRequiredTan(getRequiredTan());
			return dto;
		}
		else{
			MoneyTransferDTO dto=new MoneyTransferDTO();
			dto.setAmount(getAmount());
			dto.setSenderAccountNr(s.getAccountNr());
			dto.setSenderBankNr(s.getBank().getBlz());
			dto.setReceiverAccountNr("Unbekannt");
			dto.setReceiverBankNr("Unbekannt");
			dto.setRemark(getRemark());
			dto.setTimestamp(getTimestamp());
			dto.setReceiverName(getReceiverName());
			dto.setRequiredTan(getRequiredTan());
			return dto;
			
		}
	}


	public Key getId() {
		return id;
	}


	public String getReceiverAccountNr() {
		return receiverAccountNr;
	}



	public String getReceiverBLZ() {
		return receiverBLZ;
	}

	
	public String getRemark() {
		return remark;
	}
	
	public String getSenderAccountNr() {
		return senderAccountNr;
	}



	public String getSenderBLZ() {
		return senderBLZ;
	}

	public Date getTimestamp() {
		return timestamp;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void setId(Key id) {
		this.id = id;
	}
	
	public void setReceiverAccountNr(String receiverAccountNr) {
		this.receiverAccountNr = receiverAccountNr;
	}


	public void setReceiverBLZ(String receiverBLZ) {
		this.receiverBLZ = receiverBLZ;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public void setSenderAccountNr(String senderAccountNr) {
		this.senderAccountNr = senderAccountNr;
	}
	
	public void setSenderBLZ(String senderBLZ) {
		this.senderBLZ = senderBLZ;
	}
	




	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	
	
	

}
