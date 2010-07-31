package de.mrx.client;

import java.io.Serializable;


public class MoneyTransferDTO implements Serializable{

	private double amount;


	private String receiverAccountNr;


	private String receiverBankNr;


	private String senderAccountNr;


	public String getSenderAccountNr() {
		return senderAccountNr;
	}


	public void setSenderAccountNr(String senderAccountNr) {
		this.senderAccountNr = senderAccountNr;
	}

	public MoneyTransferDTO(){
		
	}
	
	public MoneyTransferDTO(double amount, String senderAccountNr,
			String receiverAccountNr, String receiverBankNr) {
		super();
		this.amount = amount;
		this.senderAccountNr = senderAccountNr;
		this.receiverAccountNr = receiverAccountNr;
		this.receiverBankNr = receiverBankNr;
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
		return "MoneyTransferDTO [amount="
				+ amount
				+ ", receiverAccountNr="
				+ receiverAccountNr
				+ ", "
				+ (receiverBankNr != null ? "receiverBankNr=" + receiverBankNr
						+ ", " : "")
				+ (senderAccountNr != null ? "sender=" + senderAccountNr : "") + "]";
	}

	
}
