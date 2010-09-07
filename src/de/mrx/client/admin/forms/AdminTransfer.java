package de.mrx.client.admin.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AdminTransfer extends Composite {

	private static AdminTransferUiBinder uiBinder = GWT
			.create(AdminTransferUiBinder.class);

	interface AdminTransferUiBinder extends UiBinder<Widget, AdminTransfer> {
	}

	@UiField
	Label title;
	
	@UiField
	Label descName;
	
	@UiField
	Label descNr;
	
	@UiField
	Label descRecipient;
	
	@UiField
	Label descSender;
	
	@UiField
	TextBox recipientName;	
	
	@UiField 
	TextBox recipientNr;
	
	@UiField
	TextBox senderName;	
	
	@UiField 
	TextBox senderNr;
	
	@UiField
	Label descAmount;
	
	@UiField
	TextBox amount;
	
	@UiField
	Button submit;
	
	public AdminTransfer(String accNr, String accOwner) {
		initWidget(uiBinder.createAndBindUi(this));
		title.setText("Transfer Money");
		
		descName.setText("Recipient");
		recipientName.setText(accOwner);
		recipientName.setEnabled(false);
		
		descNr.setText("Account Number");
		recipientNr.setText(accNr);
		recipientNr.setEnabled(false);
		
		descRecipient.setText("Recipient");
		descSender.setText("Sender");
				
		
		descAmount.setText("Amount");
		submit.setText("Submit");
		
	}

}
