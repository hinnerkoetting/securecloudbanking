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



	void checkLogin(AsyncCallback<Boolean> callback);

	
}
