package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.shared.SCBException;

/**
 * asynchron interface for Banking service.
 * @see de.mrx.client.BankingService
 *
 */
public interface BankingServiceAsync {

	
	/**
	 * gets all transaction of one account
	 * may only be called by the owner or an admin?
	 * @param accountNr
	 * @return
	 */
	void getTransaction(String accountNr,AsyncCallback<List<MoneyTransferDTO>> callback);

	/**
	 * online banking service
	 * offers all services directly related to banking * 
	 */
	void  login(String requestUri,
			AsyncCallback<SCBIdentityDTO> callback);

	/**
	 * fetches information about all account of a user
	 * @return list of account data
	 */
	void getAccounts(AsyncCallback<List<AccountDTO>> callback);

	/**
	 * open a new account for the logged in customer
	 * @throws SCBException if account can not be created
	 */
	void openNewAccount(AsyncCallback<Void> callback);

	/**
	 * fetches the balance of a given account. 
	 * Can be only used by the owner of the account or an admin?
	 * @param accountNr account that should be access
	 * @return balance of the account
	 */
	void getBalance(String accountNr, AsyncCallback<Double> callback);

	/*
	 * sends money. For this service, the sender must be a customer of SCB
	 */
	void sendMoney(String senderAccountNr, String blz, String accountNr, double amount, String remark,
			String receiverName,String bankName, String tan, AsyncCallback<Void> callback);

	/**
	 * get all account details 
	 * @param accountNr account that should be fetched
	 * @return detailled information
	 * @throws SCBException if data can not be fetched
	 */
	void getAccountDetails(String accountNr,
			AsyncCallback<AccountDetailDTO> callback);

	/**
	 * send all details for a money transaction. The money is not yet transferred, but in a second step must be confirmed with a TAN
	 * @param senderAccountNr
	 * @param blz
	 * @param accountNr
	 * @param amount
	 * @param remark
	 * @param receiverName
	 * @param bankName
	 * @return
	 * @throws SCBException
	 */
	void sendMoneyAskForConfirmationData(String senderAccountNr, String blz,
			String accountNr, double amount, String remark,
			String receiverName, String bankName,
			AsyncCallback<MoneyTransferDTO> callback);

	/**
	 * send all details for a money transaction. The recipient must be customer of SCB and is determined with his email. The money is not yet transferred, but in a second step must be confirmed with a TAN
	 */
	void sendMoneyAskForConfirmationDataWithEmail(String senderAccountNr,
			String email, double amount, String remark,
			AsyncCallback<MoneyTransferDTO> callback);

}
