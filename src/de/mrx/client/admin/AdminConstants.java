package de.mrx.client.admin;

import com.google.gwt.i18n.client.Constants;

public interface AdminConstants extends Constants{

	@DefaultStringValue("Accounts")
	  String accounts();
	
	
	@DefaultStringValue("External Banks")
	 String externalBanks();
	
	
	@DefaultStringValue("Generate Data")
	 String generateData();
	
	@DefaultStringValue("Account overview")
	 String accountOverview();
	
	@DefaultStringValue("Owner")
	 String owner();
	
	@DefaultStringValue("Account No.")
	 String accountNr();
	
	@DefaultStringValue("Balance")
	 String balance();
	
	@DefaultStringValue("Transactions")
	 String transactions();
	
	@DefaultStringValue("Transfer money")
	 String transferMoney();
	
	@DefaultStringValue("Delete account")
	 String deleteAccount();
	
	@DefaultStringValue("Search")
	 String search();
	
	@DefaultStringValue("Name")
	 String name();
	
	@DefaultStringValue("BLZ")
	 String blz();
	
	@DefaultStringValue("View accounts")
	 String viewAccounts();
	
	@DefaultStringValue("Edit details")
	 String editDetails();
	
	@DefaultStringValue("Delete")
	 String delete();
	
	@DefaultStringValue("Add bank")
	 String addBank();
	
	@DefaultStringValue("Display")
	 String display();
	
	@DefaultStringValue("Display accounts")
	 String displayAccounts();
	
	@DefaultStringValue("Transfer")
	 String transfer();
	
	@DefaultStringValue("Edit")
	 String edit();
	
	@DefaultStringValue("Are you sure? This will delete this bank and all its accounts!")
	 String deleteBankConfirm();
	
	@DefaultStringValue("Are you sure? This will delete this account!")
	 String deleteAccountConfirm();
	
	@DefaultStringValue("Are you sure?\nThis will delete all current data!")
	 String deleteDataConfirm();
	
	@DefaultStringValue("Are you sure?\nThis will create some random test data!")
	 String generateDataConfirm();
	
	@DefaultStringValue("Recipient")
	 String recipient();
	
	@DefaultStringValue("Sender")
	 String sender();
	
	@DefaultStringValue("Remark")
	 String remark();
	
	@DefaultStringValue("Amount")
	 String amount();
	
	@DefaultStringValue("Submit")
	 String submit();
	
	@DefaultStringValue("Search for sender")
	 String searchForSender();
	
	@DefaultStringValue("Insert")
	 String insert();
	
	@DefaultStringValue("Welcome to the admin interface!")
	 String adminWelcome();
	
	@DefaultStringValue("Edit bank details")
	 String editBankDetails();
	
	@DefaultStringValue("Add new bank")
	 String addNewBank();
	
	@DefaultStringValue("Sign In")
	 String signIn();
	
	@DefaultStringValue("Sign Out")
	 String signOut();
	
	@DefaultStringValue("Account details")
	 String accountDetails();
	
	@DefaultStringValue("Properties")
	 String properties();

	@DefaultStringValue("Delete data")
	 String deleteData();
	
	@DefaultStringValue("Tans")
	 String tans();
	
	@DefaultStringValue("No.")
	 String nr();
	
	@DefaultStringValue("Tan")
	 String tan();
	
	@DefaultStringValue("Invalid input!")
	String invalidInput();
	 
	@DefaultStringValue("This will reset all transactions from this account" +
			"and will reset account balance.\n Are you sure?")
	 String resetConfirmation();
	
	@DefaultStringValue("Reset")
	String reset();
	
	@DefaultStringValue("Secured by SCB")
	String securedBySCB();
	
	@DefaultStringValue("Add Shop")
	String addShop();
	
	@DefaultStringValue("Open Transactions")
	String openTransactions();
	
	@DefaultStringValue("Date")
	String date();
	
	@DefaultStringValue("Delete transactions")
	String deleteTransactions();
	
}
