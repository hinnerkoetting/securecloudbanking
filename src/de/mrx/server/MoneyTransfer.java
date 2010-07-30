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
	private Account sender;
	
	@Persistent
	private int receiverAccountNr;
	
	@Persistent
	private String receiverBankNr;

	

	public MoneyTransfer(double amount, Account sender, int receiverAccountNr,
			String receiverBankNr) {
		super();
		this.amount = amount;
		this.sender = sender;
		this.receiverAccountNr = receiverAccountNr;
		this.receiverBankNr = receiverBankNr;
	}



	public double getAmount() {
		return amount;
	}

	public Key getId() {
		return id;
	}

	public int getReceiverAccountNr() {
		return receiverAccountNr;
	}

	public String getReceiverBankNr() {
		return receiverBankNr;
	}

	public Account getSender() {
		return sender;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setReceiverAccountNr(int receiverAccountNr) {
		this.receiverAccountNr = receiverAccountNr;
	}

	public void setReceiverBankNr(String receiverBankNr) {
		this.receiverBankNr = receiverBankNr;
	}

	public void setSender(Account sender) {
		this.sender = sender;
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
				+ (sender != null ? "sender=" + sender : "") + "]";
	}
	
	public MoneyTransfer(MoneyTransferDTO dto){
		setAmount(dto.getAmount());
		setReceiverAccountNr(dto.getReceiverAccountNr());
		setReceiverBankNr(dto.getReceiverBankNr());
		setSender(new Account(dto.getSender()));
	}
	public MoneyTransferDTO getDTO(){
		MoneyTransferDTO dto=new MoneyTransferDTO(getAmount(),getSender().getDTO(),getReceiverAccountNr(),getReceiverBankNr());
		return dto;
	}
	

}
