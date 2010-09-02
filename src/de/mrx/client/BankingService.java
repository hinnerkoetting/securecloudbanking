package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.mrx.shared.SCBException;

/**
 * online banking service
 * offers all services directly related to banking * 
 */
@RemoteServiceRelativePath("banking")
public interface BankingService extends RemoteService {
	
	/**
	 * login to only banking
	 * @param requestUri page to be redirected after login and logout
	 * @return information about the customer
	 */
	public SCBIdentityDTO  login(String requestUri);
	
	/**
	 * fetches the balance of a given account. 
	 * Can be only used by the owner of the account or an admin?
	 * @param accountNr account that should be access
	 * @return balance of the account
	 */
	public double getBalance(String accountNr);
	
	/**
	 * fetches information about all account of a user
	 * @return list of account data
	 */
	public List<AccountDTO> getAccounts();
	
	/**
	 * gets all transaction of one account
	 * may only be called by the owner or an admin?
	 * @param accountNr
	 * @return
	 */
	public List<MoneyTransferDTO> getTransaction(String accountNr);
	
	/**
	 * get all account details 
	 * @param accountNr account that should be fetched
	 * @return detailled information
	 * @throws SCBException if data can not be fetched
	 */
	public AccountDetailDTO getAccountDetails(String accountNr) throws SCBException;
	
	/**
	 * open a new account for the logged in customer
	 * @throws SCBException if account can not be created
	 */
	public void openNewAccount() throws SCBException;
	
	/*
	 * sends money. For this service, the sender must be a customer of SCB
	 */
	public void sendMoney(String senderAccountNr, String blz, String accountNr, double amount, String remark, String receiverName, String bankName, String tan) throws SCBException;
	
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
	public MoneyTransferDTO sendMoneyAskForConfirmationData(String senderAccountNr, String blz, String accountNr, double amount, String remark, String receiverName, String bankName) throws SCBException;

	/**
	 * send all details for a money transaction. The recipient must be customer of SCB and is determined with his email. The money is not yet transferred, but in a second step must be confirmed with a TAN
	 */
	public MoneyTransferDTO sendMoneyAskForConfirmationDataWithEmail(String senderAccountNr, String email, double amount, String remark) throws SCBException;
	

	

}
