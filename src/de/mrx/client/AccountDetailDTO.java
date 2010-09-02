package de.mrx.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object for an account with detailed information (included all transaction)
 * @see de.mrx.server.GeneralAccount
 *
 */
public class AccountDetailDTO extends AccountDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3649194158256155725L;
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
