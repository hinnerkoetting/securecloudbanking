package de.mrx.client.admin.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.admin.Admin;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

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
	Label descBLZ;
	
	@UiField
	TextBox recipientName;	
	
	@UiField 
	TextBox recipientNr;
	
	@UiField 
	TextBox recipientBLZ;
	
	@UiField
	TextBox senderName;	
	
	@UiField 
	TextBox senderNr;
	
	@UiField 
	TextBox senderBLZ;
	
	@UiField
	Label descAmount;
	
	@UiField
	TextBox amount;
	
	@UiField
	Label descRemark;
	
	@UiField
	TextBox remark;
	
	@UiField
	Button submit;
	
	Admin adminpage;
	
	public AdminTransfer(Admin admin, String accNr, String accOwner) {
		this.adminpage = admin;
		initWidget(uiBinder.createAndBindUi(this));
		title.setText("Transfer Money");		
		descName.setText("Name");
		descNr.setText("Account Number");
		descBLZ.setText("BLZ");
		descRecipient.setText("Recipient");
		descSender.setText("Sender");
			
		
		recipientName.setText(accOwner);
		recipientName.setEnabled(false);
			
		recipientNr.setText(accNr);
		recipientNr.setEnabled(false);
		
		//TODO this should be a reference to BLZ string
		recipientBLZ.setText("1502222");
		recipientBLZ.setEnabled(false);
		
				
		descRemark.setText("Remark");
		
		descAmount.setText("Amount");
		submit.setText("Submit");
		
	}

	@UiHandler("submit")
	public void onClickSubmit(ClickEvent e) {
		double dAmount = 0;
		try {
			dAmount =new Double(amount.getText());
		}
		catch(NumberFormatException exception) {
			Window.alert("Please enter valid number for amount!");
			return;
		}
		AdminServiceAsync adminService = GWT.create(AdminService.class);
		adminService.adminSendMoney(senderNr.getText(),
						senderBLZ.getText(), recipientNr.getText(),
						dAmount, remark.getText(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log(caught.toString());
							
						}

						@Override
						public void onSuccess(String result) {
							Window.alert(result);
							adminpage.showAccounts();
						}
					});
		
	}
}
