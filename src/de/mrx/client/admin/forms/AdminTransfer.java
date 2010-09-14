package de.mrx.client.admin.forms;

import java.util.ArrayList;
import java.util.List;

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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;
import de.mrx.shared.SCBData;

public class AdminTransfer extends Composite implements Observable, Observer {

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
	SimplePanel searchForm;
	
	List<Observer> observer;
	
	
	public static final int TRANSACTION_SUCCEEDED = 50;
	
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public AdminTransfer(String accNr, String accOwner) {
		observer = new ArrayList<Observer>();
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
		
		recipientBLZ.setText(SCBData.SCB_PLZ);
		recipientBLZ.setEnabled(false);
		
		submit.setText(constants.submit());
		descAmount.setText(constants.amount());
		descRemark.setText(constants.remark());
		
		
		SearchAccountsForm search = new SearchAccountsForm();
		search.addObserver(this);
		search.enableColumns(true, true, false);
		search.enableButton(true, "Insert");
		
		searchForm.setWidget(search);		
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
							GWT.log(result);
							notifyObservers(TRANSACTION_SUCCEEDED, null);
						}
					});
		
	}

	
	/**
	 * 
	 * @param accounts
	 */
//	public void setAccounts(List<AccountDTO> accounts){
//		searchTable.clear();
//		
//
//		final int posOwner 			= 0;
//		final int posAccount 		= 1;
//		final int posInsert			= 2;
//		//add header
//		searchTable.setWidget(0, posOwner, new Label(constants.owner()));
//		searchTable.setWidget(0, posAccount, new Label(constants.accountNr()));
//		searchTable.setWidget(0, posInsert, new Label(constants.insert()));
//		
//		//add all accounts to table
//		int row = 1;		
//
//		for (AccountDTO account: accounts) {	
//			searchTable.setWidget(row, posAccount, new Label(account.getAccountNr()));			
//			searchTable.setWidget(row, posOwner, new Label(account.getOwner()));
//			Button insertButton = new Button(constants.insert());
//			final String accNr = account.getAccountNr();
//			final String accOwner = account.getOwner();
//			insertButton.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					senderBLZ.setText(SCBData.SCB_PLZ);
//					senderName.setText(accOwner);
//					senderNr.setText(accNr);
//				}
//			});
//			searchTable.setWidget(row, posInsert, insertButton);
//			row++;
//		}
//		TableStyler.setTableStyle(searchTable);
//
//
//	}

	@Override
	public void addObserver(Observer o) {
		observer.add(o);
		
	}

	@Override
	public void notifyObservers(Integer eventType, Object parameter) {
		for (Observer o: observer) {
			o.update(this, eventType, parameter);
		}
		
	}



	@Override
	public void update(Observable source, Object event, Object parameter) {
		if (source instanceof SearchAccountsForm) {
			if ((Integer)event == SearchAccountsForm.SELECTED_ACCOUNT) {
				AccountDTO account = (AccountDTO)parameter;
				senderBLZ.setText(SCBData.SCB_PLZ);
				senderName.setText(account.getOwner());
				senderNr.setText(account.getAccountNr());
				return;
			}		
		}
		GWT.log("event not implemented");
		
	}

	@Override
	public void reportInfo(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportError(String error) {
		// TODO Auto-generated method stub
		
	}
}
