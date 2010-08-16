package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mrx.shared.SCBException;

@RemoteServiceRelativePath("banking")
public interface BankingService extends RemoteService {
	
	public SCBIdentityDTO  login(String requestUri);
	
	public double getBalance(String accountNr);
	
	public List<AccountDTO> getAccounts();
	
	public List<MoneyTransferDTO> getTransaction(String accountNr);
	
	public AccountDetailDTO getAccountDetails(String accountNr) throws SCBException;
	
	public void openNewAccount();
	
	public void sendMoney(String senderAccountNr, String blz, String accountNr, double amount, String remark, String receiverName, String bankName);

	

}
