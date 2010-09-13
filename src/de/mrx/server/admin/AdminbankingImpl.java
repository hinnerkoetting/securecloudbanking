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
	public List<AccountDTO> getAllInternalAccounts() {
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
	public List<AccountDTO> searchInternalAccounts(String owner,
			String accountnr) {
		log.log(Level.INFO, "Admin is searching for account. Name like:" + owner + "Accountnr: " + accountnr);
		List<AccountDTO> result = new ArrayList<AccountDTO>();
		
		//we need to get all accounts and manually filter them as
		//jdo doesn't have a LIKE filter implemented
		List<InternalSCBAccount> accounts = getPersistentInternalAccounts();
		
		for (InternalSCBAccount account: accounts) {
			if (account.getOwner().contains(owner) && account.getAccountNr().contains(accountnr))
				result.add(account.getDTO());
		}
		return result;
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
	public String addBank(BankDTO bankDTO) {	
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
		return "Success";
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
	public String generateTestData() {
		resetData();

		//number of test data
		final int EXTERNAL_BANKS = 3;
		final int EXTERNAL_ACCS = 2;
		final int INTERNAL_ACCS = 5;
		final int TRANSACTIONS = 50;
		
		
		log.setLevel(Level.ALL);
		log.log(Level.INFO, "Generating test data");
		

		//generate test banks
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.currentTransaction().begin();
		AllBanks bankWrapper = AllBanks.getBankWrapper(pm);
		
		//create own bank
		Bank ownBank = new Bank(Bank.SCB_BLZ, "Secure Cloud Bank");
		ownBank.setId(KeyFactory.createKey(bankWrapper.getId(),
				Bank.class.getSimpleName(), Bank.SCB_BLZ));
		bankWrapper.setOwnBanks(ownBank);
		pm.makePersistent(ownBank);
		
		Random random = new Random();
		for (int i= 0; i < EXTERNAL_BANKS; i++) {
			Integer rndBlz = random.nextInt(999999);
			String bankname = "Testbank" + i;
			Bank bank = new Bank(rndBlz.toString(), bankname);
			bank.setId(KeyFactory.createKey(bankWrapper.getId(),
					Bank.class.getSimpleName(), bank.getBlz()));
			
			bankWrapper.getOtherBanks().add(bank);
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
		pm.currentTransaction().commit();
		pm.currentTransaction().begin();
		
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
		@SuppressWarnings("unchecked")
		List<ExternalAccount> externalAccounts = (List<ExternalAccount>)query.execute();
		@SuppressWarnings("unused")
		int numExternal = externalAccounts.size();
		
		//create internal to internal account transactions
		for (int i = 0; i < TRANSACTIONS; i++) {
			int pos1 = random.nextInt(numInternal - 1);
			int pos2 = random.nextInt(numInternal - 2);
			InternalSCBAccount acc1 = internalAccounts.get(pos1);
			InternalSCBAccount acc2 = internalAccounts.get(pos2);
			double amount = (random.nextDouble()- 0.5) * 100;
			String remark = "testcomment" + i;
			MoneyTransfer transfer = new MoneyTransfer(pm, acc1, acc2, amount, acc2.getOwner(), remark);
			acc1.addMoneyTransfer(transfer);
			MoneyTransfer transferRevert = new MoneyTransfer(pm, acc2, acc1, -amount, acc2.getOwner(), remark);
			acc2.addMoneyTransfer(transferRevert);
			
			acc1.changeMoney(amount);
			acc2.changeMoney(-amount);
			pm.makePersistent(acc1);
			pm.makePersistent(acc2);
			

		}
		log.log(Level.INFO, ""+internalAccounts.size() + " "+externalAccounts.size());
		pm.currentTransaction().commit();
//		MoneyTransfer transfer = new MoneyTransfer(
		return "Success!\nGenerated: \n -" + EXTERNAL_BANKS + " external banks.\n -"+ EXTERNAL_ACCS + " external accounts each \n -" + INTERNAL_ACCS + " internal accounts\n -" + TRANSACTIONS + " transactions";
	}


	@Override
	public String adminSendMoney(String senderAccountNr, String senderBLZ,
			String receiveraccountNr, double amount, String remark) {
		if (amount < 0)
			return "Amount must be positive!";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//reciever bank is internal bank
		log.setLevel(Level.ALL);
		GeneralAccount recieverAcc = InternalSCBAccount.getOwnByAccountNr(pm, receiveraccountNr);
		GeneralAccount senderAcc = GeneralAccount.getAccount(pm, senderAccountNr, senderBLZ);
	

		if (recieverAcc == null) {
			log.log(Level.INFO, "Reciever account could not be found!");
			return ("Error. Could not find reciever account.");
		}
		if (senderAcc == null) {
			log.log(Level.INFO, "Sender account could not be found!");
			return ("Error. Could not find sender account.");
		}
		log.log(Level.INFO,"Reciever: " +recieverAcc.toString());
		log.log(Level.INFO,"Sender: "   +senderAcc.toString());
		MoneyTransfer transfer = new MoneyTransfer(pm, senderAcc,
				recieverAcc, amount,recieverAcc.getOwner(),remark);
		transferMoney(pm, senderAcc, recieverAcc, transfer, amount, remark);
		return "Success.";
		
		
		
	}

	@Override
	public List<AccountDTO> getExternalAccounts(String blz) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Extent<ExternalAccount> extent = pm.getExtent(ExternalAccount.class);
		Query query = pm.newQuery(extent);
		
		@SuppressWarnings("unchecked")
		List<ExternalAccount> externalAccounts = (List<ExternalAccount>)query.execute();

		List<AccountDTO> accountsDTO = new ArrayList<AccountDTO>();
		log.log(Level.INFO, externalAccounts.size() +"");
		for (ExternalAccount account: externalAccounts) {
			if (account.getBank(pm).getBlz().equals(blz))
				accountsDTO.add(account.getDTO());
		}
		return accountsDTO;
	}

	@Override
	public String deleteBank(String blz) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Bank bank = Bank.getByBLZ(pm, blz);
		if (bank == null)
			return "Error. Bank not found!";
		
		//delete all accounts of this bank
		Extent<ExternalAccount> extentAccounts = pm.getExtent(ExternalAccount.class);
		Query query = pm.newQuery(extentAccounts);
		
		@SuppressWarnings("unchecked")
		List<ExternalAccount> accounts = (List<ExternalAccount>)query.execute();
		for (ExternalAccount account: accounts) {
			if (account.getBank(pm).getBlz().equals(blz))
				pm.deletePersistent(account);
		}
		
		//now delete the actual bank
		pm.deletePersistent(bank);
		return "Success.";
	}

	@Override
	public String deleteInternalAccount(String accountNr) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		InternalSCBAccount account = InternalSCBAccount.getOwnByAccountNr(pm, accountNr);
		if (account == null)
			return "Error. Account not found!";
		pm.deletePersistent(account);
		return "Success.";
	}

	@Override
	public String editBankDetails(String oldName, String oldBLZ,
			String newName, String newBLZ) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.currentTransaction().begin();
		Extent<Bank> extent = pm.getExtent(Bank.class);
		Query query = pm.newQuery(extent);
		query.setFilter("blz == param1 && name == param2");
		query.declareParameters("String param1, String param2");
		query.setUnique(true);
		Bank bank = (Bank)query.execute(oldBLZ, oldName);
		if (bank == null)
			return "Error. Bank not found";
		bank.setName(newName);
		bank.setBlz(newBLZ);
		pm.currentTransaction().commit();
		return "Success";
	}



}
