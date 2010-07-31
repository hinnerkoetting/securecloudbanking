package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mrx.shared.Transaction;

@RemoteServiceRelativePath("banking")
public interface BankingService extends RemoteService {
	
	public IdentityDTO  login(String requestUri);
	
	public double getBalance();
	
	public List<AccountDTO> getAccounts();
	
	public List<MoneyTransferDTO> getTransaction();
	
	public void openNewAccount();

}
