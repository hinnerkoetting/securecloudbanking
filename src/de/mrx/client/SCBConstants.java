package de.mrx.client;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Label;

public interface SCBConstants extends Constants{
	 @DefaultStringValue("Register")
	  String menuRegister();
	 
	 @DefaultStringValue("Sign In")
	 String signIn();
	 
	 @DefaultStringValue("Sign Out")
	 String signOut();
	 
	 
	 @DefaultStringValue("Our business will soon offer service. Please try again the other day!")
	 String outOfServiceNotice();

	@DefaultStringValue("Last name:")
	String registrationName();
	
	@DefaultStringValue("Street:")
	String registrationStreet();
	
	@DefaultStringValue("Gratulation. You have registered! We will progress your request as soon as we are able to!")
	String registrationSuccessMessage();
	
	
	@DefaultStringValue("We recommend you to log in first with your google account before proceeding")
	String hintRegistration();
	
	
	@DefaultStringValue("First name:")
	String registrationFirstName();
	
	
	@DefaultStringValue("House No:")
	String registrationHouseNr();
	
	@DefaultStringValue("Postal Code:")
	String registrationPostalCode();
	
	@DefaultStringValue("City: ")
	String registrationCity();
	
	
	@DefaultStringValue("Email (gmail only): ")
	String registrationEmail();
	
	@DefaultStringValue("I accept the Terms Of Services")
	String registrationtoSAGBHint();
	
	@DefaultStringValue("ToS")
	String registrationtoAGBLink();

	@DefaultStringValue("This is a academic technology study in Cloud Computing. At the moment you see an internal development state.\n This site is provided by J. Oetting")
	String impressumText();

	@DefaultStringValue("This site demonstrates cloud security technology.")
	String aboutText();
	
	@DefaultStringValue("Information")
	String menuInformation();
	
	@DefaultStringValue("UserInfo")
	String menuUserInformation();
	
	
	@DefaultStringValue("Financial state")
	String overViewBtnFinancialState();
	
	@DefaultStringValue("Open saving Account")
	String overViewBtnOpenSavingAccount();
	
	
	@DefaultStringValue("At the moment you have no account at SCB.")
	String accountOverviewHintNoAccount();
	
	
	@DefaultStringValue("Account Overview")
	String accountOverviewLblTitel();
	
	
	@DefaultStringValue("Account")
	String accountOverviewLblHeaderAccount();
	
	@DefaultStringValue("Balance")
	String accountOverviewLblHeaderBalance();
	
	@DefaultStringValue("Login failed. Please try again in 30 seconds")
	String loginFailedText();
	
	
	@DefaultStringValue("Your saving account is ready for service")
	String accountOpened();
	
	@DefaultStringValue("Current balance: ")
	String accountDetailCurrentBalance();
	
	
	@DefaultStringValue("At the moment there are no outgoing neither incoming transactions") 
	String accountDetailHintNoTransaction();
	
	
	@DefaultStringValue("Date")
	String accountDetailHeaderDate();
	
	@DefaultStringValue("Comment")
	String accountDetailHeaderComment();
	
	@DefaultStringValue("Account")
	String accountDetailHeaderAccount();
	
	
	@DefaultStringValue("Amount")
	String accountDetailHeaderAmount();
	
	
	@DefaultStringValue("Send money")
	String accountDetailSendMoneyBtn();
	
	
	@DefaultStringValue("FastEmail Transfer")
	String accountDetailSendMoneyFastBtn();
	
	@DefaultStringValue("Please enter a valid email address!")
	String fastMoneyValidateEmail();
	
	@DefaultStringValue("Amount may only contain a value between 0.00 and 9999.99.")
	String fastMoneyValidateAmount();
	
	@DefaultStringValue("Account may only contain numbers")
	String sendMoneyValidateaccount();
	
	
	@DefaultStringValue("BLZ may only contain numbers")
	String sendMoneyValidateBLZ();
	
	@DefaultStringValue("Please enter a valid name!")
	String registerValidateName();
	
	@DefaultStringValue("Please enter a valid first name!")
	String registerValidateFirstName();
	
	@DefaultStringValue("Please enter a valid email address!")
	String registerValidateEmail();
	
	@DefaultStringValue("Please enter a valid street name!")
	String registerValidateStreet();
	
	@DefaultStringValue("Please enter a valid postal code!")	
	String registerValidatePLZ();
	
	@DefaultStringValue("Please enter a valid house number!")
	String registerValidateHouseNr();
	
	@DefaultStringValue("Please enter a city! ")
	String registerValidateCity();
	
	@DefaultStringValue("Please read the Term of Services and agree to them!")
	String registerValidateToS();

	
}
