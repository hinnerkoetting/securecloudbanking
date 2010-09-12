package de.mrx.client.admin.forms;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.TableStyler;
import de.mrx.client.admin.Admin;
import de.mrx.client.admin.AdminConstants;
import de.mrx.client.admin.AdminService;
import de.mrx.client.admin.AdminServiceAsync;

public class AccountOverview extends Composite {

	private static AccountOverviewUiBinder uiBinder = GWT
			.create(AccountOverviewUiBinder.class);

	interface AccountOverviewUiBinder extends UiBinder<Widget, AccountOverview> {
	}


	@UiField
	FlexTable overviewTable;
	
	@UiField
	Label title;
	
	@UiField
	Label descOwner;
	
	@UiField
	Label descAccountNr;
	
	@UiField
	TextBox searchOwner;
	
	@UiField
	TextBox searchAccountNr;
	
	@UiField
	Button search;
	
	Admin adminPage;
	
	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public AccountOverview(Admin admin) {
		
		this.adminPage = admin;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		title.setText(constants.accountOverview());
		descOwner.setText(constants.owner());
		descAccountNr.setText(constants.accountNr());
		search.setText(constants.search());		
	}
	
	public void setAccounts(List<AccountDTO> accounts){
		overviewTable.clear();
		

		final int posOwner 			= 0;
		final int posAccount 		= 1;
		final int posBalance 		= 2;		
		final int posTransaction 	= 3;
		final int posTransfer 		= 4;
		final int posDelete 		= 5;
		
		//add header
		overviewTable.setWidget(0, posAccount, new Label(constants.owner()));
		overviewTable.setWidget(0, posBalance, new Label(constants.balance()));
		overviewTable.setWidget(0, posOwner, new Label(constants.accountNr()));
		overviewTable.setWidget(0, posTransaction, new Label(constants.transactions()));
		overviewTable.setWidget(0, posTransfer, new Label(constants.transferMoney()));
		overviewTable.setWidget(0, posDelete, new Label(constants.deleteAccount()));
		
		
		//add all accounts to table
		int row = 1;
		NumberFormat fmt = NumberFormat.getFormat("#0.00");

		for (AccountDTO account: accounts) {	
			overviewTable.setWidget(row, posAccount, new Label(account.getAccountNr()));
			String balance = fmt.format(account.getBalance());
			overviewTable.setWidget(row, posBalance, new Label(balance));
			overviewTable.setWidget(row, posOwner, new Label(account.getOwner()));
			
			final String accNr = account.getAccountNr();
			final String accOwner = account.getOwner();
			/**
			 * show transactions of this account
			 */
			Button showTransactions = new Button(constants.display());			
			
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
			Button transferMoney = new Button(constants.transfer());
			
			transferMoney.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					adminPage.showTransferMoney(accNr, accOwner);
					
				}
			});
			overviewTable.setWidget(row, posTransfer, transferMoney);
			
			Button deleteButton = new Button(constants.delete());
			deleteButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if (Window.confirm(constants.deleteAccountConfirm())) {
						AdminServiceAsync adminService = GWT.create(AdminService.class);
						adminService.deleteInternalAccount(accNr, new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								GWT.log(caught.toString());								
							}

							@Override
							public void onSuccess(String result) {
								GWT.log(result);
								adminPage.showAccounts();
								
							}
						});
					}
					
				}
			});
			overviewTable.setWidget(row, posDelete, deleteButton);
			
		
			row++;
		}
		TableStyler.setTableStyle(overviewTable);


	}
	
	@UiHandler("search")
	public void onClickSearch(ClickEvent event) {
		final AccountOverview overview = this;
		AdminServiceAsync adminService = GWT.create(AdminService.class);
		
		adminService.searchInternalAccounts(searchOwner.getText(), searchAccountNr.getText(), new AsyncCallback<List<AccountDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				
			}

			@Override
			public void onSuccess(List<AccountDTO> result) {
				overview.setAccounts(result);
				
			}
		});
	}
}
