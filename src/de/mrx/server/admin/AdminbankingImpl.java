package de.mrx.server.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.KeyFactory;

import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.client.admin.AdminService;
import de.mrx.server.AllBanks;
import de.mrx.server.Bank;
import de.mrx.server.BankServiceImpl;
import de.mrx.server.CustomerServiceImpl;
import de.mrx.server.ExternalAccount;
import de.mrx.server.GeneralAccount;
import de.mrx.server.InternalSCBAccount;
import de.mrx.server.MoneyTransfer;
import de.mrx.server.MoneyTransferPending;
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
	 * 
	 */
	private List<InternalSCBAccount> getPersistentInternalAccounts() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Extent<InternalSCBAccount> extent = pm.getExtent(InternalSCBAccount.class);
		Query query = pm.newQuery(extent);

		@SuppressWarnings("unchecked")
		List<InternalSCBAccount> accounts = (List<InternalSCBAccount>)query.execute();
		 
		return accounts;
	}
	/**
	 * get all accounts of all users
	 */
	@Override
	public List<AccountDTO> getAllAccounts() {
//		log.setLevel(Level.OFF);
		log.log(Level.INFO, "Admin requests accounts");
		
		List<AccountDTO> accountsDTO = new ArrayList<AccountDTO>();
		
		List<InternalSCBAccount> accounts =getPersistentInternalAccounts();
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

	/**
	 * 
	 * @return stored banks
	 */
	private List<Bank> getPersistentBanks() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Extent<Bank> extent = pm.getExtent(Bank.class);
		Query query = pm.newQuery(extent);
		
		@SuppressWarnings("unchecked")
		List<Bank> banks = (List<Bank>)query.execute();
		
		return banks;
	}
	
	/**
	 * get stores banks as DTO
	 */
	@Override
	public List<BankDTO> getAllBanks() {
		log.log(Level.INFO,  "Admin requests list of banks");
		
		List<BankDTO> banksDTO = new ArrayList<BankDTO>();
		
		List<Bank> banks = getPersistentBanks();
		for (Bank bank: banks) {
			banksDTO.add(bank.getDTO());
		}
		
		
		return banksDTO;
	}

	@Override
	public boolean addBank(BankDTO bankDTO) {	
		//TODO: check for valid input
		log.setLevel(Level.ALL);
		log.log(Level.INFO, "Requesting to add new bank. Name: " + bankDTO.getName() + " - BLZ: " + bankDTO.getBlz());
		AllBanks bankWrapper = AllBanks.getBankWrapper(PMF.get()
					.getPersistenceManager());
		
		Bank bank = new Bank(bankDTO.getBlz(), bankDTO.getName());
		bank.setId(KeyFactory.createKey(bankWrapper.getId(),
				Bank.class.getSimpleName(), bankDTO.getBlz()));
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(bank);
		return true;
	}

	/**
	 * deletes all elements in data storage
	 * @param pm 
	 * @param cl: class to be deleted
	 */
	private void delete(PersistenceManager pm, @SuppressWarnings("rawtypes") Class cl) {
		Query query = pm.newQuery(cl);
		query.deletePersistentAll(query);	
	}
	private void resetData() {
		
		log.log(Level.INFO, "Resetting all data");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		delete(pm, Bank.class);
		delete(pm, ExternalAccount.class);		    		 
		delete(pm, MoneyTransfer.class);
		delete(pm, MoneyTransferPending.class);
		delete(pm, InternalSCBAccount.class);
	}
	
	@Override
	public boolean generateTestData() {
		resetData();
		
		//number of test data
		final int EXTERNAL_BANKS = 5;
		final int EXTERNAL_ACCS = 2;
		final int INTERNAL_ACCS = 10;
		final int TRANSACTIONS = 20;
		
		
		log.setLevel(Level.ALL);
		log.log(Level.INFO, "Generating test data");
		

		//generate test banks
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AllBanks bankWrapper = AllBanks.getBankWrapper(PMF.get()
				.getPersistenceManager());
		
		//create own bank
		Bank ownBank = new Bank(CustomerServiceImpl.SCB_BLZ, "Secure Cloud Bank");
		ownBank.setId(KeyFactory.createKey(bankWrapper.getId(),
				Bank.class.getSimpleName(), CustomerServiceImpl.SCB_BLZ));
		bankWrapper.setOwnBanks(ownBank);
		pm.makePersistent(ownBank);
		
		Random random = new Random();
		for (int i= 0; i < EXTERNAL_BANKS; i++) {
			Integer rndBlz = random.nextInt(999999);
			String bankname = "Testbank" + i;
			Bank bank = new Bank(rndBlz.toString(), bankname);
			bank.setId(KeyFactory.createKey(bankWrapper.getId(),
					Bank.class.getSimpleName(), bank.getBlz()));
			pm.makePersistent(bank);
			
			//generate accounts for each bank
			for (int j=0; j < EXTERNAL_ACCS; j++) {
				Integer accNr = random.nextInt(99999999);
				GeneralAccount acc = new ExternalAccount( "Testaccount" + j + "@" + bankname + ".com",accNr.toString(), bank);


				acc.setId(KeyFactory.createKey(bank.getId(), ExternalAccount.class
						.getSimpleName(), accNr));
				pm.makePersistent(acc);
			}
			
		}
		
		//create test internal accounts		
		for (int i = 0; i < INTERNAL_ACCS ; i++) {
			String accOwner = "Testaccount" + i;
			Integer accNr = random.nextInt(99999999);
			InternalSCBAccount acc = new InternalSCBAccount(accOwner, accNr.toString(), 0, ownBank);
			acc.setAccountType(AccountDTO.SAVING_ACCOUNT);
			acc.setAccountDescription(AccountDTO.SAVING_ACCOUNT_DES);
			acc.setOwnerEmail(accOwner + "@scbbank.com");
			
			acc.setId(KeyFactory.createKey(ownBank.getId(), InternalSCBAccount.class
					.getSimpleName(), accNr));
			pm.makePersistent(acc);
		}
		
		
		
		//create test transactions
		
		//get all internal accounts
		Extent<InternalSCBAccount> extentInternal = pm.getExtent(InternalSCBAccount.class);
		Query query = pm.newQuery(extentInternal);
		@SuppressWarnings("unchecked")
		List<InternalSCBAccount> internalAccounts = (List<InternalSCBAccount>)query.execute();
		int numInternal = internalAccounts.size();
		
		//get all external accounts
		Extent<ExternalAccount> extentExternal = pm.getExtent(ExternalAccount.class);
		query = pm.newQuery(extentExternal);
		List<ExternalAccount> externalAccounts = (List<ExternalAccount>)query.execute();
		@SuppressWarnings("unused")
		int numExternal = externalAccounts.size();
		
		//create internal to internal account transactions
		for (int i = 0; i < TRANSACTIONS; i++) {
			int pos1 = random.nextInt(numInternal - 1);
			int pos2 = random.nextInt(numInternal - 2);
			InternalSCBAccount acc1 = internalAccounts.get(pos1);
			InternalSCBAccount acc2 = internalAccounts.get(pos2);
			MoneyTransfer transfer = new MoneyTransfer(acc1, acc2, (random.nextDouble()- 0.5) * 100,acc2.getOwner(),"testcomment" + i);
			acc1.addMoneyTransfer(transfer);

		}
		log.log(Level.INFO, ""+internalAccounts.size() + " "+externalAccounts.size());
		
//		MoneyTransfer transfer = new MoneyTransfer(
		
		return true;
	}


}
