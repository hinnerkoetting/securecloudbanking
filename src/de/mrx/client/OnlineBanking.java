package de.mrx.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class OnlineBanking implements EntryPoint {

	private static final String PAGEID_CLOUDBANKING = "cloudbanking";

	public void onModuleLoad() {
		RootPanel r=RootPanel.get(PAGEID_CLOUDBANKING);
	    if (r==null){
	    	GWT.log("Root nicht gefunden: '"+PAGEID_CLOUDBANKING+"'");
	    	return ;
	    }
	    GWT.log("Root gefunden");

	}

}
