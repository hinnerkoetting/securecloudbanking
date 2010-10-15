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
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class Adminmenu extends Composite implements Observable {

	private static AdminmenuUiBinder uiBinder = GWT
			.create(AdminmenuUiBinder.class);

	interface AdminmenuUiBinder extends UiBinder<Widget, Adminmenu> {
	}

	@UiField
	Button accounts;

	@UiField
	Button externalBanks;
	
	@UiField
	Button deleteData;
	
	@UiField
	Button generateData;
	
	@UiField
	Button securedBySCB;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	List<Observer> observer;
	
	public static final int SHOW_ACCOUNTS = 50;
	public static final int SHOW_EXTERNAL_BANKS = 51;
	public static final int SHOW_3S				= 52;
	
	public Adminmenu() {
		observer = new ArrayList<Observer>();

		initWidget(uiBinder.createAndBindUi(this));
		
		accounts.setText(constants.accounts());
		externalBanks.setText(constants.externalBanks());
		deleteData.setText(constants.deleteData());
		generateData.setText(constants.generateData());
		securedBySCB.setText(constants.securedBySCB());
	}

	@UiHandler("accounts")
	void onClickAccounts(ClickEvent e) {
		notifyObservers(SHOW_ACCOUNTS, null);
	}
	

	@UiHandler("externalBanks")
	void onClickExternalBanks(ClickEvent e) {
		notifyObservers(SHOW_EXTERNAL_BANKS, null);
	}
	
	@UiHandler("securedBySCB")
	void onClickSecuredBySCB(ClickEvent e) {
		notifyObservers(SHOW_3S, null);
	}
	
	@UiHandler("generateData")
	void onClickGenerateData(ClickEvent e) {
		if (Window.confirm(constants.generateDataConfirm())) {
			AdminServiceAsync bankingService = GWT.create(AdminService.class);
			bankingService.generateTestData(new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
						Window.alert(result);	
				}
				
				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString());	
				}
			});
		}
	}

	@UiHandler("deleteData")
	void onClickDeleteData(ClickEvent e) {
		if (Window.confirm(constants.deleteDataConfirm())) {
			AdminServiceAsync bankingService = GWT.create(AdminService.class);
			bankingService.deleteData(new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
						Window.alert(result);	
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
}
