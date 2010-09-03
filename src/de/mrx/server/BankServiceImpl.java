package de.mrx.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.BankService;
import de.mrx.client.MoneyTransferDTO;

public abstract class BankServiceImpl extends RemoteServiceServlet implements
BankService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(CustomerServiceImpl.class.getName());
	
	
	/**
	 * gets all transaction of one account
	 * may only be called by the owner or an admin?
	 * @param accountNr
	 * @return
	 */
	public List<MoneyTransferDTO> getTransaction(String accountNr) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
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
}
