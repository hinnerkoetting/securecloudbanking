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

	@DefaultStringValue("Your name:")
	String registrationName();
	
	@DefaultStringValue("Street:")
	String registrationStreet();
	
	@DefaultStringValue("Gratulation. You have registered! We will progress your request as soon as we are able to!")
	String registrationSuccessMessage();
}
