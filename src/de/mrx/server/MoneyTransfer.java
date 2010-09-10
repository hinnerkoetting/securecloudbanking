package de.mrx.server;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.mrx.client.MoneyTransferDTO;


/**
 * represents an moneytransfer between an account of a customer of SCB-Bank and another account (the other account be an InternalSBAccount or an external account
 *  
 * @author IV#11C9
 *
 */
@PersistenceCapable
public class MoneyTransfer {

	
	@Persistent	
	private double amount;
	
	@PrimaryKey
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
 
	@Persistent
	private boolean pending;
	
	@Persistent
	private String receiverAccountNr;
	

	@Persistent
	private String receiverBLZ;


	@Persistent
	private String receiverName;


	@Persistent
	private String remark;


	@Persistent
	private int requiredTan;






	@Persistent
	private String senderAccountNr;


	@Persistent
	private String senderBLZ;

	@Persistent
	private Date timestamp;


	public MoneyTransfer(){
		
	}
	
	public MoneyTransfer( GeneralAccount sender, GeneralAccount receiver, double amount, String receiverName, String remark) {
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
		this.receiverName=receiverName;
		this.remark=remark;
		timestamp=new Date();
		
	}


	

	public double getAmount() {
		return amount;
	}


	public MoneyTransferDTO getDTO(PersistenceManager pm){		
		InternalSCBAccount s=(InternalSCBAccount)InternalSCBAccount.getOwnByAccountNr(pm,senderAccountNr);
		Bank b=Bank.getByBLZ(pm,receiverBLZ);
		GeneralAccount receiverAccount;
		if (b.getBlz().equals(Bank.SCB_BLZ)){
			receiverAccount=InternalSCBAccount.getOwnByAccountNr(pm,receiverAccountNr);
		}
		else{
			receiverAccount=(ExternalAccount)ExternalAccount.getAccountByBLZAndAccountNr(pm,b,receiverAccountNr);	
		}		
		if (receiverAccount!=null){
		MoneyTransferDTO dto=new MoneyTransferDTO(s.getBank().getBlz(),s.getAccountNr(),receiverAccount.getBank().getBlz(),receiverAccount.getAccountNr(),getAmount());
		dto.setTimestamp(getTimestamp());
		dto.setRemark(getRemark());
		dto.setReceiverName(getReceiverName());
		dto.setRequiredTan(getRequiredTan());
		dto.setReceiverBankName(b.getName());
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


	public String getReceiverName() {
		return receiverName;
	}


	public String getRemark() {
		return remark;
	}


	public int getRequiredTan() {
		return requiredTan;
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

	
	public boolean isPending() {
		return pending;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}



	public void setId(Key id) {
		this.id = id;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}


	public void setReceiverAccountNr(String receiverAccountNr) {
		this.receiverAccountNr = receiverAccountNr;
	}
	
	public void setReceiverBLZ(String receiverBLZ) {
		this.receiverBLZ = receiverBLZ;
	}
	
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}



	public void setRequiredTan(int requiredTan) {
		this.requiredTan = requiredTan;
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
