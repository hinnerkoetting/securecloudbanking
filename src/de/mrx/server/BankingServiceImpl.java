package de.mrx.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceAware;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;
import de.mrx.client.BankingService;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.SCBIdentityDTO;

@SuppressWarnings("serial")
@PersistenceAware
public class BankingServiceImpl extends RemoteServiceServlet implements
		BankingService {

	private static final String SCB_BLZ = "1502222";
	Logger log = Logger.getLogger(BankingServiceImpl.class.getName());
	PersistenceManager pm = PMF.get().getPersistenceManager();

	Bank ownBank;
	AllBanks bankWrapper;

	public BankingServiceImpl() {
		loadInitialData();
	}

	public List<AccountDTO> getAccounts() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		// Extent<Account> e=pm.getExtent(Account.class,true);
		// for ( Account acc: e){
		// log.info( acc.toString());
		// }
		pm=PMF.get().getPersistenceManager();
		String query = " SELECT FROM " + Account.class.getName()
				+ " WHERE owner =='" + user.getEmail() + "'";
		log.info("geTAccounts Query: " + query);
		List<Account> accounts = (List<Account>) pm.newQuery(query).execute();
		List<AccountDTO> accountDTOs = new ArrayList<AccountDTO>();
		for (Account acc : accounts) {
			AccountDTO dto = acc.getDTO();
			accountDTOs.add(dto);
			log.info(dto.toString());
		}
		return accountDTOs;
	}

	private void loadInitialData() {
		try{
		bankWrapper=AllBanks.getBankWrapper(PMF.get().getPersistenceManager());
		pm.currentTransaction().begin();
		if (bankWrapper==null){
			
			bankWrapper=new AllBanks();
			pm.makePersistent(bankWrapper);
			
		}
		ownBank=bankWrapper.getOwnBanks();
		if (ownBank==null){
			ownBank = new Bank(SCB_BLZ, "Secure Cloud Bank");
			ownBank.setId(KeyFactory.createKey(bankWrapper.getId(),Bank.class.getSimpleName(), "SCB"));
			bankWrapper.setOwnBanks(ownBank);
			
			
		}
		
		pm.currentTransaction().commit();
		
		}
		finally{
			if (pm.currentTransaction().isActive()){
				pm.currentTransaction().rollback();
				
			}
			pm.close();
		}
//		String query = "SELECT FROM " + Bank.class.getName() + " WHERE blz=='"
//				+ SCB_BLZ + "'";
//
//		List<Bank> ownBanks = (List<Bank>) pm.newQuery(query).execute();
//		if (ownBanks.size() == 0) {
//			ownBank = new Bank(SCB_BLZ, "Secure Cloud Bank");
//			pm.makePersistent(ownBank);
//		} else {
//			ownBank = ownBanks.get(0);
//		}
	}

	public double getBalance(String accountNr) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = " SELECT FROM " + Account.class.getName()
				+ " WHERE owner =='" + user.getEmail() + "' && accountNr=='"
				+ accountNr + "'";
		// Extent<Account> e=pm.getExtent(Account.class,true);
		// for ( Account acc: e){
		// log.info( acc.toString());
		// }
		// log.info("geTAccounts Query: "+query);
		List<Account> accounts = (List<Account>) pm.newQuery(query).execute();
		if (accounts.size() != 1) {
			throw new RuntimeException("Anzahl Accounts mit Nr '" + accountNr
					+ "' ist " + accounts.size());
		}
		return accounts.get(0).getBalance();
	}

	public List<MoneyTransferDTO> getTransaction(String accountNr) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GeneralAccount acc = Account.getOwnByAccountNr(pm,accountNr);
		// String query = " SELECT FROM " + MoneyTransfer.class.getName()
		// + " WHERE senderAccountNr =='" + accountNr + "'";
		// List<MoneyTransfer> moneyTransfers = (List<MoneyTransfer>)
		// pm.newQuery(
		// query).execute();
		List<MoneyTransferDTO> mtDTOs = new ArrayList<MoneyTransferDTO>();
		for (MoneyTransfer mtransfer : acc.getTransfers()) {
			MoneyTransferDTO dto = mtransfer.getDTO(pm);
			mtDTOs.add(dto);
			log.info(dto.toString());
		}
		return mtDTOs;
	}

	public SCBIdentityDTO login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		SCBIdentityDTO identityInfo;

		if (user != null) {

			log.fine("Login: " + user);

			identityInfo = getIdentity(user);

			identityInfo.setLoggedIn(true);			
			identityInfo.setLogoutUrl(userService.createLogoutURL(requestUri));

		} else {
			identityInfo = new SCBIdentityDTO();
			identityInfo.setLoggedIn(false);
			identityInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return identityInfo;
	}

	private SCBIdentityDTO getIdentity(User user) {
		SCBIdentity id=SCBIdentity.getIdentity(user);
		if (id==null){
			id= new SCBIdentity(user.getEmail());
			id.setNickName(user.getNickname());			
		}
		return id.getDTO();

		
		
	}

	public void openNewAccount() {
		try{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new RuntimeException("Nicht eingeloggt. Zugriff unterbunden");
		}
		SCBIdentityDTO identityInfo = getIdentity(user);
		Random rd = new Random();
		int kontoNr = rd.nextInt(100000) + 10000;
		Account acc = new Account(identityInfo.getEmail(), "" + kontoNr, 5,
				ownBank);
		acc.setBank(ownBank);
		acc.setId(  KeyFactory.createKey(ownBank.getId(),Account.class.getSimpleName(),kontoNr));

		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.currentTransaction().begin();
		pm.makePersistent(acc);
		ownBank.addAccount(acc);
		pm.currentTransaction().commit();
		log.info("account neu geoeffnet : " + acc);

		}
		finally{
			if (pm.currentTransaction().isActive()){
				pm.currentTransaction().rollback();
			}
			pm.close();
		}

	}

	public void sendMoney(String senderAccountNr, String blz,
			String receiveraccountNr, double amount, String remark, String receiverName, String bankName) {
		try{
			
			pm=PMF.get().getPersistenceManager();
			bankWrapper=AllBanks.getBankWrapper(pm);
			ownBank=bankWrapper.getOwnBanks();
		Account senderAccount = Account.getOwnByAccountNr(pm,senderAccountNr);
		if (senderAccount==null){
			throw new RuntimeException("Sender Account "+senderAccountNr+" existiert nicht!");
		}

		 
		

		Bank receiverBank = Bank.getByBLZ(pm,blz);
		if (receiverBank == null) {
			if (bankName==null || bankName.trim().equals("")){
				bankName="Neue Bank";
			}
			receiverBank = new Bank(blz.trim(), bankName);
			receiverBank.setId(KeyFactory.createKey(bankWrapper.getId(),Bank.class.getSimpleName(),blz));
			
			bankWrapper.getOtherBanks().add(receiverBank);
			log.info("Create external bank");
			pm.currentTransaction().begin();
			pm.makePersistent(bankWrapper);
			pm.currentTransaction().commit();
			
		}

		GeneralAccount recAccount;
		if (receiverBank.equals(ownBank)) {
			recAccount = Account.getOwnByAccountNr(pm,receiveraccountNr);
			if (recAccount==null){
				throw new RuntimeException("Dieser Account existiert nicht bei der Bank "+receiveraccountNr);
			}

		} else {
			recAccount=ExternalAccount.getAccountByBLZAndAccountNr(pm,receiverBank, receiveraccountNr);
			
			if (recAccount == null) {
				log.info("Account"+ receiveraccountNr+" is not yet known at "+receiverBank.getName()+"("+receiverBank.getBlz()+"). Create it.");
				log.info("Create external account");
				pm.currentTransaction().begin();
				recAccount = new ExternalAccount(receiverName,
						receiveraccountNr, receiverBank);
				recAccount.setId(KeyFactory.createKey(receiverBank.getId(),ExternalAccount.class.getSimpleName(),receiverBank.getBlz()+"_"+receiveraccountNr));
				receiverBank.addAccount(recAccount);
				pm.makePersistent(recAccount);
				
				pm.currentTransaction().commit();
			}
		}
//		pm.currentTransaction().commit();
		MoneyTransfer transfer = new MoneyTransfer(senderAccount, recAccount,
				amount);
		transfer.setRemark(remark);
		transfer.setReceiverName(receiverName);
//		transfer.setId(KeyFactory.createKey(senderAccount.getId(), MoneyTransfer.class.getSimpleName(), 1));
		log.info("Save Moneytransfer");
		pm.currentTransaction().begin();
		
//		pm.makePersistent(transfer);
		senderAccount.addMoneyTransfer(transfer);
//		recAccount.addMoneyTransfer(transfer);Später eine Kopie anlegen
		
		senderAccount.setBalance(senderAccount.getBalance() - amount);
		pm.makePersistent(senderAccount);
		 pm.currentTransaction().commit();
		
		
		}
		finally{
			if (pm.currentTransaction().isActive()){
				pm.currentTransaction().rollback();
			}
			 pm.close();
		}
	}

	public AccountDetailDTO getAccountDetails( String accountNr) {
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Account acc=Account.getOwnByAccountNr(pm,accountNr);
		return acc.getDetailedDTO(pm);
	}

}
