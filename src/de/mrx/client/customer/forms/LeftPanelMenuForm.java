package de.mrx.client.customer.forms;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
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

import de.mrx.client.CustomerService;
import de.mrx.client.CustomerServiceAsync;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.SCBConstants;

public class LeftPanelMenuForm extends Composite implements Observable {
	interface MyUiBinder extends UiBinder<Widget, LeftPanelMenuForm> {
	}

	public final static Integer EVENT_SHOW_OVERVIEW = 1;
	public final static Integer EVENT_SHOW_SAVING_ACCOUNT = 2;
	public final static Integer EVENT_SEND_MONEY = 3;
	public final static Integer EVENT_SEND_MONEY_FAST = 4;
	public final static Integer EVENT_SHOW_REGISTRATION = 5;

	private static MyUiBinder menuUiBinder = GWT.create(MyUiBinder.class);
	CustomerServiceAsync customerService = GWT.create(CustomerService.class);
	private SCBConstants constants = GWT.create(SCBConstants.class);

	List<Observer> observers = new ArrayList<Observer>();

	@UiField
	Button overviewBtn;

	@UiField
	Button savingAccDetailBtn;

	@UiField
	Button sendMoneyBtn;

	@UiField
	Button sendFastMoneyBtn;

	@UiField
	Button openNewAccount;

	@UiField
	Button registerBtn;

	public LeftPanelMenuForm() {
		initWidget(menuUiBinder.createAndBindUi(this));
	}

	public void setStateOpenedSavingAccount() {
		overviewBtn.setVisible(true);
		savingAccDetailBtn.setVisible(true);
		sendMoneyBtn.setVisible(true);
		sendFastMoneyBtn.setVisible(true);
		openNewAccount.setVisible(false);
		registerBtn.setVisible(false);
		setVisible(true);
	}

	public void setStateRegisteredButNoSavingAccount() {
		overviewBtn.setVisible(false);
		savingAccDetailBtn.setVisible(false);
		sendMoneyBtn.setVisible(false);
		sendFastMoneyBtn.setVisible(false);
		openNewAccount.setVisible(true);
		registerBtn.setVisible(false);
		setVisible(true);
	}

	public void setStateNotRegistered() {
		overviewBtn.setVisible(false);
		savingAccDetailBtn.setVisible(false);
		sendMoneyBtn.setVisible(false);
		sendFastMoneyBtn.setVisible(false);
		openNewAccount.setVisible(false);
		registerBtn.setVisible(true);
		setVisible(true);
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void notifyObservers(Integer eventType, Object parameter) {
		for (Observer o : observers) {
			o.update(this, eventType, parameter);
		}
	}

	@UiHandler("overviewBtn")
	public void showOverview(ClickEvent e) {
		notifyObservers(EVENT_SHOW_OVERVIEW, null);

	}

	@UiHandler("registerBtn")
	public void showRegistrationForm(ClickEvent e) {
		notifyObservers(EVENT_SHOW_REGISTRATION, null);
	}

	@UiHandler("savingAccDetailBtn")
	public void showSavingAccount(ClickEvent e) {
		notifyObservers(EVENT_SHOW_SAVING_ACCOUNT, null);
	}

	@UiHandler("openNewAccount")
	public void openAccount(ClickEvent e) {

		if (customerService == null) {
			customerService = GWT.create(CustomerService.class);
		}
		customerService.openNewAccount(new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				Log.error("Open new account failed", caught);
			}

			public void onSuccess(Void result) {
				Log.info("Account created");
				Window.alert(constants.accountOpened());
				Window.Location.reload();

			}
		});

	}

	// customerService.getSavingAccount(new AsyncCallback<AccountDetailDTO>() {
	//
	// @Override
	// public void onSuccess(AccountDetailDTO result) {
	// notifyObservers(EVENT_SHOW_SAVING_ACCOUNT,result);
	//
	//
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// GWT.log("showOverviewErro",caught);
	// Window.alert("Fehler: "+caught.getMessage());
	// // TODO Auto-generated method stub
	//
	// }
	// });

	@UiHandler("sendMoneyBtn")
	public void showStandardMoneyTransform(ClickEvent e) {
		notifyObservers(EVENT_SEND_MONEY, null);
	}

	@UiHandler("sendFastMoneyBtn")
	public void showFastTransform(ClickEvent e) {
		notifyObservers(EVENT_SEND_MONEY_FAST, null);
	}

}
