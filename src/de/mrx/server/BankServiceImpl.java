package de.mrx.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.AccountDetailDTO;
import de.mrx.client.BankService;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.shared.SCBException;

public abstract class BankServiceImpl extends RemoteServiceServlet implements
BankService {

	/**
	 * get all account details
	 * 
	 * @param accountNr
	 *            account that should be fetched
	 * @return detailled information
	 * @throws SCBException
	 *             if data can not be fetched
	 */
	public AccountDetailDTO getAccountDetails(String accountNr)
			throws SCBException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		InternalSCBAccount acc = InternalSCBAccount.getOwnByAccountNr(pm,
				accountNr);
		if (acc == null) {
			throw new SCBException("Account data for Account '" + accountNr
					+ "' can not be loaded at the moment!");
		}
		return acc.getDetailedDTO(pm);
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(CustomerServiceImpl.class.getName());
	
	/**
	 * transfers money from one account to antother
	 * @param pm
	 * @param senderAccount
	 * @param recAccount
	 * @param transfer
	 * @param amount
	 * @param remark
	 */
	protected void transferMoney(PersistenceManager pm, GeneralAccount senderAccount, GeneralAccount recAccount, MoneyTransfer transfer, double amount, String remark) {
		pm.currentTransaction().begin();

		senderAccount.addMoneyTransfer(transfer);
		// recAccount.addMoneyTransfer(transfer);Sp�ter eine Kopie anlegen

		senderAccount.changeMoney(-amount);
		MoneyTransfer receivertransfer = new MoneyTransfer(pm, recAccount,
				senderAccount, -amount,senderAccount.getOwner(),remark);
		
		recAccount.addMoneyTransfer(receivertransfer);
						
		recAccount.changeMoney(amount);
		
		
		
		pm.makePersistent(senderAccount);
		pm.makePersistent(recAccount);
		pm.currentTransaction().commit();
	}
	
	
	/**
	 * gets all transaction of one account
	 * may only be called by the owner or an admin?
	 * @param accountNr
	 * @return
	 */
	public List<MoneyTransferDTO> getTransaction(String accountNr) {		
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GeneralAccount acc = InternalSCBAccount.getOwnByAccountNr(pm, accountNr);		
		List<MoneyTransferDTO> mtDTOs = new ArrayList<MoneyTransferDTO>();
		for (MoneyTransfer mtransfer : acc.getTransfers()) {
			MoneyTransferDTO dto = mtransfer.getDTO(pm);
			mtDTOs.add(dto);
			log.info(dto.toString());
		}
		return mtDTOs;
	}
	
	
	protected SCBIdentityDTO getIdentity(PersistenceManager pm, User user) {
		SCBIdentity id = SCBIdentity.getIdentity(pm, user);
		if (id == null) {
			id = new SCBIdentity(user.getEmail());
			id.setNickName(user.getNickname());
		}
		return id.getDTO();

	}
	
	
	/**
	 * login to only banking
	 * @param requestUri page to be redirected after login and logout
	 * @return information about the customer
	 */
	public SCBIdentityDTO login(String requestUri) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		
		SCBIdentityDTO identityInfo;

		if (user != null) {

			log.fine("Login: " + user);

			identityInfo = getIdentity(pm, user);
			
			identityInfo.setLoggedIn(true);
			identityInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			if (userService.isUserAdmin())
				identityInfo.setAdmin(true);
			else 
				identityInfo.setAdmin(false);

		} else {
			identityInfo = new SCBIdentityDTO();
			identityInfo.setLoggedIn(false);
			identityInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		
		
		return identityInfo;
	}
	
}
