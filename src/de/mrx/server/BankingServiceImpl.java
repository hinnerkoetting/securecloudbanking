package de.mrx.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.jdo.JDOHelper;
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

import net.sourceforge.htmlunit.corejs.javascript.ast.ThrowStatement;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.mrx.client.AccountDTO;
import de.mrx.client.AccountDetailDTO;
import de.mrx.client.BankingService;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.SCBIdentityDTO;
import de.mrx.shared.SCBException;

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
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		SCBIdentityDTO identityInfo;

		if (user != null) {

			log.fine("Login: " + user);

			identityInfo = getIdentity(pm,user);

			identityInfo.setLoggedIn(true);			
			identityInfo.setLogoutUrl(userService.createLogoutURL(requestUri));

		} else {
			identityInfo = new SCBIdentityDTO();
			identityInfo.setLoggedIn(false);
			identityInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return identityInfo;
	}

	private SCBIdentityDTO getIdentity(PersistenceManager pm, User user) {
		SCBIdentity id=SCBIdentity.getIdentity(pm,user);
		if (id==null){
			id= new SCBIdentity(user.getEmail());
			id.setNickName(user.getNickname());			
		}
		return id.getDTO();

		
		
	}

	public void openNewAccount() throws SCBException {
		try{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new RuntimeException("Nicht eingeloggt. Zugriff unterbunden");
		}
		SCBIdentityDTO identityInfo = getIdentity(pm,user);
		Random rd = new Random();
		int kontoNr = rd.nextInt(100000) + 1000;
		
		DecimalFormat format=new DecimalFormat("##");
		format.setMinimumIntegerDigits(6);
		
		Account acc = new Account(identityInfo.getEmail(), format.format(kontoNr), 5,
				ownBank);
		acc.setBank(ownBank);
		acc.setId(  KeyFactory.createKey(ownBank.getId(),Account.class.getSimpleName(),kontoNr));
		acc.setAccountType(AccountDTO.SAVING_ACCOUNT);
		acc.setAccountDescription(AccountDTO.SAVING_ACCOUNT_DES);

		
		pm.currentTransaction().begin();
		pm.makePersistent(acc);
		ownBank.addAccount(acc);
		sendPINList(pm,acc);
		pm.currentTransaction().commit();
		log.info("account neu geoeffnet : " + acc);

		}
		catch (Exception e){
			e.printStackTrace();
			throw new SCBException("Error opening the account",e);
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

	public AccountDetailDTO getAccountDetails( String accountNr) throws SCBException {
		PersistenceManager pm=PMF.get().getPersistenceManager();
		Account acc=Account.getOwnByAccountNr(pm,accountNr);
		if (acc==null){
			throw new SCBException("Account data for Account '"+accountNr+"' can not be loaded at the moment!");
		}
		return acc.getDetailedDTO(pm);
	}

	
	private MimeBodyPart createPINAttachment(Account account)
	throws DocumentException, MessagingException, IOException {
		
Document document = new Document();
ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
PdfWriter.getInstance(document, byteOut);

document.open();
Paragraph titel=new Paragraph("Transaction numbers for Secure Cloud Banking");
titel.getFont().setStyle(Font.BOLD);
document.add(titel);
document.add(new Paragraph("Please keep the following TANs private!\n\n"));
PdfPTable table = new PdfPTable(4); // Code 1
DecimalFormat format=new DecimalFormat("##");
format.setMinimumIntegerDigits(2);

for (int i=0;i< account.getTans().getTan().size();i++){
	String text=format.format(i)+": "+account.getTans().getTan().get(i);
	table.addCell(text);
}
document.add(table);
document.close();
byte[] invitationAttachment = byteOut.toByteArray();

MimeBodyPart mimeAttachment = new MimeBodyPart();
mimeAttachment.setFileName("TAN.pdf");
ByteArrayDataSource mimePartDataSource = new ByteArrayDataSource(
		new ByteArrayInputStream(invitationAttachment), "application/pdf");
mimeAttachment.setDataHandler(new DataHandler(mimePartDataSource));

return mimeAttachment;

}

	private void sendPINList(PersistenceManager pm,Account account) throws SCBException {
		try{
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			SCBIdentity id=SCBIdentity.getIdentity(pm,user);
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
			msg.setFrom(new InternetAddress("support@securecloudbanking.appspotmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(id
					.getEmail(), id.getName()));
			msg.setSubject("SCB Account "+account.getAccountNr()+" opened");
			msg
					.setText("Congratulations. You have activated your account at Secure Cloud Banking. Attached you find the Transaction numbers");
			msg.setContent(outboundMultipart);
			Transport.send(msg);

			
			log.info("Registration received: " + id);

		}
		catch (Exception e){
		 throw new SCBException("TAN letter can not be sent",e);		 
		}

		

	}

	public MoneyTransferDTO sendMoneyAskForConfirmationData(String senderAccountNr, String blz,
	String receiveraccountNr, double amount, String remark, String receiverName, String bankName) throws SCBException  {
		
		try{
			
			pm=PMF.get().getPersistenceManager();
			bankWrapper=AllBanks.getBankWrapper(pm);
			ownBank=bankWrapper.getOwnBanks();
		Account senderAccount = Account.getOwnByAccountNr(pm,senderAccountNr);
		if (senderAccount==null){
			throw new SCBException("Sender Account "+senderAccountNr+" existiert nicht!");
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
		MoneyTransferPending transfer = new MoneyTransferPending();
		transfer.setRemark(remark);
		transfer.setReceiverName(receiverName);
		transfer.setSenderAccountNr(senderAccountNr);
		transfer.setReceiverBLZ(blz);
		transfer.setReceiverAccountNr(receiveraccountNr);
		transfer.setAmount(amount);
		
		Random r=new Random();
		int transNr=r.nextInt(100);
		transfer.setRequiredTan(transNr);
//		transfer.setId(KeyFactory.createKey(senderAccount.getId(), MoneyTransfer.class.getSimpleName(), 19));
		log.info("Save Moneytransfer");
		pm.currentTransaction().begin();
		
//		pm.makePersistent(transfer);
		senderAccount.setPendingTransaction(transfer);
		
		pm.makePersistent(senderAccount);
		 pm.currentTransaction().commit();
		 return transfer.getDTO();

	}
		catch (Exception e){
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new SCBException("Überweisung kann derzeit nicht ausgeführt werden",e);			
		}
	}
	
}
