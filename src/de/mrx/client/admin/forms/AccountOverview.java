package de.mrx.client.admin.forms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.admin.Admin;
import de.mrx.client.admin.AdminConstants;

public class AccountOverview extends Composite implements UseSearchForm, Observable, Observer{

	private static AccountOverviewUiBinder uiBinder = GWT
			.create(AccountOverviewUiBinder.class);

	interface AccountOverviewUiBinder extends UiBinder<Widget, AccountOverview> {
	}


	@UiField
	SimplePanel content;


	
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	
	
	List<Observer> observer;
	
			
	
		
	
	SearchAccountsForm searchForm;
	public AccountOverview(Admin admin) {
		observer = new ArrayList<Observer>();

		
		initWidget(uiBinder.createAndBindUi(this));
		searchForm = new SearchAccountsForm(this);
		searchForm.enableColumns(true, true, true);
		searchForm.enableButton(true, constants.edit());
		content.setWidget(searchForm);
		
	
	}
	
	public void setAccounts(List<AccountDTO> accounts){
		searchForm.setAccounts(accounts);
		


	}

	@Override
	public void clickedOnAccount(AccountDTO account) {
		AccountDetails accountDetails = new AccountDetails(account);
		accountDetails.addObserver(this);
		content.setWidget(accountDetails);
		
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
	

}
