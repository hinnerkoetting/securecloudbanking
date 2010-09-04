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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.mrx.client.AccountDTO;
import de.mrx.client.MoneyTransferDTO;
import de.mrx.client.SCB;
import de.mrx.client.admin.forms.AccountOverview;
import de.mrx.client.admin.forms.AdminWelcome;
import de.mrx.client.admin.forms.Adminmenu;


public class Admin implements EntryPoint {

	AdminConstants constants = GWT.create(AdminConstants.class);
	
	public final String ADMIN_PAGE_ID = "admin";
	
	
	//test
	private VerticalPanel page;
	private Grid pageGrid;
	
    /**
     * show alle transactions
     * TODO: the same code is also in SCB.java
     */
	public void showAccountTransfers(List<MoneyTransferDTO> transfers) {
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

	public void showNewTransaction() {
		setContent(new Label("PLaceholder1"));
	}
	
	public void showExternalBanks() {
		setContent(new Label("PLaceholder2"));
	}
	
	/**
	 * show information about all accounts
	 */
	public void showAccounts() {
		final AccountOverview accountOverview = new AccountOverview(this);
		
		AdminServiceAsync bankingService = GWT.create(AdminService.class);
		bankingService.getAllAccounts(new AsyncCallback<List<AccountDTO>>() {

			@Override
			public void onFailure(Throwable caught) {					
				GWT.log(caught.toString());
			}

			@Override
			public void onSuccess(List<AccountDTO> result) {
				
				accountOverview.setAccounts(result);
				setContent(accountOverview);
			}
			
		});
	}
	/**
	 * 
	 * @param widget
	 * set content of the admin page
	 */
	private void setContent(Widget widget) {
		pageGrid.setWidget(0, 1, widget);
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
		
		
		pageGrid.setWidget(0, 0, new Adminmenu(this));
		pageGrid.setWidget(0, 1, new AdminWelcome());
		
		//temp workaround for header
		SCB scb = new SCB();
		scb.createMainPanel();
		r = RootPanel.get(SCB.PAGEID_HEADER);
		r.add(scb.createMainPanel());
		
		

	}

}
