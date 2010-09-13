package de.mrx.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceAware;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;
import de.mrx.client.CustomerService;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.shared.AccountNotExistException;
import de.mrx.shared.SCBException;

/**
 * implementation class for the bankingservice
 * @see de.mrx.client.CustomerService
 *
 */
@SuppressWarnings("serial")
@PersistenceAware
public class CustomerServiceImpl extends BankServiceImpl implements
		CustomerService {

	
	
	PersistenceManager pm = PMF.get().getPersistenceManager();

	/**
	 * SCB-Bank 
	 */
	Bank ownBank;
	
	/**
	 * reference to the bank container
	 */
	AllBanks bankWrapper;

	public CustomerServiceImpl() {
		loadInitialData();
	}

	/**
	 * receive all accounts of the currently logged in user 
	 */
	public List<AccountDTO> getAccounts() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		

		pm = PMF.get().getPersistenceManager();
		String query = " SELECT FROM " + InternalSCBAccount.class.getName()
				+ " WHERE owner =='" + user.getEmail() + "'";
		log.info("geTAccounts Query: " + query);
		List<InternalSCBAccount> accounts = (List<InternalSCBAccount>) pm.newQuery(query).execute();
		List<AccountDTO> accountDTOs = new ArrayList<AccountDTO>();
		for (InternalSCBAccount acc : accounts) {
			AccountDTO dto = acc.getDTO();
			accountDTOs.add(dto);
			log.info(dto.toString());
		}
		return accountDTOs;
	}
	
	@Override
	public AccountDetailDTO getSavingAccount(){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user==null) {
			return null;
		}

		pm = PMF.get().getPersistenceManager();
		InternalSCBAccount savingAccount= InternalSCBAccount.getOwnByEmail(pm,user.getEmail());
		if (savingAccount==null) {
			return null;
		}
		return savingAccount.getDetailedDTO(pm);
	}

	/**
	 * initialisation. After DB-Resets stores basic data in the JDO-database.
	 */
	private void loadInitialData() {
		try {
			bankWrapper = AllBanks.getBankWrapper(PMF.get()
					.getPersistenceManager());
			pm.currentTransaction().begin();
			if (bankWrapper == null) {

				bankWrapper = new AllBanks();
				pm.makePersistent(bankWrapper);

			}
			ownBank = bankWrapper.getOwnBanks();
			if (ownBank == null) {
				ownBank = new Bank(Bank.SCB_BLZ, "Secure Cloud Bank");
				ownBank.setId(KeyFactory.createKey(bankWrapper.getId(),
						Bank.class.getSimpleName(), Bank.SCB_BLZ));
				bankWrapper.setOwnBanks(ownBank);

			}

			pm.currentTransaction().commit();

		} finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();

			}
			pm.close();
		}
		
	}

	/**
	 * fetches the balance of a given account. 
	 * Can be only used by the owner of the account or an admin?
	 * @param accountNr account that should be access
	 * @return balance of the account
	 */
	public double getBalance(String accountNr) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = " SELECT FROM " + InternalSCBAccount.class.getName()
				+ " WHERE owner =='" + user.getEmail() + "' && accountNr=='"
				+ accountNr + "'";
		// Extent<Account> e=pm.getExtent(Account.class,true);
		// for ( Account acc: e){
		// log.info( acc.toString());
		// }
		// log.info("geTAccounts Query: "+query);
		List<InternalSCBAccount> accounts = (List<InternalSCBAccount>) pm.newQuery(query).execute();
		if (accounts.size() != 1) {
			throw new RuntimeException("Anzahl Accounts mit Nr '" + accountNr
					+ "' ist " + accounts.size());
		}
		return accounts.get(0).getBalance();
	}



	

	

	/**
	 * open a new account for the logged in customer
	 * @throws SCBException if account can not be created
	 */
	public void openNewAccount() throws SCBException {
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			if (user == null) {
				throw new RuntimeException(
						"Nicht eingeloggt. Zugriff unterbunden");
			}
			SCBIdentityDTO identityInfo = getIdentity(pm, user);
			Random rd = new Random();
			int kontoNr = rd.nextInt(100000) + 1000;

			DecimalFormat format = new DecimalFormat("##");
			format.setMinimumIntegerDigits(6);

			InternalSCBAccount acc = new InternalSCBAccount(identityInfo.getEmail(), format
					.format(kontoNr), 5, ownBank);
			acc.setBank(ownBank);
			acc.setId(KeyFactory.createKey(ownBank.getId(), InternalSCBAccount.class
					.getSimpleName(), kontoNr));
			acc.setAccountType(AccountDTO.SAVING_ACCOUNT);
			acc.setAccountDescription(AccountDTO.SAVING_ACCOUNT_DES);
			
			acc.setOwnerEmail(identityInfo.getEmail());

			pm.currentTransaction().begin();
			pm.makePersistent(acc);
			ownBank.addAccount(acc);
			sendPINList(pm, acc);
			pm.currentTransaction().commit();
			log.info("account neu geoeffnet : " + acc);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCBException("Error opening the account", e);
		} finally {
			if (pm.isClosed()) {
				return;
			}
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
			pm.close();
		}

	}

	/*
	 * sends money. For this service, the sender must be a customer of SCB
	 * the receiver must now already be known, he is created before confirmation
	 */
	public void sendMoney(String senderAccountNr, String blz,
			String receiveraccountNr, double amount, String remark,
			String receiverName, String bankName, String tan)
			throws SCBException {
		try {

			pm = PMF.get().getPersistenceManager();
			bankWrapper = AllBanks.getBankWrapper(pm);
			ownBank = bankWrapper.getOwnBanks();
			InternalSCBAccount senderAccount = InternalSCBAccount.getOwnByAccountNr(pm,
					senderAccountNr);
			if (senderAccount == null) {
				throw new SCBException("Sender Account " + senderAccountNr
						+ " doesn't exist! Bug?");
			}

			Bank receiverBank = Bank.getByBLZ(pm, blz);
			if (receiverBank == null) {
					throw new SCBException("Bank with BLZ "+blz+ " is not known. Bug?");
			}

			GeneralAccount recAccount;
			if (receiverBank.equals(ownBank)) {
				recAccount = InternalSCBAccount.getOwnByAccountNr(pm, receiveraccountNr);
				if (recAccount == null) {
					throw new AccountNotExistException(receiveraccountNr);
				}

			} else {//external Bank
				recAccount = ExternalAccount.getAccountByBLZAndAccountNr(pm,
						receiverBank, receiveraccountNr);

				if (recAccount == null) {
					throw new RuntimeException ("this account must exist by now");				}
			}
			MoneyTransferPending pendingTrans = senderAccount
					.getPendingTransaction();
			if (pendingTrans == null) {
				log
						.severe("Hacking attempt!. No pending transaction before commit");
				throw new SCBException("Invalid transaction");
			}
			int tanPos = pendingTrans.getRequiredTan();
			String referenzTan = senderAccount.getTan(tanPos);
//			if (!tan.equals(referenzTan)) {
//				log.severe("Wrong TAN. Request TAN Pos : " + tanPos
//						+ " \t Send TAN: " + tan);
//				senderAccount.increaseWrongTANCounter();
//				throw new WrongTANException(senderAccount.getWrongTANCounter());
//			} else {
//				senderAccount.resetWrongTANCounter();
//			}
			

			MoneyTransfer transfer = new MoneyTransfer(pm, senderAccount,
					recAccount, amount,recAccount.getOwner(),remark);
			
			// transfer.setId(KeyFactory.createKey(senderAccount.getId(),
			// MoneyTransfer.class.getSimpleName(), 1));
			log.info("Save Moneytransfer");
			transferMoney(pm, senderAccount, recAccount, transfer, amount, remark);
			

		} finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
			pm.close();
		}
	}

	/**
	 * get all account details 
	 * @param accountNr account that should be fetched
	 * @return detailled information
	 * @throws SCBException if data can not be fetched
	 */
	public AccountDetailDTO getAccountDetails(String accountNr)
			throws SCBException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		InternalSCBAccount acc = InternalSCBAccount.getOwnByAccountNr(pm, accountNr);
		if (acc == null) {
			throw new SCBException("Account data for Account '" + accountNr
					+ "' can not be loaded at the moment!");
		}
		return acc.getDetailedDTO(pm);
	}

	private MimeBodyPart createPINAttachment(InternalSCBAccount account)
			throws DocumentException, MessagingException, IOException {

		Document document = new Document();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, byteOut);

		document.open();
		Paragraph titel = new Paragraph(
				"Transaction numbers for Secure Cloud Banking");
		titel.getFont().setStyle(Font.BOLD);
		document.add(titel);
		document.add(new Paragraph(
				"Please keep the following TANs private!\n\n"));
		PdfPTable table = new PdfPTable(4); // Code 1
		DecimalFormat format = new DecimalFormat("##");
		format.setMinimumIntegerDigits(2);

		for (int i = 0; i < account.getTans().getTan().size(); i++) {
			String text = format.format(i) + ": "
					+ account.getTans().getTan().get(i);
			table.addCell(text);
		}
		document.add(table);
		document.close();
		byte[] invitationAttachment = byteOut.toByteArray();

		MimeBodyPart mimeAttachment = new MimeBodyPart();
		mimeAttachment.setFileName("TAN.pdf");
		ByteArrayDataSource mimePartDataSource = new ByteArrayDataSource(
				new ByteArrayInputStream(invitationAttachment),
				"application/pdf");
		mimeAttachment.setDataHandler(new DataHandler(mimePartDataSource));

		return mimeAttachment;

	}

	private void sendPINList(PersistenceManager pm, InternalSCBAccount account)
			throws SCBException {
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			SCBIdentity id = SCBIdentity.getIdentity(pm, user);
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			Multipart outboundMultipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart
					.setContent(
							"<html><head>Account openened</head><body>Congratulations. You have activated your account at Secure Cloud Banking</body></html>",
							"text/html");
			messageBodyPart
					.setText(
							"Account openened. \nYou have activated your account at Secure Cloud Banking",
							"text/plain");
			outboundMultipart.addBodyPart(messageBodyPart);

			outboundMultipart.addBodyPart(createPINAttachment(account));

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(
					"support@securecloudbanking.appspotmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(id
					.getEmail(), id.getName()));
			msg.setSubject("SCB Account " + account.getAccountNr() + " opened");
			msg
					.setText("Congratulations. You have activated your account at Secure Cloud Banking. Attached you find the Transaction numbers");
			msg.setContent(outboundMultipart);
			
			Transport.send(msg);

			log.info("Registration received: " + id);

		} catch (Exception e) {
			throw new SCBException("TAN letter can not be sent", e);
		}

	}

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
	public MoneyTransferDTO sendMoneyAskForConfirmationData(
			String senderAccountNr, String blz, String receiveraccountNr,
			double amount, String remark, String receiverName, String bankName)
			throws SCBException {

		try {

			pm = PMF.get().getPersistenceManager();
			bankWrapper = AllBanks.getBankWrapper(pm);
			ownBank = bankWrapper.getOwnBanks();
			InternalSCBAccount senderAccount = InternalSCBAccount.getOwnByAccountNr(pm,
					senderAccountNr);
			if (senderAccount == null) {
				throw new SCBException("Sender Account " + senderAccountNr
						+ " existiert nicht!");
			}

			Bank receiverBank = Bank.getByBLZ(pm, blz);
			if (receiverBank == null) {
				if (bankName == null || bankName.trim().equals("")) {
					bankName = "Neue Bank";
				}
				receiverBank = new Bank(blz.trim(), bankName);
				receiverBank.setId(KeyFactory.createKey(bankWrapper.getId(),
						Bank.class.getSimpleName(), blz));

				bankWrapper.getOtherBanks().add(receiverBank);
				log.info("Create external bank");
				pm.currentTransaction().begin();
				pm.makePersistent(bankWrapper);
				pm.currentTransaction().commit();

			}

			GeneralAccount recAccount;
			if (receiverBank.equals(ownBank)) {
				recAccount = InternalSCBAccount.getOwnByAccountNr(pm, receiveraccountNr);
				if (recAccount == null) {
					throw new RuntimeException(
							"Dieser Account existiert nicht bei der Bank "
									+ receiveraccountNr);
				}

			} else {
				recAccount = ExternalAccount.getAccountByBLZAndAccountNr(pm,
						receiverBank, receiveraccountNr);

				if (recAccount == null) {
					log.info("Account" + receiveraccountNr
							+ " is not yet known at " + receiverBank.getName()
							+ "(" + receiverBank.getBlz() + "). Create it.");
					log.info("Create external account");
					pm.currentTransaction().begin();
					recAccount = new ExternalAccount(receiverName,
							receiveraccountNr, receiverBank);
					recAccount.setId(KeyFactory.createKey(receiverBank.getId(),
							ExternalAccount.class.getSimpleName(), receiverBank
									.getBlz()
									+ "_" + receiveraccountNr));
					receiverBank.addAccount(recAccount);
					pm.makePersistent(recAccount);

					pm.currentTransaction().commit();
				}
			}
			// pm.currentTransaction().commit();
			MoneyTransferPending transfer = new MoneyTransferPending();
			transfer.setRemark(remark);
			transfer.setReceiverName(receiverName);
			transfer.setReceiverBankName(bankName);
			transfer.setSenderAccountNr(senderAccountNr);
			transfer.setReceiverBLZ(blz);
			transfer.setReceiverAccountNr(receiveraccountNr);
			transfer.setAmount(amount);

			Random r = new Random();
			int transNr = r.nextInt(100);
			transfer.setRequiredTan(transNr);
			// transfer.setId(KeyFactory.createKey(senderAccount.getId(),
			// MoneyTransfer.class.getSimpleName(), 19));
			log.info("Save Moneytransfer");
			pm.currentTransaction().begin();

			// pm.makePersistent(transfer);
			senderAccount.setPendingTransaction(transfer);

			pm.makePersistent(senderAccount);
			pm.currentTransaction().commit();
			return transfer.getDTO();

		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new SCBException(
					"Transaction can not be executed", e);
		}
		finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
			pm.close();
		}
	}

	/**
	 * send all details for a money transaction. The recipient must be customer of SCB and is determined with his email. The money is not yet transferred, but in a second step must be confirmed with a TAN
	 */
	public MoneyTransferDTO sendMoneyAskForConfirmationDataWithEmail(
			String senderAccountNr, String email, double amount, String remark) throws SCBException {
		try {

			pm = PMF.get().getPersistenceManager();
			bankWrapper = AllBanks.getBankWrapper(pm);
			ownBank = bankWrapper.getOwnBanks();
			InternalSCBAccount senderAccount = InternalSCBAccount.getOwnByAccountNr(pm,
					senderAccountNr);
			if (senderAccount == null) {
				throw new SCBException("Sender Account " + senderAccountNr
						+ " existiert nicht!");
			}
			
			InternalSCBAccount receiverAcc= InternalSCBAccount.getOwnByEmail(pm,email);
			if (receiverAcc==null){
				throw new AccountNotExistException();
			}
			
			
			MoneyTransferPending transfer = new MoneyTransferPending();
			transfer.setRemark(remark);
			transfer.setReceiverName(receiverAcc.getOwnerEmail());
			transfer.setSenderAccountNr(senderAccountNr);
			transfer.setReceiverBLZ(receiverAcc.getBank(pm).getBlz());			
			transfer.setReceiverAccountNr(receiverAcc.getAccountNr());
			transfer.setAmount(amount);
			transfer.setReceiverBankName(receiverAcc.getBank(pm).getName());

			Random r = new Random();
			int transNr = r.nextInt(100);
			transfer.setRequiredTan(transNr);
			log.info("Save Moneytransfer");
			pm.currentTransaction().begin();


			senderAccount.setPendingTransaction(transfer);

			pm.makePersistent(senderAccount);
			pm.currentTransaction().commit();
			return transfer.getDTO();

			
			
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			if (e instanceof SCBException ){
				throw (SCBException)e;
			}
			else{	throw new SCBException(
					"Can not executed transaction.", e);
			}
		}
		finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
			pm.close();
		}
	}

	
}
