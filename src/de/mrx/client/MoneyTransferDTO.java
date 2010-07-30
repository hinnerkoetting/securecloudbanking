package de.mrx.client;

import de.mrx.server.Account;

public class MoneyTransferDTO {

	private double amount;


	private int receiverAccountNr;


	private String receiverBankNr;


	private AccountDTO sender;


	public MoneyTransferDTO(double amount, AccountDTO sender,
			int receiverAccountNr, String receiverBankNr) {
		super();
		this.amount = amount;
		this.sender = sender;
		this.receiverAccountNr = receiverAccountNr;
		this.receiverBankNr = receiverBankNr;
	}


	public double getAmount() {
		return amount;
	}


	public int getReceiverAccountNr() {
		return receiverAccountNr;
	}


	public String getReceiverBankNr() {
		return receiverBankNr;
	}


	public AccountDTO getSender() {
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


	public void setSender(AccountDTO sender) {
		this.sender = sender;
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
				+ (sender != null ? "sender=" + sender : "") + "]";
	}

	
}
