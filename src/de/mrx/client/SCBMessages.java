package de.mrx.client;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;

/**
 * Internationalisation with GWT static String internationlisation
 * @see http://code.google.com/intl/de-DE/webtoolkit/doc/1.6/DevGuideI18nAndA11y.html
 * english is default language
 *
 */
public interface SCBMessages extends Messages {
	 @DefaultMessage("''{0}'' is not a valid symbol.")
	  String invalidSymbol(String symbol);

	  @DefaultMessage("Last update: {0,date,medium} {0,time,medium}")
	  String lastUpdate(Date timestamp);
	  
	  
	  @DefaultMessage("There was a problem: {0}")
	  String scbError(String detail);
	  
	  @DefaultMessage("Registration could not proceed: {0}")
	  String registrationError(String detail);

}
