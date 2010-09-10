package de.mrx.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

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
		MoneyTransfer receivertransfer = new MoneyTransfer(recAccount,
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
}
