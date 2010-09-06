package de.mrx.server.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;





import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.admin.AdminService;
import de.mrx.server.AllBanks;
import de.mrx.server.Bank;
import de.mrx.server.BankServiceImpl;
import de.mrx.server.InternalSCBAccount;
import de.mrx.server.PMF;
import de.mrx.shared.SCBException;

public class AdminbankingImpl extends BankServiceImpl implements
AdminService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064677766020894396L;
	Logger log = Logger.getLogger(AdminbankingImpl.class.getName());
	
	@Override
	public AccountDetailDTO getAccountDetails(String accountNr)
			throws SCBException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * get all accounts of all users
	 */
	@Override
	public List<AccountDTO> getAllAccounts() {
//		log.setLevel(Level.OFF);
		log.log(Level.INFO, "Admin requests accounts");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Extent<InternalSCBAccount> extent = pm.getExtent(InternalSCBAccount.class);
		Query query = pm.newQuery(extent);

		@SuppressWarnings("unchecked")
		List<InternalSCBAccount> accounts = (List<InternalSCBAccount>)query.execute();
		 
		List<AccountDTO> accountsDTO = new ArrayList<AccountDTO>();
		
		for (InternalSCBAccount account: accounts) {
			accountsDTO.add(account.getDTO());
		}
		return accountsDTO;
	}

	@Override
	public double getBalance(String accountNr) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public SCBIdentityDTO login(String requestUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BankDTO> getAllBanks() {
		log.log(Level.INFO,  "Admin requests list of banks");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Extent<Bank> extent = pm.getExtent(Bank.class);
		Query query = pm.newQuery(extent);
		
		@SuppressWarnings("unchecked")
		List<Bank> banks = (List<Bank>)query.execute();
		
		List<BankDTO> banksDTO = new ArrayList<BankDTO>();
		
		for (Bank bank: banks) {
			banksDTO.add(bank.getDTO());
		}
		
		
		return banksDTO;
	}

	@Override
	public boolean addBank(BankDTO bankDTO) {	
		//TODO: check for unique, check for valid input
		//check ID
		log.setLevel(Level.ALL);
		log.log(Level.INFO, "Requesting to add new bank. Name: " + bankDTO.getName() + " - BLZ: " + bankDTO.getBlz());
		AllBanks bankWrapper = AllBanks.getBankWrapper(PMF.get()
					.getPersistenceManager());
		
		Bank bank = new Bank(bankDTO.getBlz(), bankDTO.getName());
		bank.setId(KeyFactory.createKey(bankWrapper.getId(),
				Bank.class.getSimpleName(), bankDTO.getName()));
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(bank);
		return true;
	}


}
