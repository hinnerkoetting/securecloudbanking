package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.mrx.shared.SCBException;

/**
 * asynchron interface for Banking service.
 * @see de.mrx.client.CustomerService
 *
 */
public interface CustomerServiceAsync extends BankServiceAsync {

	
	

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

	

	/*
	 * sends money. For this service, the sender must be a customer of SCB
	 */
	void sendMoney(String senderAccountNr, String blz, String accountNr, double amount, String remark,
			String receiverName,String bankName, String tan, AsyncCallback<Void> callback);

	
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
