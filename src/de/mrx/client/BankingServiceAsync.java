package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BankingServiceAsync {

	

	void getTransaction(String accountNr,AsyncCallback<List<MoneyTransferDTO>> callback);

	void  login(String requestUri,
			AsyncCallback<SCBIdentityDTO> callback);

	void getAccounts(AsyncCallback<List<AccountDTO>> callback);

	void openNewAccount(AsyncCallback<Void> callback);

	void getBalance(String accountNr, AsyncCallback<Double> callback);

	void sendMoney(String senderAccountNr, String blz, String accountNr, double amount,
			AsyncCallback<Void> callback);

	void getAccountDetails(String accountNr,
			AsyncCallback<AccountDetailDTO> callback);

}
