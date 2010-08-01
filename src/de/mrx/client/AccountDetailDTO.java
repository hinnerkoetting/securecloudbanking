package de.mrx.client;

import java.util.List;

public class AccountDetailDTO extends AccountDTO {
	
	private List<MoneyTransferDTO> transfers;

	public List<MoneyTransferDTO> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<MoneyTransferDTO> transfers) {
		this.transfers = transfers;
	}

}
