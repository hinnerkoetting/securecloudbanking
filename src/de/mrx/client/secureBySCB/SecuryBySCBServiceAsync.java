package de.mrx.client.secureBySCB;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.client.BankServiceAsync;
import de.mrx.client.TransactionDTO;

public interface SecuryBySCBServiceAsync extends BankServiceAsync {

	void addTransaction(String shop, Double amount,
			AsyncCallback<Long> callback);

	void confirmTransaction(Long id, AsyncCallback<Boolean> callback);

	void getTransactionData(Long id, AsyncCallback<TransactionDTO> callback);


}
