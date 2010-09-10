package de.mrx.client.admin.forms;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.BankDTO;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.Admin;
/**
 * 
 * shows all accounts of an external bank
 *
 */
public class ExternalAccountOverview extends Composite {

	private static ExternalAccountOverviewUiBinder uiBinder = GWT
			.create(ExternalAccountOverviewUiBinder.class);

	interface ExternalAccountOverviewUiBinder extends
			UiBinder<Widget, ExternalAccountOverview> {
	}

	@UiField
	FlexTable table;
	
	Admin adminPage;
	
	public ExternalAccountOverview( Admin admin) {
		initWidget(uiBinder.createAndBindUi(this));
//		table.setWidget(0,0, new Label(bank.getBlz()));
//		table.setWidget(0,1, new Label(bank.getName()));
		this.adminPage = admin;
		
	}
	
	

	public void setAccounts(List<AccountDTO> accounts){
		final int posAccount 		= 0;
		final int posOwner 			= 1;

		
		//add header
		table.setWidget(0, posAccount, new Label("Account No."));
		table.setWidget(0, posOwner, new Label("Owner"));

		
		//add all accounts to table
		int row = 1;
		for (AccountDTO account: accounts) {	
			table.setWidget(row, posAccount, new Label(account.getAccountNr()));
			table.setWidget(row, posOwner, new Label(account.getOwner()));
			row++;
		}
		
		TableStyler.setTableStyle(table);
	}
}
