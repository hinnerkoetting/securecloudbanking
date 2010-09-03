//
//
// Administration Client page
//
//

package de.mrx.client.admin;




import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.mrx.client.AccountDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.SCB;


public class Admin implements EntryPoint {

	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public final String ADMIN_PAGE_ID = "admin";
	
	
	
	private VerticalPanel page;
	private Grid pageGrid;
	
    /**
     * show alle transactions
     * TODO: the same code is also in SCB.java
     */
	private void showAccountTransfers(List<MoneyTransferDTO> transfers) {
		GWT.log("temp code");
		HTMLTable accountDetailTable = new FlexTable();
		Label dateLbl = new Label("Date");
		Label commentLbl = new Label("Comment");
		Label accountLbl = new Label("Account");
		Label amountLbl = new Label("Amount");
		commentLbl.setStyleName("TransfersHeader");
		dateLbl.setStyleName("TransfersHeader");
		accountLbl.setStyleName("TransfersHeader");
		amountLbl.setStyleName("TransfersHeader");

		accountDetailTable.setWidget(0, 0, dateLbl);
		accountDetailTable.setWidget(0, 1, commentLbl);
		accountDetailTable.setWidget(0, 2, accountLbl);
		accountDetailTable.setWidget(0, 3, amountLbl);
		int pos = 1;
		Log
				.debug("Money transfer entries: "
						+ transfers.size());
		for (MoneyTransferDTO transfer : transfers) {
			Log.info("Transfer: " + transfer);
			Label entryDateLbl = new Label(DateTimeFormat
					.getMediumDateFormat().format(
							transfer.getTimestamp()));
			Label entryRemarkLbl = new Label(transfer
					.getRemark());
			Label entryReceiverDetailsLbl = new Label(transfer
					.getReceiverBankNr()
					+ ": " + transfer.getReceiverAccountNr());

			Label entryAmountLbl = new Label(NumberFormat
					.getCurrencyFormat().format(
							transfer.getAmount()));
			if (pos % 2 == 0) {
				entryDateLbl.setStyleName("TransfersOdd");
				entryRemarkLbl.setStyleName("TransfersOdd");
				entryReceiverDetailsLbl
						.setStyleName("TransfersOdd");

			} else {
				entryDateLbl.setStyleName("TransfersEven");
				entryRemarkLbl.setStyleName("TransfersEven");
				entryReceiverDetailsLbl
						.setStyleName("TransfersEven");

			}
			if (transfer.getAmount() >= 0) {
				entryAmountLbl.setStyleName("positiveMoney");
			} else {
				entryAmountLbl.setStyleName("negativeMoney");
			}
			
			accountDetailTable.setWidget(pos, 0, entryDateLbl);
			accountDetailTable
					.setWidget(pos, 1, entryRemarkLbl);
			accountDetailTable.setWidget(pos, 2,
					entryReceiverDetailsLbl);
			accountDetailTable
					.setWidget(pos, 3, entryAmountLbl);
			pos++;
			
		}
		pageGrid.setWidget(0, 1, accountDetailTable);
	}

	
	/*
	 * create menu on the left 
	 */
	private VerticalPanel createAdminMenu() {
		
		/**
		 * overview of all accounts
		 */
		ClickHandler clickOverview = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				
				AdminServiceAsync bankingService = GWT.create(AdminService.class);
				bankingService.getAllAccounts(new AsyncCallback<List<AccountDTO>>() {

					@Override
					public void onFailure(Throwable caught) {					
						GWT.log(caught.toString());
					}

					@Override
					public void onSuccess(List<AccountDTO> result) {
						
						if (result == null) {
							GWT.log("Error: empty result");
							return;
						}
						//arrange page layout
						FlexTable table = new FlexTable();
						pageGrid.setWidget(0, 1, table);
						

						
						//add header
						table.setWidget(0, 0, new Label("Account No."));
						table.setWidget(0, 1, new Label("Balance"));
						table.setWidget(0, 2, new Label("Owner"));
						table.setWidget(0, 3, new Label("Transactions"));
						
						//add all accounts to table
						int row = 1;
						for (AccountDTO account: result) {	
							table.setWidget(row, 0, new Label(account.getAccountNr()));
							table.setWidget(row, 1, new Label(""+account.getBalance()));
							table.setWidget(row, 2, new Label(account.getOwner()));
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
											showAccountTransfers(result);
											
										}
									});
									
								}
							});
							
							table.setWidget(row, 3, showTransactions);
							row++;
						}
						
					}
					
				});
				
			}	
		};
		
		ClickHandler clickNewTransaction = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageGrid.setWidget(0, 1, new Label("New Transaction"));				
			}	
		};
		
		ClickHandler clickExternalBanks = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageGrid.setWidget(0, 1, new Label("External Banks"));				
			}			
		};
		
		ClickHandler clickHistory = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pageGrid.setWidget(0, 1, new Label("History"));
			}
		};
		
		VerticalPanel panel = new VerticalPanel();		
		
		
		//add all buttons
		Button btn = new Button(constants.overviewAccount(), clickOverview);
		panel.add(btn);
		
		btn = new Button(constants.newTransaction(), clickNewTransaction);
		panel.add(btn);
		
		btn = new Button(constants.externalBanks(), clickExternalBanks);
		panel.add(btn);
		
		btn = new Button(constants.history(), clickHistory);
		panel.add(btn);
		
		
		return panel;
	}
	
	/*
	 * create content for overview of admin page
	 */
	private VerticalPanel createContentOverview() {

		VerticalPanel content = new VerticalPanel();
		
		Label label = new Label("PLACEHOLDER");		
		content.add(label);
		
		return content;
	}
	
	@Override
	public void onModuleLoad() {
		
		RootPanel r = RootPanel.get(ADMIN_PAGE_ID);		
		if (r == null)
			return;
		page = new VerticalPanel();

		Label title = new Label("Administration");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		
		r.add(title);
		
		pageGrid = new Grid(1, 2 );
		page.add(pageGrid);
		r.add(page);
		
		VerticalPanel adminMenu = createAdminMenu();
		adminMenu.setSpacing(10);
		
		pageGrid.setWidget(0, 0, adminMenu);
		pageGrid.setWidget(0, 1, createContentOverview());
		
		//temp workaround for header
		SCB scb = new SCB();
		scb.createMainPanel();
		r = RootPanel.get(SCB.PAGEID_HEADER);
		r.add(scb.createMainPanel());
		
		

	}

}
