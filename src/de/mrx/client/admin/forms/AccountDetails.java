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
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.TansDTO;
import de.mrx.client.TransferHistoryForm;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class AccountDetails extends Composite implements Observable, Observer{

	private static AccountDetailsUiBinder uiBinder = GWT
			.create(AccountDetailsUiBinder.class);

	interface AccountDetailsUiBinder extends UiBinder<Widget, AccountDetails> {
	}
	@UiField
	SimplePanel content;
	
	@UiField
	Button delete;
	
	@UiField
	Button enableTransfer;
	
	@UiField
	Button enableTransactions;
	
	@UiField
	Button enableTans;
	
	@UiField
	Button reset;
	
	@UiField
	Label title;
	
	@UiField
	Label subTitle;
	
	public final static int ACCOUNT_DELETED = 20;
	public final static int ACCOUNT_RESET = 21;
	
	 @UiField MyStyle style;
	interface MyStyle extends CssResource {
		    String active();
		    String nonActive();
	}

		
	
	public void showTransactions(List<MoneyTransferDTO> result) {
		content.setWidget(new TransferHistoryForm(result));
	}
	
	private void setActive(Button button) {
		//reset all buttons
		delete.setStyleName(style.nonActive());
		enableTransfer.setStyleName(style.nonActive());
		enableTransactions.setStyleName(style.nonActive());
		enableTans.setStyleName(style.nonActive());
		reset.setStyleName(style.nonActive());
		
		//activate the button
		button.setStyleName(style.active());
		
		
	}
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	AccountDTO account;
	
	List<Observer> observer;
	
	public AccountDetails(AccountDTO account) {	
		
		observer = new ArrayList<Observer>();
		this.account = account;
		initWidget(uiBinder.createAndBindUi(this));
		
		title.setText(constants.accountDetails());
		subTitle.setText(account.getOwner());
		enableTransfer.setText(constants.transfer());
		enableTransactions.setText(constants.transactions());
		enableTans.setText(constants.tans());
		delete.setText(constants.delete());
		reset.setText(constants.reset());
		clickOnTransfer(null);
	
		
		
	}

	public void showTans(TansDTO tanList) {
		TanTable tanTable = new TanTable(tanList.getTans());
		content.setWidget(tanTable);
	}
	
	@UiHandler("enableTransactions")
	public void clickOnTransactions(ClickEvent e) {
		setActive(enableTransactions);
		AdminServiceAsync service = GWT.create(AdminService.class);
		service.getTransaction(account.getAccountNr(), new AsyncCallback<List<MoneyTransferDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());											
			}

			@Override
			public void onSuccess(List<MoneyTransferDTO> result) {
				showTransactions(result);							
			}
		});
	}
	
	
	@UiHandler("enableTransfer")
	public void clickOnTransfer(ClickEvent e) {
		setActive(enableTransfer);
		AdminTransfer adminTransfer = new AdminTransfer(account.getAccountNr(), account.getOwner());
		adminTransfer.addObserver(this);
		content.setWidget(adminTransfer);
		
	}

	@UiHandler("enableTans")
	public void clickOnTans(ClickEvent e) {
		setActive(enableTans);
		AdminServiceAsync adminService = GWT.create(AdminService.class);
		adminService.getTans(account.getAccountNr(), new AsyncCallback<TansDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(TansDTO result) {
				showTans(result);
				
			}
			
		});

	}
			
	@UiHandler("reset") 
	public void clickOnReset(ClickEvent e) {
		if (Window.confirm(constants.resetConfirmation())) {
			AdminServiceAsync adminService = GWT.create(AdminService.class);
			adminService.resetInternalAccount(account.getAccountNr(), new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString());		
					
				}

				@Override
				public void onSuccess(String result) {
					notifyObservers(ACCOUNT_RESET, null);
					
				}
			});
		}
	}
	
	@UiHandler("delete")
	public void clickOnDelete(ClickEvent e) {
		if (Window.confirm(constants.deleteAccountConfirm())) {
			AdminServiceAsync adminService = GWT.create(AdminService.class);
			adminService.deleteInternalAccount(account.getAccountNr(), new AsyncCallback<String>() {
	
				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString());								
				}
	
				@Override
				public void onSuccess(String result) {
					GWT.log(result);
					notifyObservers(ACCOUNT_DELETED, null);
					
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
			o.update(this,eventType, parameter);
	}

	@Override
	public void update(Observable source, Object event, Object parameter) {
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
