package de.mrx.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.mrx.client.MoneyTransferDTO;

/**
 * a moneytransfer that is waiting for TAN-Based confirmation
 *
 */
@PersistenceCapable
public class MoneyTransferPending {
	
	
	
	@Persistent	
	private double amount;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

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

	
	@Persistent
	private String receiverBankName;

	
	public String getReceiverBankName() {
		return receiverBankName;
	}

	public void setReceiverBankName(String receiverBankName) {
		this.receiverBankName = receiverBankName;
	}

	public MoneyTransferDTO getDTO(){
		MoneyTransferDTO dto=new MoneyTransferDTO();
		dto.setAmount(getAmount());
		dto.setReceiverAccountNr(getReceiverAccountNr());
		dto.setReceiverBankNr(getReceiverBLZ());
		dto.setReceiverName(getReceiverName());
		dto.setRemark(getRemark());
		dto.setRequiredTan(getRequiredTan());
		dto.setSenderAccountNr(getSenderAccountNr());
		dto.setSenderBankNr(getSenderBLZ());
		dto.setTimestamp(getTimestamp());
		dto.setReceiverBankName(getReceiverBankName());
		
		return dto;
	}
	
	public double getAmount() {
		return amount;
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

	public void setAmount(double amount) {
		this.amount = amount;
	}

 
	public MoneyTransferPending(String remark, String receiverName, String bankName,
								InternalSCBAccount senderAccount, String blz, String receiveraccountNr,
								double amount, int requiredTan) {
		this.remark = remark;
		this.receiverName =  receiverName;
		this.receiverBankName = bankName;
		this.senderAccountNr = senderAccount.getAccountNr();
		this.receiverBLZ = blz;
		this.receiverAccountNr = receiveraccountNr;
		this.amount = amount;
		this.requiredTan = requiredTan;
		this.timestamp = new Date();
		this.id = KeyFactory.createKey(senderAccount.getId(), MoneyTransferPending.class.getSimpleName(),
							senderAccount.getAccountNr()+ "_" + requiredTan + "_" + timestamp);
		
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


//	public void setRequiredTan(int requiredTan) {
//		this.requiredTan = requiredTan;
//	}






	public void setSenderAccountNr(String senderAccountNr) {
		this.senderAccountNr = senderAccountNr;
	}


	public void setSenderBLZ(String senderBLZ) {
		this.senderBLZ = senderBLZ;
	}


}
