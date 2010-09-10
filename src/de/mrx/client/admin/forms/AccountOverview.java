package de.mrx.client.admin.forms;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.Admin;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class AccountOverview extends Composite {

	private static AccountOverviewUiBinder uiBinder = GWT
			.create(AccountOverviewUiBinder.class);

	interface AccountOverviewUiBinder extends UiBinder<Widget, AccountOverview> {
	}


	@UiField
	FlexTable overviewTable;
	
	Admin adminPage;
	public AccountOverview(Admin admin) {
		this.adminPage = admin;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setAccounts(List<AccountDTO> accounts){

		

	
		final int posAccount 		= 0;
		final int posBalance 		= 1;
		final int posOwner 			= 2;
		final int posTransaction 	= 3;
		final int posTransfer 		= 4;
		
		//add header
		overviewTable.setWidget(0, posAccount, new Label("Account No."));
		overviewTable.setWidget(0, posBalance, new Label("Balance"));
		overviewTable.setWidget(0, posOwner, new Label("Owner"));
		overviewTable.setWidget(0, posTransaction, new Label("Transactions"));
		overviewTable.setWidget(0, posTransfer, new Label("Transfer Money"));
		

		//add all accounts to table
		int row = 1;
		for (AccountDTO account: accounts) {	
			overviewTable.setWidget(row, posAccount, new Label(account.getAccountNr()));
			overviewTable.setWidget(row, posBalance, new Label(""+account.getBalance()));
			overviewTable.setWidget(row, posOwner, new Label(account.getOwner()));

			final String accNr = account.getAccountNr();
			final String accOwner = account.getOwner();
			/**
			 * show transactions of this account
			 */
			Button showTransactions = new Button("Display");			
			
			showTransactions.addClickHandler(new ClickHandler() {	
				public void onClick(ClickEvent event) {
					GWT.log("Requesting transactions of Account "+accNr);
					AdminServiceAsync service = GWT.create(AdminService.class);
					service.getTransaction(accNr, new AsyncCallback<List<MoneyTransferDTO>>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log(caught.toString());											
						}

						@Override
						public void onSuccess(List<MoneyTransferDTO> result) {
							adminPage.showAccountTransfers(result);							
						}
					});
					
				}
			});
			
			overviewTable.setWidget(row, posTransaction, showTransactions);
			
			
			/**
			 * add money to this account
			 */
			Button transferMoney = new Button("Transfer");
			
			transferMoney.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					adminPage.showTransferMoney(accNr, accOwner);
					
				}
			});
			overviewTable.setWidget(row, posTransfer, transferMoney);
			
			
			
			
			if (account.getBalance() < 0)
				overviewTable.getCellFormatter().addStyleName(row, 1, "negativeMoney");
			else //balance >= 0
				overviewTable.getCellFormatter().addStyleName(row, 1, "positiveMoney");
			row++;
		}
		TableStyler.setTableStyle(overviewTable);


	}
}
