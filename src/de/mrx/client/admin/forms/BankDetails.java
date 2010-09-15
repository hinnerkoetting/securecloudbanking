package de.mrx.client.admin.forms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.TransferHistoryForm;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class BankDetails extends Composite implements Observer, Observable {

	private static BankDetailsUiBinder uiBinder = GWT
			.create(BankDetailsUiBinder.class);

	interface BankDetailsUiBinder extends UiBinder<Widget, BankDetails> {
	}

	@UiField
	Label title;
	
	@UiField
	Label subTitle;
	
	@UiField
	MyStyle style;
	
	@UiField
	Button enableDisplayAccounts;
	
	@UiField
	Button enableEdit;
	
	@UiField
	Button delete;
	
	@UiField
	SimplePanel content;
	
	interface MyStyle extends CssResource {
		    String active();
		    String nonActive();
	}
	
	List<Observer> observer;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	
	String name;
	String blz;
	
	public final static int DELETE_BANK_SUCCEED = 8432;
	
	private void setActive(Button button) {
		//reset all buttons
		delete.setStyleName(style.nonActive());
		enableDisplayAccounts.setStyleName(style.nonActive());
		enableEdit.setStyleName(style.nonActive());
		
		//activate the button
		button.setStyleName(style.active());
		
		
	}
	
	public BankDetails(String name, String blz) {
		this.name = name;
		this.blz = blz;
		
		observer = new ArrayList<Observer>();
		initWidget(uiBinder.createAndBindUi(this));
		
		title.setText(constants.externalBanks());
		subTitle.setText(name + "-" + blz);
		
		enableDisplayAccounts.setText(constants.displayAccounts());
		enableEdit.setText(constants.editBankDetails());
		delete.setText(constants.delete());
		onClickAccounts(null);
	}
	
	ExternalAccountOverview externalAccountOverview;
	
	@UiHandler("enableDisplayAccounts")
	public void onClickAccounts(ClickEvent e) {
		setActive(enableDisplayAccounts);
		externalAccountOverview = new ExternalAccountOverview();
		externalAccountOverview.addObserver(this);
		content.setWidget(externalAccountOverview);
		AdminServiceAsync bankingService = GWT.create(AdminService.class);
		bankingService.getExternalAccounts(blz, new AsyncCallback<List<AccountDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(List<AccountDTO> result) {
				
				externalAccountOverview.setAccounts(result);
				
			}
		});
	}
	
	@UiHandler("enableEdit")
	public void onClickEdit(ClickEvent e) {
		setActive(enableEdit);
		EditBank bank = new EditBank(blz, name);
		bank.addObserver(this);
		content.setWidget(bank);
	}

	@UiHandler("delete")
	public void onClick(ClickEvent event) {
		if (Window.confirm(constants.deleteBankConfirm())) {
			AdminServiceAsync adminService = GWT.create(AdminService.class);
			adminService.deleteBank(blz, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					notifyObservers(DELETE_BANK_SUCCEED, null);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString());
					
				}
			});
		}
	}
	
	@Override
	public void addObserver(Observer o) {
		observer.add(o);
		
	}

	@Override
	public void notifyObservers(Integer eventType, Object parameter) {
		for (Observer o: observer)
			o.update(this, eventType, parameter);
		
	}

	
	private void showTransactions(List<MoneyTransferDTO> transfers, String accountNr, String name) {
		
		VerticalPanel panel = new VerticalPanel();
		
		Label subTitle = new Label(name + ":" + accountNr);
		subTitle.setStyleName("subtitle");
		panel.add(subTitle);
		panel.add(new TransferHistoryForm(transfers));
		content.setWidget(panel);
	}
	
	@Override
	public void update(Observable source, Object event, Object parameter) {
		if (source instanceof ExternalAccountOverview) {
			if ((Integer)event == ExternalAccountOverview.DISPLAY_EXTERNAL_TRANSACTIONS) {
				final AccountDTO account = (AccountDTO)parameter;
				AdminServiceAsync adminService = GWT.create(AdminService.class);
				adminService.getTransfers(account.getAccountNr(), this.blz, new AsyncCallback<List<MoneyTransferDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log(caught.toString());
						
					}

					@Override
					public void onSuccess(List<MoneyTransferDTO> result) {
						showTransactions(result, account.getAccountNr(), account.getOwner());
						
					}
				});
			}
		}
		notifyObservers((Integer)event, parameter);
		
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
