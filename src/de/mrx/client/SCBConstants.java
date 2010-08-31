package de.mrx.client;

import com.google.gwt.i18n.client.Constants;

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
	
}
