package de.mrx.server.admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.ShopDTO;
import de.mrx.client.TansDTO;
import de.mrx.client.Transaction3SDTO;
import de.mrx.client.admin.AdminService;
import de.mrx.server.AllBanks;
import de.mrx.server.Bank;
import de.mrx.server.BankServiceImpl;
import de.mrx.server.ExternalAccount;
import de.mrx.server.GeneralAccount;
import de.mrx.server.InternalSCBAccount;
import de.mrx.server.MoneyTransfer;
import de.mrx.server.MoneyTransferPending;
import de.mrx.server.NumberFormater;
import de.mrx.server.PMF;
import de.mrx.server.SCBIdentity;
import de.mrx.server.Shop;
import de.mrx.server.TANList;
import de.mrx.server.secureBySCB.Transaction3S;
import de.mrx.shared.SCBData;

public class AdminbankingImpl extends BankServiceImpl implements
AdminService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064677766020894396L;
	Logger log = Logger.getLogger(AdminbankingImpl.class.getName());
	
	private boolean checkAdmin() {
		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserAdmin()) {
			log.severe("Unauthorized user called admin function!");
			return false;
		}
		return true;
	}
	/**
	 * 
	 */
	private List<InternalSCBAccount> getPersistentInternalAccounts(PersistenceManager pm) {
		if (!checkAdmin())
			return null;

		
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

		if (!checkAdmin())
			return null;
		log.log(Level.INFO, "Admin requests accounts");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			
			List<AccountDTO> accountsDTO = new ArrayList<AccountDTO>();
			
			List<InternalSCBAccount> accounts =getPersistentInternalAccounts(pm);
			for (InternalSCBAccount account: accounts) {
				accountsDTO.add(account.getDTO());
			}
			return accountsDTO;
		}
		finally {
			pm.close();
    	}
	}


	@Override
	public List<AccountDTO> searchInternalAccounts(String owner,
			String accountnr) {
		if (!checkAdmin())
			return null;
		log.log(Level.INFO, "Admin is searching for account. Name like:" + owner + "Accountnr: " + accountnr);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<AccountDTO> result = new ArrayList<AccountDTO>();
			
			//we need to get all accounts and manually filter them as
			//jdo doesn't have a LIKE filter implemented
			List<InternalSCBAccount> accounts = getPersistentInternalAccounts(pm);
			
			for (InternalSCBAccount account: accounts) {
				if (account.getOwner().contains(owner) && account.getAccountNr().contains(accountnr))
					result.add(account.getDTO());
			}
			return result;
		}
		finally {
			pm.close();
    	}
	}
	
	
	/**
	 * get stored banks as DTO
	 */
	@Override
	public List<BankDTO> getAllBanks() {
		if (!checkAdmin())
			return null;
		log.log(Level.INFO,  "Admin requests list of banks");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<BankDTO> banksDTO = new ArrayList<BankDTO>();
		
		AllBanks banks = AllBanks.getSingleton(pm);
		
		for (Bank bank: banks.getBanks()) {
			banksDTO.add(bank.getDTO());
		}
		
		
		return banksDTO;
	}

	@Override
	public String addBank(BankDTO bankDTO) {
		if (!checkAdmin())
			return "Error";
		String blz = bankDTO.getBlz();
		try {
			Double.parseDouble(blz);
		}
		catch (NumberFormatException e) {
			log("Invalid BLZ");
			return "Error. Invalid BLZ";
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			log.log(Level.INFO, "Requesting to add new bank. Name: " + bankDTO.getName() + " - BLZ: " + bankDTO.getBlz());
			AllBanks bankWrapper = AllBanks.getSingleton(pm);
			
			Bank bank = new Bank(bankDTO.getBlz(), bankDTO.getName(), bankWrapper);
			bankWrapper.addBank(bank);
			pm.makePersistent(bank);
			return "Success";
		}
		finally {
			pm.close();
    	}
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

	@Override
	public String generateTestData() {
		if (!checkAdmin())
			return null;

		//number of test data
		final int EXTERNAL_BANKS = 3;
		final int EXTERNAL_ACCS = 2;
		final int INTERNAL_ACCS = 5;
		final int TRANSACTIONS = 20;
		
		
		log.setLevel(Level.ALL);
		log.log(Level.INFO, "Generating test data");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			//generate test banks
		
			AllBanks bankWrapper = AllBanks.getSingleton(pm);
	
			
			Bank ownBank = bankWrapper.getSCBBank(pm);
			//create own bank
			if (ownBank == null) {
				ownBank = new Bank(SCBData.SCB_PLZ, SCBData.SCB_NAME, bankWrapper);
				bankWrapper.addBank(ownBank);
				pm.makePersistent(ownBank);
			}
			
			Random random = new Random();
			for (int i= 0; i < EXTERNAL_BANKS; i++) {
				Integer rndBlz = random.nextInt(999999);
				String bankname = "Testbank" + i;
				Bank bank = new Bank(rndBlz.toString(), bankname, bankWrapper);
	
				
				bankWrapper.addBank(bank);
				pm.makePersistent(bank);
				//generate accounts for each bank
				for (int j=0; j < EXTERNAL_ACCS; j++) {
					Integer accNr = random.nextInt(99999999);
					GeneralAccount acc = new ExternalAccount( "Testaccount" + j + "@" + bankname + ".com",accNr.toString(), bank);
	
					bank.addAccount(acc);
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
				
				
				ownBank.addAccount(acc);
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
			@SuppressWarnings("unchecked")
			List<ExternalAccount> externalAccounts = (List<ExternalAccount>)query.execute();

			
			//create internal to internal account transactions
			for (int i = 0; i < TRANSACTIONS; i++) {
				int pos1 = random.nextInt(numInternal);
				int pos2 = random.nextInt(numInternal);
				while (pos2 == pos1) {
					pos2 = random.nextInt(numInternal);
				}
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
				
	
			}
			log.log(Level.INFO, ""+internalAccounts.size() + " "+externalAccounts.size());
	//		MoneyTransfer transfer = new MoneyTransfer(
			return "Success!\nGenerated: \n -" + EXTERNAL_BANKS + " external banks.\n -"+ EXTERNAL_ACCS + " external accounts each \n -" + INTERNAL_ACCS + " internal accounts\n -" + TRANSACTIONS + " transactions";
		}
		finally {
			pm.close();
		}
	}

	/**
	 * @throws NumberFormatException
	 */
	@Override
	public String adminSendMoney(String senderAccountNr, String senderBLZ,
			String receiveraccountNr, double amount, String remark) {
		if (!checkAdmin())
			return null;
		if (amount < 0)
			throw new NumberFormatException("Amount must not be negative!");
		String formatedSenderAccountNr;
		String formatedSenderBLZ;
		String formatedRecAccountNr;
		try{
			formatedSenderAccountNr = NumberFormater.convertToStorageFormat(senderAccountNr);
		}
		catch (NumberFormatException e){
			throw new NumberFormatException("Sender accountnumber");
		}
		try{
			formatedSenderBLZ = NumberFormater.convertToStorageFormat(senderBLZ);
		}
		catch (NumberFormatException e){
			throw new NumberFormatException("Sender BLZ");
		}
		try {
			formatedRecAccountNr = NumberFormater.convertToStorageFormat(receiveraccountNr);
		}
		catch (NumberFormatException e){
			throw new NumberFormatException("Reciever accountnumber");
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			//reciever bank is always internal bank
			
			GeneralAccount recieverAcc = InternalSCBAccount.getOwnByAccountNr(pm, formatedRecAccountNr);
			GeneralAccount senderAcc = GeneralAccount.getAccount(pm, formatedSenderAccountNr, formatedSenderBLZ);
		
	
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
		finally {
			pm.close();
		}
		
		
		
	}

	@Override
	public List<AccountDTO> getExternalAccounts(String blz) {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Extent<ExternalAccount> extent = pm.getExtent(ExternalAccount.class);
			Query query = pm.newQuery(extent);
			
			@SuppressWarnings("unchecked")
			List<ExternalAccount> externalAccounts = (List<ExternalAccount>)query.execute();
	
			List<AccountDTO> accountsDTO = new ArrayList<AccountDTO>();
			log.log(Level.INFO, externalAccounts.size() +"");
			for (ExternalAccount account: externalAccounts) {
				if (account.getBLZ().equals(blz))
					accountsDTO.add(account.getDTO());
			}
			return accountsDTO;
		}
		finally {
			pm.close();
		}
	}

	@Override
	public String deleteBank(String blz) {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Bank bank = Bank.getByBLZ(pm, blz);
			if (bank == null)
				return "Error. Bank not found!";
			
			//delete all accounts of this bank
			Extent<ExternalAccount> extentAccounts = pm.getExtent(ExternalAccount.class);
			Query query = pm.newQuery(extentAccounts);
			
			@SuppressWarnings("unchecked")
			List<ExternalAccount> accounts = (List<ExternalAccount>)query.execute();
			for (ExternalAccount account: accounts) {
				if (account.getBLZ().equals(blz))
					pm.deletePersistent(account);
			}
			
			//now delete the actual bank
			pm.deletePersistent(bank);
			return "Success.";
		}
		finally {
			pm.close();
		}
	}

	@Override
	public String deleteInternalAccount(String accountNr) {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			InternalSCBAccount account = InternalSCBAccount.getOwnByAccountNr(pm, accountNr);
			if (account == null)
				return "Error. Account not found!";
			pm.deletePersistent(account);
			return "Success.";
		}
		finally {
			pm.close();
		}
	}

	@Override
	public String editBankDetails(String oldName, String oldBLZ,
			String newName, String newBLZ) {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
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
			return "Success";
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public String deleteData() {
		if (!checkAdmin())
			return null;
		log.log(Level.INFO, "Deleting all data");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			delete(pm, AllBanks.class);
			delete(pm, Bank.class);
			delete(pm, ExternalAccount.class);		    		 
			delete(pm, MoneyTransfer.class);
			delete(pm, MoneyTransferPending.class);
			delete(pm, InternalSCBAccount.class);
			delete(pm, TANList.class);
			delete(pm, SCBIdentity.class);
			return "Success";
		}
		finally {
			pm.close();
		}
	}
	/**
	 * get all tans from internal account
	 */
	@Override
	public TansDTO getTans(String accountNr) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			InternalSCBAccount account = InternalSCBAccount.getOwnByAccountNr(pm, accountNr);
			return new TansDTO(account.getTans().getTan());
		}
		finally {
			pm.close();
		}

		
	}
	@Override
	public List<MoneyTransferDTO> getTransfers(String accountNr, String blz) {
		//internal account
		if (blz.equals(SCBData.SCB_PLZ))
			return getTransaction(accountNr);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			GeneralAccount acc = ExternalAccount.getExternalAccount(pm, accountNr, blz);		
			List<MoneyTransferDTO> mtDTOs = new ArrayList<MoneyTransferDTO>();
			for (MoneyTransfer mtransfer : acc.getTransfers()) {
				MoneyTransferDTO dto = mtransfer.getDTO(pm);
				mtDTOs.add(dto);
				log.info(dto.toString());
			}
			return mtDTOs;
		}
		finally {
			pm.close();
		}
	}
	@Override
	public String resetInternalAccount(String accountNr) {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			InternalSCBAccount account = InternalSCBAccount.getOwnByAccountNr(pm, accountNr);
			account.getTransfers().clear();
			account.setPendingTransaction(null);
			account.setBalance(5.0);
			return "Success.";
		}
		finally {
			pm.close();
		}
	}
	@Override
	public Boolean addShop(String name, String url) {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
				
			
			//generate accountnr
			Random rd = new Random();
			int accountNr = rd.nextInt(100000) + 1000;
	
			DecimalFormat format = new DecimalFormat("##");
			format.setMinimumIntegerDigits(6);
			
			Bank scbBank = Bank.getByBLZ(pm, SCBData.SCB_PLZ);
			Shop shop = new Shop(name, format.format(accountNr), url, scbBank);
			pm.makePersistent(shop);
			return true;
		}
		finally {
			pm.close();
		}
	}
	@Override
	public List<ShopDTO> getAllShops() {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Extent<Shop> extent = pm.getExtent(Shop.class);
			Query query = pm.newQuery(extent);
			List<Shop> shops = (List<Shop>)query.execute();
			
			List<ShopDTO> result = new ArrayList<ShopDTO>();
			for (Shop s: shops) {
				result.add(s.getDTO());
			}
			return result;
		}
		finally {
			pm.close();
		}
	}
	@Override
	public List<Transaction3SDTO> getAllOpenTransactions(String shopName) {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Extent<Transaction3S> extent = pm.getExtent(Transaction3S.class);
			Query query = pm.newQuery(extent);
			query.setFilter("shopName == param");
			query.declareParameters("String param");
			List<Transaction3S> transactions = (List<Transaction3S>)query.execute(shopName);
			List<Transaction3SDTO> result = new ArrayList<Transaction3SDTO>();
			for (Transaction3S t: transactions) {
				result.add(t.getDTO());
			}
			return result;
		}
		finally {
			pm.close();
		}
		
	}
	@Override
	public Boolean deleteOpenTransactions() {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			delete(pm, Transaction3S.class);
			return true;
		}
		finally {
			pm.close();
		}
	}
	@Override
	public String editShopDetails(String oldName, String oldURL,
			String newName, String newURL) {
		if (!checkAdmin())
			return null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Extent<Shop> extent = pm.getExtent(Shop.class);
			Query query = pm.newQuery(extent);
			query.setFilter("url == param1 && name == param2");
			query.declareParameters("String param1, String param2");
			query.setUnique(true);
			Shop shop = (Shop)query.execute(oldURL, oldName);
			if (shop == null)
				return "Error. Bank not found";
			shop.setName(newName);
			shop.setUrl(newURL);
			return "Success";
		}
		finally {
			pm.close();
		}
	}



}
