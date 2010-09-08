package de.mrx.client;

import java.util.List;

import javax.jdo.PersistenceManager;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import de.mrx.server.Bank;
import de.mrx.server.CustomerServiceImpl;
import de.mrx.server.ExternalAccount;
import de.mrx.server.GeneralAccount;
import de.mrx.server.InternalSCBAccount;
import de.mrx.server.MoneyTransfer;
import de.mrx.server.MoneyTransferPending;
import de.mrx.server.PMF;
import de.mrx.server.RegisterServiceImpl;
import de.mrx.shared.WrongTANException;

public class CustomerServiceTest {
	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	PersistenceManager pmf;
	String emailAdress="JohnDoe@googlemail.com";
	
	@Before 
	public void setUp() {
		helper.setUp();
		helper.setEnvIsLoggedIn(true);
		helper.setEnvEmail(emailAdress);
		helper.setEnvAuthDomain("testDomain");
		pmf=PMF.get().getPersistenceManager();
	}
	
	 @After
	    public void tearDown() {
	        helper.tearDown();
	        pmf.close();
	    }
	 
	 private SCBIdentityDTO getStandardDummyUser(String email){
		 SCBIdentityDTO id=new SCBIdentityDTO("Doe",email);
			
			id.setFirstName("John"); 
			id.setCity("Bremen");
			id.setHouseNr("10");
			id.setStreet("Teststreet");			
			return id;
	 }
	 
	 @Test
	 public void openAccountTest()throws Exception{
		 
			SCBIdentityDTO id=getStandardDummyUser(emailAdress);
			RegisterServiceImpl regService=new RegisterServiceImpl();
			regService.register(id);
//			pmf=PMF.get().getPersistenceManager();
			CustomerServiceImpl customerService=new CustomerServiceImpl();
			customerService.openNewAccount();
			List<AccountDTO> accounts=customerService.getAccounts();
			Assert.assertEquals(1,accounts.size());
	 }
	 
	 private final static String TEST_FREMDBANK_BLZ="17";
	 private final static String TEST_FREMDBANK_RECEIVER_ACC_NR="12";
	 private final static int TEST_SEND_AMOUNT=10;
	 private final static String TEST_FREMDBANK_RECEIVER_NAME="John Doe";
	 private final static String TEST_FREMDBANK_NAME="Test Bank";
	  
	 @Test
	 public void transferMoney() throws Exception{
		 PersistenceManager pm= PMF.get().getPersistenceManager();
		 openAccountTest();
		 CustomerServiceImpl customerService=new CustomerServiceImpl();
		 List<AccountDTO> accounts=customerService.getAccounts();
		 //only one saving account
		 AccountDTO savingAccount=accounts.get(0);
		 
		 double beforeMoney=savingAccount.getBalance();
		 MoneyTransferDTO confirmation=customerService.sendMoneyAskForConfirmationData(savingAccount.getAccountNr(),TEST_FREMDBANK_BLZ, TEST_FREMDBANK_RECEIVER_ACC_NR, TEST_SEND_AMOUNT, "Test", TEST_FREMDBANK_RECEIVER_NAME,TEST_FREMDBANK_NAME);
		 InternalSCBAccount accData=InternalSCBAccount.getOwnByAccountNr(pm, savingAccount.getAccountNr());		 
		 String TAN=accData.getTan(confirmation.getRequiredTan());
		 //Check Pending Transaction
		 MoneyTransferPending mtP=accData.getPendingTransaction();
		 Assert.assertEquals(TEST_FREMDBANK_NAME, mtP.getReceiverBankName());
		 Assert.assertEquals(TEST_FREMDBANK_RECEIVER_ACC_NR, mtP.getReceiverAccountNr());
		 Assert.assertEquals(TEST_FREMDBANK_RECEIVER_NAME, mtP.getReceiverName());
		 Assert.assertEquals(TEST_FREMDBANK_BLZ, mtP.getReceiverBLZ());
		 
		 //Check receiver account
		 Bank receiverB=Bank.getByBLZ(pm,TEST_FREMDBANK_BLZ);
		 ExternalAccount receiverAccount= ExternalAccount.getAccountByBLZAndAccountNr(pm, receiverB, TEST_FREMDBANK_RECEIVER_ACC_NR);
		 Assert.assertNotNull(receiverAccount);
		 Assert.assertEquals(TEST_FREMDBANK_RECEIVER_ACC_NR, receiverAccount.getAccountNr());
		 Assert.assertEquals(TEST_FREMDBANK_RECEIVER_NAME, receiverAccount.getOwner());
		 
		 
		 customerService.sendMoney(savingAccount.getAccountNr(),TEST_FREMDBANK_BLZ, TEST_FREMDBANK_RECEIVER_ACC_NR, TEST_SEND_AMOUNT, "Test", TEST_FREMDBANK_RECEIVER_NAME,TEST_FREMDBANK_NAME,TAN);
		 accounts=customerService.getAccounts();
		 //only one saving account
		 savingAccount=accounts.get(0);
		 double afterMoney=savingAccount.getBalance();		 
		 //Transactions subtract transaction amount, because it is a double	value, don't require complete equality	 
		 Assert.assertTrue((afterMoney-beforeMoney-TEST_SEND_AMOUNT)<0.001);
		 
		 //DB-Check
		 GeneralAccount dbSenderAccount=InternalSCBAccount.getOwnByAccountNr(pm, savingAccount.getAccountNr());
		 MoneyTransfer dbMoneyTransfer=dbSenderAccount.getTransfers().get(0);
		 Assert.assertEquals(TEST_SEND_AMOUNT, (int)dbMoneyTransfer.getAmount());
		 Assert.assertEquals(TEST_FREMDBANK_BLZ, dbMoneyTransfer.getReceiverBLZ());		 
		 Assert.assertEquals(TEST_FREMDBANK_RECEIVER_ACC_NR, dbMoneyTransfer.getReceiverAccountNr());
		 Assert.assertEquals(TEST_FREMDBANK_RECEIVER_NAME, dbMoneyTransfer.getReceiverName());
	 }
	 
	 
	 
	 @Test
	 public void transferMoneyWithWrongTAN() throws Exception{
		 openAccountTest();
		 CustomerServiceImpl customerService=new CustomerServiceImpl();
		 List<AccountDTO> accounts=customerService.getAccounts();
		 //only one saving account
		 AccountDTO savingAccount=accounts.get(0);
		 savingAccount.getAccountNr();		 
		 customerService.sendMoneyAskForConfirmationData(savingAccount.getAccountNr(),TEST_FREMDBANK_BLZ, TEST_FREMDBANK_RECEIVER_ACC_NR, TEST_SEND_AMOUNT, "Test", TEST_FREMDBANK_RECEIVER_NAME,TEST_FREMDBANK_NAME);		 		 
		 String TAN="WRONG TAN";
		 try{
		 customerService.sendMoney(savingAccount.getAccountNr(),TEST_FREMDBANK_BLZ, TEST_FREMDBANK_RECEIVER_ACC_NR, TEST_SEND_AMOUNT, "Test", TEST_FREMDBANK_RECEIVER_NAME,TEST_FREMDBANK_NAME,TAN);
		 }
		 catch (WrongTANException e){
			 //that was required
			 return;
		 }
		 Assert.fail("Wrong TAN accepted");
		 
	 }
	 
	 /**
	  * transfer between two account of one user
	  * @throws Exception
	  */
	 @Test
	 public void transferMoneyToOtherAccountOfSameUser() throws Exception{
		 String ownName="myself";
		 PersistenceManager pm= PMF.get().getPersistenceManager();
		 openAccountTest();
		 
		 CustomerServiceImpl customerService=new CustomerServiceImpl();
		 //open up second account 
		 customerService.openNewAccount();
		 List<AccountDTO> accounts=customerService.getAccounts();
		 //first saving account is sender
		 AccountDTO senderSavingAccount=accounts.get(0);
		 AccountDTO receiverSavingAccount=accounts.get(1);
		 senderSavingAccount.getAccountNr();
		 double beforeMoneySender=senderSavingAccount.getBalance();
		 double beforeMoneyReceiver=senderSavingAccount.getBalance();
		 MoneyTransferDTO confirmation=customerService.sendMoneyAskForConfirmationData(senderSavingAccount.getAccountNr(),CustomerService.SCB_BLZ,receiverSavingAccount.getAccountNr(),TEST_SEND_AMOUNT,"Eigentransaction", ownName,CustomerService.SCB_NAME);
		 InternalSCBAccount accData=InternalSCBAccount.getOwnByAccountNr(pm, senderSavingAccount.getAccountNr());
		 MoneyTransferPending mtP=accData.getPendingTransaction();
		 Assert.assertEquals(CustomerService.SCB_NAME, mtP.getReceiverBankName());
		 Assert.assertEquals(receiverSavingAccount.getAccountNr(), mtP.getReceiverAccountNr());
		 Assert.assertEquals(ownName, mtP.getReceiverName());
		 Assert.assertEquals(CustomerService.SCB_BLZ, mtP.getReceiverBLZ());

		 String TAN=accData.getTan(confirmation.getRequiredTan());		 
		 customerService.sendMoney(senderSavingAccount.getAccountNr(),CustomerService.SCB_BLZ,receiverSavingAccount.getAccountNr(),TEST_SEND_AMOUNT,"Eigentransaction", ownName,CustomerService.SCB_NAME,TAN);
		 accounts=customerService.getAccounts();		 
		 senderSavingAccount=accounts.get(0);
		 receiverSavingAccount=accounts.get(1);
		 double afterMoneySender=senderSavingAccount.getBalance();
		 double afterMoneyReceiver=receiverSavingAccount.getBalance();
		 //Transactions subtract transaction amount, because it is a double	value, don't require complete equality	 
		 Assert.assertTrue((afterMoneySender-beforeMoneySender-TEST_SEND_AMOUNT)<0.001);
		 Assert.assertTrue((afterMoneyReceiver-beforeMoneyReceiver-TEST_SEND_AMOUNT)<0.001);
		 
		 //DB-Check
		 GeneralAccount dbSenderAccount=InternalSCBAccount.getOwnByAccountNr(pm, senderSavingAccount.getAccountNr());
		 MoneyTransfer dbMoneyTransfer=dbSenderAccount.getTransfers().get(dbSenderAccount.getTransfers().size()-1);
		 Assert.assertEquals(TEST_SEND_AMOUNT, (int)dbMoneyTransfer.getAmount());
		 Assert.assertEquals(CustomerService.SCB_BLZ, dbMoneyTransfer.getReceiverBLZ());		 
		 Assert.assertEquals(receiverSavingAccount.getAccountNr(), dbMoneyTransfer.getReceiverAccountNr());
		 Assert.assertEquals("JohnDoe@googlemail.com", dbMoneyTransfer.getReceiverName());
		 
		 //getAccountDetailsCheck
		 AccountDetailDTO detailDTO= customerService.getAccountDetails(senderSavingAccount.getAccountNr());
		 List<MoneyTransferDTO> transfers=	 detailDTO.getTransfers();
		 MoneyTransferDTO transfer=transfers.get(0);
		 Assert.assertEquals(TEST_SEND_AMOUNT,(int)transfer.getAmount());
		 Assert.assertEquals(CustomerService.SCB_BLZ,transfer.getReceiverBankName());
		 Assert.assertEquals(CustomerService.SCB_BLZ,transfer.getReceiverBankNr());
		 Assert.assertEquals(receiverSavingAccount.getAccountNr(),transfer.getReceiverAccountNr());
		 Assert.assertEquals("JohnDoe@googlemail.com",transfer.getReceiverName());
		 
		 
		 

	 }

}
