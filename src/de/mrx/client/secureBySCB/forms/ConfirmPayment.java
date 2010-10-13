package de.mrx.client.secureBySCB.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmPayment extends Composite {

	private static ConfirmPaymentUiBinder uiBinder = GWT
			.create(ConfirmPaymentUiBinder.class);

	interface ConfirmPaymentUiBinder extends UiBinder<Widget, ConfirmPayment> {
	}



	public ConfirmPayment() {
		initWidget(uiBinder.createAndBindUi(this));

	}
	
	@UiHandler("accept")	
	public void clickOnAccept(ClickEvent e) {
		//send confirmation to shop
		//close this window
	}
}
