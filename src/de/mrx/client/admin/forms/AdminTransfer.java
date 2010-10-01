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
import de.mrx.client.TableStyler;
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
	
	private void setLabels(String accNr, String accOwner) {
		//descriptions
		title.setText(constants.transferMoney());		
		descName.setText(constants.name());
		descNr.setText(constants.accountNr());
		descBLZ.setText(constants.blz());
		descRecipient.setText(constants.recipient());
		descSender.setText(constants.sender());
		descAmount.setText(constants.amount());
		descRemark.setText(constants.remark());
		submit.setText(constants.submit());
		
		//searchform
		SearchAccountsForm search = new SearchAccountsForm();
		search.addObserver(this);
		search.enableColumns(true, true, false);
		searchForm.setWidget(search);
		search.enableButton(true, constants.insert());
		
		//recipient data
		recipientName.setText(accOwner);
		recipientName.setEnabled(false);
			
		recipientNr.setText(accNr);
		TableStyler.expandNumber(recipientNr);
		recipientNr.setEnabled(false);
		
		recipientBLZ.setText(SCBData.SCB_PLZ);
		TableStyler.expandNumber(recipientBLZ);
		recipientBLZ.setEnabled(false);
	}
	
	public AdminTransfer(String accNr, String accOwner) {
		observer = new ArrayList<Observer>();
		initWidget(uiBinder.createAndBindUi(this));
		setLabels(accNr, accOwner);
		
			
		
				
	}

	@UiHandler("submit")
	public void onClickSubmit(ClickEvent e) {
		double dAmount = 0;
		try {
			dAmount =new Double(amount.getText());
		}
		catch(NumberFormatException exception) {
			Window.alert("Amount has to be a number!");
			return;
		}
		AdminServiceAsync adminService = GWT.create(AdminService.class);
		adminService.adminSendMoney(senderNr.getText(),
						senderBLZ.getText(), recipientNr.getText(),
						dAmount, remark.getText(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Invalid input: " + caught.getMessage());
							GWT.log(caught.toString());
						}

						@Override
						public void onSuccess(String result) {
							GWT.log(result);
							notifyObservers(TRANSACTION_SUCCEEDED, null);
						}
					});
		
	}

	

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
