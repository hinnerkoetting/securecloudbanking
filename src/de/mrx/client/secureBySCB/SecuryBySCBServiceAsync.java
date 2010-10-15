package de.mrx.client.secureBySCB;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.client.BankServiceAsync;
import de.mrx.client.Transaction3SDTO;

public interface SecuryBySCBServiceAsync extends BankServiceAsync {



	void confirmTransaction(Integer id, AsyncCallback<Boolean> callback);

	void getTransactionData(Integer id, AsyncCallback<Transaction3SDTO> callback);


}
