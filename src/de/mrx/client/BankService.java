package de.mrx.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import de.mrx.shared.SCBException;


/**
 * 
 * @author hinnerk
 * Interface for accounts service that are offered by the server
 */
public interface BankService extends RemoteService {
	
	/**
	 * login to only banking
	 * @param requestUri page to be redirected after login and logout
	 * @return information about the customer
	 */
	public SCBIdentityDTO  login(String requestUri);

	
	
	
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
	
}
