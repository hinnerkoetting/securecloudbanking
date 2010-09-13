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
//	@UiField
//	FlexTable overviewTable;
//	
//	@UiField
//	Label title;
//	
//	@UiField
//	Label descOwner;
//	
//	@UiField
//	Label descAccountNr;
//	
//	@UiField
//	TextBox searchOwner;
//	
//	@UiField
//	TextBox searchAccountNr;
//	
//	@UiField
//	Button search;
//	

	
	
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
		
//		
//		title.setText(constants.accountOverview());
//		descOwner.setText(constants.owner());
//		descAccountNr.setText(constants.accountNr());
//		search.setText(constants.search());		
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
	
//	@UiHandler("search")
//	public void onClickSearch(ClickEvent event) {
//		final AccountOverview overview = this;
//		AdminServiceAsync adminService = GWT.create(AdminService.class);
//		
//		adminService.searchInternalAccounts(searchOwner.getText(), searchAccountNr.getText(), new AsyncCallback<List<AccountDTO>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				GWT.log(caught.toString());
//				
//			}
//
//			@Override
//			public void onSuccess(List<AccountDTO> result) {
//				overview.setAccounts(result);
//				
//			}
//		});
//	}
//	
//	private void switchToPage(int page) {
//		int row = 1;
//		NumberFormat fmt = NumberFormat.getFormat("#0.00");
//		for (int i = 0; i < ACCOUNTS_PER_PAGE; i++) {
//			int index = (page-1) * ACCOUNTS_PER_PAGE + i;
//			if (index >= accounts.size()) {
//				if (overviewTable.getRowCount() > row)
//					overviewTable.removeRow(row);
//				//row++ is not needed because the last row was just removed
//				continue;
//			}
//			//add all accounts to table
//			AccountDTO account = accounts.get(index);
//			
//
//			
//			overviewTable.setWidget(row, posOwner, new Label(account.getOwner()));
//			overviewTable.setWidget(row, posAccount, new Label(account.getAccountNr()));
//			String balance = fmt.format(account.getBalance());
//			overviewTable.setWidget(row, posBalance, new Label(balance));
//			
//			
//			final String accNr = account.getAccountNr();
//			final String accOwner = account.getOwner();
//			/**
//			 * show transactions of this account
//			 */
//			Button showTransactions = new Button(constants.display());			
//			
//			showTransactions.addClickHandler(new ClickHandler() {	
//				public void onClick(ClickEvent event) {
//					GWT.log("Requesting transactions of Account "+accNr);
//					AdminServiceAsync service = GWT.create(AdminService.class);
//					service.getTransaction(accNr, new AsyncCallback<List<MoneyTransferDTO>>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							GWT.log(caught.toString());											
//						}
//
//						@Override
//						public void onSuccess(List<MoneyTransferDTO> result) {
//							adminPage.showAccountTransfers(result);							
//						}
//					});
//					
//				}
//			});
//			
//			overviewTable.setWidget(row, posTransaction, showTransactions);
//			
//			
//			
//			
//			/**
//			 * add money to this account
//			 */
//			Button transferMoney = new Button(constants.transfer());
//			
//			transferMoney.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					adminPage.showTransferMoney(accNr, accOwner);
//					
//				}
//			});
//			overviewTable.setWidget(row, posTransfer, transferMoney);
//			
//			Button deleteButton = new Button(constants.delete());
//			deleteButton.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					if (Window.confirm(constants.deleteAccountConfirm())) {
//						AdminServiceAsync adminService = GWT.create(AdminService.class);
//						adminService.deleteInternalAccount(accNr, new AsyncCallback<String>() {
//
//							@Override
//							public void onFailure(Throwable caught) {
//								GWT.log(caught.toString());								
//							}
//
//							@Override
//							public void onSuccess(String result) {
//								GWT.log(result);
//								adminPage.showAccounts();
//								
//							}
//						});
//					}
//					
//				}
//			});
//			overviewTable.setWidget(row, posDelete, deleteButton);
//			
//		
//			row++;
//		}
//		TableStyler.setTableStyle(overviewTable);
//		
//	}
}
