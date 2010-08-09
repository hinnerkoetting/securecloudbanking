package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("banking")
public interface BankingService extends RemoteService {
	
	public SCBIdentityDTO  login(String requestUri);
	
	public double getBalance(String accountNr);
	
	public List<AccountDTO> getAccounts();
	
	public List<MoneyTransferDTO> getTransaction(String accountNr);
	
	public AccountDetailDTO getAccountDetails(String accountNr);
	
	public void openNewAccount();
	
	public void sendMoney(String senderAccountNr, String blz, String accountNr, double amount, String remark);

	

}
