package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.shared.SCBException;

public interface BankServiceAsync {

	/**
	 * online banking service
	 * offers all services directly related to banking * 
	 */
	void  login(String requestUri,
			AsyncCallback<SCBIdentityDTO> callback);
	
	/**
	 * fetches the balance of a given account. 
	 * Can be only used by the owner of the account or an admin?
	 * @param accountNr account that should be access
	 * @return balance of the account
	 */
	void getBalance(String accountNr, AsyncCallback<Double> callback);
	
	/**
	 * gets all transaction of one account
	 * may only be called by the owner or an admin?
	 * @param accountNr
	 * @return
	 */
	void getTransaction(String accountNr,AsyncCallback<List<MoneyTransferDTO>> callback);

	
	/**
	 * get all account details 
	 * @param accountNr account that should be fetched
	 * @return detailled information
	 * @throws SCBException if data can not be fetched
	 */
	void getAccountDetails(String accountNr,
			AsyncCallback<AccountDetailDTO> callback);

	
}
