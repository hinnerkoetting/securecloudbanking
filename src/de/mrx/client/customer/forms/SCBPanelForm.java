package de.mrx.client.customer.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.Observable;
import de.mrx.client.customer.forms.MoneyTransferForm.MyUiBinder;

public class SCBPanelForm extends Composite{
	interface MyUiBinder extends UiBinder<Widget, SCBPanelForm> {
	}
	
	
	
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	

}
