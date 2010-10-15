package de.mrx.client.admin.forms.external;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.Observable;
import de.mrx.client.Observer;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.AdminConstants;
/**
 * 
 * shows all accounts of an external bank
 *
 */
public class ExternalAccountOverview extends Composite implements Observable {

	private static ExternalAccountOverviewUiBinder uiBinder = GWT
			.create(ExternalAccountOverviewUiBinder.class);

	interface ExternalAccountOverviewUiBinder extends
			UiBinder<Widget, ExternalAccountOverview> {
	}

	@UiField
	FlexTable table;
	
	List<Observer> observer;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public final static int DISPLAY_EXTERNAL_TRANSACTIONS = 37;
	
	public ExternalAccountOverview() {
		observer = new ArrayList<Observer>();
		initWidget(uiBinder.createAndBindUi(this));
//		table.setWidget(0,0, new Label(bank.getBlz()));
//		table.setWidget(0,1, new Label(bank.getName()));
		
	}
	
	
	public void setAccounts(List<AccountDTO> accounts){
		final int posAccount 		= 0;
		final int posOwner 			= 1;
		final int posTransactions   = 2;
		
		//add header
		table.setWidget(0, posAccount, new Label(constants.accountNr()));
		table.setWidget(0, posOwner, new Label(constants.owner()));
		table.setWidget(0, posTransactions, new Label(constants.transactions()));

		
		//add all accounts to table
		int row = 1;
		for (final AccountDTO account: accounts) {	
			table.setWidget(row, posAccount, new Label(account.getAccountNr()));
			table.setWidget(row, posOwner, new Label(account.getOwner()));
			Button displayButton = new Button(constants.display());
			displayButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					notifyObservers(DISPLAY_EXTERNAL_TRANSACTIONS, account);
					
				}
				
			});
			table.setWidget(row, posTransactions, displayButton);
			row++;
		}
		
		TableStyler.setTableStyle(table);
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
