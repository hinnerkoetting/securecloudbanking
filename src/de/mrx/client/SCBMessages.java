package de.mrx.client;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;

public interface SCBMessages extends Messages {
	 @DefaultMessage("''{0}'' is not a valid symbol.")
	  String invalidSymbol(String symbol);

	  @DefaultMessage("Last update: {0,date,medium} {0,time,medium}")
	  String lastUpdate(Date timestamp);

}
