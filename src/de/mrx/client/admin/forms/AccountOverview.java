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
	}
	
	public void setAccounts(List<AccountDTO> accounts){

		initWidget(uiBinder.createAndBindUi(this));

	
		//add header
		overviewTable.setWidget(0, 0, new Label("Account No."));
		overviewTable.setWidget(0, 1, new Label("Balance"));
		overviewTable.setWidget(0, 2, new Label("Owner"));
		overviewTable.setWidget(0, 3, new Label("Transactions"));

	
		for (int i = 0; i < 4; i++) {
			
			overviewTable.getCellFormatter().setStyleName(0, i, "TransfersHeader");
		}
		
		//add all accounts to table
		int row = 1;
		for (AccountDTO account: accounts) {	
			overviewTable.setWidget(row, 0, new Label(account.getAccountNr()));
			overviewTable.setWidget(row, 1, new Label(""+account.getBalance()));
			overviewTable.setWidget(row, 2, new Label(account.getOwner()));
			Button showTransactions = new Button("Display");
			final String finalAccNr = account.getAccountNr();
			
			/**
			 * show transactions of this account
			 */
			showTransactions.addClickHandler(new ClickHandler() {	
				public void onClick(ClickEvent event) {
					GWT.log("Requesting transactions of Account "+finalAccNr);
					AdminServiceAsync service = GWT.create(AdminService.class);
					service.getTransaction(finalAccNr, new AsyncCallback<List<MoneyTransferDTO>>() {

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
			
			overviewTable.setWidget(row, 3, showTransactions);
			row++;
		}



	}
}
