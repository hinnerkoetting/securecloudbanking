package de.mrx.client;

import java.util.ArrayList;
import java.util.List;

public class AccountDetailDTO extends AccountDTO {
	
	private List<MoneyTransferDTO> transfers=new ArrayList<MoneyTransferDTO>();

	public List<MoneyTransferDTO> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<MoneyTransferDTO> transfers) {
		this.transfers = transfers;
	}
	
	public AccountDetailDTO(){
		
	}
	public AccountDetailDTO(String owner, String accountNr) {
		super(owner,accountNr);
		
	
	}
	
	public void addTranfer(MoneyTransferDTO moneyTransfer){
		transfers.add(moneyTransfer);
	}

}
