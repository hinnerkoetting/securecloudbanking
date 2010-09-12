package de.mrx.client.admin.forms;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.Admin;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;
import de.mrx.shared.SCBData;

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
	
	@UiField
	TextBox searchOwner;
	
	@UiField
	TextBox searchAccountNr;
	
	@UiField
	Button search;
	
	@UiField
	Label searchTitle;
	
	@UiField
	Label descOwner;
	
	@UiField
	Label descAccountNr;
	
	@UiField
	FlexTable searchTable;
	
	DialogBox a;
	
	Admin adminpage;
	
	
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public AdminTransfer(Admin admin, String accNr, String accOwner) {
		this.adminpage = admin;
		initWidget(uiBinder.createAndBindUi(this));
		title.setText(constants.transferMoney());		
		descName.setText(constants.name());
		descNr.setText(constants.accountNr());
		descBLZ.setText(constants.blz());
		descRecipient.setText("Recipient");
		descSender.setText("Sender");
			
		
		recipientName.setText(accOwner);
		recipientName.setEnabled(false);
			
		recipientNr.setText(accNr);
		recipientNr.setEnabled(false);
		
		//TODO this should be a reference to BLZ string
		recipientBLZ.setText(SCBData.SCB_PLZ);
		recipientBLZ.setEnabled(false);
		
		descOwner.setText(constants.owner());
		descAccountNr.setText(constants.accountNr());			
		descRemark.setText(constants.remark());		
		descAmount.setText(constants.amount());
		submit.setText(constants.submit());
		search.setText(constants.search());
		searchTitle.setText(constants.searchForSender());
		
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
	
	@UiHandler("search")
	public void onClickSearch(ClickEvent event) {

		AdminServiceAsync adminService = GWT.create(AdminService.class);
		
		adminService.searchInternalAccounts(searchOwner.getText(), searchAccountNr.getText(), new AsyncCallback<List<AccountDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(List<AccountDTO> result) {
				setAccounts(result);
				
			}
		});
	}
	
	/**
	 * 
	 * @param accounts
	 */
	public void setAccounts(List<AccountDTO> accounts){
		searchTable.clear();
		

		final int posOwner 			= 0;
		final int posAccount 		= 1;
		final int posInsert			= 2;
		//add header
		searchTable.setWidget(0, posOwner, new Label(constants.owner()));
		searchTable.setWidget(0, posAccount, new Label(constants.accountNr()));
		searchTable.setWidget(0, posInsert, new Label(constants.insert()));
		
		//add all accounts to table
		int row = 1;		

		for (AccountDTO account: accounts) {	
			searchTable.setWidget(row, posAccount, new Label(account.getAccountNr()));			
			searchTable.setWidget(row, posOwner, new Label(account.getOwner()));
			Button insertButton = new Button(constants.insert());
			final String accNr = account.getAccountNr();
			final String accOwner = account.getOwner();
			insertButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					senderBLZ.setText(SCBData.SCB_PLZ);
					senderName.setText(accOwner);
					senderNr.setText(accNr);
				}
			});
			searchTable.setWidget(row, posInsert, insertButton);
			row++;
		}
		TableStyler.setTableStyle(searchTable);


	}
}
