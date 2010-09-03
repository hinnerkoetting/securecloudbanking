package de.mrx.client.admin;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

public interface AdminConstants extends Constants{

	@DefaultStringValue("Accounts")
	  String overviewAccount();
	
	@DefaultStringValue("New Transaction")
	  String newTransaction();
	
	@DefaultStringValue("External Banks")
	 String externalBanks();
	
	@DefaultStringValue("History")
	 String history();
}
