package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.shared.Transaction;

public interface BankingServiceAsync {

	void getBalance(AsyncCallback<Double> callback);

	void getTransaction(AsyncCallback<List<MoneyTransferDTO>> callback);

	void  login(String requestUri,
			AsyncCallback<IdentityDTO> callback);

	void getAccounts(AsyncCallback<List<AccountDTO>> callback);

	void openNewAccount(AsyncCallback<Void> callback);

}
