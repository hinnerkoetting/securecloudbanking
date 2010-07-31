package de.mrx.server;

import java.io.Serializable;

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
	private double amount;
	
	@Persistent
	private String senderAccountNr;
	
	public String getSenderAccountNr() {
		return senderAccountNr;
	}



	public void setSenderAccountNr(String senderAccountNr) {
		this.senderAccountNr = senderAccountNr;
	}
	@Persistent
	private String receiverAccountNr;
	
	@Persistent
	private String receiverBankNr;

	

	public MoneyTransfer(double amount, Account sender, String receiverAccountNr,
			String receiverBankNr) {
		super();
		this.amount = amount;
		this.senderAccountNr = sender.getAccountNr();
		this.receiverAccountNr = receiverAccountNr;
		this.receiverBankNr = receiverBankNr;
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

	public String getReceiverBankNr() {
		return receiverBankNr;
	}

	

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setReceiverAccountNr(String receiverAccountNr) {
		this.receiverAccountNr = receiverAccountNr;
	}

	public void setReceiverBankNr(String receiverBankNr) {
		this.receiverBankNr = receiverBankNr;
	}

	
	@Override
	public String toString() {
		return "MoneyTransfer [amount="
				+ amount
				+ ", receiverAccountNr="
				+ receiverAccountNr
				+ ", "
				+ (receiverBankNr != null ? "receiverBankNr=" + receiverBankNr
						+ ", " : "")
				+ (senderAccountNr != null ? "sender=" + senderAccountNr : "") + "]";
	}
	
	public MoneyTransfer(MoneyTransferDTO dto){
		setAmount(dto.getAmount());
		setReceiverAccountNr(dto.getReceiverAccountNr());
		setReceiverBankNr(dto.getReceiverBankNr());
		setSenderAccountNr(dto.getSenderAccountNr());
	}
	public MoneyTransferDTO getDTO(){
		MoneyTransferDTO dto=new MoneyTransferDTO(getAmount(),getSenderAccountNr(),getReceiverAccountNr(),getReceiverBankNr());
		return dto;
	}
	

}
