package de.mrx.client;

import java.io.Serializable;


public class MoneyTransferDTO implements Serializable{

	
	private boolean selfSended=false;
	private double amount;


	public boolean isSelfSended() {
		return selfSended;
	}


	public void setSelfSended(boolean selfSended) {
		this.selfSended = selfSended;
	}




	private String receiverAccountNr;


	public String getSenderBankNr() {
		return senderBankNr;
	}


	public void setSenderBankNr(String senderBankNr) {
		this.senderBankNr = senderBankNr;
	}




	private String receiverBankNr;


	private String senderAccountNr;
	private String senderBankNr;


	public String getSenderAccountNr() {
		return senderAccountNr;
	}


	public void setSenderAccountNr(String senderAccountNr) {
		this.senderAccountNr = senderAccountNr;
	}

	public MoneyTransferDTO(){
		
	}
	
	public MoneyTransferDTO(String senderBLZ, String senderAccountNr,String receiverBLZ,
			String receiverAccountNr, double amount) {
		super();
		this.amount = amount;
		this.senderAccountNr = senderAccountNr;
		this.receiverAccountNr = receiverAccountNr;
		this.receiverBankNr = receiverBLZ;
		this.senderBankNr=senderBLZ;
		
	}


	public double getAmount() {
		return amount;
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
		return "MoneyTransferDTO [selfSended=" + selfSended + ", amount="
				+ amount + ", receiverAccountNr=" + receiverAccountNr
				+ ", receiverBankNr=" + receiverBankNr + ", senderAccountNr="
				+ senderAccountNr + ", senderBankNr=" + senderBankNr + "]";
	}

	
}
